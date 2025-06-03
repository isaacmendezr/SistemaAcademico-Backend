package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {
    private Long idAlumno;
    private String cedula;
    private String nombre;
    private String telefono;
    private String email;
    private LocalDate fechaNacimiento;
    private Long pkCarrera;
}
