package info.jemsit.auth_service.controller;

import info.jemsit.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("details")
    public ResponseEntity<?> getUserDetails() {
        return ResponseEntity.ok(userService.getUserDetails());
    }
}
