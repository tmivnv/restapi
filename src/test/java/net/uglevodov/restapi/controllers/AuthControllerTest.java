package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.RestapiApplication;
import net.uglevodov.restapi.dto.LoginDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestapiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    @Test
    public void signin() {


        LoginDto loginDto = new LoginDto();
        loginDto.setPassword("qwerty");
        loginDto.setAuthName("test@gmail.com");

        HttpEntity<LoginDto> entity = new HttpEntity<>(loginDto, headers);



        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:"+port+"/api/auth/signin",
                HttpMethod.POST, entity, String.class);


        assertTrue((response.getStatusCodeValue()==200)&&response.toString().contains("accessToken"));
    }
}