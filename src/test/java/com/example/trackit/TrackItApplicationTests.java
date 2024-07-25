package com.example.trackit;

import com.example.trackit.service.UserDataService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClient;



@SpringBootTest
class TrackItApplicationTests {
    @MockBean
    private UserDataService userDataService;


    @Test
    void contextLoads() {
    }
}
