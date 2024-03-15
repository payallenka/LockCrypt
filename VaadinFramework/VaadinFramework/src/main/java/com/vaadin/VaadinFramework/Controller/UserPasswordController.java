//package com.vaadin.VaadinFramework.Controller;
//
//
//import com.vaadin.VaadinFramework.Model.UserData;
//import com.vaadin.VaadinFramework.Service.UserDetails;
//import com.vaadin.VaadinFramework.Temp.DataHoldFunction;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import com.vaadin.VaadinFramework.Service.UserDetails.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/")
//public class UserPasswordController {
//    @Autowired
//    private UserDetails userDetails;
//
//    @Autowired
//    private DataHoldFunction temp;
//
//    @PostMapping("userPasswordDetailsFetch")
//    @ResponseBody
//    public ResponseEntity<List<UserData>> getData() {
//
////        System.out.println(userDetails.DataReterive().toString());
//        List<UserData> userDataList = userDetails.DataReterive();
//
//        System.out.println(userDataList.toString());
//
//        for(UserData u: userDataList){
////            System.out.println(u);
//            temp.allUserDetails(u);
//        }
//        return ResponseEntity.ok(userDataList);
//    }
//}
