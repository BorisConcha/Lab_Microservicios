package com.lab.usuarios.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.usuarios.usuarios.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByRut(String rut);
    
    List<Usuario> findByRol(Usuario.RolUsuario rol);
    
    List<Usuario> findByActivo(Integer activo);
    
    boolean existsByEmail(String email);
    
    boolean existsByRut(String rut);
    
}
