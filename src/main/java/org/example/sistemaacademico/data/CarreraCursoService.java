package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.CarreraCurso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarreraCursoService {

    private static final Logger logger = LoggerFactory.getLogger(CarreraCursoService.class);

    private static final String INSERTAR_CURSO_A_CARRERA = "{call insertarCursoACarrera(?,?,?)}";
    private static final String ELIMINAR_CURSO_DE_CARRERA = "{call eliminarCursoDeCarrera(?,?)}";
    private static final String MODIFICAR_ORDEN_CURSO_CARRERA = "{call modificarOrdenCursoCarrera(?,?,?)}";
    private static final String BUSCAR_CURSOS_POR_CARRERA = "{?=call buscarCursosPorCarrera(?)}";
    private static final String BUSCAR_CURSOS_POR_CARRERA_Y_CICLO = "{?=call buscarCursosPorCarreraYCiclo(?,?)}";
    private static final String LISTAR_CARRERA_CURSO = "{?=call listarCarreraCurso()}";

    private final DataSource dataSource;

    private final GrupoService grupoService;

    @Autowired
    public CarreraCursoService(DataSource dataSource, GrupoService grupoService) {
        this.dataSource = dataSource;
        this.grupoService = grupoService;
    }

    public void insertar(CarreraCurso carreraCurso) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CURSO_A_CARRERA)) {
            pstmt.setLong(1, carreraCurso.getPkCarrera());
            pstmt.setLong(2, carreraCurso.getPkCurso());
            pstmt.setLong(3, carreraCurso.getPkCiclo());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new NoDataException("No se realizó la inserción de la relación Carrera-Curso");
            }
        } catch (SQLException e) {
            logger.error("Error al insertar relación Carrera-Curso: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar relación Carrera-Curso");
        }
    }

    public void eliminar(Long idCarrera, Long idCurso) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CURSO_DE_CARRERA)) {
            pstmt.setLong(1, idCarrera);
            pstmt.setLong(2, idCurso);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new NoDataException("No se realizó el borrado: la relación Carrera-Curso no existe");
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar relación Carrera-Curso: {}", e.getMessage(), e);
            handleDeleteSQLException(e);
        }
    }

    public void modificar(CarreraCurso carreraCurso) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_ORDEN_CURSO_CARRERA)) {
            pstmt.setLong(1, carreraCurso.getPkCarrera());
            pstmt.setLong(2, carreraCurso.getPkCurso());
            pstmt.setLong(3, carreraCurso.getPkCiclo());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new NoDataException("No se realizó la actualización de la relación Carrera-Curso");
            }
        } catch (SQLException e) {
            logger.error("Error al modificar relación Carrera-Curso: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar relación Carrera-Curso");
        }
    }

    public List<CursoDto> buscarCursosPorCarreraYCiclo(Long idCarrera, Long idCiclo) throws GlobalException, NoDataException {
        if (idCarrera == null) {
            throw new GlobalException("El ID de la carrera no puede ser nulo.");
        }

        List<CursoDto> cursos = new ArrayList<>();
        String query = (idCiclo != null) ? BUSCAR_CURSOS_POR_CARRERA_Y_CICLO : BUSCAR_CURSOS_POR_CARRERA;

        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(query)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, idCarrera);
            if (idCiclo != null) {
                pstmt.setLong(3, idCiclo);
            }
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    CursoDto curso = new CursoDto();
                    curso.setIdCurso(rs.getLong("id_curso"));
                    curso.setCodigo(rs.getString("codigo"));
                    curso.setNombre(rs.getString("nombre"));
                    curso.setCreditos(rs.getLong("creditos"));
                    curso.setHorasSemanales(rs.getLong("horas_semanales"));
                    curso.setIdCarreraCurso(rs.getLong("id_carrera_curso"));
                    // Campos adicionales para buscarCursosPorCarrera
                    if (idCiclo == null) {
                        curso.setAnio(rs.getLong("anio"));
                        curso.setNumero(rs.getLong("numero"));
                        curso.setIdCiclo(rs.getLong("id_ciclo"));
                    }
                    cursos.add(curso);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar cursos por carrera {} y ciclo {}: {}", idCarrera, idCiclo, e.getMessage(), e);
            throw new GlobalException("Error al buscar cursos por carrera y ciclo: " + e.getMessage());
        }
        if (cursos.isEmpty()) {
            throw new NoDataException("No hay cursos asociados a la carrera " + idCarrera + (idCiclo != null ? " en el ciclo " + idCiclo : ""));
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

    public boolean tieneGruposAsociados(Long idCarrera, Long idCurso) throws GlobalException {
        try {
            grupoService.buscarGruposPorCarreraCurso(idCarrera, idCurso);
            return true;
        } catch (NoDataException e) {
            return false;
        }
    }

    private CarreraCurso mapResultSetToCarreraCurso(ResultSet rs) throws SQLException {
        return new CarreraCurso(
                rs.getLong("id_carrera_curso"),
                rs.getLong("pk_carrera"),
                rs.getLong("pk_curso"),
                rs.getLong("pk_ciclo")
        );
    }

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        int errorCode = Math.abs(e.getErrorCode());
        String errorMessage = switch (errorCode) {
            case 20026 -> "Ya existe una asociación de ese curso en esa carrera y ciclo.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }

    private void handleDeleteSQLException(SQLException e) throws GlobalException {
        int errorCode = Math.abs(e.getErrorCode());
        String errorMessage = switch (errorCode) {
            case 20035 -> "No se puede eliminar la relación Carrera-Curso: tiene grupos asociados.";
            default -> "Error al eliminar relación Carrera-Curso: " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
