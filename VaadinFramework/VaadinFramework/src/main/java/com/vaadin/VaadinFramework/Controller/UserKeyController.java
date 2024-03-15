package com.vaadin.VaadinFramework.Controller;

import com.vaadin.VaadinFramework.Model.KeysDetails;
import com.vaadin.VaadinFramework.Service.allKeys; // Update import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserKeyController {

    @Autowired
    private allKeys allKeysService; // Update service reference

    @PostMapping("receiveAllKeys")
    @ResponseBody
    public ResponseEntity<List<KeysDetails>> getKeys() {
        List<KeysDetails> allKeys = allKeysService.keysReterive(); // Update method call

        System.out.println(allKeys.toString());

        for (KeysDetails k : allKeys) {
            System.out.println(k);
        }
        return ResponseEntity.ok(allKeys);
    }
}
