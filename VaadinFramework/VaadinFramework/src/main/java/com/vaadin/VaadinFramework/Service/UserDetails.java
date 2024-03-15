package com.vaadin.VaadinFramework.Service;

import com.vaadin.VaadinFramework.Configuration.RestTemplateConfig;
import com.vaadin.VaadinFramework.Model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetails {

    @Autowired
    private RestTemplateConfig restTemplateConfig;

    public List<UserData> DataReterive(){
        String url= "http://localhost:8000/UserDataSend";
        ResponseEntity<List<UserData>> responseEntity = restTemplateConfig.restTemplate().exchange(
                url,
                HttpMethod.POST,  // Adjust the HttpMethod based on your endpoint
                null,
                new ParameterizedTypeReference<List<UserData>>() {});

        List<UserData> userDataList = responseEntity.getBody();
        return userDataList;
    }

    public void DataSend(UserData u) {
        String url = "http://localhost:8000/UserDataReceive";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserData> requestEntity = new HttpEntity<>(u, headers);

        ResponseEntity<String> responseEntity = restTemplateConfig.restTemplate().exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        String response = responseEntity.getBody();
//        System.out.println(response);
    }

}
