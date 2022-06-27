package com.ead.authuser.controllers;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.dto.UserDto.UserView;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

  @Autowired
  UserService userService;

  @GetMapping
  public ResponseEntity<List<UserModel>> getAllUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId) {
    Optional<UserModel> userModelOptional = userService.findById(userId);
    if (!userModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    } else {
      return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
    Optional<UserModel> userModelOptional = userService.findById(userId);
    if (!userModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    } else {
      userService.delete(userModelOptional.get());
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted with success");
    }
  }

  @PutMapping("/{userId}")
  public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
      @RequestBody @JsonView(UserView.UserPut.class) UserDto userDto) {
    Optional<UserModel> userModelOptional = userService.findById(userId);
    if (!userModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    } else {
      var userModel = userModelOptional.get();
      userModel.setFullName(userDto.getFullName());
      userModel.setPhoneNumber(userDto.getPhoneNumber());
      userModel.setCpf(userDto.getCpf());
      userModel.setLastUpdateData(LocalDateTime.now(ZoneId.of("UTC")));
      userService.save(userModel);
      return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }
  }

  @PutMapping("/{userId}/password")
  public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
      @RequestBody @JsonView(UserView.PasswordPut.class) UserDto userDto) {
    Optional<UserModel> userModelOptional = userService.findById(userId);
    if (!userModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    if (!userModelOptional.get().getPassword().equals(userDto.getOldPassword())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatch old password!");
    } else {
      var userModel = userModelOptional.get();
      userModel.setPassword(userDto.getPassword());
      userModel.setLastUpdateData(LocalDateTime.now(ZoneId.of("UTC")));
      userService.save(userModel);
      return ResponseEntity.status(HttpStatus.OK).body("Password updated sucessfully.");
    }
  }

  @PutMapping("/{userId}/image")
  public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
      @RequestBody @JsonView(UserView.ImagePut.class) UserDto userDto) {
    Optional<UserModel> userModelOptional = userService.findById(userId);
    if (!userModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    } else {
      var userModel = userModelOptional.get();
      userModel.setImageUrl(userDto.getImageUrl());
      userModel.setLastUpdateData(LocalDateTime.now(ZoneId.of("UTC")));
      userService.save(userModel);
      return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }
  }

}
