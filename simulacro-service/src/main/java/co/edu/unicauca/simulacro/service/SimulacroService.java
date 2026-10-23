package co.edu.unicauca.simulacro.service;

import co.edu.unicauca.simulacro.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SimulacroService {

    private final List<Pregunta> bancoPreguntasPublicadas = Collections.synchronizedList(new ArrayList<>());
    private final Map<Long, Simulacro> simulacros = new ConcurrentHashMap<>();
    private final List<IntentoSimulacro> intentos = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong simulacroSeq = new AtomicLong(1);
    private final AtomicLong intentoSeq = new AtomicLong(1);
    private final AtomicLong reporteSeq = new AtomicLong(1);

    @PostConstruct
    public void seedSampleData() {
        Pregunta p1 = new Pregunta(
                101L,
                "En una arquitectura basada en microservicios se requiere desacoplar el envio de notificaciones del flujo principal.",
                "¿Cual patron de comunicacion permite procesar notificaciones sin bloquear la operacion principal?",
                "Ingenieria de Software",
                "Arquitectura de Microservicios",
                NivelDificultad.MEDIO,
                EstadoPregunta.PUBLICADA,
                List.of(
                        new OpcionRespuesta(1L, "Comunicacion asincrona orientada a eventos mediante RabbitMQ", true),
                        new OpcionRespuesta(2L, "Llamada RPC bloqueante en cascada", false),
                        new OpcionRespuesta(3L, "Acoplamiento directo a la misma tabla de base de datos", false),
                        new OpcionRespuesta(4L, "Peticion HTTP sincrona con espera activa infinita", false)
                )
        );
        Pregunta p2 = new Pregunta(
                102L,
                "El microservicio Lifecycle Service es la unica autoridad sobre el estado de las preguntas.",
                "¿Que codigo HTTP devuelve Question Service cuando recibe una solicitud asincrona de envio a revision?",
                "Ingenieria de Software",
                "Comunicacion Asincrona",
                NivelDificultad.BAJO,
                EstadoPregunta.PUBLICADA,
                List.of(
                        new OpcionRespuesta(5L, "202 Accepted junto con un operationId", true),
                        new OpcionRespuesta(6L, "500 Internal Server Error", false),
                        new OpcionRespuesta(7L, "401 Unauthorized", false),
                        new OpcionRespuesta(8L, "301 Moved Permanently", false)
                )
        );
        bancoPreguntasPublicadas.add(p1);
        bancoPreguntasPublicadas.add(p2);

        Usuario docente = new Usuario(1L, "Docente Autor SaberPRO", "docente1", true, List.of(Rol.DOCENTE));
        Simulacro sim = new Simulacro(simulacroSeq.getAndIncrement(), "Simulacro Saber PRO - Ingenieria de Software II", 60, docente, bancoPreguntasPublicadas);
        sim.generarPreguntasAleatorias("Ingenieria de Software", null, null, 2);
        simulacros.put(sim.getId(), sim);

        Usuario estudiante = new Usuario(4L, "Estudiante Ingenieria de Sistemas", "estudiante1", true, List.of(Rol.ESTUDIANTE));
        IntentoSimulacro intento = new IntentoSimulacro(
                intentoSeq.getAndIncrement(),
                estudiante,
                sim,
                List.of(
                        new RespuestaEstudiante(1L, p1, p1.getOpciones().get(0)),
                        new RespuestaEstudiante(2L, p2, p2.getOpciones().get(0))
                )
        );
        intento.finalizarIntento();
        intentos.add(intento);
    }

    public Simulacro crearSimulacro(String titulo, int tiempoMaximoMinutos, String competencia, String tema, String dificultadStr, int cantidad) {
        Usuario docente = new Usuario(1L, "Docente Autor SaberPRO", "docente1", true, List.of(Rol.DOCENTE));
        Simulacro simulacro = new Simulacro(
                simulacroSeq.getAndIncrement(),
                titulo != null ? titulo : "Simulacro Saber PRO",
                tiempoMaximoMinutos > 0 ? tiempoMaximoMinutos : 60,
                docente,
                bancoPreguntasPublicadas
        );
        NivelDificultad dificultad = null;
        if (dificultadStr != null && !dificultadStr.isBlank()) {
            try {
                dificultad = NivelDificultad.valueOf(dificultadStr.toUpperCase());
            } catch (Exception ignored) {
            }
        }
        simulacro.generarPreguntasAleatorias(competencia, tema, dificultad, cantidad > 0 ? cantidad : 2);
        simulacros.put(simulacro.getId(), simulacro);
        return simulacro;
    }

    public Collection<Simulacro> listarSimulacros() {
        return simulacros.values();
    }

    public ReporteAcademico generarReporteEstudiante(Long estudianteId) {
        Usuario est = new Usuario(estudianteId, "Estudiante #" + estudianteId, "estudiante" + estudianteId, true, List.of(Rol.ESTUDIANTE));
        ReporteAcademico reporte = new ReporteAcademico(reporteSeq.getAndIncrement());
        reporte.calcularEstadisticasEstudiante(est, intentos);
        return reporte;
    }

    public ReporteAcademico generarReporteGrupal() {
        ReporteAcademico reporte = new ReporteAcademico(reporteSeq.getAndIncrement());
        reporte.generarReporteGrupal(intentos);
        return reporte;
    }
}
