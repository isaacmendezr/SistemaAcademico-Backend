package org.example.sistemaacademico.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Servicio {
    private static Servicio instancia = null;
    public Connection conexion = null;

    public Servicio() {}
    public static Servicio getInstancia() throws ClassNotFoundException, SQLException {
        if (instancia == null) {
            instancia = new Servicio();
        }
        return instancia;
    }


    public void conectar() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "root");
    }

    public void desconectar() throws SQLException {
        if (!this.conexion.isClosed()) {
            this.conexion.close();
        }

    }
}
