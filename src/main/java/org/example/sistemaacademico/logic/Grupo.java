package org.example.sistemaacademico.logic;

public class Grupo {
    private Long idGrupo;
    private Long idCarreraCurso;
    private Long numeroGrupo;
    private String horario;
    private Long idProfesor;
    private String nombreProfesor;

    public Grupo(Long idGrupo, Long idCarreraCurso, Long numeroGrupo, String horario, Long idProfesor) {
        this.idGrupo = idGrupo;
        this.idCarreraCurso = idCarreraCurso;
        this.numeroGrupo = numeroGrupo;
        this.horario = horario;
        this.idProfesor = idProfesor;
    }

    public Grupo(Long idGrupo, Long idCarreraCurso, Long numeroGrupo, String horario, Long idProfesor, String nombreProfesor) {
        this.idGrupo = idGrupo;
        this.idCarreraCurso = idCarreraCurso;
        this.numeroGrupo = numeroGrupo;
        this.horario = horario;
        this.idProfesor = idProfesor;
        this.nombreProfesor = nombreProfesor;
    }

    public Long getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Long getIdCarreraCurso() {
        return idCarreraCurso;
    }

    public void setIdCarreraCurso(Long idCarreraCurso) {
        this.idCarreraCurso = idCarreraCurso;
    }

    public Long getNumeroGrupo() {
        return numeroGrupo;
    }

    public void setNumeroGrupo(Long numeroGrupo) {
        this.numeroGrupo = numeroGrupo;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Long getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Long idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
}
