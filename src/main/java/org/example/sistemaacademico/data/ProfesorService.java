package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesorService {

    private static final String INSERTAR_PROFESOR = "{call insertarProfesor(?,?,?,?)}";
    private static final String MODIFICAR_PROFESOR = "{call modificarProfesor(?,?,?,?,?)}";
    private static final String ELIMINAR_PROFESOR = "{call eliminarProfesor(?)}";
    private static final String ELIMINAR_PROFESOR_POR_CEDULA = "{call eliminarProfesorPorCedula(?)}";
    private static final String LISTAR_PROFESORES = "{?=call listarProfesores()}";
    private static final String BUSCAR_POR_CEDULA = "{?=call buscarProfesorPorCedula(?)}";
    private static final String BUSCAR_POR_NOMBRE = "{?=call buscarProfesorPorNombre(?)}";

    private final DataSource dataSource;

    @Autowired
    public ProfesorService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertar(Profesor profesor) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_PROFESOR)) {
            pstmt.setString(1, profesor.getCedula());
            pstmt.setString(2, profesor.getNombre());
            pstmt.setString(3, profesor.getTelefono());
            pstmt.setString(4, profesor.getEmail());
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó la inserción del profesor");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al insertar profesor");
        }
    }

    public void modificar(Profesor profesor) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_PROFESOR)) {
            pstmt.setLong(1, profesor.getIdProfesor());
            pstmt.setString(2, profesor.getCedula());
            pstmt.setString(3, profesor.getNombre());
            pstmt.setString(4, profesor.getTelefono());
            pstmt.setString(5, profesor.getEmail());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización del profesor");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al modificar profesor");
        }
    }

    public void eliminar(Long idProfesor) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_PROFESOR)) {
            pstmt.setLong(1, idProfesor);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó el borrado: el profesor no existe");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al eliminar profesor");
        }
    }

    public void eliminarPorCedula(String cedula) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_PROFESOR_POR_CEDULA)) {
            pstmt.setString(1, cedula);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó el borrado: el profesor con cédula " + cedula + " no existe");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al eliminar profesor por cédula");
        }
    }

    public List<Profesor> listar() throws GlobalException, NoDataException {
        List<Profesor> profesores = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_PROFESORES)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    profesores.add(mapResultSetToProfesor(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al listar profesores");
        }
        if (profesores.isEmpty()) {
            throw new NoDataException("No hay profesores registrados");
        }
        return profesores;
    }

    public Profesor buscarPorCedula(String cedula) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_CEDULA)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToProfesor(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al buscar profesor por cédula");
        }
        throw new NoDataException("No se encontró un profesor con cédula: " + cedula);
    }

    public Profesor buscarPorNombre(String nombre) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_NOMBRE)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, nombre);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToProfesor(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al buscar profesor por nombre");
        }
        throw new NoDataException("No se encontró un profesor con nombre: " + nombre);
    }

    // ========== Métodos utilitarios ==========

    public void verificarEliminar(Long idProfesor) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM Grupo WHERE pk_profesor = ? " +
                             "UNION ALL " +
                             "SELECT COUNT(*) FROM Usuario WHERE cedula = (SELECT cedula FROM Profesor WHERE id_profesor = ?) AND tipo = 'Profesor'")) {
            pstmt.setLong(1, idProfesor);
            pstmt.setLong(2, idProfesor);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new GlobalException("No se puede eliminar el profesor: tiene grupos asignados.");
                }
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new GlobalException("No se puede eliminar el profesor: existe un usuario asociado.");
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al verificar eliminación de profesor: " + e.getMessage());
        }
    }

    public void verificarEliminarPorCedula(String cedula) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM Grupo g JOIN Profesor p ON g.pk_profesor = p.id_profesor WHERE p.cedula = ? " +
                             "UNION ALL " +
                             "SELECT COUNT(*) FROM Usuario WHERE cedula = ? AND tipo = 'Profesor'")) {
            pstmt.setString(1, cedula);
            pstmt.setString(2, cedula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new GlobalException("No se puede eliminar el profesor: tiene grupos asignados.");
                }
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new GlobalException("No se puede eliminar el profesor: existe un usuario asociado.");
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al verificar eliminación de profesor por cédula: " + e.getMessage());
        }
    }

    private Profesor mapResultSetToProfesor(ResultSet rs) throws SQLException {
        return new Profesor(
                rs.getLong("id_profesor"),
                rs.getString("cedula"),
                rs.getString("nombre"),
                rs.getString("telefono"),
                rs.getString("email")
        );
    }

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        int errorCode = Math.abs(e.getErrorCode());
        String errorMessage = switch (errorCode) {
            case 20010 -> "No se puede eliminar el profesor: existe un usuario asociado.";
            case 20024 -> "El nombre del profesor no puede estar vacío.";
            case 20025 -> "El correo del profesor no tiene un formato válido.";
            case 20030 -> "No se puede eliminar el profesor: tiene grupos asignados.";
            case 20038 -> "La cédula del profesor debe ser de 9 dígitos numéricos.";
            case 20039 -> "El teléfono del profesor debe ser de 8 dígitos numéricos.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
