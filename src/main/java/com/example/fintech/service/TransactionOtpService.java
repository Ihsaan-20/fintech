package com.example.fintech.service;


import com.example.fintech.dto.CompleteTransferDTO;
import com.example.fintech.dto.ReceiverAccountDTO;
import com.example.fintech.dto.VerifyReceiverRequestDTO;
import com.example.fintech.model.*;
import com.example.fintech.repository.TransactionOtpRepository;
import com.example.fintech.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransactionOtpService {
    private static final Logger log = LoggerFactory.getLogger(TransactionOtpService.class);
    private final TransactionOtpRepository transactionOtpRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final BankAccountService bankAccountService;

    // OTP validity time in minutes
    private final int OTP_EXPIRY_MINUTES = 1;

    public String generateAndSaveOtp(
            Long userId,
            String receiverInput,
            String transferType,
            String bankName,
            BigDecimal amount
    ) {
        // 1. Generate OTP
        String otp = String.valueOf((int)(Math.random() * 9000) + 1000);

        // 2. Save to table
        TransactionOtp transactionOtp = new TransactionOtp();
        transactionOtp.setUserId(userId);
        transactionOtp.setReceiverInput(receiverInput);
        transactionOtp.setTransferType(transferType);
        transactionOtp.setBankName(bankName);
        transactionOtp.setAmount(amount);
        transactionOtp.setOtp(otp);
        transactionOtp.setCreatedAt(LocalDateTime.now());
        transactionOtp.setExpiresAt(LocalDateTime.now().plusMinutes(1));

        transactionOtpRepository.save(transactionOtp);

        return otp;
    }

    public boolean completeTransfer(Long senderUserId, CompleteTransferDTO request) {
        log.info("CompleteTransferDTO request: {}", request);
        // 1. Validate OTP
        boolean validOtp = this.validateOtp(senderUserId, request.getReceiverInput(), request.getOtp());
        if (!validOtp) {
            return false;
        }

        // 2. Find sender User
        User sender = userService.findById(senderUserId)
                .orElseThrow(() -> new RuntimeException("Sender user not found"));

        // 3. Prepare receiver info
        User receiver = null;
        String noteTo = "";

        if ("BANKS".equalsIgnoreCase(request.getTransferType())) {
            // BANK transfer - only note the receiver's name
            BankAccount receiverAccount = bankAccountService.findByAccount(request.getReceiverInput())
                    .orElseThrow(() -> new RuntimeException("Bank account not found"));
            noteTo = receiverAccount.getAccountHolderName();

        }else if ("OTHERWALLET".equalsIgnoreCase(request.getTransferType())) {
            // BANK transfer - only note the receiver's name
            BankAccount receiverAccount = bankAccountService.findByAccount(request.getReceiverInput())
                    .orElseThrow(() -> new RuntimeException("Bank account not found"));
            noteTo = receiverAccount.getAccountHolderName();

        }else if ("FINTECH".equalsIgnoreCase(request.getTransferType())) {
            // FINTECH transfer - full user info
            receiver = userService.findByMobileNumber(request.getReceiverInput())
                    .orElseThrow(() -> new RuntimeException("Receiver user not found"));
            noteTo = receiver.getFirstName() + " " + receiver.getLastName();
        }

        // 4. Calculate new sender balance
        BigDecimal senderBalance = sender.getBalance();
        BigDecimal newSenderBalance = senderBalance.subtract(request.getAmount());

        if (newSenderBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 5. Determine transaction type
        TransactionType transType = determineTransactionType(
                request.getTransferType(),
                request.getBankName()
        );

        // 6. Create transaction
        Transaction txn = new Transaction(
                sender,
                receiver, // can be null for BANK
                request.getAmount(),
                "Transfer to " + noteTo,
                transType,
                TransactionStatus.SUCCESS,
                newSenderBalance
        );

        // 7. Save transaction
        transactionRepository.save(txn);

        // 8. Update sender balance
        sender.setBalance(newSenderBalance);
        userService.save(sender);

        // 9. Update receiver balance only if FINTECH
        if ("FINTECH".equalsIgnoreCase(request.getTransferType()) && receiver != null) {
            BigDecimal receiverBalance = receiver.getBalance();
            BigDecimal newReceiverBalance = receiverBalance.add(request.getAmount());
            receiver.setBalance(newReceiverBalance);
            userService.save(receiver);
        }

        // 10. Invalidate OTP if needed (Not implemented here)

        return true;
    }



    // Dynamic transaction type determination
    private TransactionType determineTransactionType(String transferType, String bankName) {
        // Wallet providers
        List<String> wallets = Arrays.asList(
                "Easypaisa", "Nayapay", "Jazzcash", "Sadapay"
        );

        // Traditional banks
        List<String> banks = Arrays.asList(
                "HBL", "UBL", "MCB", "Meezan", "ABL", "NBP",
                "Standard Chartered", "Faysal Bank", "Bank Alfalah", "Askari Bank"
        );

        if ("FINTECH".equalsIgnoreCase(transferType)) {
            return TransactionType.WALLET_TRANSFER;
        }

        if ("OTHERWALLET".equalsIgnoreCase(transferType)) {
            return TransactionType.WALLET_TRANSFER;
        }

        if ("BANKS".equalsIgnoreCase(transferType)) {
            if (bankName != null) {
                boolean isWallet = wallets.stream()
                        .anyMatch(w -> w.equalsIgnoreCase(bankName));

                if (isWallet) {
                    return TransactionType.WALLET_TRANSFER;
                }

                boolean isBank = banks.stream()
                        .anyMatch(b -> b.equalsIgnoreCase(bankName));

                if (isBank) {
                    return TransactionType.IBFT; // Inter Bank Funds Transfer
                }
            }
            return TransactionType.IBFT; // fallback for BANKS
        }

        if ("RAAST".equalsIgnoreCase(transferType)) {
            return TransactionType.RAAST;
        }

        return TransactionType.OTHER; // fallback for anything else
    }


    public boolean validateOtp(Long userId, String receiverInput, String otp) {
        // 1. Fetch OTP record from transaction_otp table by userId and receiverInput,
        //    and ensure it's not expired (for example, created within 1 min).
        Optional<TransactionOtp> otpRecordOpt = transactionOtpRepository
                .findTopByUserIdAndReceiverInputOrderByCreatedAtDesc(userId, receiverInput);

        if (otpRecordOpt.isEmpty()) {
            return false; // No OTP record found
        }

        TransactionOtp otpRecord = otpRecordOpt.get();

        // 2. Check OTP matches
        if (!otpRecord.getOtp().equals(otp)) {
            return false;
        }

        // 3. Check OTP is not expired (e.g., 1 min validity)
        LocalDateTime now = LocalDateTime.now();
        if (otpRecord.getExpiresAt() == null || now.isAfter(otpRecord.getExpiresAt())) {
            return false; // OTP expired or invalid expiresAt
        }

        // 4. Optionally: Invalidate OTP after use (update or delete OTP record)

        return true;
    }




}
