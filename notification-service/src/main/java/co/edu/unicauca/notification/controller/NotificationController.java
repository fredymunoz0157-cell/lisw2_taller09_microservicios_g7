package co.edu.unicauca.notification.controller;

import co.edu.unicauca.notification.model.Notification;
import co.edu.unicauca.notification.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> listNotifications() {
        return ResponseEntity.ok(emailService.getAllNotifications());
    }

    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<List<Notification>> listByRecipient(@PathVariable Long recipientId) {
        return ResponseEntity.ok(emailService.getNotificationsByRecipient(recipientId));
    }
}
