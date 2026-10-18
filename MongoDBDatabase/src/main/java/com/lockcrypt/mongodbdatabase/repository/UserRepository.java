package com.lockcrypt.mongodbdatabase.repository;

import com.lockcrypt.mongodbdatabase.model.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserData, String> {

    List<UserData> findByOwner(String owner);

    Optional<UserData> findByIdAndOwner(String id, String owner);
}
