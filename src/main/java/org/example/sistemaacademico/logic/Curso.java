package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    private Long idCurso;
    private String codigo;
    private String nombre;
    private Long creditos;
    private Long horasSemanales;
}
