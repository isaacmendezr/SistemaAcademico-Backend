package org.example.sistemaacademico.database;

import java.io.Serial;
import java.io.Serializable;

/**
 * Excepción personalizada para errores generales del sistema, como problemas de base de datos o lógica de negocio.
 * Es una excepción no verificada (unchecked) que permite un manejo más flexible en aplicaciones Spring.
 */
public class GlobalException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor por defecto con un mensaje genérico.
     */
    public GlobalException() {
        super("Error en el sistema.");
    }

    /**
     * Constructor con un mensaje personalizado.
     *
     * @param message El mensaje de error.
     */
    public GlobalException(String message) {
        super(message);
    }

    /**
     * Constructor con un mensaje y una causa.
     *
     * @param message El mensaje de error.
     * @param cause La causa subyacente de la excepción.
     */
    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor con una causa.
     *
     * @param cause La causa subyacente de la excepción.
     */
    public GlobalException(Throwable cause) {
        super(cause);
    }
}
