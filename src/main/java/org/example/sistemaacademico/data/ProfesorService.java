package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.database.Servicio;
import org.example.sistemaacademico.logic.Profesor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ProfesorService {
    private static final String insertarProfesor = "{call insertarProfesor (?,?,?,?)}";
    private static final String modificarProfesor = "{call modificarProfesor (?,?,?,?,?)}";
    private static final String eliminarProfesor = "{call eliminarProfesor(?)}";
    private static final String listarProfesores= "{?=call listarProfesores()}";
    private static final String buscarPorCedula = "{?=call buscarProfesorPorCedula(?)}";
    private static final String buscarPorNombre = "{?=call buscarProfesorPorNombre(?)}";
    private Servicio servicio;

    public ProfesorService() throws ClassNotFoundException, SQLException {
        this.servicio = Servicio.getInstancia();
    }
    public void insertarProfesor(Profesor profesor) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(insertarProfesor);
            pstmt.setString(1, profesor.getCedula());
            pstmt.setString(2, profesor.getNombre());
            pstmt.setString(3, profesor.getTelefono());
            pstmt.setString(4, profesor.getEmail());
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
    public void modificarProfesor(Profesor profesor) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(modificarProfesor);
            pstmt.setLong(1, profesor.getIdProfesor());
            pstmt.setString(2, profesor.getCedula());
            pstmt.setString(3, profesor.getNombre());
            pstmt.setString(4, profesor.getTelefono());
            pstmt.setString(5, profesor.getEmail());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizo la actualizaci�n");
            }

            //System.out.println("\nModificaci�n Satisfactoria!");
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
    public void eliminarProfesor(Long profesor) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareStatement(eliminarProfesor);
            pstmt.setLong(1, profesor);
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
    public List<Profesor> listarProfesores() throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var14) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var15) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        ArrayList<Profesor> coleccion = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(listarProfesores);
            pstmt.registerOutParameter(1, -10);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);

            while(rs.next()) {
                coleccion.add(new Profesor(
                        rs.getLong("id_profesor"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email")));
            }
        } catch (SQLException var16) {
            var16.printStackTrace();
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var13) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

        if (coleccion != null && coleccion.size() != 0) {
            return coleccion;
        } else {
            throw new NoDataException("No hay datos");
        }
    }
    public Profesor buscarProfesorPorCedula(String cedula) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var15) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var16) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Profesor profesor = null;
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarPorCedula);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, cedula);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            if (rs.next()) {
                profesor = new Profesor(
                        rs.getLong("id_profesor"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"));
            }
        } catch (SQLException var17) {
            var17.printStackTrace();
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var14) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

        return profesor;
    }
    public Profesor buscarProfesorPorNombre(String nombre) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var15) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var16) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Profesor profesor = null;
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarPorNombre);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, nombre);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            if (rs.next()) {
                profesor = new Profesor(
                        rs.getLong("id_profesor"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"));
            }
        } catch (SQLException var17) {
            var17.printStackTrace();
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var14) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

        return profesor;
    }
}
