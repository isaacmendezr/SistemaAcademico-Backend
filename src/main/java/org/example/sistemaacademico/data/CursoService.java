package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Curso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CursoService {

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

    @Autowired
    public CursoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertarCurso(Curso curso) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CURSO)) {
            pstmt.setString(1, curso.getCodigo());
            pstmt.setString(2, curso.getNombre());
            pstmt.setLong(3, curso.getCreditos());
            pstmt.setLong(4, curso.getHorasSemanales());
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó la inserción del curso");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al insertar curso");
        }
    }

    public void modificarCurso(Curso curso) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_CURSO)) {
            pstmt.setLong(1, curso.getIdCurso());
            pstmt.setString(2, curso.getCodigo());
            pstmt.setString(3, curso.getNombre());
            pstmt.setLong(4, curso.getCreditos());
            pstmt.setLong(5, curso.getHorasSemanales());
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó la actualización del curso");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al modificar curso");
        }
    }

    public void eliminarCurso(Long idCurso) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CURSO)) {
            pstmt.setLong(1, idCurso);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó el borrado: el curso no existe");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al eliminar curso");
        }
    }

    public List<Curso> listarCursos() throws GlobalException, NoDataException {
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
            throw new GlobalException("Error al listar cursos: " + e.getMessage());
        }
        if (cursos.isEmpty()) {
            throw new NoDataException("No hay cursos registrados");
        }
        return cursos;
    }

    public Curso buscarCursoPorNombre(String nombre) throws GlobalException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CURSO_POR_NOMBRE)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, nombre);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToCurso(rs);
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al buscar curso por nombre: " + e.getMessage());
        }
        return null;
    }

    public Curso buscarCursoPorCodigo(String codigo) throws GlobalException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CURSO_POR_CODIGO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, codigo);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToCurso(rs);
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al buscar curso por código: " + e.getMessage());
        }
        return null;
    }

    public List<CursoDto> buscarCursosPorCarrera(Long idCarrera) throws GlobalException, NoDataException {
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
            throw new GlobalException("Error al buscar cursos por carrera: " + e.getMessage());
        }
        if (cursos.isEmpty()) {
            throw new NoDataException("No hay cursos asociados a esta carrera");
        }
        return cursos;
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

    public List<CursoDto> buscarCursosPorCiclo(Long idCiclo) throws GlobalException, NoDataException {
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
            throw new GlobalException("Error al buscar cursos por ciclo: " + e.getMessage());
        }
        if (cursos.isEmpty()) {
            throw new NoDataException("No hay cursos asociados a este ciclo");
        }
        return cursos;
    }

    // Utilitarios

    public void verificarEliminar(Long idCurso) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM Carrera_Curso WHERE pk_curso = ? " +
                             "UNION ALL " +
                             "SELECT COUNT(*) FROM Grupo g JOIN Carrera_Curso cc ON g.pk_carrera_curso = cc.id_carrera_curso WHERE cc.pk_curso = ?")) {
            pstmt.setLong(1, idCurso);
            pstmt.setLong(2, idCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new GlobalException("No se puede eliminar el curso: está asociado a una carrera.");
                }
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new GlobalException("No se puede eliminar el curso: tiene grupos asociados.");
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al verificar eliminación de curso: " + e.getMessage());
        }
    }

    private Curso mapResultSetToCurso(ResultSet rs) throws SQLException {
        return new Curso(
                rs.getLong("id_curso"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getLong("creditos"),
                rs.getLong("horas_semanales")
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
                rs.getLong("anio"),
                rs.getLong("numero"),
                rs.getLong("id_ciclo")
        );
    }

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        int errorCode = Math.abs(e.getErrorCode());
        String errorMessage = switch (errorCode) {
            case 20003 -> "No se puede eliminar el curso: está asociado a una carrera o tiene grupos.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
