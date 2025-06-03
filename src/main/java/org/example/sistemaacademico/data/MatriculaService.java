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

    @Autowired
    public MatriculaService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        throw new GlobalException(message + ": " + e.getMessage());
    }
}
