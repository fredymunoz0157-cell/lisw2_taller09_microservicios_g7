package co.edu.unicauca.validation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase ValidadorEstructural definida exactamente en el diagrama UML (Cluster: Banco de Preguntas).
 * Atributos:
 * - expresionesProhibidas: List<String>
 * Metodos:
 * + validarPregunta(pregunta: Pregunta): boolean
 * + verificarContextoYEnunciado(pregunta: Pregunta): boolean
 * + verificarCantidadOpciones(opciones: List<OpcionRespuesta>): boolean
 * + verificarExpresionesProhibidas(opciones: List<OpcionRespuesta>): boolean
 * + verificarLongitudYGramatica(opciones: List<OpcionRespuesta>): boolean
 */
public class ValidadorEstructural {

    private List<String> expresionesProhibidas = new ArrayList<>(List.of(
            "todas las anteriores",
            "ninguna de las anteriores",
            "todas son correctas",
            "ninguna es correcta",
            "no se"
    ));

    public ValidadorEstructural() {
    }

    public ValidadorEstructural(List<String> expresionesProhibidas) {
        if (expresionesProhibidas != null) {
            this.expresionesProhibidas = new ArrayList<>(expresionesProhibidas);
        }
    }

    /**
     * Metodo principal del UML: +validarPregunta(pregunta: Pregunta): boolean
     */
    public boolean validarPregunta(Pregunta pregunta) {
        if (pregunta == null) {
            return false;
        }
        return verificarContextoYEnunciado(pregunta)
                && verificarCantidadOpciones(pregunta.getOpciones())
                && verificarExpresionesProhibidas(pregunta.getOpciones())
                && verificarLongitudYGramatica(pregunta.getOpciones());
    }

    /**
     * Metodo del UML: +verificarContextoYEnunciado(pregunta: Pregunta): boolean
     */
    public boolean verificarContextoYEnunciado(Pregunta pregunta) {
        if (pregunta == null) {
            return false;
        }
        boolean enunciadoValido = pregunta.getPreguntaDirecta() != null
                && pregunta.getPreguntaDirecta().trim().length() >= 10;
        boolean contextoValido = pregunta.getContexto() == null
                || pregunta.getContexto().trim().isEmpty()
                || pregunta.getContexto().trim().length() >= 10;
        return enunciadoValido && contextoValido;
    }

    /**
     * Metodo del UML: +verificarCantidadOpciones(opciones: List<OpcionRespuesta>): boolean
     * Acepta 4 opciones (Guia Paso 2: enunciado, 4 opciones y 1 clave) o 5 opciones (UML: 1 correcta + 4 distractores),
     * garantizando siempre exactamente 1 opcion correcta.
     */
    public boolean verificarCantidadOpciones(List<OpcionRespuesta> opciones) {
        if (opciones == null || (opciones.size() != 4 && opciones.size() != 5)) {
            return false;
        }
        long correctas = opciones.stream().filter(OpcionRespuesta::isEsCorrecta).count();
        return correctas == 1;
    }

    /**
     * Metodo del UML: +verificarExpresionesProhibidas(opciones: List<OpcionRespuesta>): boolean
     */
    public boolean verificarExpresionesProhibidas(List<OpcionRespuesta> opciones) {
        if (opciones == null || opciones.isEmpty()) {
            return false;
        }
        for (OpcionRespuesta opcion : opciones) {
            if (opcion.getTexto() == null) {
                return false;
            }
            String lower = opcion.getTexto().toLowerCase().trim();
            for (String prohibida : expresionesProhibidas) {
                if (lower.contains(prohibida.toLowerCase())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Metodo del UML: +verificarLongitudYGramatica(opciones: List<OpcionRespuesta>): boolean
     */
    public boolean verificarLongitudYGramatica(List<OpcionRespuesta> opciones) {
        if (opciones == null || opciones.isEmpty()) {
            return false;
        }
        for (OpcionRespuesta opcion : opciones) {
            if (opcion.getTexto() == null || opcion.getTexto().trim().length() < 2) {
                return false;
            }
        }
        return true;
    }

    public List<String> getExpresionesProhibidas() {
        return expresionesProhibidas;
    }

    public void setExpresionesProhibidas(List<String> expresionesProhibidas) {
        this.expresionesProhibidas = expresionesProhibidas;
    }
}
