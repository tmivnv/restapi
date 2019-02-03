package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.RestapiApplication;
import net.uglevodov.restapi.config.TestAppConfig;
import net.uglevodov.restapi.dto.LoginDto;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.entities.UserRole;
import net.uglevodov.restapi.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestapiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestAppConfig.class})
@ActiveProfiles("test")
public class UserControllerIT {
    @Autowired
    UserService userService;

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    private User user;

    @Before
    public void setup() {
        Pageable wholePage = Pageable.unpaged();
        for (User user : userService.getAll(wholePage)) {
            userService.delete(user.getId());
        }

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ROLE_USER);
        user = new User(1L, "test@gmail.com", "password", null, "Alex", "firstName", "lastName", true, LocalDateTime.now(), roles);


        user = userService.save(user);

        LoginDto loginDto = new LoginDto();
        loginDto.setPassword("password");
        loginDto.setAuthName("test@gmail.com");

        HttpEntity<LoginDto> entity = new HttpEntity<>(loginDto, headers);


        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/auth/signin",
                HttpMethod.POST, entity, String.class);

        String body = response.getBody();
        String token = body.split(",")[0].split(":")[1];

        this.headers.add("Authorization", "Bearer " + token.substring(1, token.length() - 1));

    }

    @Test
    public void get() {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/users/get?id=" + user.getId(),
                HttpMethod.GET, entity, String.class);

        String expected = "Alex";

        assertTrue(response.getBody().contains(expected) && response.getStatusCodeValue() == 200);

    }

    @Test
    public void update() {
    }

    @Test
    public void changePassword() {
    }

    @Test
    public void getAll() {
    }
}