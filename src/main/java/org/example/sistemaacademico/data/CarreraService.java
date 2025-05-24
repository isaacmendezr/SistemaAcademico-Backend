package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;

import org.example.sistemaacademico.database.Servicio;
import org.example.sistemaacademico.logic.Carrera;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
public class CarreraService {
    private static final String insertarCarrera = "{call insertarCarrera (?,?,?)}";
    private static final String modificarCarrera = "{call modificarCarrera (?,?,?,?)}";
    private static final String eliminarCarrera = "{call eliminarCarrera(?)}";
    private static final String listarCarreras = "{?=call listarCarreras()}";
    private static final String buscarCarreraPorCodigo = "{?=call buscarCarreraPorCodigo(?)}";
    private static final String buscarCarreraPorNombre = "{?=call buscarCarreraPorNombre(?)}";

    //CARRERA_CURSO
    private static final String insertarCursoACarrera = "{call insertarCursoACarrera(?,?,?)}";
    private static final String eliminarCursoDeCarrera = "{call eliminarCursoDeCarrera(?,?)}";
    private static final String modificarOrdenCursoCarrera = "{call modificarOrdenCursoCarrera(?,?,?)}";
    private Servicio servicio;

    public CarreraService() throws ClassNotFoundException, SQLException {
        this.servicio = Servicio.getInstancia();
    }
    public void insertarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(insertarCarrera);
            pstmt.setString(1, carrera.getCodigo());
            pstmt.setString(2, carrera.getNombre());
            pstmt.setString(3, carrera.getTitulo());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizo la insercion");
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
    public void modificarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(modificarCarrera);
            pstmt.setLong(1, carrera.getIdCarrera());
            pstmt.setString(2, carrera.getCodigo());
            pstmt.setString(3, carrera.getNombre());
            pstmt.setString(4, carrera.getTitulo());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizo la actualizacion");
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
    public void eliminarCarrera(Long carrera) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareStatement(eliminarCarrera);
            pstmt.setLong(1, carrera);
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
    public List<Carrera> listarCarreras() throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var14) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var15) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        ArrayList<Carrera> coleccion = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(listarCarreras);
            pstmt.registerOutParameter(1, -10);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);

            while(rs.next()) {
                coleccion.add(new Carrera(
                        rs.getLong("id_carrera"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("titulo"))
                );
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
    public Carrera buscarCarreraPorNombre(String nombre) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var15) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var16) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Carrera carrera = null;
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarCarreraPorNombre);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, nombre);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            if (rs.next()) {
                carrera = new Carrera(
                        rs.getLong("id_carrera"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("titulo")
                );
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

        return carrera;
    }
    public Carrera buscarCarreraPorCodigo(String codigo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var15) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var16) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Carrera carrera = null;
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarCarreraPorCodigo);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, codigo);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            if (rs.next()) {
                carrera = new Carrera(
                        rs.getLong("id_carrera"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("titulo"));
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

        return carrera;
    }

    //CARRERA_CURSO
    public void insertarCursoACarrera(Long carreraId, Long cursoId, Long cicloId) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.servicio.conexion.prepareCall(insertarCursoACarrera);
            pstmt.setLong(1, carreraId);
            pstmt.setLong(2, cursoId);
            pstmt.setLong(3, cicloId);

            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al insertar curso en carrera");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.servicio.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }
    }
    public void eliminarCursoDeCarrera(Long carreraId, Long cursoId) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.servicio.conexion.prepareCall(eliminarCursoDeCarrera);
            pstmt.setLong(1, carreraId);
            pstmt.setLong(2, cursoId);

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar curso de carrera");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.servicio.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }
    }
    public void modificarOrdenCursoCarrera(Long carreraId, Long cursoId, Long nuevoCicloId) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.servicio.conexion.prepareCall(modificarOrdenCursoCarrera);
            pstmt.setLong(1, carreraId);
            pstmt.setLong(2, cursoId);
            pstmt.setLong(3, nuevoCicloId);

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al modificar ciclo del curso en la carrera");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.servicio.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }
    }
}
