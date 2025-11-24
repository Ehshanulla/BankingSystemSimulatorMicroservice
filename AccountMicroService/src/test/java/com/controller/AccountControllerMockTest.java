package com.controller;

import com.controller.AccountController;
import com.document.Account;
import com.bean.Transaction;
import com.dto.requests.AccountCreateRequest;
import com.dto.responses.AccountResponse;
import com.dto.responses.TransactionResponse;
import com.enums.TransactionStatus;
import com.enums.TransactionType;
import com.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerMockTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void testCreateAccount() {
        AccountCreateRequest account = new AccountCreateRequest();
        account.setHolderName("John Doe");

        Account acc = new Account();

        acc.setAccountNumber("JOH1234");
        acc.setHolderName(account.getHolderName());

        Mockito.when(accountService.createAccount("John Doe")).thenReturn(acc);

        var responseEntity = accountController.createAccount(account);
        AccountResponse response = responseEntity.getBody();

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertNotNull(response);
        assertEquals("JOH1234", response.getAccountNumber());
        assertEquals("John Doe", response.getHolderName());
    }

    @Test
    void testGetAccount() {
        Account account = new Account();
        account.setAccountNumber("JOH1234");
        account.setHolderName("John Doe");

        Mockito.when(accountService.getAccount("JOH1234")).thenReturn(account);

        var responseEntity = accountController.getAccount("JOH1234");
        AccountResponse response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(response);
        assertEquals("JOH1234", response.getAccountNumber());
        assertEquals("John Doe", response.getHolderName());
    }

    @Test
    void testGetAccountTransactions() {
        Transaction t1 = new Transaction();
        t1.setTransactionId("TXN-20251107-001");
        t1.setAmount(500);
        t1.setType(TransactionType.DEPOSIT);
        t1.setStatus(TransactionStatus.SUCCESS);

        Transaction t2 = new Transaction();
        t2.setTransactionId("TXN-20251107-002");
        t2.setAmount(300);
        t2.setType(TransactionType.WITHDRAW);
        t2.setStatus(TransactionStatus.SUCCESS);

        Mockito.when(accountService.getAllTransactions("JOH1234"))
                .thenReturn(List.of(t1, t2));

        var responseEntity = accountController.getAccountTransactions("JOH1234");
        List<TransactionResponse> list = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(list);
        assertEquals(2, list.size());

        assertEquals("TXN-20251107-001", list.get(0).getTransactionId());
        assertEquals(TransactionType.DEPOSIT.toString(), list.get(0).getType());
        assertEquals(500, list.get(0).getAmount());
        assertEquals("SUCCESS", list.get(0).getStatus());

        assertEquals("TXN-20251107-002", list.get(1).getTransactionId());
        assertEquals(TransactionType.WITHDRAW.toString(), list.get(1).getType());
        assertEquals(300, list.get(1).getAmount());
        assertEquals("SUCCESS", list.get(1).getStatus());
    }

    @Test
    public void testUpdateAccount_Success() {
        Account request = new Account();
        request.setAccountNumber("12345");
        request.setBalance(1000.0);

        Account updatedAccount = new Account();
        updatedAccount.setAccountNumber("12345");
        updatedAccount.setBalance(1500.0);

        // Mock service behavior
        Mockito.when(accountService.updateAccount(request)).thenReturn(updatedAccount);

        ResponseEntity<AccountResponse> response = accountController.updateAccount(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("12345", response.getBody().getAccountNumber());
        assertEquals(1500.0, response.getBody().getBalance());
    }

    // ======================== DELETE /{accountNumber} ========================
    @Test
    public void testDeleteAccount_Success() {
        String accountNumber = "12345";

        Mockito.when(accountService.deleteAccount(accountNumber)).thenReturn(true);

        ResponseEntity<Void> response = accountController.deleteAccount(accountNumber);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteAccount_NotFound() {
        String accountNumber = "99999";

        Mockito.when(accountService.deleteAccount(accountNumber)).thenReturn(false);

        ResponseEntity<Void> response = accountController.deleteAccount(accountNumber);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
