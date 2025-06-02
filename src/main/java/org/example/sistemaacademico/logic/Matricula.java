package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una matrícula.
 * Utiliza Lombok para generar getters, setters, constructores, toString, equals y hashCode.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {
    private Long idMatricula;
    private Long pkAlumno;
    private Long pkGrupo;
    private Long nota;
}
