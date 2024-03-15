package com.mongodb.MongoDBDatabase.Repository;

import com.mongodb.MongoDBDatabase.Model.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface  UserRepository extends MongoRepository<UserData,String> {
}
