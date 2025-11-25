package com.laboratorio.laboratorio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "laboratorios")
public class Laboratorio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 50)
    private String tipo;
    
    @Column(nullable = false)
    private String direccion;
    
    @Column(nullable = false, length = 20)
    private String telefono;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(length = 500)
    private String especialidades;
    
    @Column(nullable = false)
    private String horarioAtencion;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(nullable = false)
    private Integer capacidadDiaria;
    
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    public Laboratorio() {}
    
    public Laboratorio(String nombre, String tipo, String direccion, 
                       String telefono, String email, String especialidades,
                       String horarioAtencion, Integer capacidadDiaria) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.especialidades = especialidades;
        this.horarioAtencion = horarioAtencion;
        this.capacidadDiaria = capacidadDiaria;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEspecialidades() {
        return especialidades;
    }
    
    public void setEspecialidades(String especialidades) {
        this.especialidades = especialidades;
    }
    
    public String getHorarioAtencion() {
        return horarioAtencion;
    }
    
    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public Integer getCapacidadDiaria() {
        return capacidadDiaria;
    }
    
    public void setCapacidadDiaria(Integer capacidadDiaria) {
        this.capacidadDiaria = capacidadDiaria;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

}
