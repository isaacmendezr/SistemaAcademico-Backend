package org.example.sistemaacademico.logic;

public class Carrera {
    private Long idCarrera;
    private String codigo;
    private String nombre;
    private String titulo;

    public Carrera(Long idCarrera, String codigo, String nombre, String titulo) {
        this.idCarrera = idCarrera;
        this.codigo = codigo;
        this.nombre = nombre;
        this.titulo = titulo;
    }

    public Long getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Long idCarrera) {
        this.idCarrera = idCarrera;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
