package org.example.sistemaacademico.data;


import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.database.Servicio;
import org.example.sistemaacademico.logic.Matricula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class MatriculaService {
    private static final String insertarMatricula = "{call insertarMatricula (?,?)}";
    private static final String modificarMatricula = "{call modificarMatricula (?,?,?,?)}";
    private static final String eliminarMatricula = "{call eliminarMatricula(?)}";
    private static final String listarMatriculas = "{?=call listarMatriculas()}";
    private Servicio servicio;

    public MatriculaService() throws ClassNotFoundException, SQLException {
        this.servicio = Servicio.getInstancia();
    }
    public void insertarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(insertarMatricula);
            pstmt.setLong(1, matricula.getPkAlumno());
            pstmt.setLong(2, matricula.getPkGrupo());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizo la inserci�n");
            }
        } catch (SQLException var15) {
            var15.printStackTrace();
            throw new GlobalException("Llave duplicada");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var12) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

    }
    public void modificarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(modificarMatricula);
            pstmt.setLong(1, matricula.getIdMatricula());
            pstmt.setLong(2, matricula.getPkAlumno());
            pstmt.setLong(3, matricula.getPkGrupo());
            pstmt.setLong(4, matricula.getNota());

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizo la actualizaci�n");
            }

            System.out.println("\nModificaci�n Satisfactoria!");
        } catch (SQLException var15) {
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var12) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
    }
    public void eliminarMatricula(Long matricula) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareStatement(eliminarMatricula);
            pstmt.setLong(1, matricula);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizo el borrado");
            }

            //System.out.println("\nEliminaci�n Satisfactoria!");
        } catch (SQLException var15) {
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var12) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
    }
}
