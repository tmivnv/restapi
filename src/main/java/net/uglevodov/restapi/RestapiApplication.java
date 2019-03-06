package net.uglevodov.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.sql.DataSource;

@SpringBootApplication

public class RestapiApplication  {


    public static void main(String[] args) {
        SpringApplication.run(RestapiApplication.class, args);

    }


}

