package co.edu.unicauca.question.service;

import co.edu.unicauca.question.client.ValidationClient;
import co.edu.unicauca.question.dto.*;
import co.edu.unicauca.question.messaging.CommandPublisher;
import co.edu.unicauca.question.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class QuestionService {

    private final Map<Long, Pregunta> questions = new ConcurrentHashMap<>();
    private final Map<String, OperationResultEventDTO> operations = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    private final ValidationClient validationClient;
    private final CommandPublisher commandPublisher;

    public QuestionService(ValidationClient validationClient, CommandPublisher commandPublisher) {
        this.validationClient = validationClient;
        this.commandPublisher = commandPublisher;
    }

    public Pregunta createQuestion(QuestionDTO dto) {
        ValidationResultDTO validationResult = validationClient.validateQuestion(dto);
        if (!validationResult.isValid()) {
            commandPublisher.publishValidationFailedEvent(new ValidationFailedEventDTO(
                    UUID.randomUUID().toString(),
                    null,
                    validationResult.getErrors()
            ));
            throw new IllegalArgumentException(String.join(" | ", validationResult.getErrors()));
        }

        Pregunta pregunta = new Pregunta();
        pregunta.setId(idGenerator.getAndIncrement());
        String enunciado = (dto.getPreguntaDirecta() != null && !dto.getPreguntaDirecta().isBlank())
                ? dto.getPreguntaDirecta()
                : dto.getText();
        pregunta.setPreguntaDirecta(enunciado);
        pregunta.setContexto(dto.getContexto());
        pregunta.setJustificacionRespuesta(dto.getJustificacionRespuesta());
        pregunta.setBibliografia(dto.getBibliografia());
        pregunta.setCompetencia(dto.getCompetencia() != null ? dto.getCompetencia() : "Ingenieria de Software");
        pregunta.setTema(dto.getTema() != null ? dto.getTema() : "Arquitectura de Microservicios");
        pregunta.setSubtema(dto.getSubtema() != null ? dto.getSubtema() : "Comunicacion Sincrona y Asincrona");
        if (dto.getDificultad() != null) {
            try {
                pregunta.setDificultad(NivelDificultad.valueOf(dto.getDificultad().toUpperCase()));
            } catch (Exception ignored) {
                pregunta.setDificultad(NivelDificultad.MEDIO);
            }
        }
        pregunta.setAuthorId(dto.getAuthorId() != null ? dto.getAuthorId() : 1L);
        pregunta.setCorrectOptionId(dto.getCorrectOptionId());

        List<OpcionRespuesta> opciones = new ArrayList<>();
        if (dto.getOptions() != null) {
            for (OptionDTO opt : dto.getOptions()) {
                boolean esCorrecta = opt.isCorrect()
                        || (dto.getCorrectOptionId() != null && dto.getCorrectOptionId().equalsIgnoreCase(opt.getId()));
                opciones.add(new OpcionRespuesta(opt.getId(), opt.getText(), esCorrecta));
            }
        }
        pregunta.setOpciones(opciones);
        pregunta.create();

        questions.put(pregunta.getId(), pregunta);
        validationClient.registerInitialStateInLifecycle(pregunta.getId(), "DRAFT", "Autor-" + pregunta.getAuthorId());
        return pregunta;
    }

    /**
     * Permite corregir una pregunta en estado DRAFT o REJECTED para que pueda volver al proceso de revision.
     */
    public Pregunta updateQuestion(Long id, QuestionDTO dto) {
        Pregunta pregunta = questions.get(id);
        if (pregunta == null) {
            throw new NoSuchElementException("No existe la pregunta con ID: " + id);
        }

        ValidationResultDTO validationResult = validationClient.validateQuestion(dto);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException(String.join(" | ", validationResult.getErrors()));
        }

        String enunciado = (dto.getPreguntaDirecta() != null && !dto.getPreguntaDirecta().isBlank())
                ? dto.getPreguntaDirecta()
                : dto.getText();

        pregunta.modificarContenido(
                dto.getContexto(),
                enunciado,
                dto.getJustificacionRespuesta(),
                dto.getBibliografia(),
                dto.getCompetencia() != null ? dto.getCompetencia() : pregunta.getCompetencia(),
                dto.getTema() != null ? dto.getTema() : pregunta.getTema(),
                dto.getSubtema() != null ? dto.getSubtema() : pregunta.getSubtema()
        );

        if (dto.getOptions() != null) {
            List<OpcionRespuesta> opciones = new ArrayList<>();
            for (OptionDTO opt : dto.getOptions()) {
                boolean esCorrecta = opt.isCorrect()
                        || (dto.getCorrectOptionId() != null && dto.getCorrectOptionId().equalsIgnoreCase(opt.getId()));
                opciones.add(new OpcionRespuesta(opt.getId(), opt.getText(), esCorrecta));
            }
            pregunta.setOpciones(opciones);
        }
        pregunta.setCorrectOptionId(dto.getCorrectOptionId());

        // Sincronizar con Lifecycle Service que la pregunta corregida vuelve a DRAFT
        validationClient.registerInitialStateInLifecycle(pregunta.getId(), "DRAFT", "Autor-" + pregunta.getAuthorId());
        return pregunta;
    }

    public Map<String, String> submitForReview(Long questionId) {
        String operationId = UUID.randomUUID().toString();

        OperationResultEventDTO pendingOp = new OperationResultEventDTO();
        pendingOp.setId(operationId);
        pendingOp.setQuestionId(questionId);
        pendingOp.setType("SUBMIT_FOR_REVIEW");
        pendingOp.setStatus("PENDING");
        pendingOp.setMessage("Solicitud de envio a revision en cola para procesamiento por Lifecycle Service.");
        pendingOp.setCreatedAt(LocalDateTime.now());
        operations.put(operationId, pendingOp);

        SubmitCommandDTO command = new SubmitCommandDTO(questionId, operationId);
        commandPublisher.publishSubmitCommand(command);

        return Map.of(
                "message", "Solicitud de envio a revision aceptada para procesamiento.",
                "operationId", operationId,
                "status", "PROCESSING"
        );
    }

    public void updateOperationResult(OperationResultEventDTO result) {
        if (result == null || result.getId() == null) {
            return;
        }
        operations.put(result.getId(), result);
        if ("COMPLETED".equalsIgnoreCase(result.getStatus()) && result.getQuestionId() != null) {
            Pregunta pregunta = questions.get(result.getQuestionId());
            if (pregunta != null && result.getNewState() != null) {
                pregunta.cambiarEstado(EstadoPregunta.fromString(result.getNewState()), "LifecycleService");
            }
        }
    }

    public void syncStateFromLifecycle(Long questionId, String newState, String responsible) {
        Pregunta pregunta = questions.get(questionId);
        if (pregunta != null) {
            pregunta.cambiarEstado(EstadoPregunta.fromString(newState), responsible);
        }
    }

    public Optional<Pregunta> findById(Long id) {
        return Optional.ofNullable(questions.get(id));
    }

    public Collection<Pregunta> findAll() {
        return questions.values();
    }

    public Optional<OperationResultEventDTO> getOperationStatus(String operationId) {
        return Optional.ofNullable(operations.get(operationId));
    }
}
