package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Profesor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesorService {

    private static final Logger logger = LoggerFactory.getLogger(ProfesorService.class);

    // Consultas SQL para procedimientos almacenados
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
        logger.debug("Insertando profesor: cedula {}, nombre {}", profesor.getCedula(), profesor.getNombre());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_PROFESOR)) {
            pstmt.setString(1, profesor.getCedula());
            pstmt.setString(2, profesor.getNombre());
            pstmt.setString(3, profesor.getTelefono());
            pstmt.setString(4, profesor.getEmail());
            pstmt.executeUpdate();
            logger.info("Profesor insertado exitosamente: cedula {}", profesor.getCedula());
        } catch (SQLException e) {
            logger.error("Error al insertar profesor: {}", e.getMessage(), e);
            handleSQLExceptionInsert(e);
        }
    }

    public void modificar(Profesor profesor) throws GlobalException, NoDataException {
        logger.debug("Modificando profesor: id {}, cedula {}, nombre {}",
                profesor.getIdProfesor(), profesor.getCedula(), profesor.getNombre());
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
            logger.info("Profesor modificado exitosamente: id {}", profesor.getIdProfesor());
        } catch (SQLException e) {
            logger.error("Error al modificar profesor: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar profesor");
        }
    }

    public void eliminar(Long idProfesor) throws GlobalException, NoDataException {
        logger.debug("Eliminando profesor: id {}", idProfesor);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_PROFESOR)) {
            pstmt.setLong(1, idProfesor);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: el profesor no existe");
            }
            logger.info("Profesor eliminado exitosamente: id {}", idProfesor);
        } catch (SQLException e) {
            logger.error("Error al eliminar profesor: {}", e.getMessage(), e);
            handleDeleteSQLException(e);
        }
    }

    public void eliminarPorCedula(String cedula) throws GlobalException, NoDataException {
        logger.debug("Eliminando profesor por cédula: {}", cedula);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_PROFESOR_POR_CEDULA)) {
            pstmt.setString(1, cedula);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: el profesor con cédula " + cedula + " no existe");
            }
            logger.info("Profesor eliminado exitosamente por cédula: {}", cedula);
        } catch (SQLException e) {
            logger.error("Error al eliminar profesor por cédula: {}", e.getMessage(), e);
            handleDeleteSQLException(e);
        }
    }

    public List<Profesor> listar() throws GlobalException, NoDataException {
        logger.debug("Listando todos los profesores");
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
            logger.error("Error al listar profesores: {}", e.getMessage(), e);
            handleSQLException(e, "Error al listar profesores");
        }
        if (profesores.isEmpty()) {
            throw new NoDataException("No hay profesores registrados");
        }
        logger.info("Listado de profesores obtenido exitosamente, total: {}", profesores.size());
        return profesores;
    }

    public Profesor buscarPorCedula(String cedula) throws GlobalException, NoDataException {
        logger.debug("Buscando profesor por cédula: {}", cedula);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_CEDULA)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Profesor profesor = mapResultSetToProfesor(rs);
                    logger.info("Profesor encontrado por cédula: {}", cedula);
                    return profesor;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar profesor por cédula: {}", e.getMessage(), e);
            handleSQLException(e, "Error al buscar profesor por cédula");
        }
        throw new NoDataException("No se encontró un profesor con cédula: " + cedula);
    }

    public Profesor buscarPorNombre(String nombre) throws GlobalException, NoDataException {
        logger.debug("Buscando profesor por nombre: {}", nombre);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_NOMBRE)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, nombre);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Profesor profesor = mapResultSetToProfesor(rs);
                    logger.info("Profesor encontrado por nombre: {}", nombre);
                    return profesor;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar profesor por nombre: {}", e.getMessage(), e);
            handleSQLException(e, "Error al buscar profesor por nombre");
        }
        throw new NoDataException("No se encontró un profesor con nombre: " + nombre);
    }

    // Métodos utilitarios privados
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
        throw new GlobalException(message + ": " + e.getMessage());
    }

    private void handleSQLExceptionInsert(SQLException e) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage = errorCode == -20007 ?
                "No se puede insertar el profesor: ya existe un profesor con esta cédula." :
                "Error al insertar profesor: " + e.getMessage();
        throw new GlobalException(errorMessage);
    }

    private void handleDeleteSQLException(SQLException e) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage = errorCode == -20010 ?
                "No se puede eliminar el profesor: tiene grupos asignados." :
                "Error al eliminar profesor: " + e.getMessage();
        throw new GlobalException(errorMessage);
    }
}
