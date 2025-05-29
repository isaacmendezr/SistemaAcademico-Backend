package org.example.sistemaacademico.logic.dto;

public class MatriculaAlumnoDto {
    private Long idMatricula;
    private Double nota;
    private String numeroGrupo;
    private String horario;
    private String codigoCarrera;
    private String nombreCarrera;
    private String codigoCurso;
    private String nombreCurso;
    private String nombreProfesor;
    private String cedulaProfesor;

    // Constructor
    public MatriculaAlumnoDto(Long idMatricula, Double nota, String numeroGrupo, String horario,
                              String codigoCarrera, String nombreCarrera, String codigoCurso, String nombreCurso,
                              String nombreProfesor, String cedulaProfesor) {
        this.idMatricula = idMatricula;
        this.nota = nota;
        this.numeroGrupo = numeroGrupo;
        this.horario = horario;
        this.codigoCarrera = codigoCarrera;
        this.nombreCarrera = nombreCarrera;
        this.codigoCurso = codigoCurso;
        this.nombreCurso = nombreCurso;
        this.nombreProfesor = nombreProfesor;
        this.cedulaProfesor = cedulaProfesor;
    }

    public Long getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(Long idMatricula) {
        this.idMatricula = idMatricula;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getNumeroGrupo() {
        return numeroGrupo;
    }

    public void setNumeroGrupo(String numeroGrupo) {
        this.numeroGrupo = numeroGrupo;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getCodigoCarrera() {
        return codigoCarrera;
    }

    public void setCodigoCarrera(String codigoCarrera) {
        this.codigoCarrera = codigoCarrera;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public String getCedulaProfesor() {
        return cedulaProfesor;
    }

    public void setCedulaProfesor(String cedulaProfesor) {
        this.cedulaProfesor = cedulaProfesor;
    }
}
