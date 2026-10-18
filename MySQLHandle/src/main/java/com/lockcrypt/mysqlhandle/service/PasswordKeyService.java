package com.lockcrypt.mysqlhandle.service;

import com.lockcrypt.mysqlhandle.model.Password;
import com.lockcrypt.mysqlhandle.model.PasswordKeyRequest;
import com.lockcrypt.mysqlhandle.repository.PasswordKeyRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordKeyService {

    private final PasswordKeyRepo passwordKeyRepo;

    public PasswordKeyService(PasswordKeyRepo passwordKeyRepo) {
        this.passwordKeyRepo = passwordKeyRepo;
    }

    public List<Password> getKeys(String owner) {
        return passwordKeyRepo.findByOwner(owner);
    }

    public Password saveKey(PasswordKeyRequest request) {
        Password passwordKey = new Password();
        passwordKey.setOwner(request.owner());
        passwordKey.setPasswordKey(request.passwordKey());
        return passwordKeyRepo.save(passwordKey);
    }

    public void deleteKey(Integer id, String owner) {
        Password passwordKey = passwordKeyRepo.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new KeyNotFoundException(id));
        passwordKeyRepo.delete(passwordKey);
    }
}
