package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Matricula;
import org.example.sistemaacademico.logic.dto.MatriculaAlumnoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con Matrículas en la base de datos.
 * Implementa operaciones CRUD y búsquedas, asegurando manejo adecuado de excepciones y cierre de recursos.
 */
@Service
public class MatriculaService {

    private static final Logger logger = LoggerFactory.getLogger(MatriculaService.class);

    // Consultas SQL para procedimientos almacenados
    private static final String INSERTAR_MATRICULA = "{call insertarMatricula(?,?)}";
    private static final String MODIFICAR_MATRICULA = "{call modificarMatricula(?,?,?,?)}";
    private static final String ELIMINAR_MATRICULA = "{call eliminarMatricula(?)}";
    private static final String LISTAR_MATRICULAS_POR_ALUMNO = "{?=call listarMatriculasPorAlumno(?)}";
    private static final String LISTAR_MATRICULAS_POR_ALUMNO_Y_CICLO = "{?=call listarMatriculasPorAlumnoYCiclo(?,?)}";

    private final DataSource dataSource;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el DataSource.
     *
     * @param dataSource El DataSource gestionado por Spring Boot.
     */
    @Autowired
    public MatriculaService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserta una nueva matrícula en la base de datos.
     *
     * @param matricula El objeto Matricula a insertar.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos, como una llave duplicada o sentencia inválida.
     * @throws NoDataException Si la inserción no se realiza.
     */
    public void insertarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        logger.debug("Insertando matrícula: alumno {}, grupo {}", matricula.getPkAlumno(), matricula.getPkGrupo());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_MATRICULA)) {
            pstmt.setLong(1, matricula.getPkAlumno());
            pstmt.setLong(2, matricula.getPkGrupo());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción de la matrícula");
            }
            logger.info("Matrícula insertada exitosamente: alumno {}, grupo {}", matricula.getPkAlumno(), matricula.getPkGrupo());
        } catch (SQLException e) {
            logger.error("Error al insertar matrícula: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar matrícula: llave duplicada o sentencia inválida");
        }
    }

    /**
     * Modifica una matrícula existente en la base de datos.
     *
     * @param matricula El objeto Matricula con los datos actualizados.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos, como una sentencia inválida.
     * @throws NoDataException Si la actualización no se realiza.
     */
    public void modificarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        logger.debug("Modificando matrícula: id {}, alumno {}, grupo {}, nota {}",
                matricula.getIdMatricula(), matricula.getPkAlumno(), matricula.getPkGrupo(), matricula.getNota());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_MATRICULA)) {
            pstmt.setLong(1, matricula.getIdMatricula());
            pstmt.setLong(2, matricula.getPkAlumno());
            pstmt.setLong(3, matricula.getPkGrupo());
            pstmt.setLong(4, matricula.getNota());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización de la matrícula");
            }
            logger.info("Matrícula modificada exitosamente: id {}", matricula.getIdMatricula());
        } catch (SQLException e) {
            logger.error("Error al modificar matrícula: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar matrícula: sentencia inválida");
        }
    }

    /**
     * Elimina una matrícula por su ID.
     *
     * @param idMatricula El ID de la matrícula a eliminar.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si la matrícula no existe o no se elimina.
     */
    public void eliminarMatricula(Long idMatricula) throws GlobalException, NoDataException {
        logger.debug("Eliminando matrícula: id {}", idMatricula);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_MATRICULA)) {
            pstmt.setLong(1, idMatricula);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: la matrícula no existe");
            }
            logger.info("Matrícula eliminada exitosamente: id {}", idMatricula);
        } catch (SQLException e) {
            logger.error("Error al eliminar matrícula: {}", e.getMessage(), e);
            handleSQLException(e, "Error al eliminar matrícula: sentencia inválida");
        }
    }

    /**
     * Lista las matrículas de un alumno por su cédula.
     *
     * @param cedula La cédula del alumno.
     * @return Lista de objetos MatriculaAlumnoDto con información de las matrículas.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay matrículas para el alumno.
     */
    public List<MatriculaAlumnoDto> listarMatriculasPorAlumno(String cedula) throws GlobalException, NoDataException {
        logger.debug("Listando matrículas por alumno con cédula: {}", cedula);
        List<MatriculaAlumnoDto> matriculas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_MATRICULAS_POR_ALUMNO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    matriculas.add(mapResultSetToMatriculaAlumnoDto(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al listar matrículas por alumno: {}", e.getMessage(), e);
            throw new GlobalException("Error al listar matrículas por alumno: " + e.getMessage());
        }
        if (matriculas.isEmpty()) {
            throw new NoDataException("No hay matrículas registradas para el alumno con cédula: " + cedula);
        }
        logger.info("Matrículas encontradas para alumno con cédula {}: {}", cedula, matriculas.size());
        return matriculas;
    }

    /**
     * Lista las matrículas de un alumno en un ciclo específico.
     *
     * @param idAlumno El ID del alumno.
     * @param idCiclo El ID del ciclo.
     * @return Lista de objetos MatriculaAlumnoDto con información de las matrículas.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay matrículas para el alumno y ciclo seleccionados.
     */
    public List<MatriculaAlumnoDto> listarMatriculasPorAlumnoYCiclo(Long idAlumno, Long idCiclo) throws GlobalException, NoDataException {
        logger.debug("Listando matrículas por alumno {} y ciclo: {}", idAlumno, idCiclo);
        List<MatriculaAlumnoDto> matriculas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_MATRICULAS_POR_ALUMNO_Y_CICLO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, idAlumno);
            pstmt.setLong(3, idCiclo);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    matriculas.add(mapResultSetToMatriculaAlumnoDto(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al listar matrículas por alumno y ciclo: {}", e.getMessage(), e);
            throw new GlobalException("Error al listar matrículas por alumno y ciclo: " + e.getMessage());
        }
        if (matriculas.isEmpty()) {
            throw new NoDataException("No hay matrículas registradas para el alumno " + idAlumno + " en el ciclo " + idCiclo);
        }
        logger.info("Matrículas encontradas para alumno {} y ciclo {}: {}", idAlumno, idCiclo, matriculas.size());
        return matriculas;
    }

    // Métodos utilitarios privados

    /**
     * Mapea un ResultSet a un objeto MatriculaAlumnoDto.
     *
     * @param rs El ResultSet con los datos de la matrícula.
     * @return Un objeto MatriculaAlumnoDto mapeado.
     * @throws SQLException Si ocurre un error al leer los datos.
     */
    private MatriculaAlumnoDto mapResultSetToMatriculaAlumnoDto(ResultSet rs) throws SQLException {
        return new MatriculaAlumnoDto(
                rs.getLong("id_matricula"),
                rs.getDouble("nota"),
                rs.getString("numero_grupo"),
                rs.getString("horario"),
                rs.getString("codigo_carrera"),
                rs.getString("nombre_carrera"),
                rs.getString("codigo_curso"),
                rs.getString("nombre_curso"),
                rs.getString("nombre_profesor"),
                rs.getString("cedula_profesor")
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
}
