package co.edu.unicauca.notification.service;

import co.edu.unicauca.notification.dto.ReviewerAssignedEventDTO;
import co.edu.unicauca.notification.model.Notification;
import co.edu.unicauca.notification.model.NotificationType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Servicio de simulacion de envio diferido de notificaciones (Guia: EmailService.java).
 */
@Service
public class EmailService {

    private final List<Notification> notifications = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong idSeq = new AtomicLong(1);

    public Notification sendReviewerAssignedNotification(ReviewerAssignedEventDTO event) {
        Notification notification = new Notification(
                idSeq.getAndIncrement(),
                event.getReviewerId(),
                NotificationType.REVIEWER_ASSIGNED,
                "Nueva pregunta asignada para revision Saber PRO (Pregunta #" + event.getQuestionId() + ")",
                "Se le ha asignado la revision por pares de la pregunta ID " + event.getQuestionId()
                        + " por el administrador ID " + event.getAssignedBy() + "."
        );
        notification.sendEmail();
        notifications.add(notification);
        return notification;
    }

    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications);
    }

    public List<Notification> getNotificationsByRecipient(Long recipientId) {
        return notifications.stream()
                .filter(n -> Objects.equals(n.getRecipientId(), recipientId))
                .collect(Collectors.toList());
    }
}
