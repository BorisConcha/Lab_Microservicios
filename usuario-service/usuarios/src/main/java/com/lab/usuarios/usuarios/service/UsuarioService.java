package com.lab.usuarios.usuarios.service;


import com.lab.usuarios.usuarios.model.Usuario;
import com.lab.usuarios.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional 
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Usuario crearUsuario(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        

        if (usuarioRepository.existsByRut(usuario.getRut())) {
            throw new RuntimeException("El RUT ya está registrado");
        }
        

        return usuarioRepository.save(usuario);
    }
    

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
    

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    

    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    

    public List<Usuario> obtenerUsuariosPorRol(Usuario.RolUsuario rol) {
        return usuarioRepository.findByRol(rol);
    }
    

    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByActivo(true);
    }
    

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        

        if (!usuario.getEmail().equals(usuarioActualizado.getEmail()) 
                && usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        

        if (!usuario.getRut().equals(usuarioActualizado.getRut()) 
                && usuarioRepository.existsByRut(usuarioActualizado.getRut())) {
            throw new RuntimeException("El RUT ya está registrado");
        }
        

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setApellido(usuarioActualizado.getApellido());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setRut(usuarioActualizado.getRut());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setActivo(usuarioActualizado.getActivo());
        

        if (usuarioActualizado.getPassword() != null 
                && !usuarioActualizado.getPassword().isEmpty()) {
            usuario.setPassword(usuarioActualizado.getPassword());
        }
        

        return usuarioRepository.save(usuario);
    }
    

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        
        usuarioRepository.deleteById(id);
    }
    
    public Usuario iniciarSesion(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
        
        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }
        
        return usuario;
    }
    
    public Usuario desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(false);
        return usuarioRepository.save(usuario);
    }
    
    public Usuario activarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }
    
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    public boolean rutExiste(String rut) {
        return usuarioRepository.existsByRut(rut);
    }
}
