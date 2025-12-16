package com.example.user_service.controllers.user;

import com.example.user_service.dto.user.UserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserControllers {
    @Autowired
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<EntityModel<UserResponse>> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toEntityModel(userService.createUser(userRequest)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(toEntityModel(userService.getUserById(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest userRequest
    ){
        return ResponseEntity.ok(toEntityModel(userService.partialUpdateUser(id, userRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Пользователь с ID " + id + " успешно удален");
        response.put("status", "Успешно");

        EntityModel<Map<String, String>> entityModel = EntityModel.of(response);

        entityModel.add(linkTo(methodOn(UserControllers.class)
                .createUser(null)).withRel("register"));

        return ResponseEntity.ok(entityModel);
    }

    //-----------------Без кафки-------------------

    @PostMapping("/no_kafka")
    public ResponseEntity<EntityModel<UserResponse>> createUserWithOutKafka(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toEntityModel(userService.createUser(userRequest)));
    }

    @DeleteMapping("/no_kafka/{id}")
    public ResponseEntity<?> deleteUserWithOutKafka(@PathVariable Long id){
        userService.deleteUserWithOutKafka(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Пользователь с ID " + id + " успешно удален");
        response.put("status", "Успешно");

        EntityModel<Map<String, String>> entityModel = EntityModel.of(response);

        entityModel.add(linkTo(methodOn(UserControllers.class)
                .createUser(null)).withRel("register"));

        return ResponseEntity.ok(entityModel);
    }

    private EntityModel<UserResponse> toEntityModel(UserResponse userResponse) {
        if (userResponse == null) {
            return null;
        }
        Link selfLink = linkTo(methodOn(UserControllers.class)
                .getUser(userResponse.getId())).withRel("Перезагрузить пользователя");
        Link createLink = linkTo(methodOn(UserControllers.class)
                .createUser(null)).withRel("Зарегистрировать");
        Link updateLink = linkTo(methodOn(UserControllers.class).updateUser(userResponse.getId(), null))
                .withRel("Обновить");
        Link deleteLink = linkTo(methodOn(UserControllers.class)
                .deleteUser(userResponse.getId())).withRel("Удалить");

        return EntityModel.of(userResponse,
                selfLink,
                createLink,
                updateLink,
                deleteLink
        );
    }
}