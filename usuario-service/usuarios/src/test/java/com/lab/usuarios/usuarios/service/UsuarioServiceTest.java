package com.lab.usuarios.usuarios.service;

import com.lab.usuarios.usuarios.model.Usuario;
import com.lab.usuarios.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {
        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setNombre("Juan");
        usuarioTest.setApellido("Pérez");
        usuarioTest.setEmail("juan.perez@test.com");
        usuarioTest.setPassword("password123");
        usuarioTest.setRut("12345678-9");
        usuarioTest.setTelefono("+56912345678");
        usuarioTest.setRol(Usuario.RolUsuario.PACIENTE);
        usuarioTest.setActivo(1);
        usuarioTest.setFechaRegistro(LocalDateTime.now());
        usuarioTest.setFechaActualizacion(LocalDateTime.now());
    }

    
    @Test
    void crearUsuario_DebeCrearUsuarioExitosamente() {
        when(usuarioRepository.existsByEmail(usuarioTest.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByRut(usuarioTest.getRut())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        Usuario resultado = usuarioService.crearUsuario(usuarioTest);

        assertNotNull(resultado);
        assertEquals(usuarioTest.getEmail(), resultado.getEmail());
        verify(usuarioRepository, times(1)).save(usuarioTest);
    }

    @Test
    void crearUsuario_DebeLanzarExcepcionCuandoEmailYaExiste() {
        when(usuarioRepository.existsByEmail(usuarioTest.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioTest);
        });

        assertEquals("El email ya está registrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_DebeLanzarExcepcionCuandoRutYaExiste() {
        when(usuarioRepository.existsByEmail(usuarioTest.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByRut(usuarioTest.getRut())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioTest);
        });

        assertEquals("El RUT ya está registrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    
    @Test
    void obtenerTodosLosUsuarios_DebeRetornarListaDeUsuarios() {
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setEmail("maria@test.com");
        
        List<Usuario> usuarios = Arrays.asList(usuarioTest, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void obtenerTodosLosUsuarios_DebeRetornarListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList());

        List<Usuario> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    
    @Test
    void obtenerUsuarioPorId_DebeRetornarUsuarioCuandoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));

        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(usuarioTest.getId(), resultado.get().getId());
    }

    @Test
    void obtenerUsuarioPorId_DebeRetornarVacioCuandoNoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(999L);

        assertFalse(resultado.isPresent());
    }

    
    @Test
    void obtenerUsuarioPorEmail_DebeRetornarUsuarioCuandoExiste() {
        when(usuarioRepository.findByEmail("juan.perez@test.com"))
                .thenReturn(Optional.of(usuarioTest));

        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorEmail("juan.perez@test.com");

        assertTrue(resultado.isPresent());
        assertEquals("juan.perez@test.com", resultado.get().getEmail());
    }

    @Test
    void obtenerUsuarioPorEmail_DebeRetornarVacioCuandoNoExiste() {
        when(usuarioRepository.findByEmail("noexiste@test.com"))
                .thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorEmail("noexiste@test.com");

        assertFalse(resultado.isPresent());
    }

    
    @Test
    void obtenerUsuariosPorRol_DebeRetornarUsuariosDelRol() {
        List<Usuario> pacientes = Arrays.asList(usuarioTest);
        when(usuarioRepository.findByRol(Usuario.RolUsuario.PACIENTE))
                .thenReturn(pacientes);

        List<Usuario> resultado = usuarioService.obtenerUsuariosPorRol(Usuario.RolUsuario.PACIENTE);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(Usuario.RolUsuario.PACIENTE, resultado.get(0).getRol());
    }

    
    @Test
    void obtenerUsuariosActivos_DebeRetornarSoloUsuariosActivos() {
        List<Usuario> activos = Arrays.asList(usuarioTest);
        when(usuarioRepository.findByActivo(1)).thenReturn(activos);

        List<Usuario> resultado = usuarioService.obtenerUsuariosActivos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getActivo());
    }

    
    @Test
    void actualizarUsuario_DebeActualizarUsuarioExitosamente() {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Juan Carlos");
        usuarioActualizado.setApellido("Pérez González");
        usuarioActualizado.setEmail("juan.perez@test.com");
        usuarioActualizado.setRut("12345678-9");
        usuarioActualizado.setTelefono("+56987654321");
        usuarioActualizado.setRol(Usuario.RolUsuario.MEDICO);
        usuarioActualizado.setActivo(1);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        Usuario resultado = usuarioService.actualizarUsuario(1L, usuarioActualizado);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario(999L, usuarioTest);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
    }

    @Test
    void actualizarUsuario_DebeLanzarExcepcionCuandoNuevoEmailYaExiste() {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setEmail("nuevo@test.com");
        usuarioActualizado.setRut("12345678-9");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(usuarioRepository.existsByEmail("nuevo@test.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario(1L, usuarioActualizado);
        });

        assertEquals("El email ya está registrado", exception.getMessage());
    }

    @Test
    void actualizarUsuario_DebeLanzarExcepcionCuandoNuevoRutYaExiste() {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setEmail("juan.perez@test.com");
        usuarioActualizado.setRut("98765432-1");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(usuarioRepository.existsByRut("98765432-1")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario(1L, usuarioActualizado);
        });

        assertEquals("El RUT ya está registrado", exception.getMessage());
    }

    @Test
    void actualizarUsuario_DebeActualizarPasswordCuandoSeProvee() {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Juan");
        usuarioActualizado.setApellido("Pérez");
        usuarioActualizado.setEmail("juan.perez@test.com");
        usuarioActualizado.setRut("12345678-9");
        usuarioActualizado.setTelefono("+56912345678");
        usuarioActualizado.setRol(Usuario.RolUsuario.PACIENTE);
        usuarioActualizado.setActivo(1);
        usuarioActualizado.setPassword("nuevaPassword123");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        usuarioService.actualizarUsuario(1L, usuarioActualizado);

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    
    @Test
    void eliminarUsuario_DebeEliminarUsuarioExitosamente() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarUsuario_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.eliminarUsuario(999L);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    
    @Test
    void iniciarSesion_DebeRetornarUsuarioCuandoCredencialesSonCorrectas() {
        when(usuarioRepository.findByEmail("juan.perez@test.com"))
                .thenReturn(Optional.of(usuarioTest));

        Usuario resultado = usuarioService.iniciarSesion("juan.perez@test.com", "password123");

        assertNotNull(resultado);
        assertEquals("juan.perez@test.com", resultado.getEmail());
    }

    @Test
    void iniciarSesion_DebeLanzarExcepcionCuandoEmailNoExiste() {
        when(usuarioRepository.findByEmail("noexiste@test.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.iniciarSesion("noexiste@test.com", "password123");
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
    }

    @Test
    void iniciarSesion_DebeLanzarExcepcionCuandoPasswordEsIncorrecto() {
        when(usuarioRepository.findByEmail("juan.perez@test.com"))
                .thenReturn(Optional.of(usuarioTest));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.iniciarSesion("juan.perez@test.com", "passwordIncorrecto");
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
    }

    @Test
    void iniciarSesion_DebeLanzarExcepcionCuandoUsuarioEstaInactivo() {
        usuarioTest.setActivo(0);
        when(usuarioRepository.findByEmail("juan.perez@test.com"))
                .thenReturn(Optional.of(usuarioTest));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.iniciarSesion("juan.perez@test.com", "password123");
        });

        assertEquals("Usuario inactivo", exception.getMessage());
    }

    @Test
    void iniciarSesion_DebeLanzarExcepcionCuandoActivoEsNull() {
        usuarioTest.setActivo(null);
        when(usuarioRepository.findByEmail("juan.perez@test.com"))
                .thenReturn(Optional.of(usuarioTest));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.iniciarSesion("juan.perez@test.com", "password123");
        });

        assertEquals("Usuario inactivo", exception.getMessage());
    }

    
    @Test
    void desactivarUsuario_DebeDesactivarUsuarioExitosamente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        Usuario resultado = usuarioService.desactivarUsuario(1L);

        assertNotNull(resultado);
        assertEquals(0, resultado.getActivo());
        verify(usuarioRepository, times(1)).save(usuarioTest);
    }

    @Test
    void desactivarUsuario_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.desactivarUsuario(999L);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
    }

    
    @Test
    void activarUsuario_DebeActivarUsuarioExitosamente() {
        usuarioTest.setActivo(0);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        Usuario resultado = usuarioService.activarUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.getActivo());
        verify(usuarioRepository, times(1)).save(usuarioTest);
    }

    @Test
    void activarUsuario_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.activarUsuario(999L);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
    }

    
    @Test
    void emailExiste_DebeRetornarTrueCuandoEmailExiste() {
        when(usuarioRepository.existsByEmail("juan.perez@test.com")).thenReturn(true);

        boolean resultado = usuarioService.emailExiste("juan.perez@test.com");

        assertTrue(resultado);
    }

    @Test
    void emailExiste_DebeRetornarFalseCuandoEmailNoExiste() {
        when(usuarioRepository.existsByEmail("noexiste@test.com")).thenReturn(false);

        boolean resultado = usuarioService.emailExiste("noexiste@test.com");

        assertFalse(resultado);
    }

    
    @Test
    void rutExiste_DebeRetornarTrueCuandoRutExiste() {
        when(usuarioRepository.existsByRut("12345678-9")).thenReturn(true);

        boolean resultado = usuarioService.rutExiste("12345678-9");

        assertTrue(resultado);
    }

    @Test
    void rutExiste_DebeRetornarFalseCuandoRutNoExiste() {
        when(usuarioRepository.existsByRut("98765432-1")).thenReturn(false);

        boolean resultado = usuarioService.rutExiste("98765432-1");

        assertFalse(resultado);
    }
}