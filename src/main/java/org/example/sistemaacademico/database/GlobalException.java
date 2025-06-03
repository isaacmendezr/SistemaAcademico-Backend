package org.example.sistemaacademico.database;

import java.io.Serial;
import java.io.Serializable;

public class GlobalException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public GlobalException() {
        super("Error en el sistema.");
    }

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalException(Throwable cause) {
        super(cause);
    }
}
