package info.jemsit.auth_service.controller;

import info.jemsit.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("details")
    public ResponseEntity<?> getUserDetails() {
        return ResponseEntity.ok(userService.getUserDetails());
    }

    @PutMapping("update-user")
    public ResponseEntity<?> updateUser(@RequestBody UserDetailsRequestDTO request) {
        userService.updateUserDetails(request);
        return ResponseEntity.ok().build();
    }
}
