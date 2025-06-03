package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Carrera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarreraService {

    private static final Logger logger = LoggerFactory.getLogger(CarreraService.class);

    // Consultas SQL para procedimientos almacenados
    private static final String INSERTAR_CARRERA = "{call insertarCarrera(?,?,?)}";
    private static final String MODIFICAR_CARRERA = "{call modificarCarrera(?,?,?,?)}";
    private static final String ELIMINAR_CARRERA = "{call eliminarCarrera(?)}";
    private static final String LISTAR_CARRERAS = "{?=call listarCarreras()}";
    private static final String BUSCAR_CARRERA_POR_CODIGO = "{?=call buscarCarreraPorCodigo(?)}";
    private static final String BUSCAR_CARRERA_POR_NOMBRE = "{?=call buscarCarreraPorNombre(?)}";

    // Consultas SQL para procedimientos de Carrera_Curso
    private static final String INSERTAR_CURSO_A_CARRERA = "{call insertarCursoACarrera(?,?,?)}";
    private static final String ELIMINAR_CURSO_DE_CARRERA = "{call eliminarCursoDeCarrera(?,?)}";
    private static final String MODIFICAR_ORDEN_CURSO_CARRERA = "{call modificarOrdenCursoCarrera(?,?,?)}";

    private final DataSource dataSource;

    @Autowired
    public CarreraService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        logger.debug("Insertando carrera: {}", carrera.getCodigo());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CARRERA)) {
            pstmt.setString(1, carrera.getCodigo());
            pstmt.setString(2, carrera.getNombre());
            pstmt.setString(3, carrera.getTitulo());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
            logger.info("Carrera insertada exitosamente: {}", carrera.getCodigo());
        } catch (SQLException e) {
            logger.error("Error al insertar carrera: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar carrera: llave duplicada o sentencia inválida");
        }
    }

    public void modificarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        logger.debug("Modificando carrera: {}", carrera.getCodigo());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_CARRERA)) {
            pstmt.setLong(1, carrera.getIdCarrera());
            pstmt.setString(2, carrera.getCodigo());
            pstmt.setString(3, carrera.getNombre());
            pstmt.setString(4, carrera.getTitulo());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            logger.info("Carrera modificada exitosamente: {}", carrera.getCodigo());
        } catch (SQLException e) {
            logger.error("Error al modificar carrera: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar carrera: sentencia no válida");
        }
    }

    public void eliminarCarrera(Long idCarrera) throws GlobalException, NoDataException {
        logger.debug("Eliminando carrera con ID: {}", idCarrera);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CARRERA)) {
            pstmt.setLong(1, idCarrera);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: la carrera no existe");
            }
            logger.info("Carrera eliminada exitosamente: {}", idCarrera);
        } catch (SQLException e) {
            logger.error("Error al eliminar carrera: {}", e.getMessage(), e);
            handleDeleteSQLException(e, "Error al eliminar carrera");
        }
    }

    public List<Carrera> listarCarreras() throws GlobalException, NoDataException {
        logger.debug("Listando todas las carreras");
        List<Carrera> carreras = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_CARRERAS)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    carreras.add(mapResultSetToCarrera(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al listar carreras: {}", e.getMessage(), e);
            throw new GlobalException("Error al listar carreras: " + e.getMessage());
        }
        if (carreras.isEmpty()) {
            throw new NoDataException("No hay carreras registradas");
        }
        logger.info("Listado de carreras obtenido exitosamente, total: {}", carreras.size());
        return carreras;
    }

    public Carrera buscarCarreraPorCodigo(String codigo) throws GlobalException {
        logger.debug("Buscando carrera por código: {}", codigo);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CARRERA_POR_CODIGO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, codigo);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Carrera carrera = mapResultSetToCarrera(rs);
                    logger.info("Carrera encontrada por código: {}", codigo);
                    return carrera;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar carrera por código: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar carrera por código: " + e.getMessage());
        }
        logger.info("No se encontró carrera con código: {}", codigo);
        return null;
    }

    public Carrera buscarCarreraPorNombre(String nombre) throws GlobalException {
        logger.debug("Buscando carrera por nombre: {}", nombre);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CARRERA_POR_NOMBRE)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, nombre);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Carrera carrera = mapResultSetToCarrera(rs);
                    logger.info("Carrera encontrada por nombre: {}", nombre);
                    return carrera;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar carrera por nombre: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar carrera por nombre: " + e.getMessage());
        }
        logger.info("No se encontró carrera con nombre: {}", nombre);
        return null;
    }

    public void insertarCursoACarrera(Long carreraId, Long cursoId, Long cicloId) throws GlobalException, NoDataException {
        logger.debug("Insertando curso en carrera: carreraId={}, cursoId={}, cicloId={}", carreraId, cursoId, cicloId);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CURSO_A_CARRERA)) {
            pstmt.setLong(1, carreraId);
            pstmt.setLong(2, cursoId);
            pstmt.setLong(3, cicloId);
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
            logger.info("Curso insertado en carrera exitosamente: carreraId={}, cursoId={}", carreraId, cursoId);
        } catch (SQLException e) {
            logger.error("Error al insertar curso en carrera: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar curso en carrera: datos inválidos o duplicados");
        }
    }

    public void eliminarCursoDeCarrera(Long carreraId, Long cursoId) throws GlobalException, NoDataException {
        logger.debug("Eliminando curso de carrera: carreraId={}, cursoId={}", carreraId, cursoId);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CURSO_DE_CARRERA)) {
            pstmt.setLong(1, carreraId);
            pstmt.setLong(2, cursoId);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: relación no encontrada");
            }
            logger.info("Curso eliminado de carrera exitosamente: carreraId={}, cursoId={}", carreraId, cursoId);
        } catch (SQLException e) {
            logger.error("Error al eliminar curso de carrera: {}", e.getMessage(), e);
            handleDeleteSQLException(e, "Error al eliminar curso de carrera");
        }
    }

    public void modificarOrdenCursoCarrera(Long carreraId, Long cursoId, Long nuevoCicloId) throws GlobalException, NoDataException {
        logger.debug("Modificando ciclo de curso en carrera: carreraId={}, cursoId={}, nuevoCicloId={}", carreraId, cursoId, nuevoCicloId);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_ORDEN_CURSO_CARRERA)) {
            pstmt.setLong(1, carreraId);
            pstmt.setLong(2, cursoId);
            pstmt.setLong(3, nuevoCicloId);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            logger.info("Ciclo de curso modificado exitosamente: carreraId={}, cursoId={}", carreraId, cursoId);
        } catch (SQLException e) {
            logger.error("Error al modificar ciclo de curso en carrera: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar ciclo de curso en carrera: datos inválidos");
        }
    }

    // Métodos utilitarios privados
    private Carrera mapResultSetToCarrera(ResultSet rs) throws SQLException {
        return new Carrera(
                rs.getLong("id_carrera"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("titulo")
        );
    }

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        throw new GlobalException(message + ": " + e.getMessage());
    }

    private void handleDeleteSQLException(SQLException e, String message) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage = switch (errorCode) {
            case -20001 -> "No se puede eliminar la carrera: tiene cursos asociados.";
            case -20002 -> "No se puede eliminar la carrera: tiene alumnos inscritos.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
