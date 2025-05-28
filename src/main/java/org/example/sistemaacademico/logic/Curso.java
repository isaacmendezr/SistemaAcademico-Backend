package org.example.sistemaacademico.logic;

public class Curso {
    private Long idCurso;
    private String codigo;
    private String nombre;
    private Long creditos;
    private Long horasSemanales;
    private Long idCarreraCurso;
    private Long anio;
    private Long numero;
    private Long idCiclo;

    public Curso(Long idCurso, String codigo, String nombre, Long creditos, Long horasSemanales) {
        this.idCurso = idCurso;
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horasSemanales = horasSemanales;
    }

    public Curso(Long idCurso, String codigo, String nombre, Long creditos, Long horasSemanales, Long idCarreraCurso, Long anio, Long numero, Long idCiclo) {
        this.idCurso = idCurso;
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horasSemanales = horasSemanales;
        this.idCarreraCurso = idCarreraCurso;
        this.anio = anio;
        this.numero = numero;
        this.idCiclo = idCiclo;
    }

    public Curso(Long idCurso, String codigo, String nombre, Long creditos, Long horasSemanales, Long idCarreraCurso) {
        this.idCurso = idCurso;
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horasSemanales = horasSemanales;
        this.idCarreraCurso = idCarreraCurso;
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

    public Long getIdCarreraCurso() {
        return idCarreraCurso;
    }

    public void setIdCarreraCurso(Long idCarreraCurso) {
        this.idCarreraCurso = idCarreraCurso;
    }

    public Long getAnio() {
        return anio;
    }

    public void setAnio(Long anio) {
        this.anio = anio;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Long getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(Long idCiclo) {
        this.idCiclo = idCiclo;
    }
}
