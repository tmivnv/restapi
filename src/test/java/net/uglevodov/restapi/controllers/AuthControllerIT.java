package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.RestapiApplication;
import net.uglevodov.restapi.config.TestAppConfig;
import net.uglevodov.restapi.dto.LoginDto;
import net.uglevodov.restapi.dto.SignupDto;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.entities.UserRole;
import net.uglevodov.restapi.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
public class AuthControllerIT {

    @Autowired
    UserService userService;

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    @Before
    public void setup() {
        Pageable wholePage = Pageable.unpaged();
        for (User user : userService.getAll(wholePage)) {
            userService.delete(user.getId());
        }

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ROLE_USER);
        User user = new User(1L, "test@gmail.com", "password", null, "nickname", "firstName", "lastName", true, LocalDateTime.now(), roles);


        userService.save(user);

    }

    @Test
    public void signin() {


        LoginDto loginDto = new LoginDto();
        loginDto.setPassword("password");
        loginDto.setAuthName("test@gmail.com");

        HttpEntity<LoginDto> entity = new HttpEntity<>(loginDto, headers);


        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/auth/signin",
                HttpMethod.POST, entity, String.class);


        assertTrue((response.getStatusCodeValue() == 200) && response.toString().contains("accessToken"));
    }

    @Test
    public void signup() {
        SignupDto signupDto = new SignupDto();
        signupDto.setEmail("test1@gmail.com");
        signupDto.setPassword("qwerty");
        signupDto.setFirstName("Name");
        signupDto.setLastName("Surname");
        signupDto.setNickname("nick1");


        HttpEntity<SignupDto> entity = new HttpEntity<>(signupDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/auth/signup",
                HttpMethod.POST, entity, String.class);

        String expected = "{\"success\":true,\"message\":\"User successfully registered!\"}";
        assertTrue((response.getStatusCodeValue() == 201) && response.toString().contains(expected));
    }
}