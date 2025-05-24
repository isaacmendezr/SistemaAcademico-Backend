package org.example.sistemaacademico.logic;

public class Grupo {
    private Long idGrupo;
    private Long pkCarreraCurso;
    private Long numeroGrupo;
    private String horario;
    private Long pkProfesor;

    public Grupo(Long idGrupo, Long pkCarreraCurso, Long numeroGrupo, String horario, Long pkProfesor) {
        this.idGrupo = idGrupo;
        this.pkCarreraCurso = pkCarreraCurso;
        this.numeroGrupo = numeroGrupo;
        this.horario = horario;
        this.pkProfesor = pkProfesor;
    }

    public Long getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Long getPkCarreraCurso() {
        return pkCarreraCurso;
    }

    public void setPkCarreraCurso(Long pkCarreraCurso) {
        this.pkCarreraCurso = pkCarreraCurso;
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

    public Long getPkProfesor() {
        return pkProfesor;
    }

    public void setPkProfesor(Long pkProfesor) {
        this.pkProfesor = pkProfesor;
    }
}
