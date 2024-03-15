package com.vaadin.VaadinFramework.Temp;

import com.vaadin.VaadinFramework.Model.UserData;
import org.apache.catalina.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DataHoldFunction {

    public List<UserData> userDet = new ArrayList<>();


    public List<UserData> allUserDetails(UserData u){

        UserData user = new UserData();
        userDet.add(user);

        return userDet;

    }

    public List<UserData> sendDet(){
        return userDet;
    }
}
