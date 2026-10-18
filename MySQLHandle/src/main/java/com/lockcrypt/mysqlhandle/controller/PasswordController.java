package com.lockcrypt.mysqlhandle.controller;

import com.lockcrypt.mysqlhandle.model.Password;
import com.lockcrypt.mysqlhandle.model.PasswordKeyRequest;
import com.lockcrypt.mysqlhandle.service.PasswordKeyService;
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
@RequestMapping("/keys")
public class PasswordController {

    private final PasswordKeyService passwordKeyService;

    public PasswordController(PasswordKeyService passwordKeyService) {
        this.passwordKeyService = passwordKeyService;
    }

    @GetMapping
    public List<Password> getKeys(@RequestParam String owner) {
        return passwordKeyService.getKeys(owner);
    }

    @PostMapping
    public ResponseEntity<Password> saveKey(@Valid @RequestBody PasswordKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passwordKeyService.saveKey(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKey(@PathVariable Integer id, @RequestParam String owner) {
        passwordKeyService.deleteKey(id, owner);
        return ResponseEntity.noContent().build();
    }
}
