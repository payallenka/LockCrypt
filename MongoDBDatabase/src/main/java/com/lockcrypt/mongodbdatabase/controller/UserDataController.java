package com.lockcrypt.mongodbdatabase.controller;

import com.lockcrypt.mongodbdatabase.model.UserData;
import com.lockcrypt.mongodbdatabase.model.UserDataRequest;
import com.lockcrypt.mongodbdatabase.service.UserDataService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userData")
public class UserDataController {

    private final UserDataService userDataService;

    public UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping
    public List<UserData> getUserData(@RequestParam String owner) {
        return userDataService.getUserData(owner);
    }

    @PostMapping
    public ResponseEntity<UserData> saveUserData(@Valid @RequestBody UserDataRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userDataService.saveUserData(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserData(@PathVariable String id, @RequestParam String owner) {
        userDataService.deleteUserData(id, owner);
        return ResponseEntity.noContent().build();
    }
}
