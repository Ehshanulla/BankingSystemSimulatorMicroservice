package com.service;


import com.document.Transaction;
import com.enums.TransactionType;
import com.service.factory.DepositTransactionFactory;
import com.service.factory.TransferTransactionFactory;
import com.service.factory.TxnSequenceService;
import com.service.factory.WithDrawTransactionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionFactoryTest {

    @Test
    void testDepositFactoryCreatesCorrectTransaction() {

        TxnSequenceService seqMock = mock(TxnSequenceService.class);
        when(seqMock.nextSequence()).thenReturn(101);

        DepositTransactionFactory factory = new DepositTransactionFactory(seqMock);

        Transaction tx = factory.create(TransactionType.DEPOSIT,"ACC001", 500);

        assertEquals("TXN-20251123-101", tx.getTransactionId());
        assertEquals("ACC001", tx.getSourceAccountId());
        assertEquals(500, tx.getAmount());
        assertEquals(TransactionType.DEPOSIT, tx.getType());
    }

    @Test
    void testWithdrawFactoryCreatesCorrectTransaction() {

        TxnSequenceService seqMock = mock(TxnSequenceService.class);
        when(seqMock.nextSequence()).thenReturn(202);

        WithDrawTransactionFactory factory = new WithDrawTransactionFactory(seqMock);

        Transaction tx = factory.create(TransactionType.WITHDRAW,"ACC002", 300);

        assertEquals("TXN-20251123-202", tx.getTransactionId());
        assertEquals("ACC002", tx.getSourceAccountId());
        assertEquals(300, tx.getAmount());
        assertEquals(TransactionType.WITHDRAW, tx.getType());
    }

    @Test
    void testTransferFactoryCreatesCorrectTransaction() {

        TxnSequenceService seqMock = mock(TxnSequenceService.class);
        when(seqMock.nextSequence()).thenReturn(303);

        TransferTransactionFactory factory = new TransferTransactionFactory(seqMock);

        Transaction tx = factory.create(TransactionType.TRANSFER,"SRC123", "DST456", 700);

        assertEquals("TXN-20251123-303", tx.getTransactionId());
        assertEquals("SRC123", tx.getSourceAccountId());
        assertEquals("DST456", tx.getDestinationAccountId());
        assertEquals(700, tx.getAmount());
        assertEquals(TransactionType.TRANSFER, tx.getType());
    }
}
