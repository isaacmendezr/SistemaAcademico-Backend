package org.example.sistemaacademico.logic;

public class Profesor {
    private Long idProfesor;
    private String cedula;
    private String nombre;
    private String telefono;
    private String email;

    public Profesor(Long idProfesor, String cedula, String nombre, String telefono, String email) {
        this.idProfesor = idProfesor;
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public Long getIdProfesor() {
        return idProfesor;
    }
    public void setIdProfesor(Long idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
