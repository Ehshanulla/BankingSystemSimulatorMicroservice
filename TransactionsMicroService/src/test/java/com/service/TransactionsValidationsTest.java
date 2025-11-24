package com.service;

import com.bean.Account;
import com.document.Transaction;
import com.enums.TransactionStatus;
import com.enums.TransactionType;
import com.exceptions.InsufficientBalanceException;
import com.exceptions.InvalidAmountException;
import com.service.accountmanager.AccountBalanceManager;
import com.service.logs.DefaultTransactionLogService;
import com.service.processor.DepositProcessor;
import com.service.processor.TransferProcessor;
import com.service.processor.WithdrawProcessor;
import com.service.validators.DefaultTransactionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionsValidationsTest {

    private final RestTemplate rest = mock(RestTemplate.class);
    private final DefaultTransactionLogService logService = mock(DefaultTransactionLogService.class);
    private final AccountBalanceManager balanceManager = mock(AccountBalanceManager.class);
    private final DefaultTransactionValidator validator = new DefaultTransactionValidator();

    // ---------------------------------------------------------
    // ✔ DepositProcessor Test
    // ---------------------------------------------------------
    @Test
    void testDepositSuccess() {

        DepositProcessor processor =
                new DepositProcessor(rest, logService, validator, balanceManager);

        Account acc = new Account();
        acc.setAccountNumber("A");
        acc.setBalance(0);

        when(rest.getForObject("http://ACCOUNTMICROSERVICE/api/accounts/A",
                Account.class)).thenReturn(acc);

        Transaction t = new Transaction();
        t.setTransactionId("TXN-001");
        t.setSourceAccountId("A");
        t.setAmount(500);
        t.setType(TransactionType.DEPOSIT);

        Transaction result = processor.process(t);

        assertEquals(TransactionStatus.SUCCESS, result.getStatus());
        verify(balanceManager).credit(acc, 500);
        verify(logService).save(t);
    }

    // ---------------------------------------------------------
    // ✔ WithdrawProcessor Success Test
    // ---------------------------------------------------------
    @Test
    void testWithdrawSuccess() {

        WithdrawProcessor processor =
                new WithdrawProcessor(rest, validator, logService, balanceManager);

        Account acc = new Account();
        acc.setAccountNumber("B");
        acc.setBalance(1000);

        when(rest.getForObject("http://ACCOUNTMICROSERVICE/api/accounts/B",
                Account.class)).thenReturn(acc);

        Transaction t = new Transaction();
        t.setTransactionId("TXN-002");
        t.setSourceAccountId("B");
        t.setAmount(500);
        t.setType(TransactionType.WITHDRAW);

        Transaction result = processor.process(t);

        assertEquals(TransactionStatus.SUCCESS, result.getStatus());
        verify(balanceManager).debit(acc, 500);
        verify(logService).save(t);
    }

    // ---------------------------------------------------------
    // ❌ WithdrawProcessor – Insufficient Balance
    // ---------------------------------------------------------
    @Test
    void testWithdrawInsufficientBalance() {

        WithdrawProcessor processor =
                new WithdrawProcessor(rest, validator, logService, balanceManager);

        Account acc = new Account();
        acc.setAccountNumber("C");
        acc.setBalance(100);

        when(rest.getForObject("http://ACCOUNTMICROSERVICE/api/accounts/C",
                Account.class)).thenReturn(acc);

        Transaction t = new Transaction();
        t.setTransactionId("TXN-003");
        t.setSourceAccountId("C");
        t.setAmount(500);
        t.setType(TransactionType.WITHDRAW);

        assertThrows(InsufficientBalanceException.class,
                () -> processor.process(t));
    }

    // ---------------------------------------------------------
    // ✔ TransferProcessor Success Test
    // ---------------------------------------------------------
    @Test
    void testTransferSuccess() {

        TransferProcessor processor =
                new TransferProcessor(rest, validator, logService, balanceManager);

        Account src = new Account();
        src.setAccountNumber("SRC");
        src.setBalance(1000);

        Account dst = new Account();
        dst.setAccountNumber("DST");
        dst.setBalance(100);

        when(rest.getForObject("http://ACCOUNTMICROSERVICE/api/accounts/SRC",
                Account.class)).thenReturn(src);

        when(rest.getForObject("http://ACCOUNTMICROSERVICE/api/accounts/DST",
                Account.class)).thenReturn(dst);

        Transaction t = new Transaction();
        t.setTransactionId("TXN-004");
        t.setSourceAccountId("SRC");
        t.setDestinationAccountId("DST");
        t.setAmount(200);
        t.setType(TransactionType.TRANSFER);

        Transaction result = processor.process(t);

        assertEquals(TransactionStatus.SUCCESS, result.getStatus());
        verify(balanceManager).debit(src, 200);
        verify(balanceManager).credit(dst, 200);
        verify(logService).save(t);
    }

    // ---------------------------------------------------------
    // ✔ Validator – Invalid Amount
    // ---------------------------------------------------------
    @Test
    void testInvalidAmount() {
        Transaction t = new Transaction();
        t.setAmount(0);
        assertThrows(InvalidAmountException.class, () -> validator.validate(t));
    }

    // ---------------------------------------------------------
    // ✔ Validator – Valid Amount
    // ---------------------------------------------------------
    @Test
    void testValidAmount() {
        Transaction t = new Transaction();
        t.setAmount(100);

        validator.validate(t);

        assertEquals(100, t.getAmount());
    }
}
