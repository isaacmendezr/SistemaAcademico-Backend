package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrera {
    private Long idCarrera;
    private String codigo;
    private String nombre;
    private String titulo;
}
