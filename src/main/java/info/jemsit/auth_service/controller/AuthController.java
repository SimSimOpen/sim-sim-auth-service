package info.jemsit.auth_service.controller;

import info.jemsit.auth_service.service.AuthService;
import info.jemsit.common.dto.request.AuthenticationRequestDTO;
import info.jemsit.common.dto.request.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request)  {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("register/client")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        authService.registerClient(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("register/agent")
    public ResponseEntity<?> registerAgent(@RequestBody RegisterRequestDTO request) {
        authService.registerAgent(request);
        return ResponseEntity.ok().build();
    }
}
