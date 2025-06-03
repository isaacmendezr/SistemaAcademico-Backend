package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Grupo;
import org.example.sistemaacademico.logic.dto.GrupoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GrupoService {

    private static final String INSERTAR_GRUPO = "{call insertarGrupo(?,?,?,?)}";
    private static final String MODIFICAR_GRUPO = "{call modificarGrupo(?,?,?,?,?)}";
    private static final String ELIMINAR_GRUPO = "{call eliminarGrupo(?)}";
    private static final String LISTAR_GRUPOS = "{?=call listarGrupos()}";
    private static final String BUSCAR_GRUPOS_POR_CARRERA_CURSO = "{?=call buscarGruposPorCarreraCurso(?,?)}";
    private static final String BUSCAR_GRUPOS_POR_CURSO_CICLO_CARRERA = "{?=call buscarGruposPorCursoCicloCarrera(?,?,?)}";
    private static final String BUSCAR_GRUPOS_POR_PROFESOR = "{?=call buscarGruposPorProfesor(?)}";

    private final DataSource dataSource;

    @Autowired
    public GrupoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertarGrupo(Grupo grupo) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_GRUPO)) {
            pstmt.setLong(1, grupo.getIdCarreraCurso());
            pstmt.setLong(2, grupo.getNumeroGrupo());
            pstmt.setString(3, grupo.getHorario());
            pstmt.setLong(4, grupo.getIdProfesor());
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó la inserción del grupo");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al insertar grupo");
        }
    }

    public void modificarGrupo(Grupo grupo) throws GlobalException, NoDataException {
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
        } catch (SQLException e) {
            throw new GlobalException("Error al modificar grupo: sentencia inválida: " + e.getMessage());
        }
    }

    public void eliminarGrupo(Long idGrupo) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_GRUPO)) {
            pstmt.setLong(1, idGrupo);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó el borrado: el grupo no existe");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al eliminar grupo");
        }
    }

    public List<Grupo> listarGrupos() throws GlobalException, NoDataException {
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
            throw new GlobalException("Error al listar grupos: " + e.getMessage());
        }
        if (grupos.isEmpty()) {
            throw new NoDataException("No hay grupos registrados");
        }
        return grupos;
    }

    public List<GrupoDto> buscarGruposPorCarreraCurso(Long idCarrera, Long idCurso) throws GlobalException, NoDataException {
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
            throw new GlobalException("Error al buscar grupos por carrera y curso: " + e.getMessage());
        }
        if (grupos.isEmpty()) {
            throw new NoDataException("No hay grupos para la carrera y curso indicados");
        }
        return grupos;
    }

    public List<GrupoDto> buscarGruposPorCursoCicloCarrera(Long idCurso, Long idCiclo, Long idCarrera) throws GlobalException, NoDataException {
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
            throw new GlobalException("Error al buscar grupos por curso, ciclo y carrera: " + e.getMessage());
        }
        if (grupos.isEmpty()) {
            throw new NoDataException("No hay grupos para el curso, ciclo y carrera indicados");
        }
        return grupos;
    }

    public List<GrupoDto> buscarGruposPorProfesor(String cedula) throws GlobalException, NoDataException {
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
            throw new GlobalException("Error al buscar grupos por profesor: " + e.getMessage());
        }
        if (grupos.isEmpty()) {
            throw new NoDataException("No hay grupos asignados al profesor con cédula: " + cedula);
        }
        return grupos;
    }

    // Utilitarios
    public void verificarEliminar(Long idGrupo) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM Matricula WHERE pk_grupo = ?")) {
            pstmt.setLong(1, idGrupo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new GlobalException("No se puede eliminar el grupo: tiene estudiantes matriculados.");
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al verificar eliminación de grupo: " + e.getMessage());
        }
    }

    private Grupo mapResultToGrupo(ResultSet rs) throws SQLException {
        return new Grupo(
                rs.getLong("id_grupo"),
                rs.getLong("pk_carrera_curso"),
                rs.getLong("numero_grupo"),
                rs.getString("horario"),
                rs.getLong("pk_profesor")
        );
    }

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

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        int errorCode = Math.abs(e.getErrorCode());
        String errorMessage = switch (errorCode) {
            case 20006 -> "No se puede eliminar el grupo: tiene estudiantes matriculados.";
            case 20027 -> "Ya existe un grupo con ese número para el mismo curso y ciclo.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
