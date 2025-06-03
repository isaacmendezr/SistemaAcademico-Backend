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

    // Consultas SQL para procedimientos almacenados
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
        logger.debug("Insertando relación Carrera-Curso: carrera {}, curso {}, ciclo {}",
                carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CURSO_A_CARRERA)) {
            pstmt.setLong(1, carreraCurso.getPkCarrera());
            pstmt.setLong(2, carreraCurso.getPkCurso());
            pstmt.setLong(3, carreraCurso.getPkCiclo());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción de la relación Carrera-Curso");
            }
            logger.info("Relación Carrera-Curso insertada exitosamente: carrera {}, curso {}, ciclo {}",
                    carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());
        } catch (SQLException e) {
            logger.error("Error al insertar relación Carrera-Curso: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar relación Carrera-Curso: llave duplicada o sentencia inválida");
        }
    }

    public void eliminar(Long idCarrera, Long idCurso) throws GlobalException, NoDataException {
        logger.debug("Eliminando relación Carrera-Curso: carrera {}, curso {}", idCarrera, idCurso);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CURSO_DE_CARRERA)) {
            pstmt.setLong(1, idCarrera);
            pstmt.setLong(2, idCurso);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: la relación Carrera-Curso no existe");
            }
            logger.info("Relación Carrera-Curso eliminada exitosamente: carrera {}, curso {}", idCarrera, idCurso);
        } catch (SQLException e) {
            logger.error("Error al eliminar relación Carrera-Curso: {}", e.getMessage(), e);
            handleDeleteSQLException(e);
        }
    }

    public void modificar(CarreraCurso carreraCurso) throws GlobalException, NoDataException {
        logger.debug("Modificando relación Carrera-Curso: carrera {}, curso {}, nuevo ciclo {}",
                carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_ORDEN_CURSO_CARRERA)) {
            pstmt.setLong(1, carreraCurso.getPkCarrera());
            pstmt.setLong(2, carreraCurso.getPkCurso());
            pstmt.setLong(3, carreraCurso.getPkCiclo());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización de la relación Carrera-Curso");
            }
            logger.info("Relación Carrera-Curso modificada exitosamente: carrera {}, curso {}, ciclo {}",
                    carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());
        } catch (SQLException e) {
            logger.error("Error al modificar relación Carrera-Curso: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar relación Carrera-Curso: sentencia inválida");
        }
    }

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

    public List<CarreraCurso> listar() throws GlobalException, NoDataException {
        logger.debug("Listando todas las relaciones Carrera-Curso");
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
            logger.error("Error al listar relaciones Carrera-Curso: {}", e.getMessage(), e);
            throw new GlobalException("Error al listar relaciones Carrera-Curso: " + e.getMessage());
        }
        if (lista.isEmpty()) {
            throw new NoDataException("No hay relaciones Carrera-Curso registradas");
        }
        logger.info("Listado de relaciones Carrera-Curso obtenido exitosamente, total: {}", lista.size());
        return lista;
    }

    // Métodos utilitarios privados
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
                null, // anio (not provided by buscarCursosPorCarreraYCiclo)
                null, // numero (not provided by buscarCursosPorCarreraYCiclo)
                null  // idCiclo (not provided by buscarCursosPorCarreraYCiclo)
        );
    }

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        throw new GlobalException(message + ": " + e.getMessage());
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
