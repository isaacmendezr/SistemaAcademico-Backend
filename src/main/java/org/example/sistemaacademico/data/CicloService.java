package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Ciclo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con Ciclos en la base de datos.
 * Implementa operaciones CRUD, activación y búsquedas, asegurando manejo adecuado de
 * excepciones y cierre de recursos.
 */
@Service
public class CicloService {

    private static final Logger logger = LoggerFactory.getLogger(CicloService.class);

    // Consultas SQL para procedimientos almacenados
    private static final String INSERTAR_CICLO = "{call insertarCiclo(?,?,?,?,?)}";
    private static final String MODIFICAR_CICLO = "{call modificarCiclo(?,?,?,?,?,?)}";
    private static final String ELIMINAR_CICLO = "{call eliminarCiclo(?)}";
    private static final String LISTAR_CICLOS = "{?=call listarCiclos()}";
    private static final String BUSCAR_POR_ANNIO = "{?=call buscarCicloPorAnnio(?)}";
    private static final String ACTIVAR_CICLO = "{call activarCiclo(?)}";
    private static final String BUSCAR_POR_ID = "{?=call buscarCicloPorId(?)}";

    private final DataSource dataSource;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el DataSource.
     *
     * @param dataSource El DataSource gestionado por Spring Boot.
     */
    @Autowired
    public CicloService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserta un nuevo ciclo en la base de datos.
     *
     * @param ciclo El objeto Ciclo a insertar.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la inserción no se realiza.
     */
    public void insertarCiclo(Ciclo ciclo) throws GlobalException, NoDataException {
        logger.debug("Insertando ciclo: año={}, número={}", ciclo.getAnio(), ciclo.getNumero());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CICLO)) {
            pstmt.setLong(1, ciclo.getAnio());
            pstmt.setLong(2, ciclo.getNumero());
            pstmt.setDate(3, Date.valueOf(ciclo.getFechaInicio()));
            pstmt.setDate(4, Date.valueOf(ciclo.getFechaFin()));
            pstmt.setString(5, ciclo.getEstado());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
            logger.info("Ciclo insertado exitosamente: año={}, número={}", ciclo.getAnio(), ciclo.getNumero());
        } catch (SQLException e) {
            logger.error("Error al insertar ciclo: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar ciclo: llave duplicada o sentencia inválida");
        }
    }

    /**
     * Modifica un ciclo existente en la base de datos.
     *
     * @param ciclo El objeto Ciclo con los datos actualizados.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la actualización no se realiza.
     */
    public void modificarCiclo(Ciclo ciclo) throws GlobalException, NoDataException {
        logger.debug("Modificando ciclo: id={}", ciclo.getIdCiclo());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_CICLO)) {
            pstmt.setLong(1, ciclo.getIdCiclo());
            pstmt.setLong(2, ciclo.getAnio());
            pstmt.setLong(3, ciclo.getNumero());
            pstmt.setDate(4, Date.valueOf(ciclo.getFechaInicio()));
            pstmt.setDate(5, Date.valueOf(ciclo.getFechaFin()));
            pstmt.setString(6, ciclo.getEstado());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            logger.info("Ciclo modificado exitosamente: id={}", ciclo.getIdCiclo());
        } catch (SQLException e) {
            logger.error("Error al modificar ciclo: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar ciclo: sentencia inválida");
        }
    }

    /**
     * Elimina un ciclo por su ID.
     *
     * @param idCiclo ID del ciclo a eliminar.
     * @throws GlobalException Si hay dependencias o errores en la base de datos.
     * @throws NoDataException Si el ciclo no existe o no se elimina.
     */
    public void eliminarCiclo(Long idCiclo) throws GlobalException, NoDataException {
        logger.debug("Eliminando ciclo con ID: {}", idCiclo);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CICLO)) {
            pstmt.setLong(1, idCiclo);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: el ciclo no existe");
            }
            logger.info("Ciclo eliminado exitosamente: {}", idCiclo);
        } catch (SQLException e) {
            logger.error("Error al eliminar ciclo: {}", e.getMessage(), e);
            handleDeleteSQLException(e); // Eliminado el parámetro message
        }
    }

    /**
     * Lista todos los ciclos registrados.
     *
     * @return Lista de objetos Ciclo.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay datos disponibles.
     */
    public List<Ciclo> listarCiclos() throws GlobalException, NoDataException {
        logger.debug("Listando todos los ciclos");
        List<Ciclo> ciclos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_CICLOS)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    ciclos.add(mapResultSetToCiclo(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al listar ciclos: {}", e.getMessage(), e);
            throw new GlobalException("Error al listar ciclos: " + e.getMessage());
        }
        if (ciclos.isEmpty()) {
            throw new NoDataException("No hay ciclos registrados");
        }
        logger.info("Listado de ciclos obtenido exitosamente, total: {}", ciclos.size());
        return ciclos;
    }

    /**
     * Busca un ciclo por su año.
     *
     * @param anio Año del ciclo a buscar.
     * @return El objeto Ciclo encontrado, o null si no existe.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     */
    public Ciclo buscarCicloPorAnio(Long anio) throws GlobalException {
        logger.debug("Buscando ciclo por año: {}", anio);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_ANNIO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, anio);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Ciclo ciclo = mapResultSetToCiclo(rs);
                    logger.info("Ciclo encontrado por año: {}", anio);
                    return ciclo;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar ciclo por año: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar ciclo por año: " + e.getMessage());
        }
        logger.info("No se encontró ciclo con año: {}", anio);
        return null;
    }

    /**
     * Busca un ciclo por su ID.
     *
     * @param id ID del ciclo a buscar.
     * @return El objeto Ciclo encontrado, o null si no existe.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     */
    public Ciclo buscarCicloPorId(Long id) throws GlobalException {
        logger.debug("Buscando ciclo por ID: {}", id);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_ID)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, id);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Ciclo ciclo = mapResultSetToCiclo(rs);
                    logger.info("Ciclo encontrado por ID: {}", id);
                    return ciclo;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar ciclo por ID: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar ciclo por ID: " + e.getMessage());
        }
        logger.info("No se encontró ciclo con ID: {}", id);
        return null;
    }

    /**
     * Activa un ciclo por su ID.
     *
     * @param idCiclo ID del ciclo a activar.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la activación no se realiza.
     */
    public void activarCiclo(Long idCiclo) throws GlobalException, NoDataException {
        logger.debug("Activando ciclo con ID: {}", idCiclo);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ACTIVAR_CICLO)) {
            pstmt.setLong(1, idCiclo);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la activación: el ciclo no existe");
            }
            logger.info("Ciclo activado exitosamente: {}", idCiclo);
        } catch (SQLException e) {
            logger.error("Error al activar ciclo: {}", e.getMessage(), e);
            handleSQLException(e, "Error al activar ciclo: sentencia inválida");
        }
    }

    // Métodos utilitarios privados

    /**
     * Mapea un ResultSet a un objeto Ciclo.
     *
     * @param rs El ResultSet con los datos del ciclo.
     * @return Un objeto Ciclo mapeado.
     * @throws SQLException Si ocurre un error al leer los datos.
     */
    private Ciclo mapResultSetToCiclo(ResultSet rs) throws SQLException {
        return new Ciclo(
                rs.getLong("id_ciclo"),
                rs.getLong("anio"),
                rs.getLong("numero"),
                rs.getDate("fecha_inicio").toLocalDate(),
                rs.getDate("fecha_fin").toLocalDate(),
                rs.getString("estado")
        );
    }

    /**
     * Maneja excepciones SQL genéricas y lanza GlobalException con un mensaje específico.
     *
     * @param e       La excepción SQL capturada.
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
     * @throws GlobalException Con el mensaje detallado del error, incluyendo mensajes de triggers.
     */
    private void handleDeleteSQLException(SQLException e) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage = switch (errorCode) {
            case -20005 -> "No se puede eliminar el ciclo: tiene cursos asociados.";
            case -20006 -> "No se puede eliminar el ciclo: tiene matrículas asociadas.";
            default -> "Error al eliminar ciclo: " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
