package org.example.sistemaacademico.database;

import java.io.Serial;
import java.io.Serializable;

/**
 * Excepción personalizada para indicar que no se encontraron datos en una operación.
 * Es una excepción no verificada (unchecked) que permite un manejo más flexible en aplicaciones Spring.
 */
public class NoDataException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor por defecto con un mensaje genérico.
     */
    public NoDataException() {
        super("No se encontraron datos.");
    }

    /**
     * Constructor con un mensaje personalizado.
     *
     * @param message El mensaje de error.
     */
    public NoDataException(String message) {
        super(message);
    }

    /**
     * Constructor con un mensaje y una causa.
     *
     * @param message El mensaje de error.
     * @param cause La causa subyacente de la excepción.
     */
    public NoDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor con una causa.
     *
     * @param cause La causa subyacente de la excepción.
     */
    public NoDataException(Throwable cause) {
        super(cause);
    }
}
