package com.example.trackit.service;

import com.example.trackit.model.dto.UserData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserDataService {
    private final RestClient restClient;

    public UserDataService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void saveUserData(UserData userData) {
        restClient.post()
                .uri("http://localhost:8081/api/userdata")
                .body(userData)
                .retrieve();
    }
    public void updateUserData(UserData userData){
        restClient.post()
                .uri("http://localhost:8081/api/userdata/update")
                .body(userData)
                .retrieve();
    }
}
