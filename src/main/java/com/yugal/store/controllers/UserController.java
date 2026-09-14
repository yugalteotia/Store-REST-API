package com.yugal.store.controllers;

import com.yugal.store.dtos.ChangePasswordRequest;
import com.yugal.store.dtos.RegisterUserRequest;
import com.yugal.store.dtos.UpdateUserRequest;
import com.yugal.store.dtos.UserDto;
import com.yugal.store.mappers.UserMapper;
import com.yugal.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

import static java.util.Objects.nonNull;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //    @RequestMapping(method = RequestMethod.GET, value = "/users")
    //    @GetMapping("/users")
    @GetMapping
    public Iterable<UserDto> getAllUsers(
//            @RequestHeader(required = false, defaultValue = "", name = "x-auth-token") String authToken,
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy) {
//        sort = nonNull(sort) && Set.of("name", "email").contains(sort) ? sort : "name";
//        System.out.println("authToken: " + authToken);
        sortBy = Set.of("name", "email").contains(sortBy) ? sortBy : "name";
        return userRepository.findAll(Sort.by(sortBy)).stream().map(userMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder) {
        var user = userRepository.save(userMapper.toEntity(request));
        return ResponseEntity.created(uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri()).body(userMapper.toDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id, @RequestBody UpdateUserRequest request) {
        var user = userRepository.findById(id).orElse(null);
        return nonNull(user) ? ResponseEntity.ok(userMapper.toDto(userRepository.save(userMapper.update(request, user)))) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (nonNull(user)) {
            userRepository.delete(user);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> updatePassword(@PathVariable(name = "id") Long id, @RequestBody ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (!nonNull(user)) {
            return ResponseEntity.notFound().build();
        }
        if (!request.getOldPassword().equals(user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }
}
