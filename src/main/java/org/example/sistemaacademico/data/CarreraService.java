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

/**
 * Servicio para gestionar operaciones relacionadas con Carreras en la base de datos.
 * Implementa operaciones CRUD y búsquedas, asegurando manejo adecuado de excepciones
 * y cierre de recursos. Sigue el patrón MVVM con ViewModel implícito en la lógica.
 */
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

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el DataSource.
     *
     * @param dataSource El DataSource gestionado por Spring Boot.
     */
    @Autowired
    public CarreraService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserta una nueva carrera en la base de datos.
     *
     * @param carrera El objeto Carrera a insertar.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la inserción no se realiza.
     */
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

    /**
     * Modifica una carrera existente en la base de datos.
     *
     * @param carrera El objeto Carrera con los datos actualizados.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la actualización no se realiza.
     */
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

    /**
     * Elimina una carrera por su ID.
     *
     * @param idCarrera ID de la carrera a eliminar.
     * @throws GlobalException Si hay dependencias o errores en la base de datos.
     * @throws NoDataException Si la carrera no existe o no se elimina.
     */
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

    /**
     * Lista todas las carreras registradas.
     *
     * @return Lista de objetos Carrera.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay datos disponibles.
     */
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

    /**
     * Busca una carrera por su código.
     *
     * @param codigo Código de la carrera a buscar.
     * @return El objeto Carrera encontrado, o null si no existe.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     */
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

    /**
     * Busca una carrera por su nombre.
     *
     * @param nombre Nombre de la carrera a buscar.
     * @return El objeto Carrera encontrado, o null si no existe.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     */
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

    /**
     * Inserta un curso en una carrera.
     *
     * @param carreraId ID de la carrera.
     * @param cursoId ID del curso.
     * @param cicloId ID del ciclo.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la inserción no se realiza.
     */
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

    /**
     * Elimina un curso de una carrera.
     *
     * @param carreraId ID de la carrera.
     * @param cursoId ID del curso.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si el borrado no se realiza.
     */
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

    /**
     * Modifica el ciclo de un curso en una carrera.
     *
     * @param carreraId ID de la carrera.
     * @param cursoId ID del curso.
     * @param nuevoCicloId Nuevo ID del ciclo.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la actualización no se realiza.
     */
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

    /**
     * Mapea un ResultSet a un objeto Carrera.
     *
     * @param rs El ResultSet con los datos de la carrera.
     * @return Un objeto Carrera mapeado.
     * @throws SQLException Si ocurre un error al leer los datos.
     */
    private Carrera mapResultSetToCarrera(ResultSet rs) throws SQLException {
        return new Carrera(
                rs.getLong("id_carrera"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("titulo")
        );
    }

    /**
     * Maneja excepciones SQL genéricas y lanza GlobalException con un mensaje específico.
     *
     * @param e La excepción SQL capturada.
     * @param message El mensaje base para la excepción.
     * @throws GlobalException Con el mensaje detallado del error.
     */
    private void handleSQLException(SQLException e, String message) throws GlobalException {
        throw new GlobalException(message + ": " + e.getMessage());
    }

    /**
     * Maneja excepciones SQL específicas para operaciones de eliminación, mapeando códigos de error de triggers.
     *
     * @param e La excepción SQL capturada.
     * @param message El mensaje base para la excepción.
     * @throws GlobalException Con el mensaje detallado del error, incluyendo mensajes de triggers.
     */
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
