package org.example.sistemaacademico.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long idUsuario;
    private String cedula;
    private String clave;
    private String tipo;

    /**
     * Constructor con 3 parámetros para compatibilidad con código existente.
     * El campo 'clave' será null.
     *
     * @param idUsuario ID del usuario.
     * @param cedula    Cédula del usuario.
     * @param tipo      Tipo de usuario.
     * @deprecated Usa el constructor completo para inicializar todos los campos, incluyendo 'clave'.
     */
    @Deprecated
    public Usuario(Long idUsuario, String cedula, String tipo) {
        this.idUsuario = idUsuario;
        this.cedula = cedula;
        this.tipo = tipo;
        this.clave = null;
    }
}
