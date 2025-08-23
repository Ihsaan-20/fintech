package com.example.fintech.service;



import com.example.fintech.model.User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UtilityService {

    public String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 4; i++) {  // 4 groups
            if (i > 0) {
                cardNumber.append("-");
            }
            for (int j = 0; j < 4; j++) {  // 4 digits per group
                int digit = random.nextInt(10);
                cardNumber.append(digit);
            }
        }
        return cardNumber.toString();
    }

}
