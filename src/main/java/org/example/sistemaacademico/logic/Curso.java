package org.example.sistemaacademico.logic;

public class Curso {
    private Long idCurso;
    private String codigo;
    private String nombre;
    private Long creditos;
    private Long horasSemanales;

    public Curso(Long idCurso, String codigo, String nombre, Long creditos, Long horasSemanales) {
        this.idCurso = idCurso;
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horasSemanales = horasSemanales;
    }

    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCreditos() {
        return creditos;
    }

    public void setCreditos(Long creditos) {
        this.creditos = creditos;
    }

    public Long getHorasSemanales() {
        return horasSemanales;
    }

    public void setHorasSemanales(Long horasSemanales) {
        this.horasSemanales = horasSemanales;
    }


}
