package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ciclo {
    private Long idCiclo;
    private Long anio;
    private Long numero;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
}
