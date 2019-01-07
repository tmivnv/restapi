package net.uglevodov.restapi;

import net.uglevodov.restapi.config.AppConfig;
import net.uglevodov.restapi.config.TestAppConfig;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.entities.UserRole;
import net.uglevodov.restapi.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class})
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application.properties")
public class IntegrationTests {

    @Autowired
    UserService service;

    @Test
    public void contextLoads() {

    }

    @Test
    public void userServiceTest()
    {
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ROLE_USER);
        User user = new User(1L,"test@test.ru","password",null,"nickname","firstName","lastName",true, LocalDate.now(), roles );


        service.save(user);

        User found = service.get(user.getId());

        junit.framework.Assert.assertEquals(user, found);

    }

}

