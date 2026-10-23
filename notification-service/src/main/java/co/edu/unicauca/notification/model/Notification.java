package co.edu.unicauca.notification.model;

import java.time.LocalDateTime;

/**
 * Clase Notification <<Entity>> de la Figura 1.
 * Metodos:
 * + sendEmail(): void
 * + markAsSent(): void
 */
public class Notification {
    private Long id;
    private Long recipientId;
    private NotificationType type;
    private String subject;
    private String message;
    private NotificationStatus status;
    private LocalDateTime sentAt;

    public Notification() {
    }

    public Notification(Long id, Long recipientId, NotificationType type, String subject, String message) {
        this.id = id;
        this.recipientId = recipientId;
        this.type = type;
        this.subject = subject;
        this.message = message;
        this.status = NotificationStatus.PENDING;
    }

    public void sendEmail() {
        System.out.println("[NotificationService - Email] Enviando correo a destinatario ID " + recipientId
                + " | Asunto: " + subject + " | Mensaje: " + message);
        markAsSent();
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
