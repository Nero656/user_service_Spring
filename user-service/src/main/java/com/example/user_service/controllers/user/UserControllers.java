package com.example.user_service.controllers.user;

import com.example.user_service.dto.user.UserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserControllers {
    @Autowired
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequest));
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id)).getBody();
    }

    @PatchMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.partialUpdateUser(id, userRequest), HttpStatus.OK).getBody();
    }

    @DeleteMapping("/{id}")
    public UserResponse deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new UserResponse();
    }
}
