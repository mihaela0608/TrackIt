package com.example.trackit.service;

import com.example.trackit.model.dto.UserData;
import com.example.trackit.model.dto.UserDetailsDto;
import org.springframework.http.MediaType;
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

    public UserDetailsDto mapToDetails(UserDetailsDto userDetailsDto) {
        return  restClient.get()
                .uri("http://localhost:8081/api/userdata/" + userDetailsDto.getId())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(UserDetailsDto.class);
    }
}
