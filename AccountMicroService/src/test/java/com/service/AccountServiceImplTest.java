package com.service;

import com.document.Account;
import com.bean.Transaction;
import com.enums.TransactionType;
import com.exceptions.AccountNotFoundException;
import com.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceImplTest {

    private AccountRepository repo;
    private AccountServiceImpl service;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(AccountRepository.class);
        service = new AccountServiceImpl(repo);
    }

    @Test
    void testGetAccountSuccess() {
        Account a = new Account();
        a.setAccountNumber("JOH1234");
        Mockito.when(repo.findByAccountNumber("JOH1234")).thenReturn(Optional.of(a));

        Account result = service.getAccount("JOH1234");
        assertEquals("JOH1234", result.getAccountNumber());
    }

    @Test
    void testGetAccountFail() {
        Mockito.when(repo.findByAccountNumber("X")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> service.getAccount("X"));
    }

    @Test
    void testCreateAccount() {
        Account a = new Account();
        a.setAccountNumber("ACC001");
        a.setHolderName("John");

        Mockito.when(repo.save(Mockito.any(Account.class))).thenReturn(a);

        Account result = service.createAccount("John");
        assertEquals("John", result.getHolderName());
        assertNotNull(result.getAccountNumber());
    }

    @Test
    void testGetAllTransactionIdsEmpty() {
        Account a = new Account();
        a.setAccountNumber("ACC002");
        a.setTransactions(List.of());

        Mockito.when(repo.findByAccountNumber("ACC002")).thenReturn(Optional.of(a));

        List<Transaction> transactions = service.getAllTransactions("ACC002");
        assertTrue(transactions.isEmpty());
    }

    @Test
    void testGetAllTransactionIdsWithData() {
        Account a = new Account();
        a.setAccountNumber("ACC003");
        Transaction t1 = new Transaction();
        t1.setTransactionId("TXN-001");
        t1.setType(TransactionType.DEPOSIT);
        a.setTransactions(List.of(t1));

        Mockito.when(repo.findByAccountNumber("ACC003")).thenReturn(Optional.of(a));

        List<Transaction> transactions = service.getAllTransactions("ACC003");
        assertEquals(1, transactions.size());
        assertEquals("TXN-001", transactions.get(0).getTransactionId());
    }


    @Test
    void testUpdateAccountSuccess() {
        Account existing = new Account();
        existing.setAccountNumber("ACC123");
        existing.setHolderName("John");
        existing.setStatus("ACTIVE");

        Account updated = new Account();
        updated.setAccountNumber("ACC123");
        updated.setHolderName("John Doe");
        updated.setStatus("ACTIVE");

        // Mock repository findByAccountNumber
        Mockito.when(repo.findByAccountNumber("ACC123")).thenReturn(Optional.of(existing));
        // Mock repository save
        Mockito.when(repo.save(Mockito.any(Account.class))).thenReturn(updated);

        Account result = service.updateAccount(updated);

        assertNotNull(result);
        assertEquals("ACC123", result.getAccountNumber());
        assertEquals("John Doe", result.getHolderName());
    }

    @Test
    void testUpdateAccountFailNotFound() {
        Account updated = new Account();
        updated.setAccountNumber("NONEXIST");
        updated.setHolderName("Someone");

        Mockito.when(repo.findByAccountNumber("NONEXIST")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> service.updateAccount(updated));
    }


    @Test
    void testDeleteAccountSuccess() {
        Account existing = new Account();
        existing.setAccountNumber("ACCDEL");

        Mockito.when(repo.findByAccountNumber("ACCDEL")).thenReturn(Optional.of(existing));
        Mockito.when(repo.save(existing)).thenReturn(existing); // mock save

        boolean deleted = service.deleteAccount("ACCDEL");

        assertTrue(deleted);
        Mockito.verify(repo, Mockito.times(1)).save(existing);  // verify save instead of delete
    }

    @Test
    void testDeleteAccountFailNotFound() {
        Mockito.when(repo.findByAccountNumber("NOTFOUND")).thenReturn(Optional.empty());

        boolean deleted = service.deleteAccount("NOTFOUND");

        assertFalse(deleted);
        Mockito.verify(repo, Mockito.never()).delete(Mockito.any());
    }


}
