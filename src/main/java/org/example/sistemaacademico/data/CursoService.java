package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Curso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con Cursos en la base de datos.
 * Implementa operaciones CRUD y búsquedas, asegurando manejo adecuado de excepciones
 * y cierre de recursos.
 */
@Service
public class CursoService {

    private static final Logger logger = LoggerFactory.getLogger(CursoService.class);

    // Consultas SQL para procedimientos almacenados
    private static final String INSERTAR_CURSO = "{call insertarCurso(?,?,?,?)}";
    private static final String MODIFICAR_CURSO = "{call modificarCurso(?,?,?,?,?)}";
    private static final String ELIMINAR_CURSO = "{call eliminarCurso(?)}";
    private static final String LISTAR_CURSOS = "{?=call listarCursos()}";
    private static final String BUSCAR_CURSO_POR_NOMBRE = "{?=call buscarCursoPorNombre(?)}";
    private static final String BUSCAR_CURSO_POR_CODIGO = "{?=call buscarCursoPorCodigo(?)}";
    private static final String BUSCAR_CURSOS_POR_CARRERA = "{?=call buscarCursosPorCarrera(?)}";
    private static final String BUSCAR_CURSOS_POR_CARRERA_Y_CICLO = "{?=call buscarCursosPorCarreraYCiclo(?,?)}";
    private static final String BUSCAR_CURSOS_POR_CICLO = "{?=call buscarCursosPorCiclo(?)}";

