package com.service.factory;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TxnSequenceService {

    private LocalDate lastDate = LocalDate.now();
    private int sequence = 0;

    public synchronized int nextSequence() {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastDate)) {
            sequence = 0; // reset sequence for new day
            lastDate = today;
        }
        sequence++;
        return sequence;
    }
}
