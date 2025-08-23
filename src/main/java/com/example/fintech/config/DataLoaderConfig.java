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

            repo.save(new BankAccount("HBL", "1025136351009896", "PK50MUCB1025136351009896", "Irfan Gohar"));
            repo.save(new BankAccount("UBL", "1025136351009897", "PK50MUCB1025136351009897", "Aziz Gohar"));
            repo.save(new BankAccount("Easypaisa", "03157073693", null, "Aftab Gohar"));
            repo.save(new BankAccount("Nayapay", "03157073694", null, "Usman Chandio"));
            repo.save(new BankAccount("Jazzcash", "03157073695", null, "Wajid Jokhio"));
            repo.save(new BankAccount("Sadapay", "03157073696", null, "Hamza Chandio")); // example additional wallet
        };
    }

}
