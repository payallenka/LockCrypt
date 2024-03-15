package com.mysql.MySQLHandle.Controller;


import com.mysql.MySQLHandle.Model.Password;
import com.mysql.MySQLHandle.Repository.PasswordKeyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class PasswordController {

    @Autowired
    private PasswordKeyRepo passwordKeyRepo;

    @PostMapping("sendKeys")
    public @ResponseBody Password newKey(@RequestParam String keyOfPassword){

        Password passwordKey = new Password();
        System.out.println(keyOfPassword);
        passwordKey.setPasswordKey(keyOfPassword);
        passwordKeyRepo.save(passwordKey);
        System.out.println("from mysql end");
        return passwordKey;
    }

    @PostMapping("fetchAllKeys")
    public @ResponseBody Iterable<Password> getAllKeys(){
        return passwordKeyRepo.findAll();
    }
}
