package com.example.jizdnirady;


import com.example.jizdnirady.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"com.example.jizdnirady.services"
        , "com.example.jizdnirady.utility"
        , "com.example.jizdnirady.resources"
        , "com.example.jizdnirady.configurer"
        , "com.example.jizdnirady.filters"
        , "com.example.jizdnirady.repository"
        , "com.example.jizdnirady.connection"
        , "com.example.jizdnirady.scheduled"
        })
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class JizdniRadyApplication {
    public static void main(String[] args) {
        SpringApplication.run(JizdniRadyApplication.class, args);


    }


}
