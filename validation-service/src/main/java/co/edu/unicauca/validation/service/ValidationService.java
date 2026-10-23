package co.edu.unicauca.validation.service;

import co.edu.unicauca.validation.dto.OptionDTO;
import co.edu.unicauca.validation.dto.ValidationRequestDTO;
import co.edu.unicauca.validation.dto.ValidationResultDTO;
import co.edu.unicauca.validation.model.OpcionRespuesta;
import co.edu.unicauca.validation.model.Pregunta;
import co.edu.unicauca.validation.model.ValidadorEstructural;
import co.edu.unicauca.validation.model.ValidationRule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationService {

    private final ValidadorEstructural validadorEstructural = new ValidadorEstructural();
    private final List<ValidationRule> rules = List.of(
            new ValidationRule(1L, "REGLA_ENUNCIADO_Y_CONTEXTO", "El enunciado o pregunta directa debe tener al menos 10 caracteres.", true),
            new ValidationRule(2L, "REGLA_CANTIDAD_OPCIONES", "La pregunta debe tener 4 o 5 opciones de respuesta y exactamente 1 respuesta correcta.", true),
            new ValidationRule(3L, "REGLA_EXPRESIONES_PROHIBIDAS", "Las opciones no deben incluir frases como 'Todas las anteriores' o 'Ninguna de las anteriores'.", true),
            new ValidationRule(4L, "REGLA_LONGITUD_Y_GRAMATICA", "Cada opcion de respuesta debe tener contenido valido no vacio.", true)
    );

    public ValidationResultDTO validate(ValidationRequestDTO request) {
        List<String> errors = new ArrayList<>();

        if (request == null) {
            errors.add("La solicitud de validacion no puede ser nula.");
            return new ValidationResultDTO(false, errors);
        }

        // Construir entidad Pregunta del UML para ejecutar ValidadorEstructural
        Pregunta pregunta = new Pregunta();
        String enunciado = (request.getPreguntaDirecta() != null && !request.getPreguntaDirecta().isBlank())
                ? request.getPreguntaDirecta()
                : request.getText();
        pregunta.setPreguntaDirecta(enunciado);
        pregunta.setContexto(request.getContexto());
        pregunta.setJustificacionRespuesta(request.getJustificacionRespuesta());
        pregunta.setBibliografia(request.getBibliografia());
        pregunta.setCompetencia(request.getCompetencia());
        pregunta.setTema(request.getTema());
        pregunta.setSubtema(request.getSubtema());

        List<OpcionRespuesta> opciones = new ArrayList<>();
        if (request.getOptions() != null) {
            long idx = 1;
            for (OptionDTO opt : request.getOptions()) {
                boolean esCorrecta = opt.isCorrect()
                        || (request.getCorrectOptionId() != null && request.getCorrectOptionId().equalsIgnoreCase(opt.getId()));
                opciones.add(new OpcionRespuesta(idx++, opt.getText(), esCorrecta));
            }
        }
        pregunta.setOpciones(opciones);

        // 1. Verificar contexto y enunciado
        if (!validadorEstructural.verificarContextoYEnunciado(pregunta)) {
            errors.add("El enunciado de la pregunta es obligatorio y debe tener al menos 10 caracteres (y el contexto, si se incluye, al menos 10 caracteres).");
        }

        // 2. Verificar cantidad de opciones y clave unica
        if (!validadorEstructural.verificarCantidadOpciones(opciones)) {
            errors.add("Estructura invalida: la pregunta debe tener 4 opciones (o 5 segun UML: 1 clave y 4 distractores) y exactamente 1 opcion marcada como respuesta correcta.");
        }

        // 3. Verificar expresiones prohibidas
        if (!validadorEstructural.verificarExpresionesProhibidas(opciones)) {
            errors.add("Las opciones contienen expresiones prohibidas por los lineamientos Saber PRO (ej. 'Todas las anteriores', 'Ninguna de las anteriores').");
        }

        // 4. Verificar longitud y gramatica de las opciones
        if (!validadorEstructural.verificarLongitudYGramatica(opciones)) {
            errors.add("Todas las opciones de respuesta deben contener texto valido (minimo 2 caracteres).");
        }

        boolean isValid = errors.isEmpty() && validadorEstructural.validarPregunta(pregunta);
        return new ValidationResultDTO(isValid, errors);
    }

    public List<ValidationRule> getRules() {
        return rules;
    }
}
