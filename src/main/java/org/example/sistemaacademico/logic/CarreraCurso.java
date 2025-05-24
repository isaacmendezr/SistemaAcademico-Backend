package org.example.sistemaacademico.logic;

public class CarreraCurso {
    private Long idCarreraCurso;
    private Long pkCarrera;
    private Long pkCurso;
    private Long pkCiclo;

    public CarreraCurso(Long idCarreraCurso, Long pkCarrera, Long pkCurso, Long pkCiclo) {
        this.idCarreraCurso = idCarreraCurso;
        this.pkCarrera = pkCarrera;
        this.pkCurso = pkCurso;
        this.pkCiclo = pkCiclo;
    }

    public Long getIdCarreraCurso() {
        return idCarreraCurso;
    }

    public void setIdCarreraCurso(Long idCarreraCurso) {
        this.idCarreraCurso = idCarreraCurso;
    }

    public Long getPkCarrera() {
        return pkCarrera;
    }

    public void setPkCarrera(Long pkCarrera) {
        this.pkCarrera = pkCarrera;
    }

    public Long getPkCurso() {
        return pkCurso;
    }

    public void setPkCurso(Long pkCurso) {
        this.pkCurso = pkCurso;
    }

    public Long getPkCiclo() {
        return pkCiclo;
    }

    public void setPkCiclo(Long pkCiclo) {
        this.pkCiclo = pkCiclo;
    }
}
