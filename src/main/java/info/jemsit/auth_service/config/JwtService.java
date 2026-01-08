package info.jemsit.auth_service.config;

import info.jemsit.auth_service.data.model.User;
import info.jemsit.common.data.enums.Roles;
import info.jemsit.common.exceptions.TokenInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private  String SECRET_KEY;

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        try {

            return Jwts
                    .parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException malformedJwtException) {
            throw new TokenInvalidException("Token is invalid");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new TokenInvalidException("Token is expired");
        }
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public List<Roles> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roleStrings = claims.get("roles", List.class);

        if (roleStrings == null || roleStrings.isEmpty()) {
            return Collections.emptyList();
        }

        return roleStrings.stream()
                .map(Roles::valueOf)  // Convert String to Role enum
                .collect(Collectors.toList());
    }

//    public String generateTokenWithNOTime(User user) {
//        Map<String, Object> claims = new HashMap<>();
//        Set<String> userRoles = new HashSet<>();
//        user.getAuthorities().forEach(u -> userRoles.add(u.getAuthority()));
//        claims.put("roles", userRoles);
//        return generateTokenWithNOExpireTime(claims, user);
//    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        Set<String> userRoles = new HashSet<>();
        user.getAuthorities().forEach(u -> userRoles.add(u.getAuthority()));
        claims.put("roles", userRoles);
        return generateTokenWithExpireTime(claims, user);
    }

    public String generateTokenWithExpireTime(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) // 12 hours
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

//    public String generateTokenWithNOExpireTime(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return Jwts.builder()
//                .subject(userDetails.getUsername())
//                .claims(extraClaims)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .signWith(getSecretKey(), Jwts.SIG.HS256)
//                .compact();
//    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5)) // 5 hours
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        Set<String> userRoles = new HashSet<>();
        user.getAuthorities().forEach(u -> userRoles.add(u.getAuthority()));
        claims.put("roles", userRoles);
        return generateRefreshToken(claims, user);
    }

}
