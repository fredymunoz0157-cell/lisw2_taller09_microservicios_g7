package co.edu.unicauca.simulacro.model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Clase ReporteAcademico definida en el diagrama UML (Cluster: Simulacros y Seguimiento Academico).
 * Atributos:
 * - id: Long
 * - fechaGeneracion: LocalDateTime
 * - promedioGeneral: double
 * - fortalezasPorCompetencia: List<String>
 * - debilidadesPorCompetencia: List<String>
 * Metodos:
 * + calcularEstadisticasEstudiante(estudiante: Usuario, intentos: List<IntentoSimulacro>): void
 * + generarReporteGrupal(intentos: List<IntentoSimulacro>): void
 */
public class ReporteAcademico {
    private Long id;
    private LocalDateTime fechaGeneracion;
    private double promedioGeneral;
    private List<String> fortalezasPorCompetencia = new ArrayList<>();
    private List<String> debilidadesPorCompetencia = new ArrayList<>();

    public ReporteAcademico() {
        this.fechaGeneracion = LocalDateTime.now();
    }

    public ReporteAcademico(Long id) {
        this.id = id;
        this.fechaGeneracion = LocalDateTime.now();
    }

    /**
     * Metodo del diagrama UML:
     * +calcularEstadisticasEstudiante(estudiante: Usuario, intentos: List<IntentoSimulacro>): void
     */
    public void calcularEstadisticasEstudiante(Usuario estudiante, List<IntentoSimulacro> intentos) {
        List<IntentoSimulacro> filtrados = new ArrayList<>();
        if (intentos != null) {
            for (IntentoSimulacro intento : intentos) {
                if (estudiante == null || intento.getEstudiante() == null
                        || Objects.equals(intento.getEstudiante().getId(), estudiante.getId())) {
                    filtrados.add(intento);
                }
            }
        }
        procesarIntentos(filtrados);
    }

    /**
     * Metodo del diagrama UML:
     * +generarReporteGrupal(intentos: List<IntentoSimulacro>): void
     */
    public void generarReporteGrupal(List<IntentoSimulacro> intentos) {
        procesarIntentos(intentos != null ? intentos : List.of());
    }

    private void procesarIntentos(List<IntentoSimulacro> intentos) {
        this.fechaGeneracion = LocalDateTime.now();
        this.fortalezasPorCompetencia.clear();
        this.debilidadesPorCompetencia.clear();

        if (intentos.isEmpty()) {
            this.promedioGeneral = 0.0;
            return;
        }

        double suma = 0.0;
        Map<String, int[]> aciertosPorCompetencia = new HashMap<>();

        for (IntentoSimulacro intento : intentos) {
            suma += intento.getCalificacion();
            if (intento.getRespuestas() != null) {
                for (RespuestaEstudiante resp : intento.getRespuestas()) {
                    String comp = (resp.getPregunta() != null && resp.getPregunta().getCompetencia() != null)
                            ? resp.getPregunta().getCompetencia()
                            : "Competencia General";
                    int[] stats = aciertosPorCompetencia.computeIfAbsent(comp, k -> new int[]{0, 0});
                    stats[1]++;
                    if (resp.isEsCorrecta()) {
                        stats[0]++;
                    }
                }
            }
        }

        this.promedioGeneral = Math.round((suma / intentos.size()) * 100.0) / 100.0;

        for (Map.Entry<String, int[]> entry : aciertosPorCompetencia.entrySet()) {
            String competencia = entry.getKey();
            int aciertos = entry.getValue()[0];
            int total = entry.getValue()[1];
            double porcentaje = total > 0 ? ((double) aciertos / total) * 100.0 : 0.0;
            if (porcentaje >= 60.0) {
                fortalezasPorCompetencia.add(competencia + " (" + Math.round(porcentaje) + "% de acierto)");
            } else {
                debilidadesPorCompetencia.add(competencia + " (" + Math.round(porcentaje) + "% de acierto)");
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public double getPromedioGeneral() {
        return promedioGeneral;
    }

    public void setPromedioGeneral(double promedioGeneral) {
        this.promedioGeneral = promedioGeneral;
    }

    public List<String> getFortalezasPorCompetencia() {
        return fortalezasPorCompetencia;
    }

    public void setFortalezasPorCompetencia(List<String> fortalezasPorCompetencia) {
        this.fortalezasPorCompetencia = fortalezasPorCompetencia;
    }

    public List<String> getDebilidadesPorCompetencia() {
        return debilidadesPorCompetencia;
    }

    public void setDebilidadesPorCompetencia(List<String> debilidadesPorCompetencia) {
        this.debilidadesPorCompetencia = debilidadesPorCompetencia;
    }
}
