package co.edu.unicauca.question.model;

/**
 * Enumeracion EstadoPregunta del diagrama UML con equivalencia directa a QuestionStatus (Figura 1).
 */
public enum EstadoPregunta {
    BORRADOR("DRAFT"),
    PENDIENTE_REVISION("PENDING_REVIEW"),
    EN_REVISION("IN_REVIEW"),
    APROBADA("APPROVED"),
    RECHAZADA("REJECTED"),
    PUBLICADA("PUBLISHED"),
    ARCHIVADA("ARCHIVED");

    private final String code;

    EstadoPregunta(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static EstadoPregunta fromString(String value) {
        if (value == null) {
            return BORRADOR;
        }
        for (EstadoPregunta e : values()) {
            if (e.name().equalsIgnoreCase(value) || e.code.equalsIgnoreCase(value)) {
                return e;
            }
        }
        return BORRADOR;
    }
}
