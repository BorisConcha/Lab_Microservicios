package com.lab.resultados.resultados.repository;

import java.time.LocalDate;
import java.util.List;

import com.lab.resultados.resultados.model.Resultados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultadosRepository extends JpaRepository<Resultados, Long> {
    
    List<Resultados> findByPacienteId(Long pacienteId);
    
    List<Resultados> findByMedicoId(Long medicoId);
    
    List<Resultados> findByLaboratorio(String laboratorio);
    
    List<Resultados> findByEstado(Resultados.EstadoResultado estado);
    
    List<Resultados> findByFechaAnalisisBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    List<Resultados> findByPacienteIdAndEstado(Long pacienteId, Resultados.EstadoResultado estado);
    
    List<Resultados> findByMedicoIdAndEstado(Long medicoId, Resultados.EstadoResultado estado);
}
