package co.edu.unicauca.lifecycle.model;

/**
 * Enumeracion QuestionStatus definida en la Guia y en la Figura 1,
 * con soporte para los nombres en espanol del diagrama UML (EstadoPregunta).
 */
public enum QuestionStatus {
    DRAFT,
    PENDING_REVIEW,
    IN_REVIEW,
    APPROVED,
    REJECTED,
    PUBLISHED,
    ARCHIVED;

    public static QuestionStatus fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        String normalized = value.trim().toUpperCase();
        return switch (normalized) {
            case "DRAFT", "BORRADOR" -> DRAFT;
            case "PENDING_REVIEW", "PENDIENTE_REVISION" -> PENDING_REVIEW;
            case "IN_REVIEW", "EN_REVISION" -> IN_REVIEW;
            case "APPROVED", "APROBADA" -> APPROVED;
            case "REJECTED", "RECHAZADA" -> REJECTED;
            case "PUBLISHED", "PUBLICADA" -> PUBLISHED;
            case "ARCHIVED", "ARCHIVADA" -> ARCHIVED;
            default -> QuestionStatus.valueOf(normalized);
        };
    }
}
