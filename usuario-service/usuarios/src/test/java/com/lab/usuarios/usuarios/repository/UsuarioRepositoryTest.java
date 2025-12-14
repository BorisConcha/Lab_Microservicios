package com.lab.usuarios.usuarios.repository;

import com.lab.usuarios.usuarios.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setNombre("Juan");
        usuario1.setApellido("Pérez");
        usuario1.setEmail("juan.perez@test.com");
        usuario1.setPassword("password123");
        usuario1.setRut("12345678-9");
        usuario1.setTelefono("+56912345678");
        usuario1.setRol(Usuario.RolUsuario.PACIENTE);
        usuario1.setActivo(1);
        usuario1.setFechaRegistro(LocalDateTime.now());
        usuario1.setFechaActualizacion(LocalDateTime.now());

        usuario2 = new Usuario();
        usuario2.setNombre("María");
        usuario2.setApellido("González");
        usuario2.setEmail("maria.gonzalez@test.com");
        usuario2.setPassword("password456");
        usuario2.setRut("98765432-1");
        usuario2.setTelefono("+56987654321");
        usuario2.setRol(Usuario.RolUsuario.MEDICO);
        usuario2.setActivo(1);
        usuario2.setFechaRegistro(LocalDateTime.now());
        usuario2.setFechaActualizacion(LocalDateTime.now());
    }


    @Test
    void findByEmail_DebeEncontrarUsuarioPorEmail() {
        entityManager.persist(usuario1);
        entityManager.flush();

        Optional<Usuario> resultado = usuarioRepository.findByEmail("juan.perez@test.com");

        assertTrue(resultado.isPresent());
        assertEquals("juan.perez@test.com", resultado.get().getEmail());
        assertEquals("Juan", resultado.get().getNombre());
    }

    @Test
    void findByEmail_DebeRetornarVacioCuandoEmailNoExiste() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("noexiste@test.com");

        assertFalse(resultado.isPresent());
    }


    @Test
    void findByRut_DebeEncontrarUsuarioPorRut() {
        entityManager.persist(usuario1);
        entityManager.flush();

        Optional<Usuario> resultado = usuarioRepository.findByRut("12345678-9");

        assertTrue(resultado.isPresent());
        assertEquals("12345678-9", resultado.get().getRut());
        assertEquals("Juan", resultado.get().getNombre());
    }

    @Test
    void findByRut_DebeRetornarVacioCuandoRutNoExiste() {
        Optional<Usuario> resultado = usuarioRepository.findByRut("11111111-1");

        assertFalse(resultado.isPresent());
    }


    @Test
    void findByRol_DebeEncontrarUsuariosPorRol() {
        entityManager.persist(usuario1);
        entityManager.persist(usuario2);
        entityManager.flush();

        List<Usuario> pacientes = usuarioRepository.findByRol(Usuario.RolUsuario.PACIENTE);
        List<Usuario> medicos = usuarioRepository.findByRol(Usuario.RolUsuario.MEDICO);

        assertEquals(1, pacientes.size());
        assertEquals(Usuario.RolUsuario.PACIENTE, pacientes.get(0).getRol());
        
        assertEquals(1, medicos.size());
        assertEquals(Usuario.RolUsuario.MEDICO, medicos.get(0).getRol());
    }

    @Test
    void findByRol_DebeRetornarListaVaciaCuandoNoHayUsuariosDelRol() {
        List<Usuario> admins = usuarioRepository.findByRol(Usuario.RolUsuario.ADMIN);

        assertTrue(admins.isEmpty());
    }


    @Test
    void findByActivo_DebeEncontrarUsuariosActivos() {
        entityManager.persist(usuario1);
        
        Usuario usuarioInactivo = new Usuario();
        usuarioInactivo.setNombre("Pedro");
        usuarioInactivo.setApellido("López");
        usuarioInactivo.setEmail("pedro.lopez@test.com");
        usuarioInactivo.setPassword("password789");
        usuarioInactivo.setRut("11111111-1");
        usuarioInactivo.setTelefono("+56911111111");
        usuarioInactivo.setRol(Usuario.RolUsuario.PACIENTE);
        usuarioInactivo.setActivo(0);
        usuarioInactivo.setFechaRegistro(LocalDateTime.now());
        usuarioInactivo.setFechaActualizacion(LocalDateTime.now());
        entityManager.persist(usuarioInactivo);
        
        entityManager.flush();

        List<Usuario> activos = usuarioRepository.findByActivo(1);
        List<Usuario> inactivos = usuarioRepository.findByActivo(0);

        assertEquals(1, activos.size());
        assertEquals(1, activos.get(0).getActivo());
        
        assertEquals(1, inactivos.size());
        assertEquals(0, inactivos.get(0).getActivo());
    }

    @Test
    void findByActivo_DebeRetornarListaVaciaCuandoNoHayUsuariosConEseEstado() {
        List<Usuario> usuarios = usuarioRepository.findByActivo(1);

        assertTrue(usuarios.isEmpty());
    }


    @Test
    void existsByEmail_DebeRetornarTrueCuandoEmailExiste() {
        entityManager.persist(usuario1);
        entityManager.flush();

        boolean existe = usuarioRepository.existsByEmail("juan.perez@test.com");

        assertTrue(existe);
    }

    @Test
    void existsByEmail_DebeRetornarFalseCuandoEmailNoExiste() {
        boolean existe = usuarioRepository.existsByEmail("noexiste@test.com");

        assertFalse(existe);
    }


    @Test
    void existsByRut_DebeRetornarTrueCuandoRutExiste() {
        entityManager.persist(usuario1);
        entityManager.flush();

        boolean existe = usuarioRepository.existsByRut("12345678-9");

        assertTrue(existe);
    }

    @Test
    void existsByRut_DebeRetornarFalseCuandoRutNoExiste() {
        boolean existe = usuarioRepository.existsByRut("11111111-1");

        assertFalse(existe);
    }


    @Test
    void save_DebeGuardarUsuarioCorrectamente() {
        Usuario guardado = usuarioRepository.save(usuario1);

        assertNotNull(guardado.getId());
        assertEquals("juan.perez@test.com", guardado.getEmail());
        assertNotNull(guardado.getFechaRegistro());
        assertNotNull(guardado.getFechaActualizacion());
    }

    @Test
    void findById_DebeEncontrarUsuarioPorId() {
        Usuario guardado = entityManager.persist(usuario1);
        entityManager.flush();

        Optional<Usuario> encontrado = usuarioRepository.findById(guardado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(guardado.getId(), encontrado.get().getId());
    }

    @Test
    void findAll_DebeRetornarTodosLosUsuarios() {
        entityManager.persist(usuario1);
        entityManager.persist(usuario2);
        entityManager.flush();

        List<Usuario> usuarios = usuarioRepository.findAll();

        assertEquals(2, usuarios.size());
    }

    @Test
    void delete_DebeEliminarUsuarioCorrectamente() {
        Usuario guardado = entityManager.persist(usuario1);
        entityManager.flush();
        Long id = guardado.getId();

        usuarioRepository.deleteById(id);
        Optional<Usuario> eliminado = usuarioRepository.findById(id);

        assertFalse(eliminado.isPresent());
    }

    @Test
    void update_DebeActualizarUsuarioCorrectamente() {
        Usuario guardado = entityManager.persist(usuario1);
        entityManager.flush();
        entityManager.detach(guardado);

        guardado.setNombre("Juan Carlos");
        guardado.setTelefono("+56999999999");
        Usuario actualizado = usuarioRepository.save(guardado);

        assertEquals("Juan Carlos", actualizado.getNombre());
        assertEquals("+56999999999", actualizado.getTelefono());
    }


    @Test
    void save_NoDebePermitirEmailsDuplicados() {
        entityManager.persist(usuario1);
        entityManager.flush();

        Usuario usuarioDuplicado = new Usuario();
        usuarioDuplicado.setNombre("Otro");
        usuarioDuplicado.setApellido("Usuario");
        usuarioDuplicado.setEmail("juan.perez@test.com");
        usuarioDuplicado.setPassword("password");
        usuarioDuplicado.setRut("22222222-2");
        usuarioDuplicado.setTelefono("+56922222222");
        usuarioDuplicado.setRol(Usuario.RolUsuario.PACIENTE);
        usuarioDuplicado.setActivo(1);
        usuarioDuplicado.setFechaRegistro(LocalDateTime.now());
        usuarioDuplicado.setFechaActualizacion(LocalDateTime.now());

        assertThrows(Exception.class, () -> {
            entityManager.persist(usuarioDuplicado);
            entityManager.flush();
        });
    }

    @Test
    void save_NoDebePermitirRutsDuplicados() {
        entityManager.persist(usuario1);
        entityManager.flush();

        Usuario usuarioDuplicado = new Usuario();
        usuarioDuplicado.setNombre("Otro");
        usuarioDuplicado.setApellido("Usuario");
        usuarioDuplicado.setEmail("otro@test.com");
        usuarioDuplicado.setPassword("password");
        usuarioDuplicado.setRut("12345678-9");
        usuarioDuplicado.setTelefono("+56922222222");
        usuarioDuplicado.setRol(Usuario.RolUsuario.PACIENTE);
        usuarioDuplicado.setActivo(1);
        usuarioDuplicado.setFechaRegistro(LocalDateTime.now());
        usuarioDuplicado.setFechaActualizacion(LocalDateTime.now());

        assertThrows(Exception.class, () -> {
            entityManager.persist(usuarioDuplicado);
            entityManager.flush();
        });
    }
}