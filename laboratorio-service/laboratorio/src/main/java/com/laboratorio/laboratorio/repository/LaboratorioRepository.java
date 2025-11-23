package com.laboratorio.laboratorio.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.laboratorio.laboratorio.model.Laboratorio;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {
    
    List<Laboratorio> findByActivoTrue();
    
    List<Laboratorio> findByTipo(String tipo);
    
    List<Laboratorio> findByTipoAndActivoTrue(String tipo);
    
    Optional<Laboratorio> findByEmail(String email);
    
    @Query("SELECT l FROM Laboratorio l WHERE LOWER(l.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Laboratorio> buscarPorNombre(String nombre);
    
    Long countByTipo(String tipo);
    
    @Query("SELECT l FROM Laboratorio l WHERE l.activo = true AND l.capacidadDiaria > 0")
    List<Laboratorio> findLaboratoriosDisponibles();

}
