package com.example.fintech.service;


import com.example.fintech.model.BankAccount;
import com.example.fintech.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public Optional<BankAccount> verifyBankAccount(String bankName, String account) {
        return bankAccountRepository.findByBankNameAndAccount(bankName, account);
    }

    // In BankAccountService
    public Optional<BankAccount> findByAccount(String input) {
        return bankAccountRepository.findByAccountNumber(input);
    }

//    public Optional<BankAccount> findByAccount(String input) {
//        Optional<BankAccount> found = bankAccountRepository.findByAccountNumber(input);
//
//        if (found.isEmpty() && input.toUpperCase().startsWith("PK")) {
//            found = bankAccountRepository.findByIbanNumber(input);
//        }
//
//        return found; //
//    }


    // You can add other methods as needed
}
