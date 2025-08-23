package com.example.fintech.repository;


import com.example.fintech.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
//    Optional<BankAccount> findByBankNameAndIbanOrAccount(String bankName, String ibanOrAccount);
    @Query("SELECT b FROM BankAccount b WHERE b.bankName = :bankName AND b.accountNumber = :account")
    Optional<BankAccount> findByBankNameAndAccount(
            @Param("bankName") String bankName,
            @Param("account") String account
    );

    @Query("SELECT b FROM BankAccount b WHERE b.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumber(String accountNumber);

    @Query("SELECT b FROM BankAccount b WHERE b.accountNumber = :value OR b.ibanNumber = :value")
    Optional<BankAccount> findByAccountNumberOrIbanNumber(@Param("value") String value);

//    Optional<BankAccount> findByAccountNumber(String accountNumber);
//    Optional<BankAccount> findByIbanNumber(String ibanNumber);


}

