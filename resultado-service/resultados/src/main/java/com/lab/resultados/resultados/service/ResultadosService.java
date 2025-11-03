package com.lab.resultados.resultados.service;


import com.lab.resultados.resultados.model.Resultados;
import com.lab.resultados.resultados.repository.ResultadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class ResultadosService {
    
    @Autowired
    private ResultadosRepository resultadoRepository;
    
    public Resultados crearResultado(Resultados resultado) {
        return resultadoRepository.save(resultado);
    }
    
    public List<Resultados> obtenerTodosLosResultados() {
        return resultadoRepository.findAll();
    }
    
    public Optional<Resultados> obtenerResultadoPorId(Long id) {
        return resultadoRepository.findById(id);
    }
    
    public List<Resultados> obtenerResultadosPorPaciente(Long pacienteId) {
        return resultadoRepository.findByPacienteId(pacienteId);
    }
    
    public List<Resultados> obtenerResultadosPorMedico(Long medicoId) {
        return resultadoRepository.findByMedicoId(medicoId);
    }
    
    public List<Resultados> obtenerResultadosPorLaboratorio(String laboratorio) {
        return resultadoRepository.findByLaboratorio(laboratorio);
    }
    
    public List<Resultados> obtenerResultadosPorEstado(Resultados.EstadoResultado estado) {
        return resultadoRepository.findByEstado(estado);
    }
    
    public List<Resultados> obtenerResultadosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return resultadoRepository.findByFechaAnalisisBetween(fechaInicio, fechaFin);
    }
    
    public List<Resultados> obtenerResultadosPorPacienteYEstado(Long pacienteId, Resultados.EstadoResultado estado) {
        return resultadoRepository.findByPacienteIdAndEstado(pacienteId, estado);
    }
    
    public Resultados actualizarResultado(Long id, Resultados resultadoActualizado) {
        Resultados resultado = resultadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado con ID: " + id));
        
        resultado.setPacienteId(resultadoActualizado.getPacienteId());
        resultado.setPacienteNombre(resultadoActualizado.getPacienteNombre());
        resultado.setMedicoId(resultadoActualizado.getMedicoId());
        resultado.setMedicoNombre(resultadoActualizado.getMedicoNombre());
        resultado.setLaboratorio(resultadoActualizado.getLaboratorio());
        resultado.setTipoAnalisis(resultadoActualizado.getTipoAnalisis());
        resultado.setDescripcion(resultadoActualizado.getDescripcion());
        resultado.setResultadoDetalle(resultadoActualizado.getResultadoDetalle());
        resultado.setEstado(resultadoActualizado.getEstado());
        resultado.setFechaAnalisis(resultadoActualizado.getFechaAnalisis());
        resultado.setFechaEntrega(resultadoActualizado.getFechaEntrega());
        resultado.setObservaciones(resultadoActualizado.getObservaciones());
        resultado.setValoresReferencia(resultadoActualizado.getValoresReferencia());
        
        return resultadoRepository.save(resultado);
    }
    
    public Resultados actualizarEstado(Long id, Resultados.EstadoResultado nuevoEstado) {
        Resultados resultado = resultadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado con ID: " + id));
        
        resultado.setEstado(nuevoEstado);
        
        if (nuevoEstado == Resultados.EstadoResultado.ENTREGADO && resultado.getFechaEntrega() == null) {
            resultado.setFechaEntrega(LocalDate.now());
        }
        
        return resultadoRepository.save(resultado);
    }
    
    public void eliminarResultado(Long id) {
        if (!resultadoRepository.existsById(id)) {
            throw new RuntimeException("Resultado no encontrado con ID: " + id);
        }
        resultadoRepository.deleteById(id);
    }
}
