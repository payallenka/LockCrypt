package com.lockcrypt.mysqlhandle.repository;

import com.lockcrypt.mysqlhandle.model.Password;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordKeyRepo extends CrudRepository<Password, Integer> {

    List<Password> findByOwner(String owner);

    Optional<Password> findByIdAndOwner(Integer id, String owner);
}
