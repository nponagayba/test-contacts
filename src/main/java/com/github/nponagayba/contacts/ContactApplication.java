package com.github.nponagayba.contacts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = {"com.github.nponagayba.contacts.repository"})
@EntityScan(basePackages = {"com.github.nponagayba.contacts.domain"})
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.github.nponagayba.contacts")
public class ContactApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContactApplication.class, args);
    }
}
