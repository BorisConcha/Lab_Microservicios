package com.laboratorio.laboratorio.controller;


import com.laboratorio.laboratorio.model.Laboratorio;
import com.laboratorio.laboratorio.service.LaboratorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/laboratorios")
@CrossOrigin(origins = "*")
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService;
    
    @GetMapping
    public ResponseEntity<List<Laboratorio>> obtenerTodos() {
        List<Laboratorio> laboratorios = laboratorioService.obtenerTodos();
        return ResponseEntity.ok(laboratorios);
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<Laboratorio>> obtenerActivos() {
        List<Laboratorio> laboratorios = laboratorioService.obtenerActivos();
        return ResponseEntity.ok(laboratorios);
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<List<Laboratorio>> obtenerDisponibles() {
        List<Laboratorio> laboratorios = laboratorioService.obtenerDisponibles();
        return ResponseEntity.ok(laboratorios);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Laboratorio> laboratorio = laboratorioService.obtenerPorId(id);
        
        if (laboratorio.isPresent()) {
            return ResponseEntity.ok(laboratorio.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Laboratorio no encontrado con id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Laboratorio laboratorio) {
        try {
            Laboratorio nuevoLaboratorio = laboratorioService.crear(laboratorio);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLaboratorio);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al crear laboratorio: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Laboratorio laboratorio) {
        try {
            Laboratorio laboratorioActualizado = laboratorioService.actualizar(id, laboratorio);
            return ResponseEntity.ok(laboratorioActualizado);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al actualizar laboratorio: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            laboratorioService.eliminar(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Laboratorio desactivado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al eliminar laboratorio: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<?> eliminarPermanente(@PathVariable Long id) {
        try {
            laboratorioService.eliminarPermanente(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Laboratorio eliminado permanentemente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al eliminar laboratorio: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Laboratorio>> buscarPorTipo(@PathVariable String tipo) {
        List<Laboratorio> laboratorios = laboratorioService.buscarPorTipo(tipo);
        return ResponseEntity.ok(laboratorios);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Laboratorio>> buscarPorNombre(@RequestParam String nombre) {
        List<Laboratorio> laboratorios = laboratorioService.buscarPorNombre(nombre);
        return ResponseEntity.ok(laboratorios);
    }
    
    @PatchMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable Long id) {
        try {
            Laboratorio laboratorio = laboratorioService.activar(id);
            return ResponseEntity.ok(laboratorio);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al activar laboratorio: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
}
