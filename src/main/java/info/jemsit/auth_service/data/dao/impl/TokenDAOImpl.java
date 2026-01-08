package info.jemsit.auth_service.data.dao.impl;

import info.jemsit.auth_service.data.dao.TokenDAO;
import info.jemsit.auth_service.data.model.Token;
import info.jemsit.auth_service.data.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenDAOImpl implements TokenDAO {

    private final TokenRepository tokenRepository;

    @Override
    public Token saveToken(Token token) {
        log.info("Saving token: {}", token.getToken());
        return tokenRepository.save(token);
    }

    @Override
    public Token findByToken(String token) {
        return null;
    }

    @Override
    public void deleteToken(Token token) {

    }
}
