package com.vaadin.VaadinFramework.Service;

import com.vaadin.VaadinFramework.Configuration.RestTemplateConfig;
import com.vaadin.VaadinFramework.Model.KeysDetails;
import com.vaadin.VaadinFramework.Model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class allKeys { // Renamed from allKeys to follow Java naming conventions

    @Autowired
    private RestTemplateConfig restTemplateConfig;

    public List<KeysDetails> keysReterive() { // Renamed method for clarity
        String keyUrl = "http://localhost:7500/fetchAllKeys";
        ResponseEntity<List<KeysDetails>> responseEntity = restTemplateConfig.restTemplate().exchange(
                keyUrl,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<List<KeysDetails>>() {});

        List<KeysDetails> keys = responseEntity.getBody();
        return keys;
    }

    public void KeySend(String u) {
        String url = "http://localhost:7500/sendKeys?keyOfPassword="+u;
        System.out.println("url \n"+ url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(u, headers);

        ResponseEntity<String> responseEntity = restTemplateConfig.restTemplate().exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        String response = responseEntity.getBody();
        System.out.println("key sent");
//        System.out.println(response);
    }
}
