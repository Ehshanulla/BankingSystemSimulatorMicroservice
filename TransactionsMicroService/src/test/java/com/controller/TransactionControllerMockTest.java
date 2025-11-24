package com.controller;

import com.document.Transaction;
import com.dto.requests.DepositRequest;
import com.dto.requests.TransferRequest;
import com.dto.requests.WithdrawRequest;
import com.dto.responses.TransactionResponse;
import com.enums.TransactionStatus;
import com.enums.TransactionType;
import com.service.kafkaservices.TransactionKafkaService;
import com.service.operationservice.deposit.TransactionServiceForDeposit;
import com.service.operationservice.transfer.TransactionServiceForTransfer;
import com.service.operationservice.withdraw.TransactionServiceForWithdraw;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionControllerMockTest {

    @Mock
    private TransactionServiceForDeposit depositService;

    @Mock
    private TransactionServiceForWithdraw withdrawService;

    @Mock
    private TransactionServiceForTransfer transferService;

    @Mock
    private TransactionKafkaService transactionKafkaService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void testDeposit() {
        DepositRequest request = new DepositRequest();
        request.setAmount(500);

        Transaction t = new Transaction();
        t.setTransactionId("TXN-20251117-001");
        t.setAmount(500);
        t.setType(TransactionType.DEPOSIT);
        t.setStatus(TransactionStatus.SUCCESS);

        Mockito.when(depositService.deposit("JOH1234", 500)).thenReturn(t);

        TransactionResponse response = transactionController.deposit("JOH1234", request).getBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals("TXN-20251117-001", response.getTransactionId());
        Assertions.assertEquals(TransactionType.DEPOSIT.toString(), response.getType());
        Assertions.assertEquals(500, response.getAmount());
        Assertions.assertEquals(TransactionStatus.SUCCESS.toString(), response.getStatus());
    }

    @Test
    void testWithdraw() {
        WithdrawRequest request = new WithdrawRequest();
        request.setAmount(300);

        Transaction t = new Transaction();
        t.setTransactionId("TXN-20251117-002");
        t.setAmount(300);
        t.setType(TransactionType.WITHDRAW);
        t.setStatus(TransactionStatus.SUCCESS);

        Mockito.when(withdrawService.withdraw("JOH1234", 300)).thenReturn(t);

        TransactionResponse response = transactionController.withdraw("JOH1234", request).getBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals("TXN-20251117-002", response.getTransactionId());
        Assertions.assertEquals(TransactionType.WITHDRAW.toString(), response.getType());
        Assertions.assertEquals(300, response.getAmount());
        Assertions.assertEquals(TransactionStatus.SUCCESS.toString(), response.getStatus());
    }

    @Test
    void testTransfer() {
        TransferRequest request = new TransferRequest();
        request.setFromAccount("A1");
        request.setToAccount("A2");
        request.setAmount(200);

        Transaction t = new Transaction();
        t.setTransactionId("TXN-20251117-003");
        t.setAmount(200);
        t.setType(TransactionType.TRANSFER);
        t.setStatus(TransactionStatus.SUCCESS);

        Mockito.when(transferService.transfer("A1", "A2", 200)).thenReturn(t);

        TransactionResponse response = transactionController.transfer(request).getBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals("TXN-20251117-003", response.getTransactionId());
        Assertions.assertEquals(TransactionType.TRANSFER.toString(), response.getType());
        Assertions.assertEquals(200, response.getAmount());
        Assertions.assertEquals(TransactionStatus.SUCCESS.toString(), response.getStatus());
    }
}
