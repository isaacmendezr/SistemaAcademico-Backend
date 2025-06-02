package org.example.sistemaacademico.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un curso con información adicional como carrera y ciclo.
 * Utiliza Lombok para generar getters, setters, constructores, toString, equals y hashCode.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDto {
    private Long idCurso;
    private String codigo;
    private String nombre;
    private Long creditos;
    private Long horasSemanales;
    private Long idCarreraCurso;
    private Long anio;
    private Long numero;
    private Long idCiclo;

    /**
     * Constructor con 6 parámetros para compatibilidad con código existente.
     * Los campos anio, numero e idCiclo serán null.
     *
     * @param idCurso        ID del curso.
     * @param codigo         Código del curso.
     * @param nombre         Nombre del curso.
     * @param creditos       Créditos del curso.
     * @param horasSemanales Horas semanales del curso.
     * @param idCarreraCurso ID de la relación carrera-curso.
     */
    @Deprecated
    public CursoDto(Long idCurso, String codigo, String nombre, Long creditos, Long horasSemanales, Long idCarreraCurso) {
        this.idCurso = idCurso;
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horasSemanales = horasSemanales;
        this.idCarreraCurso = idCarreraCurso;
        this.anio = null;
        this.numero = null;
        this.idCiclo = null;
    }
}
