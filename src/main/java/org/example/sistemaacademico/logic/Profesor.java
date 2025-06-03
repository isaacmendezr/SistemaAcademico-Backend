package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesor {
    private Long idProfesor;
    private String cedula;
    private String nombre;
    private String telefono;
    private String email;
}
