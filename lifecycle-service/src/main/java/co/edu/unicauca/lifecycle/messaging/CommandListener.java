package co.edu.unicauca.lifecycle.messaging;

import co.edu.unicauca.lifecycle.dto.SubmitCommandDTO;
import co.edu.unicauca.lifecycle.service.LifecycleService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor Asincrono en Lifecycle Service (Guia Paso a Paso Seccion 4.3).
 */
@Component
public class CommandListener {

    private final LifecycleService lifecycleService;

    public CommandListener(LifecycleService lifecycleService) {
        this.lifecycleService = lifecycleService;
    }

    @RabbitListener(queues = "question.submit.review.queue")
    public void handleSubmitForReview(SubmitCommandDTO command) {
        System.out.println("[LifecycleService] Recibido comando para pregunta ID: " + command.getQuestionId());

        // Procesa la transicion de estado de forma asincrona: DRAFT -> PENDING_REVIEW
        lifecycleService.processTransition(
                command.getQuestionId(),
                "PENDING_REVIEW",
                command.getOperationId()
        );
    }
}
