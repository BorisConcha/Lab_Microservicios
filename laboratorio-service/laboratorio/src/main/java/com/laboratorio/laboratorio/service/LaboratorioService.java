package com.laboratorio.laboratorio.service;

import com.laboratorio.laboratorio.model.Laboratorio;
import com.laboratorio.laboratorio.repository.LaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class LaboratorioService {
    
    @Autowired
    private LaboratorioRepository laboratorioRepository;
    
    public List<Laboratorio> obtenerTodos() {
        return laboratorioRepository.findAll();
    }
    
    public List<Laboratorio> obtenerActivos() {
        return laboratorioRepository.findByActivoTrue();
    }
    
    public Optional<Laboratorio> obtenerPorId(Long id) {
        return laboratorioRepository.findById(id);
    }
    
    public Laboratorio crear(Laboratorio laboratorio) {
        if (laboratorioRepository.findByEmail(laboratorio.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un laboratorio con ese email");
        }
        return laboratorioRepository.save(laboratorio);
    }
    
    public Laboratorio actualizar(Long id, Laboratorio laboratorioActualizado) {
        return laboratorioRepository.findById(id)
            .map(laboratorio -> {
                laboratorio.setNombre(laboratorioActualizado.getNombre());
                laboratorio.setTipo(laboratorioActualizado.getTipo());
                laboratorio.setDireccion(laboratorioActualizado.getDireccion());
                laboratorio.setTelefono(laboratorioActualizado.getTelefono());
                laboratorio.setEmail(laboratorioActualizado.getEmail());
                laboratorio.setEspecialidades(laboratorioActualizado.getEspecialidades());
                laboratorio.setHorarioAtencion(laboratorioActualizado.getHorarioAtencion());
                laboratorio.setCapacidadDiaria(laboratorioActualizado.getCapacidadDiaria());
                laboratorio.setActivo(laboratorioActualizado.getActivo());
                return laboratorioRepository.save(laboratorio);
            })
            .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado con id: " + id));
    }
    
    public void eliminar(Long id) {
        Laboratorio laboratorio = laboratorioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado con id: " + id));
        laboratorio.setActivo(false);
        laboratorioRepository.save(laboratorio);
    }
    
    public void eliminarPermanente(Long id) {
        laboratorioRepository.deleteById(id);
    }
    
    public List<Laboratorio> buscarPorTipo(String tipo) {
        return laboratorioRepository.findByTipoAndActivoTrue(tipo);
    }
    
    public List<Laboratorio> buscarPorNombre(String nombre) {
        return laboratorioRepository.buscarPorNombre(nombre);
    }
    
    public List<Laboratorio> obtenerDisponibles() {
        return laboratorioRepository.findLaboratoriosDisponibles();
    }
    
    public Laboratorio activar(Long id) {
        Laboratorio laboratorio = laboratorioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado con id: " + id));
        laboratorio.setActivo(true);
        return laboratorioRepository.save(laboratorio);
    }
}
