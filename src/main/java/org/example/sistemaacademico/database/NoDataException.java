package org.example.sistemaacademico.database;

public class NoDataException extends Exception {
    public NoDataException() {
    }

    public NoDataException(String msg) {
        super(msg);
    }
}
