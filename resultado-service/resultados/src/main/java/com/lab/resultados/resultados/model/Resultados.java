package com.lab.resultados.resultados.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resultados {

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El ID del paciente es obligatorio")
    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;
    
    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Column(name = "paciente_nombre", nullable = false, length = 200)
    private String pacienteNombre;
    
    @NotNull(message = "El ID del médico es obligatorio")
    @Column(name = "medico_id", nullable = false)
    private Long medicoId;
    
    @NotBlank(message = "El nombre del médico es obligatorio")
    @Column(name = "medico_nombre", nullable = false, length = 200)
    private String medicoNombre;
    
    @NotBlank(message = "El laboratorio es obligatorio")
    @Column(nullable = false, length = 150)
    private String laboratorio;
    
    @NotBlank(message = "El tipo de análisis es obligatorio")
    @Size(min = 3, max = 150, message = "El tipo de análisis debe tener entre 3 y 150 caracteres")
    @Column(name = "tipo_analisis", nullable = false, length = 150)
    private String tipoAnalisis;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String resultadoDetalle;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoResultado estado;
    
    @NotNull(message = "La fecha de análisis es obligatoria")
    @Column(name = "fecha_analisis", nullable = false)
    private LocalDate fechaAnalisis;
    
    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "valores_referencia", columnDefinition = "TEXT")
    private String valoresReferencia;
    
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoResultado.PENDIENTE;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    public enum EstadoResultado {
        PENDIENTE,
        EN_PROCESO,
        COMPLETADO,
        ENTREGADO
    }
    
}
