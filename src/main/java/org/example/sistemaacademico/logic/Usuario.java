package org.example.sistemaacademico.logic;

public class Usuario {
    private Long idUsuario;
    private String cedula;
    private String clave;
    private String tipo;

    public Usuario(Long idUsuario, String cedula, String tipo) {
        this.idUsuario = idUsuario;
        this.cedula = cedula;
        this.tipo = tipo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
