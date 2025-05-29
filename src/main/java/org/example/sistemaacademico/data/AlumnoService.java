package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.database.Servicio;
import org.example.sistemaacademico.logic.Alumno;
import org.example.sistemaacademico.logic.dto.MatriculaAlumnoDto;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
public class AlumnoService {
    private static final String insertarAlumno = "{call insertarAlumno (?,?,?,?,?,?)}";
    private static final String modificarAlumno = "{call modificarAlumno (?,?,?,?,?,?,?)}";
    private static final String eliminarAlumno = "{call eliminarAlumno(?)}";
    private static final String listarAlumnos = "{?=call listarAlumnos()}";
    private static final String buscarPorCedula = "{?=call buscarAlumnoPorCedula(?)}";
    private static final String buscarPorNombre = "{?=call buscarAlumnoPorNombre(?)}";
    private static final String buscarAlumnosPorCarrera = "{?=call buscarAlumnosPorCarrera(?)}";

    private Servicio servicio;

    public AlumnoService() throws ClassNotFoundException, SQLException {
        this.servicio = Servicio.getInstancia();
    }
    public void insertarAlumno(Alumno alumno) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException | SQLException var13) {
            throw new GlobalException("No se ha localizado el driver");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(insertarAlumno);
            pstmt.setString(1, alumno.getCedula());
            pstmt.setString(2, alumno.getNombre());
            pstmt.setString(3, alumno.getTelefono());
            pstmt.setString(4, alumno.getEmail());
            pstmt.setDate(5, java.sql.Date.valueOf(alumno.getFechaNacimiento()));
            pstmt.setLong(6, alumno.getPkCarrera());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
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
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }
    }
    public void modificarAlumno(Alumno alumno) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(modificarAlumno);
            pstmt.setLong(1, alumno.getIdAlumno());
            pstmt.setString(2, alumno.getCedula());
            pstmt.setString(3, alumno.getNombre());
            pstmt.setString(4, alumno.getTelefono());
            pstmt.setString(5, alumno.getEmail());
            pstmt.setDate(6,  java.sql.Date.valueOf(alumno.getFechaNacimiento()));
            pstmt.setLong(7, alumno.getPkCarrera());

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
    public void eliminarAlumno(Long alumno) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareStatement(eliminarAlumno);
            pstmt.setLong(1, alumno);
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
    public List<Alumno> listarAlumnos() throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException | SQLException var14) {
            throw new GlobalException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        ArrayList<Alumno> coleccion = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(listarAlumnos);
            pstmt.registerOutParameter(1, -10);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);

            while (rs.next()) {
                coleccion.add(new Alumno(
                        rs.getLong("id_alumno"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getLong("pk_carrera")
                ));
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
    public Alumno buscarAlumnoPorCedula(String cedula) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var15) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var16) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Alumno alumno = null;
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarPorCedula);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, cedula);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            if (rs.next()) {
                alumno = new Alumno(
                        rs.getLong("id_alumno"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getLong("pk_carrera")
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

        return alumno;
    }
    public Alumno buscarAlumnoPorNombre(String nombre) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var15) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var16) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Alumno alumno = null;
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarPorNombre);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, nombre);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            if (rs.next()) {
                alumno = new Alumno(
                        rs.getLong("id_alumno"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getLong("pk_carrera")
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

        return alumno;
    }
    public List<Alumno> buscarAlumnosPorCarrera(Long carrera) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var14) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var15) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        ArrayList<Alumno> coleccion = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarAlumnosPorCarrera);
            pstmt.registerOutParameter(1, -10);
            pstmt.setLong(2, carrera);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);

            while(rs.next()) {
                coleccion.add(new Alumno(
                        rs.getLong("id_alumno"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getLong("pk_carrera")
                ));
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

}
