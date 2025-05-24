package org.example.sistemaacademico.logic;

import java.time.LocalDate;

public class Ciclo {
    private Long idCiclo;
    private Long anio;
    private Long numero;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;

    public Ciclo(Long idCiclo, Long anio, Long numero, LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.idCiclo = idCiclo;
        this.anio = anio;
        this.numero = numero;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public Long getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(Long idCiclo) {
        this.idCiclo = idCiclo;
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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
