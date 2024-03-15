package com.mysql.MySQLHandle.Repository;

import com.mysql.MySQLHandle.Model.Password;
import org.springframework.data.repository.CrudRepository;

public interface PasswordKeyRepo extends CrudRepository<Password,Integer> {

}
