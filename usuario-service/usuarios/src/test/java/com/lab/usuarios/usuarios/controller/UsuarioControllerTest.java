package com.lab.usuarios.usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.usuarios.usuarios.model.Usuario;
import com.lab.usuarios.usuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    void obtenerTodosLosUsuarios_DebeRetornar200ConListaDeUsuarios() throws Exception {
        when(usuarioService.obtenerTodosLosUsuarios())
                .thenReturn(Arrays.asList(usuarioTest));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("juan.perez@test.com"));

        verify(usuarioService, times(1)).obtenerTodosLosUsuarios();
    }

    @Test
    void obtenerTodosLosUsuarios_DebeRetornar500CuandoHayError() throws Exception {
        when(usuarioService.obtenerTodosLosUsuarios())
                .thenThrow(new RuntimeException("Error de base de datos"));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isInternalServerError());
    }

    
    @Test
    void obtenerUsuarioPorId_DebeRetornar200CuandoUsuarioExiste() throws Exception {
        when(usuarioService.obtenerUsuarioPorId(1L))
                .thenReturn(Optional.of(usuarioTest));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("juan.perez@test.com"));

        verify(usuarioService, times(1)).obtenerUsuarioPorId(1L);
    }

    @Test
    void obtenerUsuarioPorId_DebeRetornar404CuandoUsuarioNoExiste() throws Exception {
        when(usuarioService.obtenerUsuarioPorId(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerUsuarioPorId_DebeRetornar500CuandoHayError() throws Exception {
        when(usuarioService.obtenerUsuarioPorId(anyLong()))
                .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());
    }

    
    @Test
    void obtenerUsuarioPorEmail_DebeRetornar200CuandoUsuarioExiste() throws Exception {
        when(usuarioService.obtenerUsuarioPorEmail("juan.perez@test.com"))
                .thenReturn(Optional.of(usuarioTest));

        mockMvc.perform(get("/usuarios/email/juan.perez@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan.perez@test.com"));
    }

    @Test
    void obtenerUsuarioPorEmail_DebeRetornar404CuandoUsuarioNoExiste() throws Exception {
        when(usuarioService.obtenerUsuarioPorEmail("noexiste@test.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/email/noexiste@test.com"))
                .andExpect(status().isNotFound());
    }

    
    @Test
    void obtenerUsuariosPorRol_DebeRetornar200ConUsuariosDelRol() throws Exception {
        when(usuarioService.obtenerUsuariosPorRol(Usuario.RolUsuario.PACIENTE))
                .thenReturn(Arrays.asList(usuarioTest));

        mockMvc.perform(get("/usuarios/rol/PACIENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].rol").value("PACIENTE"));
    }

    @Test
    void obtenerUsuariosPorRol_DebeRetornar400CuandoRolEsInvalido() throws Exception {
        mockMvc.perform(get("/usuarios/rol/INVALIDO"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Rol inválido. Use: ADMIN, MEDICO o PACIENTE"));
    }

    @Test
    void obtenerUsuariosPorRol_DebeRetornar500CuandoHayError() throws Exception {
        when(usuarioService.obtenerUsuariosPorRol(any()))
                .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(get("/usuarios/rol/ADMIN"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());
    }

    
    @Test
    void obtenerUsuariosActivos_DebeRetornar200ConUsuariosActivos() throws Exception {
        when(usuarioService.obtenerUsuariosActivos())
                .thenReturn(Arrays.asList(usuarioTest));

        mockMvc.perform(get("/usuarios/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].activo").value(1));
    }

    @Test
    void obtenerUsuariosActivos_DebeRetornar500CuandoHayError() throws Exception {
        when(usuarioService.obtenerUsuariosActivos())
                .thenThrow(new RuntimeException("Error de conexión"));

        mockMvc.perform(get("/usuarios/activos"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());
    }

    
    @Test
    void crearUsuario_DebeRetornar201CuandoUsuarioEsCreadoExitosamente() throws Exception {
        when(usuarioService.crearUsuario(any(Usuario.class)))
                .thenReturn(usuarioTest);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioTest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan.perez@test.com"));

        verify(usuarioService, times(1)).crearUsuario(any(Usuario.class));
    }

    @Test
    void crearUsuario_DebeRetornar400CuandoEmailYaExiste() throws Exception {
        when(usuarioService.crearUsuario(any(Usuario.class)))
                .thenThrow(new RuntimeException("El email ya está registrado"));

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioTest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El email ya está registrado"));
    }

    @Test
    void crearUsuario_DebeRetornar500CuandoHayErrorInesperado() throws Exception {
        when(usuarioService.crearUsuario(any(Usuario.class)))
                .thenThrow(new NullPointerException("Error inesperado"));

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioTest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error al crear el usuario"));
    }

    
    @Test
    void iniciarSesion_DebeRetornar200CuandoCredencialesSonCorrectas() throws Exception {
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("email", "juan.perez@test.com");
        credenciales.put("password", "password123");

        when(usuarioService.iniciarSesion("juan.perez@test.com", "password123"))
                .thenReturn(usuarioTest);

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credenciales)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Inicio de sesión exitoso"))
                .andExpect(jsonPath("$.usuario.email").value("juan.perez@test.com"));
    }

    @Test
    void iniciarSesion_DebeRetornar400CuandoFaltaEmail() throws Exception {
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("password", "password123");

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credenciales)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email y contraseña son requeridos"));
    }

    @Test
    void iniciarSesion_DebeRetornar400CuandoFaltaPassword() throws Exception {
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("email", "juan.perez@test.com");

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credenciales)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email y contraseña son requeridos"));
    }

    @Test
    void iniciarSesion_DebeRetornar401CuandoCredencialesSonIncorrectas() throws Exception {
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("email", "juan.perez@test.com");
        credenciales.put("password", "passwordIncorrecto");

        when(usuarioService.iniciarSesion("juan.perez@test.com", "passwordIncorrecto"))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credenciales)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciales inválidas"));
    }

    @Test
    void iniciarSesion_DebeRetornar500CuandoHayErrorInesperado() throws Exception {
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("email", "juan.perez@test.com");
        credenciales.put("password", "password123");

        when(usuarioService.iniciarSesion(anyString(), anyString()))
                .thenThrow(new NullPointerException("Error inesperado"));

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credenciales)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error al iniciar sesión"));
    }

    @Test
    void actualizarUsuario_DebeRetornar200CuandoUsuarioEsActualizadoExitosamente() throws Exception {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Juan Carlos");
        usuarioActualizado.setApellido("Pérez González");
        usuarioActualizado.setEmail("juan.perez@test.com");
        usuarioActualizado.setRut("12345678-9");
        usuarioActualizado.setTelefono("+56987654321");
        usuarioActualizado.setRol(Usuario.RolUsuario.MEDICO);
        usuarioActualizado.setActivo(1);

        when(usuarioService.actualizarUsuario(eq(1L), any(Usuario.class)))
                .thenReturn(usuarioActualizado);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Carlos"));
    }

    @Test
    void actualizarUsuario_DebeRetornar400CuandoUsuarioNoExiste() throws Exception {
        when(usuarioService.actualizarUsuario(eq(999L), any(Usuario.class)))
                .thenThrow(new RuntimeException("Usuario no encontrado con ID: 999"));

        mockMvc.perform(put("/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioTest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void actualizarUsuario_DebeRetornar500CuandoHayErrorInesperado() throws Exception {
        when(usuarioService.actualizarUsuario(anyLong(), any(Usuario.class)))
                .thenThrow(new NullPointerException("Error inesperado"));

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioTest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error al actualizar el usuario"));
    }

    @Test
    void desactivarUsuario_DebeRetornar200CuandoUsuarioEsDesactivadoExitosamente() throws Exception {
        usuarioTest.setActivo(0);
        when(usuarioService.desactivarUsuario(1L))
                .thenReturn(usuarioTest);

        mockMvc.perform(put("/usuarios/1/desactivar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(0));
    }

    @Test
    void desactivarUsuario_DebeRetornar404CuandoUsuarioNoExiste() throws Exception {
        when(usuarioService.desactivarUsuario(999L))
                .thenThrow(new RuntimeException("Usuario no encontrado con ID: 999"));

        mockMvc.perform(put("/usuarios/999/desactivar"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void desactivarUsuario_DebeRetornar500CuandoHayError() throws Exception {
        when(usuarioService.desactivarUsuario(anyLong()))
                .thenThrow(new NullPointerException("Error inesperado"));

        mockMvc.perform(put("/usuarios/1/desactivar"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error al desactivar el usuario"));
    }

    
    @Test
    void activarUsuario_DebeRetornar200CuandoUsuarioEsActivadoExitosamente() throws Exception {
        usuarioTest.setActivo(1);
        when(usuarioService.activarUsuario(1L))
                .thenReturn(usuarioTest);

        mockMvc.perform(put("/usuarios/1/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(1));
    }

    @Test
    void activarUsuario_DebeRetornar404CuandoUsuarioNoExiste() throws Exception {
        when(usuarioService.activarUsuario(999L))
                .thenThrow(new RuntimeException("Usuario no encontrado con ID: 999"));

        mockMvc.perform(put("/usuarios/999/activar"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void activarUsuario_DebeRetornar500CuandoHayError() throws Exception {
        when(usuarioService.activarUsuario(anyLong()))
                .thenThrow(new NullPointerException("Error inesperado"));

        mockMvc.perform(put("/usuarios/1/activar"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error al activar el usuario"));
    }

    @Test
    void eliminarUsuario_DebeRetornar200CuandoUsuarioEsEliminadoExitosamente() throws Exception {
        doNothing().when(usuarioService).eliminarUsuario(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Usuario eliminado exitosamente"));

        verify(usuarioService, times(1)).eliminarUsuario(1L);
    }

    @Test
    void eliminarUsuario_DebeRetornar404CuandoUsuarioNoExiste() throws Exception {
        doThrow(new RuntimeException("Usuario no encontrado con ID: 999"))
                .when(usuarioService).eliminarUsuario(999L);

        mockMvc.perform(delete("/usuarios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void eliminarUsuario_DebeRetornar500CuandoHayError() throws Exception {
        doThrow(new NullPointerException("Error inesperado"))
                .when(usuarioService).eliminarUsuario(anyLong());

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error al eliminar el usuario"));
    }
}