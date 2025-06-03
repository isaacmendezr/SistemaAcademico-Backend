package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