    private final DataSource dataSource;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el DataSource.
     *
     * @param dataSource El DataSource gestionado por Spring Boot.
     */
    @Autowired
    public CursoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserta un nuevo curso en la base de datos.
     *
     * @param curso El objeto Curso a insertar.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la inserción no se realiza.
     */
    public void insertarCurso(Curso curso) throws GlobalException, NoDataException {
        logger.debug("Insertando curso: {}", curso.getCodigo());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CURSO)) {
            pstmt.setString(1, curso.getCodigo());
            pstmt.setString(2, curso.getNombre());
            pstmt.setLong(3, curso.getCreditos());
            pstmt.setLong(4, curso.getHorasSemanales());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
            logger.info("Curso insertado exitosamente: {}", curso.getCodigo());
        } catch (SQLException e) {
            logger.error("Error al insertar curso: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar curso: llave duplicada o sentencia inválida");
        }
    }

    /**
     * Modifica un curso existente en la base de datos.
     *
     * @param curso El objeto Curso con los datos actualizados.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la actualización no se realiza.
     */
    public void modificarCurso(Curso curso) throws GlobalException, NoDataException {
        logger.debug("Modificando curso: {}", curso.getCodigo());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_CURSO)) {
            pstmt.setLong(1, curso.getIdCurso()); // Changed from getId() to getIdCurso()
            pstmt.setString(2, curso.getCodigo());
            pstmt.setString(3, curso.getNombre());
            pstmt.setLong(4, curso.getCreditos());
            pstmt.setLong(5, curso.getHorasSemanales());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            logger.info("Curso modificado exitosamente: {}", curso.getCodigo());
        } catch (SQLException e) {
            logger.error("Error al modificar curso: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar curso: sentencia inválida");
        }
    }

    /**
     * Elimina un curso por su ID.
     *
     * @param idCurso ID del curso a eliminar.
     * @throws GlobalException Si hay dependencias o errores en la base de datos.
     * @throws NoDataException Si el curso no existe o no se elimina.
     */
    public void eliminarCurso(Long idCurso) throws GlobalException, NoDataException {
        logger.debug("Eliminando curso con ID: {}", idCurso);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CURSO)) {
            pstmt.setLong(1, idCurso);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: el curso no existe");
            }
            logger.info("Curso eliminado exitosamente: {}", idCurso);
        } catch (SQLException e) {
            logger.error("Error al eliminar curso: {}", e.getMessage(), e);
            handleDeleteSQLException(e);
        }
    }

    /**
     * Lista todos los cursos registrados.
     *
     * @return Lista de objetos Curso.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay datos disponibles.
     */
    public List<Curso> listarCursos() throws GlobalException, NoDataException {
        logger.debug("Listando todos los cursos");
        List<Curso> cursos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_CURSOS)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    cursos.add(mapResultSetToCurso(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al listar cursos: {}", e.getMessage(), e);
            throw new GlobalException("Error al listar cursos: " + e.getMessage());
        }
        if (cursos.isEmpty()) {
            throw new NoDataException("No hay cursos registrados");
        }
        logger.info("Listado de cursos obtenido exitosamente, total: {}", cursos.size());
        return cursos;
    }

    /**
     * Busca un curso por su nombre.
     *
     * @param nombre Nombre del curso a buscar.
     * @return El objeto Curso encontrado, o null si no existe.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     */
    public Curso buscarCursoPorNombre(String nombre) throws GlobalException {
        logger.debug("Buscando curso por nombre: {}", nombre);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CURSO_POR_NOMBRE)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, nombre);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Curso curso = mapResultSetToCurso(rs);
                    logger.info("Curso encontrado por nombre: {}", nombre);
                    return curso;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar curso por nombre: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar curso por nombre: " + e.getMessage());
        }
        logger.info("No se encontró curso con nombre: {}", nombre);
        return null;
    }

    /**
     * Busca un curso por su código.
     *
     * @param codigo Código del curso a buscar.
     * @return El objeto Curso encontrado, o null si no existe.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     */
    public Curso buscarCursoPorCodigo(String codigo) throws GlobalException {
        logger.debug("Buscando curso por código: {}", codigo);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CURSO_POR_CODIGO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, codigo);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Curso curso = mapResultSetToCurso(rs);
                    logger.info("Curso encontrado por código: {}", codigo);
                    return curso;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar curso por código: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar curso por código: " + e.getMessage());
        }
        logger.info("No se encontró curso con código: {}", codigo);
        return null;
    }

    /**
     * Busca cursos asociados a una carrera.
     *
     * @param idCarrera ID de la carrera.
     * @return Lista de objetos CursoDto.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay datos disponibles.
     */
    public List<CursoDto> buscarCursosPorCarrera(Long idCarrera) throws GlobalException, NoDataException {
        logger.debug("Buscando cursos por carrera: {}", idCarrera);
        List<CursoDto> cursos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CURSOS_POR_CARRERA)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, idCarrera);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    cursos.add(mapResultSetToCursoDto(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar cursos por carrera: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar cursos por carrera: " + e.getMessage());
        }
        if (cursos.isEmpty()) {
            throw new NoDataException("No hay cursos asociados a esta carrera");
        }
        logger.info("Cursos encontrados para carrera {}: {}", idCarrera, cursos.size());
        return cursos;
    }

    /**
     * Busca cursos por carrera y ciclo.
     *
     * @param idCarrera ID de la carrera.
     * @param idCiclo ID del ciclo.
     * @return Lista de objetos CursoDto.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay datos disponibles.
     */
    public List<CursoDto> buscarCursosPorCarreraYCiclo(Long idCarrera, Long idCiclo) throws GlobalException, NoDataException {
        logger.debug("Buscando cursos por carrera {} y ciclo: {}", idCarrera, idCiclo);
        List<CursoDto> cursos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CURSOS_POR_CARRERA_Y_CICLO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, idCarrera);
            pstmt.setLong(3, idCiclo);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    cursos.add(mapResultSetToCursoDto(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar cursos por carrera y ciclo: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar cursos por carrera y ciclo: " + e.getMessage());
        }
        if (cursos.isEmpty()) {
            throw new NoDataException("No hay cursos asociados a esta carrera y ciclo");
        }
        logger.info("Cursos encontrados para carrera {} y ciclo {}: {}", idCarrera, idCiclo, cursos.size());
        return cursos;
    }

    /**
     * Busca cursos por ciclo.
     *
     * @param idCiclo ID del ciclo.
     * @return Lista de objetos CursoDto.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay datos disponibles.
     */
    public List<CursoDto> buscarCursosPorCiclo(Long idCiclo) throws GlobalException, NoDataException {
        logger.debug("Buscando cursos por ciclo: {}", idCiclo);
        List<CursoDto> cursos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CURSOS_POR_CICLO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, idCiclo);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    cursos.add(mapResultSetToCursoDto(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar cursos por ciclo: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar cursos por ciclo: " + e.getMessage());
        }
        if (cursos.isEmpty()) {
            throw new NoDataException("No hay cursos asociados a este ciclo");
        }
        logger.info("Cursos encontrados para ciclo {}: {}", idCiclo, cursos.size());
        return cursos;
    }

    // Métodos utilitarios privados

    /**
     * Mapea un ResultSet a un objeto Curso.
     *
     * @param rs El ResultSet con los datos del curso.
     * @return Un objeto Curso mapeado.
     * @throws SQLException Si ocurre un error al leer los datos.
     */
    private Curso mapResultSetToCurso(ResultSet rs) throws SQLException {
        return new Curso(
                rs.getLong("id_curso"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getLong("creditos"),
                rs.getLong("horas_semanales")
        );
    }

    /**
     * Mapea un ResultSet a un objeto CursoDto.
     *
     * @param rs El ResultSet con los datos del curso.
     * @return Un objeto CursoDto mapeado.
     * @throws SQLException Si ocurre un error al leer los datos.
     */
    private CursoDto mapResultSetToCursoDto(ResultSet rs) throws SQLException {
        return new CursoDto(
                rs.getLong("id_curso"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getLong("creditos"),
                rs.getLong("horas_semanales"),
                rs.getLong("id_carrera_curso"),
                rs.getLong("anio"),
                rs.getLong("numero"),
                rs.getLong("id_ciclo")
        );
    }

    /**
     * Maneja excepciones SQL genéricas y lanza GlobalException con un mensaje específico.
     *
     * @param e       La excepción SQL capturada.
     * @param message El mensaje base para la excepción.
     * @throws GlobalException Si ocurre un error al ejecutar una operación SQL, encapsulando el mensaje original.
     */
    private void handleSQLException(SQLException e, String message) throws GlobalException {
        throw new GlobalException(message + ": " + e.getMessage());
    }

    /**
     * Maneja excepciones SQL específicas para operaciones de eliminación, mapeando códigos de error de triggers.
     *
     * @param e La excepción SQL capturada.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     */
    private void handleDeleteSQLException(SQLException e) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage = switch (errorCode) {
            case -20003 -> "No se puede eliminar el curso: está asociado a una carrera.";
            case -20004 -> "No se puede eliminar el curso: tiene matrículas asociadas.";
            default -> "Error al eliminar curso: " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
