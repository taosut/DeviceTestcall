package com.emailgw.master.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class checkEmail {
    @Value("${spring.application.name}")
    private String name;
}
