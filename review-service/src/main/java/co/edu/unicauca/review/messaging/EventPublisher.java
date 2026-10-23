package co.edu.unicauca.review.messaging;

import co.edu.unicauca.review.config.RabbitMQConfig;
import co.edu.unicauca.review.dto.ReviewerAssignedEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publicador de eventos de dominio hacia RabbitMQ (Guia: EventPublisher.java).
 */
@Component
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishReviewerAssigned(ReviewerAssignedEventDTO event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_REVIEWER_ASSIGNED,
                event
        );
    }
}
