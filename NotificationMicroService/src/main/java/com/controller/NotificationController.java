package com.controller;

import com.dto.TransactionNotification;
import com.service.NotificationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Queue;

@RestController
@RequestMapping("/api/notifications/")
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    public NotificationController(NotificationServiceImpl notificationService){
        this.notificationService = notificationService;
    }

    @GetMapping(value = "send")
    public ResponseEntity<Queue<String>>  getnotification(){
        return ResponseEntity.ok(notificationService.getNotifications());
    }
}
