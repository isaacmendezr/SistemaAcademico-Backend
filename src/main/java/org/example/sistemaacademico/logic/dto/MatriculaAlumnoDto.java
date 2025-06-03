package org.example.sistemaacademico.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la matrícula de un alumno con información adicional del grupo, carrera, curso y profesor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaAlumnoDto {
    private Long idMatricula;
    private Double nota;
    private String numeroGrupo;
    private String horario;
    private String codigoCarrera;
    private String nombreCarrera;
    private String codigoCurso;
    private String nombreCurso;
    private String nombreProfesor;
    private String cedulaProfesor;
}
