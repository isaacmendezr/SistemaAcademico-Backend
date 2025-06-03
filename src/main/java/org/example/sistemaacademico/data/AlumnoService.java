package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Alumno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlumnoService {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoService.class);

    private static final String INSERTAR_ALUMNO = "{call insertarAlumno(?,?,?,?,?,?)}";
    private static final String MODIFICAR_ALUMNO = "{call modificarAlumno(?,?,?,?,?,?,?)}";
    private static final String ELIMINAR_ALUMNO = "{call eliminarAlumno(?)}";
    private static final String ELIMINAR_ALUMNO_POR_CEDULA = "{call eliminarAlumnoPorCedula(?)}";
    private static final String LISTAR_ALUMNOS = "{?=call listarAlumnos()}";
    private static final String BUSCAR_POR_CEDULA = "{?=call buscarAlumnoPorCedula(?)}";
    private static final String BUSCAR_POR_NOMBRE = "{?=call buscarAlumnoPorNombre(?)}";
    private static final String BUSCAR_ALUMNOS_POR_CARRERA = "{?=call buscarAlumnosPorCarrera(?)}";

    private final DataSource dataSource;

    @Autowired
    public AlumnoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertarAlumno(Alumno alumno) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_ALUMNO)) {
            setAlumnoParameters(pstmt, alumno, false);
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
        } catch (SQLException e) {
            logger.error("Error al insertar alumno: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar alumno: llave duplicada o sentencia inválida");
        }
    }

    public void modificarAlumno(Alumno alumno) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_ALUMNO)) {
            pstmt.setLong(1, alumno.getIdAlumno());
            setAlumnoParameters(pstmt, alumno, true);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
        } catch (SQLException e) {
            logger.error("Error al modificar alumno: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar alumno: sentencia no válida");
        }
    }

    public void eliminarAlumno(Long idAlumno) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_ALUMNO)) {
            pstmt.setLong(1, idAlumno);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: el alumno no existe");
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar alumno: {}", e.getMessage(), e);
            handleDeleteSQLException(e, "Error al eliminar alumno");
        }
    }

    public void eliminarAlumnoPorCedula(String cedula) throws GlobalException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_ALUMNO_POR_CEDULA)) {
            pstmt.setString(1, cedula);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al eliminar alumno por cédula: {}", e.getMessage(), e);
            handleDeleteSQLException(e, "Error al eliminar alumno por cédula");
        }
    }

    public List<Alumno> listarAlumnos() throws GlobalException, NoDataException {
        List<Alumno> alumnos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_ALUMNOS)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    alumnos.add(mapResultSetToAlumno(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al listar alumnos: {}", e.getMessage(), e);
            throw new GlobalException("Error al listar alumnos: " + e.getMessage());
        }
        if (alumnos.isEmpty()) {
            throw new NoDataException("No hay alumnos registrados");
        }
        return alumnos;
    }

    public Alumno buscarAlumnoPorCedula(String cedula) throws GlobalException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_CEDULA)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToAlumno(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar alumno por cédula: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar alumno por cédula: " + e.getMessage());
        }
        return null;
    }

    public Alumno buscarAlumnoPorNombre(String nombre) throws GlobalException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_NOMBRE)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, nombre);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToAlumno(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar alumno por nombre: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar alumno por nombre: " + e.getMessage());
        }
        return null;
    }

    public List<Alumno> buscarAlumnosPorCarrera(Long carrera) throws GlobalException, NoDataException {
        List<Alumno> alumnos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_ALUMNOS_POR_CARRERA)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, carrera);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    alumnos.add(mapResultSetToAlumno(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar alumnos por carrera: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar alumnos por carrera: " + e.getMessage());
        }
        if (alumnos.isEmpty()) {
            throw new NoDataException("No hay alumnos asociados a esta carrera");
        }
        return alumnos;
    }

    // Métodos utilitarios
    private void setAlumnoParameters(CallableStatement pstmt, Alumno alumno, boolean isUpdate) throws SQLException {
        int startIndex = isUpdate ? 2 : 1;
        pstmt.setString(startIndex, alumno.getCedula());
        pstmt.setString(startIndex + 1, alumno.getNombre());
        pstmt.setString(startIndex + 2, alumno.getTelefono());
        pstmt.setString(startIndex + 3, alumno.getEmail());
        pstmt.setDate(startIndex + 4, Date.valueOf(alumno.getFechaNacimiento()));
        pstmt.setLong(startIndex + 5, alumno.getPkCarrera());
    }

    private Alumno mapResultSetToAlumno(ResultSet rs) throws SQLException {
        return new Alumno(
                rs.getLong("id_alumno"),
                rs.getString("cedula"),
                rs.getString("nombre"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getDate("fecha_nacimiento").toLocalDate(),
                rs.getLong("pk_carrera")
        );
    }

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        throw new GlobalException(message + ": " + e.getMessage());
    }

    private void handleDeleteSQLException(SQLException e, String message) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage = switch (errorCode) {
            case -20009 -> "No se puede eliminar el alumno: existe un usuario asociado.";
            case -20011 -> "No se puede eliminar el alumno: tiene matrículas asociadas.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
