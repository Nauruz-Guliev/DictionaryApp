package ru.kpfu.itis.gnt.languagelearningapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaRepositories
public class LanguageLearningAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(LanguageLearningAppApplication.class, args);
    }

}
