package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {
    private Long idMatricula;
    private Long pkAlumno;
    private Long pkGrupo;
    private Long nota;
}
