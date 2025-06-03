package org.example.sistemaacademico.database;

import java.io.Serial;
import java.io.Serializable;

public class NoDataException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public NoDataException() {
        super("No se encontraron datos.");
    }

    public NoDataException(String message) {
        super(message);
    }

    public NoDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDataException(Throwable cause) {
        super(cause);
    }
}
