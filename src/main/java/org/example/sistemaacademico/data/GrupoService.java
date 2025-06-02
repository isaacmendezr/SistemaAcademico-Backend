package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Grupo;
import org.example.sistemaacademico.logic.dto.GrupoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con Grupos en la base de datos.
 * Implementa operaciones CRUD y búsquedas, asegurando manejo adecuado de excepciones y cierre de recursos.
 */
@Service
public class GrupoService {

    private static final Logger logger = LoggerFactory.getLogger(GrupoService.class);

    // Consultas SQL para procedimientos almacenados
    private static final String INSERTAR_GRUPO = "{call insertarGrupo(?,?,?,?)}";
    private static final String MODIFICAR_GRUPO = "{call modificarGrupo(?,?,?,?,?)}";
    private static final String ELIMINAR_GRUPO = "{call eliminarGrupo(?)}";
    private static final String LISTAR_GRUPOS = "{?=call listarGrupos()}";
    private static final String BUSCAR_GRUPOS_POR_CARRERA_CURSO = "{?=call buscarGruposPorCarreraCurso(?,?)}";
    private static final String BUSCAR_GRUPOS_POR_CURSO_CICLO_CARRERA = "{?=call buscarGruposPorCursoCicloCarrera(?,?,?)}";
    private static final String BUSCAR_GRUPOS_POR_PROFESOR = "{?=call buscarGruposPorProfesor(?)}";

