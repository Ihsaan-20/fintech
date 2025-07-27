package com.example.fintech.config;

import com.example.fintech.model.BankAccount;
import com.example.fintech.repository.BankAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {

    @Bean
    CommandLineRunner loadDummyData(BankAccountRepository repo) {
        return args -> {
            repo.deleteAll();
            repo.save(new BankAccount(1L, "HBL", "PK36HBL01234567890123454", "Irfan Gohar"));
            repo.save(new BankAccount(2L, "UBL", "PK86UBL01234567890123454", "Aziz Gohar"));
            repo.save(new BankAccount(3L, "Easypaisa", "03157073693", "Aftab Gohar"));
            repo.save(new BankAccount(4L, "Nayapay", "03157073694", "Usman Chandio"));
            repo.save(new BankAccount(5L, "Jazzcash", "03157073695", "Wajid Jokhio"));
        };
    }
}
