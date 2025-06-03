package org.example.sistemaacademico.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un grupo con información del profesor, curso, carrera y ciclo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrupoProfesorDto {
    private Long idGrupo;
    private Long numeroGrupo;
    private String horario;
    private String codigoCurso;
    private String nombreCurso;
    private String codigoCarrera;
    private String nombreCarrera;
    private Long anio;
    private Long numeroCiclo;
}
