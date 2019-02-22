/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.config;

import net.uglevodov.restapi.controllers.AuthController;
import net.uglevodov.restapi.security.JwtAuthenticationEntryPoint;
import net.uglevodov.restapi.security.JwtAuthenticationFilter;
import net.uglevodov.restapi.security.JwtTokenProvider;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.UserService;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(value = "net.uglevodov.restapi")
@EnableTransactionManagement
@Profile("test")
//@ComponentScan(basePackages = {"net.uglevodov.restapi"}) //так не грузит контекст
@ComponentScan(basePackages = {"net.uglevodov.restapi.service", "net.uglevodov.restapi.repositories", "net.uglevodov.restapi.entities"})
//так грузит, но сервис в тесте не срабатывает
public class TestAppConfig extends WebMvcConfigurerAdapter {
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:~/test");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        em.setPackagesToScan("net.uglevodov.restapi");

        return em;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager (EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }
    @Bean
    Properties additionalProperties(){
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.enable_lazy_load_no_trans", "false");
        properties.setProperty("hibernate.id.new_generator_mappings", "false");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        return properties;
    }


}