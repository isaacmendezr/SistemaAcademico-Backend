package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un grupo.
 * Utiliza Lombok para generar getters, setters, constructores, toString, equals y hashCode.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grupo {
    private Long idGrupo;
    private Long idCarreraCurso;
    private Long numeroGrupo;
    private String horario;
    private Long idProfesor;
}
