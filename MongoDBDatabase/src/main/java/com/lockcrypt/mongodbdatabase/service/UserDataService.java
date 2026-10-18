package com.lockcrypt.mongodbdatabase.service;

import com.lockcrypt.mongodbdatabase.model.UserData;
import com.lockcrypt.mongodbdatabase.model.UserDataRequest;
import com.lockcrypt.mongodbdatabase.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDataService {

    private final UserRepository userRepository;

    public UserDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserData> getUserData(String owner) {
        return userRepository.findByOwner(owner);
    }

    public UserData saveUserData(UserDataRequest request) {
        UserData userData = new UserData();
        userData.setOwner(request.owner());
        userData.setAccountFor(request.accountFor());
        userData.setUsername(request.username());
        userData.setPassword(request.password());
        userData.setKeyId(request.keyId());
        return userRepository.save(userData);
    }

    public void deleteUserData(String id, String owner) {
        UserData userData = userRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new UserDataNotFoundException(id));
        userRepository.delete(userData);
    }
}
