package org.example.sistemaacademico.logic;
public class Matricula {
    private Long idMatricula;
    private Long pkAlumno;
    private Long pkGrupo;
    private Long nota;

    public Matricula(Long idMatricula, Long pkAlumno, Long pkGrupo, Long nota) {
        this.idMatricula = idMatricula;
        this.pkAlumno = pkAlumno;
        this.pkGrupo = pkGrupo;
        this.nota = nota;
    }

    public Long getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(Long idMatricula) {
        this.idMatricula = idMatricula;
    }

    public Long getPkAlumno() {
        return pkAlumno;
    }

    public void setPkAlumno(Long pkAlumno) {
        this.pkAlumno = pkAlumno;
    }

    public Long getPkGrupo() {
        return pkGrupo;
    }

    public void setPkGrupo(Long pkGrupo) {
        this.pkGrupo = pkGrupo;
    }

    public Long getNota() {
        return nota;
    }

    public void setNota(Long nota) {
        this.nota = nota;
    }
}
