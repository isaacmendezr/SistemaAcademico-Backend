package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.CarreraCurso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarreraCursoService {

    private static final String INSERTAR_CURSO_A_CARRERA = "{call insertarCursoACarrera(?,?,?)}";
    private static final String ELIMINAR_CURSO_DE_CARRERA = "{call eliminarCursoDeCarrera(?,?)}";
    private static final String MODIFICAR_ORDEN_CURSO_CARRERA = "{call modificarOrdenCursoCarrera(?,?,?)}";
    private static final String BUSCAR_CURSOS_POR_CARRERA_Y_CICLO = "{?=call buscarCursosPorCarreraYCiclo(?,?)}";
    private static final String LISTAR_CARRERA_CURSO = "{?=call listarCarreraCurso()}";

    private final DataSource dataSource;

    @Autowired
    public CarreraCursoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertar(CarreraCurso carreraCurso) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CURSO_A_CARRERA)) {
            pstmt.setLong(1, carreraCurso.getPkCarrera());
            pstmt.setLong(2, carreraCurso.getPkCurso());
            pstmt.setLong(3, carreraCurso.getPkCiclo());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción de la relación Carrera-Curso");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al insertar relación Carrera-Curso: llave duplicada o sentencia inválida: " + e.getMessage());
        }
    }

    public void eliminar(Long idCarrera, Long idCurso) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CURSO_DE_CARRERA)) {
            pstmt.setLong(1, idCarrera);
            pstmt.setLong(2, idCurso);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: la relación Carrera-Curso no existe");
            }
        } catch (SQLException e) {
            handleDeleteSQLException(e);
        }
    }

    public void modificar(CarreraCurso carreraCurso) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_ORDEN_CURSO_CARRERA)) {
            pstmt.setLong(1, carreraCurso.getPkCarrera());
            pstmt.setLong(2, carreraCurso.getPkCurso());
            pstmt.setLong(3, carreraCurso.getPkCiclo());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización de la relación Carrera-Curso");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al modificar relación Carrera-Curso: sentencia inválida: " + e.getMessage());
        }
    }

    public List<CursoDto> buscarCursosPorCarreraYCiclo(Long idCarrera, Long idCiclo) throws GlobalException, NoDataException {
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
            throw new GlobalException("Error al buscar cursos por carrera y ciclo: " + e.getMessage());
        }
        if (cursos.isEmpty()) {
            throw new NoDataException("No hay cursos asociados a esta carrera y ciclo");
        }
        return cursos;
    }

    public List<CarreraCurso> listar() throws GlobalException, NoDataException {
        List<CarreraCurso> lista = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_CARRERA_CURSO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    lista.add(mapResultSetToCarreraCurso(rs));
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al listar relaciones Carrera-Curso: " + e.getMessage());
        }
        if (lista.isEmpty()) {
            throw new NoDataException("No hay relaciones Carrera-Curso registradas");
        }
        return lista;
    }

    private CarreraCurso mapResultSetToCarreraCurso(ResultSet rs) throws SQLException {
        return new CarreraCurso(
                rs.getLong("id_carrera_curso"),
                rs.getLong("pk_carrera"),
                rs.getLong("pk_curso"),
                rs.getLong("pk_ciclo")
        );
    }

    private CursoDto mapResultSetToCursoDto(ResultSet rs) throws SQLException {
        return new CursoDto(
                rs.getLong("id_curso"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getLong("creditos"),
                rs.getLong("horas_semanales"),
                rs.getLong("id_carrera_curso"),
                null,
                null,
                null
        );
    }

    private void handleDeleteSQLException(SQLException e) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage;
        if (errorCode == -20035) {
            errorMessage = "No se puede eliminar la relación Carrera-Curso: tiene grupos asociados.";
        } else {
            errorMessage = "Error al eliminar relación Carrera-Curso: " + e.getMessage();
        }
        throw new GlobalException(errorMessage);
    }
}
