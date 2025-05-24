package org.example.sistemaacademico.logic;


import java.time.LocalDate;

public class Alumno {
    private Long idAlumno;
    private String cedula;
    private String nombre;
    private String telefono;
    private String email;
    private LocalDate fechaNacimiento;
    private Long pkCarrera;

    public Alumno(Long idAlumno, String cedula, String nombre, String telefono, String email, LocalDate fechaNacimiento, Long pkCarrera) {
        this.idAlumno = idAlumno;
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.pkCarrera = pkCarrera;
    }

    public Long getIdAlumno() {
        return idAlumno;
    }
    public void setIdAlumno(Long idAlumno) {
        this.idAlumno = idAlumno;
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
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public Long getPkCarrera() {
        return pkCarrera;
    }
    public void setPkCarrera(Long pkCarrera) {
        this.pkCarrera = pkCarrera;
    }
}
