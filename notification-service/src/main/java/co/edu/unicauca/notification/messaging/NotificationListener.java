package co.edu.unicauca.notification.messaging;

import co.edu.unicauca.notification.config.RabbitMQConfig;
import co.edu.unicauca.notification.dto.ReviewerAssignedEventDTO;
import co.edu.unicauca.notification.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor asincrono de eventos ReviewerAssigned (Guia: NotificationListener.java).
 */
@Component
public class NotificationListener {

    private final EmailService emailService;

    public NotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_REVIEWER_ASSIGNED)
    public void handleReviewerAssigned(ReviewerAssignedEventDTO event) {
        System.out.println("[NotificationService] Recibido evento ReviewerAssigned para pregunta ID: "
                + event.getQuestionId() + ", revisor ID: " + event.getReviewerId());
        emailService.sendReviewerAssignedNotification(event);
    }
}
