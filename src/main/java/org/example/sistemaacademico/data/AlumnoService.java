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

/**
 * Servicio para gestionar operaciones relacionadas con Alumnos en la base de datos.
 * Implementa operaciones CRUD y búsquedas, asegurando manejo adecuado de excepciones
 * y cierre de recursos.
 */
@Service
public class AlumnoService {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoService.class);

    // Consultas SQL para procedimientos almacenados
    private static final String INSERTAR_ALUMNO = "{call insertarAlumno(?,?,?,?,?,?)}";
    private static final String MODIFICAR_ALUMNO = "{call modificarAlumno(?,?,?,?,?,?,?)}";
    private static final String ELIMINAR_ALUMNO = "{call eliminarAlumno(?)}";
    private static final String ELIMINAR_ALUMNO_POR_CEDULA = "{call eliminarAlumnoPorCedula(?)}";
    private static final String LISTAR_ALUMNOS = "{?=call listarAlumnos()}";
    private static final String BUSCAR_POR_CEDULA = "{?=call buscarAlumnoPorCedula(?)}";
    private static final String BUSCAR_POR_NOMBRE = "{?=call buscarAlumnoPorNombre(?)}";
    private static final String BUSCAR_ALUMNOS_POR_CARRERA = "{?=call buscarAlumnosPorCarrera(?)}";

    private final DataSource dataSource;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el DataSource.
     *
     * @param dataSource El DataSource gestionado por Spring Boot.
     */
    @Autowired
    public AlumnoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserta un nuevo alumno en la base de datos.
     *
     * @param alumno El objeto Alumno a insertar.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la inserción no se realiza.
     */
    public void insertarAlumno(Alumno alumno) throws GlobalException, NoDataException {
        logger.debug("Insertando alumno: {}", alumno.getCedula());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_ALUMNO)) {
            setAlumnoParameters(pstmt, alumno, false);
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
            logger.info("Alumno insertado exitosamente: {}", alumno.getCedula());
        } catch (SQLException e) {
            logger.error("Error al insertar alumno: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar alumno: llave duplicada o sentencia inválida");
        }
    }

    /**
     * Modifica un alumno existente en la base de datos.
     *
     * @param alumno El objeto Alumno con los datos actualizados.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la actualización no se realiza.
     */
    public void modificarAlumno(Alumno alumno) throws GlobalException, NoDataException {
        logger.debug("Modificando alumno: {}", alumno.getCedula());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_ALUMNO)) {
            pstmt.setLong(1, alumno.getIdAlumno());
            setAlumnoParameters(pstmt, alumno, true);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            logger.info("Alumno modificado exitosamente: {}", alumno.getCedula());
        } catch (SQLException e) {
            logger.error("Error al modificar alumno: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar alumno: sentencia no válida");
        }
    }

    /**
     * Elimina un alumno por su ID.
     *
     * @param idAlumno ID del alumno a eliminar.
     * @throws GlobalException Si hay dependencias o errores en la base de datos.
     * @throws NoDataException Si el alumno no existe o no se elimina.
     */
    public void eliminarAlumno(Long idAlumno) throws GlobalException, NoDataException {
        logger.debug("Eliminando alumno con ID: {}", idAlumno);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_ALUMNO)) {
            pstmt.setLong(1, idAlumno);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: el alumno no existe");
            }
            logger.info("Alumno eliminado exitosamente: {}", idAlumno);
        } catch (SQLException e) {
            logger.error("Error al eliminar alumno: {}", e.getMessage(), e);
            handleDeleteSQLException(e, "Error al eliminar alumno");
        }
    }

    /**
     * Elimina un alumno por su cédula.
     *
     * @param cedula Cédula del alumno a eliminar.
     * @throws GlobalException Si hay dependencias o errores en la base de datos.
     */
    public void eliminarAlumnoPorCedula(String cedula) throws GlobalException {
        logger.debug("Eliminando alumno con cédula: {}", cedula);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_ALUMNO_POR_CEDULA)) {
            pstmt.setString(1, cedula);
            pstmt.executeUpdate();
            logger.info("Alumno eliminado exitosamente por cédula: {}", cedula);
        } catch (SQLException e) {
            logger.error("Error al eliminar alumno por cédula: {}", e.getMessage(), e);
            handleDeleteSQLException(e, "Error al eliminar alumno por cédula");
        }
    }

    /**
     * Lista todos los alumnos registrados.
     *
     * @return Lista de objetos Alumno.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay datos disponibles.
     */
    public List<Alumno> listarAlumnos() throws GlobalException, NoDataException {
        logger.debug("Listando todos los alumnos");
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
        logger.info("Listado de alumnos obtenido exitosamente, total: {}", alumnos.size());
        return alumnos;
    }

    /**
     * Busca un alumno por su cédula.
     *
     * @param cedula Cédula del alumno a buscar.
     * @return El objeto Alumno encontrado, o null si no existe.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     */
    public Alumno buscarAlumnoPorCedula(String cedula) throws GlobalException {
        logger.debug("Buscando alumno por cédula: {}", cedula);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_CEDULA)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Alumno alumno = mapResultSetToAlumno(rs);
                    logger.info("Alumno encontrado por cédula: {}", cedula);
                    return alumno;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar alumno por cédula: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar alumno por cédula: " + e.getMessage());
        }
        logger.info("No se encontró alumno con cédula: {}", cedula);
        return null;
    }

    /**
     * Busca un alumno por su nombre.
     *
     * @param nombre Nombre del alumno a buscar.
     * @return El objeto Alumno encontrado, o null si no existe.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     */
    public Alumno buscarAlumnoPorNombre(String nombre) throws GlobalException {
        logger.debug("Buscando alumno por nombre: {}", nombre);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_NOMBRE)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, nombre);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Alumno alumno = mapResultSetToAlumno(rs);
                    logger.info("Alumno encontrado por nombre: {}", nombre);
                    return alumno;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar alumno por nombre: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar alumno por nombre: " + e.getMessage());
        }
        logger.info("No se encontró alumno con nombre: {}", nombre);
        return null;
    }

    /**
     * Busca alumnos por el ID de su carrera.
     *
     * @param carrera ID de la carrera.
     * @return Lista de alumnos asociados a la carrera.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay datos disponibles.
     */
    public List<Alumno> buscarAlumnosPorCarrera(Long carrera) throws GlobalException, NoDataException {
        logger.debug("Buscando alumnos por carrera: {}", carrera);
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
        logger.info("Alumnos encontrados para carrera {}: {}", carrera, alumnos.size());
        return alumnos;
    }

    // Métodos utilitarios privados

    /**
     * Establece los parámetros del CallableStatement para operaciones de inserción o modificación.
     *
     * @param pstmt  El CallableStatement a configurar.
     * @param alumno El objeto Alumno con los datos.
     * @param isUpdate Indica si es una operación de actualización (true) o inserción (false).
     * @throws SQLException Si ocurre un error al establecer los parámetros.
     */
    private void setAlumnoParameters(CallableStatement pstmt, Alumno alumno, boolean isUpdate) throws SQLException {
        int startIndex = isUpdate ? 2 : 1;
        pstmt.setString(startIndex, alumno.getCedula());
        pstmt.setString(startIndex + 1, alumno.getNombre());
        pstmt.setString(startIndex + 2, alumno.getTelefono());
        pstmt.setString(startIndex + 3, alumno.getEmail());
        pstmt.setDate(startIndex + 4, Date.valueOf(alumno.getFechaNacimiento()));
        pstmt.setLong(startIndex + 5, alumno.getPkCarrera());
    }

    /**
     * Mapea un ResultSet a un objeto Alumno.
     *
     * @param rs El ResultSet con los datos del alumno.
     * @return Un objeto Alumno mapeado.
     * @throws SQLException Si ocurre un error al leer los datos.
     */
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
     * @param e       La excepción SQL capturada.
     * @param message El mensaje base para la excepción.
     * @throws GlobalException Con el mensaje detallado del error, incluyendo mensajes de triggers.
     */
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
