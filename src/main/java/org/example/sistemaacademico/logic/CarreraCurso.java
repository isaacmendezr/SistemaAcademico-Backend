package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarreraCurso {
    private Long idCarreraCurso;
    private Long pkCarrera;
    private Long pkCurso;
    private Long pkCiclo;
}
