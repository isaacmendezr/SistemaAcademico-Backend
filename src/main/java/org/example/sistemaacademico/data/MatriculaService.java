package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Matricula;
import org.example.sistemaacademico.logic.dto.MatriculaAlumnoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatriculaService {

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
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_MATRICULA)) {
            pstmt.setLong(1, matricula.getPkAlumno());
            pstmt.setLong(2, matricula.getPkGrupo());
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó la inserción de la matrícula");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al insertar matrícula");
        }
    }

    public void modificarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_MATRICULA)) {
            pstmt.setLong(1, matricula.getIdMatricula());
            pstmt.setLong(2, matricula.getPkAlumno());
            pstmt.setLong(3, matricula.getPkGrupo());
            pstmt.setLong(4, matricula.getNota());
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó la actualización de la matrícula");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al modificar matrícula");
        }
    }

    public void eliminarMatricula(Long idMatricula) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_MATRICULA)) {
            pstmt.setLong(1, idMatricula);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó el borrado: la matrícula no existe");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al eliminar matrícula");
        }
    }

    public List<MatriculaAlumnoDto> listarMatriculasPorAlumno(String cedula) throws GlobalException, NoDataException {
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
            throw new GlobalException("Error al listar matrículas por alumno: " + e.getMessage());
        }
        if (matriculas.isEmpty()) {
            throw new NoDataException("No hay matrículas registradas para el alumno con cédula: " + cedula);
        }
        return matriculas;
    }

    public List<MatriculaAlumnoDto> listarMatriculasPorAlumnoYCiclo(Long idAlumno, Long idCiclo) throws GlobalException, NoDataException {
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
            throw new GlobalException("Error al listar matrículas por alumno y ciclo: " + e.getMessage());
        }
        if (matriculas.isEmpty()) {
            throw new NoDataException("No hay matrículas registradas para el alumno " + idAlumno + " en el ciclo " + idCiclo);
        }
        return matriculas;
    }

    // Utilitarios

    public void verificarEliminar(Long idMatricula) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM Matricula WHERE id_matricula = ?")) {
            pstmt.setLong(1, idMatricula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new NoDataException("No se encontró matrícula con id: " + idMatricula);
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al verificar eliminación de matrícula: " + e.getMessage());
        }
    }

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
        int errorCode = Math.abs(e.getErrorCode());
        String errorMessage = switch (errorCode) {
            case 20029 -> "El alumno ya está matriculado en otro grupo de este curso.";
            case 20031 -> "La nota debe estar entre 0 y 100.";
            case 20040 -> "El alumno solo puede matricular cursos de su carrera.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
