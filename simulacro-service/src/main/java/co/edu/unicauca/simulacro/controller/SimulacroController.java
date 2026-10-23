package co.edu.unicauca.simulacro.controller;

import co.edu.unicauca.simulacro.model.ReporteAcademico;
import co.edu.unicauca.simulacro.model.Simulacro;
import co.edu.unicauca.simulacro.service.SimulacroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/simulacros")
public class SimulacroController {

    private final SimulacroService simulacroService;

    public SimulacroController(SimulacroService simulacroService) {
        this.simulacroService = simulacroService;
    }

    @PostMapping
    public ResponseEntity<Simulacro> crearSimulacro(@RequestBody Map<String, Object> body) {
        String titulo = (String) body.getOrDefault("titulo", "Simulacro Saber PRO");
        int tiempo = body.get("tiempoMaximoMinutos") instanceof Number n ? n.intValue() : 60;
        String competencia = (String) body.get("competencia");
        String tema = (String) body.get("tema");
        String dificultad = (String) body.get("dificultad");
        int cantidad = body.get("cantidad") instanceof Number n ? n.intValue() : 2;

        Simulacro creado = simulacroService.crearSimulacro(titulo, tiempo, competencia, tema, dificultad, cantidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public ResponseEntity<Collection<Simulacro>> listarSimulacros() {
        return ResponseEntity.ok(simulacroService.listarSimulacros());
    }

    @GetMapping("/reportes/estudiante/{estudianteId}")
    public ResponseEntity<ReporteAcademico> obtenerReporteEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(simulacroService.generarReporteEstudiante(estudianteId));
    }

    @GetMapping("/reportes/grupal")
    public ResponseEntity<ReporteAcademico> obtenerReporteGrupal() {
        return ResponseEntity.ok(simulacroService.generarReporteGrupal());
    }
}