    private final DataSource dataSource;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el DataSource.
     *
     * @param dataSource El DataSource gestionado por Spring Boot.
     */
    @Autowired
    public GrupoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserta un nuevo grupo en la base de datos.
     *
     * @param grupo El objeto Grupo a insertar.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos, como una llave duplicada o sentencia inválida.
     * @throws NoDataException Si la inserción no se realiza.
     */
    public void insertarGrupo(Grupo grupo) throws GlobalException, NoDataException {
        logger.debug("Insertando grupo: carrera-curso {}, número {}, profesor {}",
                grupo.getIdCarreraCurso(), grupo.getNumeroGrupo(), grupo.getIdProfesor());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_GRUPO)) {
            pstmt.setLong(1, grupo.getIdCarreraCurso());
            pstmt.setLong(2, grupo.getNumeroGrupo());
            pstmt.setString(3, grupo.getHorario());
            pstmt.setLong(4, grupo.getIdProfesor());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción del grupo");
            }
            logger.info("Grupo insertado exitosamente: carrera-curso {}, número {}, profesor {}",
                    grupo.getIdCarreraCurso(), grupo.getNumeroGrupo(), grupo.getIdProfesor());
        } catch (SQLException e) {
            logger.error("Error al insertar grupo: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar grupo: llave duplicada o sentencia inválida");
        }
    }

    /**
     * Modifica un grupo existente en la base de datos.
     *
     * @param grupo El objeto Grupo con los datos actualizados.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos, como una sentencia inválida.
     * @throws NoDataException Si la actualización no se realiza.
     */
    public void modificarGrupo(Grupo grupo) throws GlobalException, NoDataException {
        logger.debug("Modificando grupo: id {}, carrera-curso {}, número {}, profesor {}",
                grupo.getIdGrupo(), grupo.getIdCarreraCurso(), grupo.getNumeroGrupo(), grupo.getIdProfesor());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_GRUPO)) {
            pstmt.setLong(1, grupo.getIdGrupo());
            pstmt.setLong(2, grupo.getIdCarreraCurso());
            pstmt.setLong(3, grupo.getNumeroGrupo());
            pstmt.setString(4, grupo.getHorario());
            pstmt.setLong(5, grupo.getIdProfesor());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización del grupo");
            }
            logger.info("Grupo modificado exitosamente: id {}", grupo.getIdGrupo());
        } catch (SQLException e) {
            logger.error("Error al modificar grupo: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar grupo: sentencia inválida");
        }
    }

    /**
     * Elimina un grupo por su ID.
     *
     * @param idGrupo El ID del grupo a eliminar.
     * @throws GlobalException Si hay dependencias (como matrículas asociadas) o errores en la base de datos.
     * @throws NoDataException Si el grupo no existe o no se elimina.
     */
    public void eliminarGrupo(Long idGrupo) throws GlobalException, NoDataException {
        logger.debug("Eliminando grupo: id {}", idGrupo);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_GRUPO)) {
            pstmt.setLong(1, idGrupo);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: el grupo no existe");
            }
            logger.info("Grupo eliminado exitosamente: id {}", idGrupo);
        } catch (SQLException e) {
            logger.error("Error al eliminar grupo: {}", e.getMessage(), e);
            handleDeleteSQLException(e);
        }
    }

    /**
     * Lista todos los grupos registrados.
     *
     * @return Lista de objetos Grupo.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay datos disponibles.
     */
    public List<Grupo> listarGrupos() throws GlobalException, NoDataException {
        logger.debug("Listando todos los grupos");
        List<Grupo> grupos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_GRUPOS)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    grupos.add(mapResultToGrupo(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al listar grupos: {}", e.getMessage(), e);
            throw new GlobalException("Error al listar grupos: " + e.getMessage());
        }
        if (grupos.isEmpty()) {
            throw new NoDataException("No hay grupos registrados");
        }
        logger.info("Listado de grupos obtenido exitosamente, total: {}", grupos.size());
        return grupos;
    }

    /**
     * Busca grupos asociados a una carrera y curso específicos.
     *
     * @param idCarrera El ID de la carrera.
     * @param idCurso El ID del curso.
     * @return Lista de objetos GrupoDto con información del grupo y profesor.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay grupos para la carrera y curso seleccionados.
     */
    public List<GrupoDto> buscarGruposPorCarreraCurso(Long idCarrera, Long idCurso) throws GlobalException, NoDataException {
        logger.debug("Buscando grupos por carrera {} y curso: {}", idCarrera, idCurso);
        List<GrupoDto> grupos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_GRUPOS_POR_CARRERA_CURSO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, idCarrera);
            pstmt.setLong(3, idCurso);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    grupos.add(mapResultToGrupoDto(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar grupos por carrera y curso: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar grupos por carrera y curso: " + e.getMessage());
        }
        if (grupos.isEmpty()) {
            throw new NoDataException("No hay grupos para la carrera y curso indicados");
        }
        logger.info("Grupos encontrados para la carrera {} y curso {}: {}", idCarrera, idCurso, grupos.size());
        return grupos;
    }

    /**
     * Busca grupos asociados a un curso, ciclo y carrera específicos.
     *
     * @param idCurso El curso del curso.
     * @param idCiclo El ID del ciclo.
     * @param idCarrera El ID de la carrera.
     * @return Lista de objetos GrupoDto con información del grupo y profesor.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay grupos para el curso, ciclo y carrera seleccionados.
     */
    public List<GrupoDto> buscarGruposPorCursoCicloCarrera(Long idCurso, Long idCiclo, Long idCarrera) throws GlobalException, NoDataException {
        logger.debug("Buscando grupos por curso {}, ciclo {}, y carrera {}", idCurso, idCiclo, idCarrera);
        List<GrupoDto> grupos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_GRUPOS_POR_CURSO_CICLO_CARRERA)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, idCurso);
            pstmt.setLong(3, idCiclo);
            pstmt.setLong(4, idCarrera);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    grupos.add(mapResultToGrupoDto(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar grupos por curso, ciclo y carrera: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar grupos por curso, ciclo y carrera: " + e.getMessage());
        }
        if (grupos.isEmpty()) {
            throw new NoDataException("No hay grupos para el curso, ciclo y carrera indicados");
        }
        logger.info("Grupos encontrados para curso {}, ciclo {}, carrera {}: {}", idCurso, idCiclo, idCarrera, grupos.size());
        return grupos;
    }

    /**
     * Busca grupos asignados a un profesor por su cédula.
     *
     * @param cedula La cédula del profesor.
     * @return Lista de objetos GrupoDto con información del grupo y profesor.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay grupos asignados al profesor.
     */
    public List<GrupoDto> buscarGruposPorProfesor(String cedula) throws GlobalException, NoDataException {
        logger.debug("Buscando grupos por profesor con cédula: {}", cedula);
        List<GrupoDto> grupos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_GRUPOS_POR_PROFESOR)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    grupos.add(mapResultToGrupoDto(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar grupos por profesor: {}", e.getMessage(), e);
            throw new GlobalException("Error al buscar grupos por profesor: " + e.getMessage());
        }
        if (grupos.isEmpty()) {
            throw new NoDataException("No hay grupos asignados al profesor con cédula: " + cedula);
        }
        logger.info("Grupos encontrados para profesor con cédula {}: {}", cedula, grupos.size());
        return grupos;
    }

    // Métodos utilitarios privados

    /**
     * Mapea un ResultSet a un objeto Grupo.
     *
     * @param rs El ResultSet con los datos del grupo.
     * @return Un objeto Grupo mapeado.
     * @throws SQLException Si ocurre un error al leer los datos.
     */
    private Grupo mapResultToGrupo(ResultSet rs) throws SQLException {
        return new Grupo(
                rs.getLong("id_grupo"),
                rs.getLong("pk_carrera_curso"),
                rs.getLong("numero_grupo"),
                rs.getString("horario"),
                rs.getLong("pk_profesor")
        );
    }

    /**
     * Mapea un ResultSet a un objeto GrupoDto.
     *
     * @param rs El ResultSet con los datos del grupo.
     * @return Un objeto GrupoDto mapeado.
     * @throws SQLException Si ocurre un error al leer los datos.
     */
    private GrupoDto mapResultToGrupoDto(ResultSet rs) throws SQLException {
        return new GrupoDto(
                rs.getLong("id_grupo"),
                rs.getLong("pk_carrera_curso"),
                rs.getLong("numero_grupo"),
                rs.getString("horario"),
                rs.getLong("pk_profesor"),
                rs.getString("nombre_profesor")
        );
    }

    /**
     * Maneja excepciones SQL genéricas y lanza GlobalException con un mensaje específico.
     *
     * @param e       La excepción SQL capturada.
     * @param message El mensaje base para la excepción.
     * @throws GlobalException Si ocurre un error en la base de datos, como una sentencia SQL inválida o una llave duplicada.
     */
    private void handleSQLException(SQLException e, String message) throws GlobalException {
        throw new GlobalException(message + ": " + e.getMessage());
    }

    /**
     * Maneja excepciones SQL específicas para operaciones de eliminación, mapeando códigos de error de triggers.
     *
     * @param e La excepción SQL capturada.
     * @throws GlobalException Si el grupo no puede ser eliminado debido a matrículas asociadas o errores genéricos de base de datos.
     */
    private void handleDeleteSQLException(SQLException e) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage;
        if (errorCode == -20036) {
            errorMessage = "No se puede eliminar el grupo: tiene matrículas asociadas.";
        } else {
            errorMessage = "Error al eliminar grupo: " + e.getMessage();
        }
        throw new GlobalException(errorMessage);
    }
}
