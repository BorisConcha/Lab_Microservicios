package com.lab.resultados.resultados.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lab.resultados.resultados.model.Resultados;
import com.lab.resultados.resultados.service.ResultadosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/resultados")
@CrossOrigin(origins = "*")
public class ResultadosController {

    @Autowired
    private ResultadosService resultadoService;
    
    @GetMapping
    public ResponseEntity<List<Resultados>> obtenerTodosLosResultados() {
        try {
            List<Resultados> resultados = resultadoService.obtenerTodosLosResultados();
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerResultadoPorId(@PathVariable Long id) {
        try {
            return resultadoService.obtenerResultadoPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError(e.getMessage()));
        }
    }
    
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<?> obtenerResultadosPorPaciente(@PathVariable Long pacienteId) {
        try {
            List<Resultados> resultados = resultadoService.obtenerResultadosPorPaciente(pacienteId);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError(e.getMessage()));
        }
    }
    
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<?> obtenerResultadosPorMedico(@PathVariable Long medicoId) {
        try {
            List<Resultados> resultados = resultadoService.obtenerResultadosPorMedico(medicoId);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError(e.getMessage()));
        }
    }
    
    @GetMapping("/laboratorio/{laboratorio}")
    public ResponseEntity<?> obtenerResultadosPorLaboratorio(@PathVariable String laboratorio) {
        try {
            List<Resultados> resultados = resultadoService.obtenerResultadosPorLaboratorio(laboratorio);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError(e.getMessage()));
        }
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerResultadosPorEstado(@PathVariable String estado) {
        try {
            Resultados.EstadoResultado estadoResultado = Resultados.EstadoResultado.valueOf(estado.toUpperCase());
            List<Resultados> resultados = resultadoService.obtenerResultadosPorEstado(estadoResultado);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearMensajeError("Estado inválido. Use: PENDIENTE, EN_PROCESO, COMPLETADO, ENTREGADO"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError(e.getMessage()));
        }
    }
    
    @GetMapping("/fechas")
    public ResponseEntity<?> obtenerResultadosPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<Resultados> resultados = resultadoService.obtenerResultadosPorRangoFechas(fechaInicio, fechaFin);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError(e.getMessage()));
        }
    }
    
    @GetMapping("/paciente/{pacienteId}/estado/{estado}")
    public ResponseEntity<?> obtenerResultadosPorPacienteYEstado(
            @PathVariable Long pacienteId,
            @PathVariable String estado) {
        try {
            Resultados.EstadoResultado estadoResultado = Resultados.EstadoResultado.valueOf(estado.toUpperCase());
            List<Resultados> resultados = resultadoService.obtenerResultadosPorPacienteYEstado(pacienteId, estadoResultado);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearMensajeError("Estado inválido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<?> crearResultado(@Valid @RequestBody Resultados resultado) {
        try {
            Resultados nuevoResultado = resultadoService.crearResultado(resultado);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoResultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearMensajeError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError("Error al crear el resultado"));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarResultado(
            @PathVariable Long id,
            @Valid @RequestBody Resultados resultado) {
        try {
            Resultados resultadoActualizado = resultadoService.actualizarResultado(id, resultado);
            return ResponseEntity.ok(resultadoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearMensajeError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError("Error al actualizar el resultado"));
        }
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String estadoStr = request.get("estado");
            if (estadoStr == null) {
                return ResponseEntity.badRequest()
                        .body(crearMensajeError("El estado es requerido"));
            }
            
            Resultados.EstadoResultado nuevoEstado = Resultados.EstadoResultado.valueOf(estadoStr.toUpperCase());
            Resultados resultado = resultadoService.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearMensajeError("Estado inválido"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearMensajeError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError("Error al actualizar el estado"));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarResultado(@PathVariable Long id) {
        try {
            resultadoService.eliminarResultado(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Resultado eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearMensajeError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError("Error al eliminar el resultado"));
        }
    }
    
    private Map<String, String> crearMensajeError(String mensaje) {
        Map<String, String> error = new HashMap<>();
        error.put("error", mensaje);
        return error;
    }
    
}
