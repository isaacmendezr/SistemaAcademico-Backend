package org.example.sistemaacademico.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un grupo con información adicional del profesor.
 * Utiliza Lombok para generar getters, setters, constructores, toString, equals y hashCode.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrupoDto {
    private Long idGrupo;
    private Long idCarreraCurso;
    private Long numeroGrupo;
    private String horario;
    private Long idProfesor;
    private String nombreProfesor;
}
