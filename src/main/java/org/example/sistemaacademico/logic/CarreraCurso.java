package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la relación entre una carrera, un curso y un ciclo.
 * Utiliza Lombok para generar getters, setters, constructores, toString, equals y hashCode.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarreraCurso {
    private Long idCarreraCurso;
    private Long pkCarrera;
    private Long pkCurso;
    private Long pkCiclo;
}
