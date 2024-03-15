package com.mongodb.MongoDBDatabase.Controller;

import com.mongodb.MongoDBDatabase.Model.UserData;
import com.mongodb.MongoDBDatabase.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserDataController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("UserDataSend")
    public @ResponseBody List<UserData> UserDataFromDB(){
        return userRepository.findAll();
    }

    @PostMapping("UserDataReceive")
    public ResponseEntity<String> UserDataFromFront(@RequestBody UserData u){
        userRepository.save(u);
        return ResponseEntity.status(HttpStatus.OK).body("Details added successfully");
    }

    @RequestMapping("/hello")
    public String test(){
        return "helloWorld";
    }
}
