package co.edu.unicauca.question.messaging;

import co.edu.unicauca.question.config.RabbitMQConfig;
import co.edu.unicauca.question.dto.OperationResultEventDTO;
import co.edu.unicauca.question.dto.SubmitQuestionCommand;
import co.edu.unicauca.question.dto.ValidationFailedEventDTO;
import co.edu.unicauca.question.service.QuestionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de publicar comandos en RabbitMQ y recibir eventos de resultado de operacion (Figura 2).
 */
@Component
public class CommandPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final QuestionService questionService;

    public CommandPublisher(RabbitTemplate rabbitTemplate, @Lazy QuestionService questionService) {
        this.rabbitTemplate = rabbitTemplate;
        this.questionService = questionService;
    }

    public void publishSubmitCommand(SubmitQuestionCommand command) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_SUBMIT,
                command
        );
    }

    public void publishValidationFailedEvent(ValidationFailedEventDTO event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY_VALIDATION_FAILED,
                    event
            );
        } catch (Exception ex) {
            System.out.println("[QuestionService] Aviso al publicar ValidationFailed: " + ex.getMessage());
        }
    }

    /**
     * Escucha el evento OperationResult publicado por Lifecycle Service (Figura 2: routing key question.operation-result).
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_OPERATION_RESULT)
    public void handleOperationResult(OperationResultEventDTO result) {
        System.out.println("[QuestionService] Recibido resultado de operacion asincrona: "
                + result.getId() + " -> " + result.getStatus());
        questionService.updateOperationResult(result);
    }
}
