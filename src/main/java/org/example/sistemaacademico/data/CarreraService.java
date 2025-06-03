package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Carrera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarreraService {

    private static final Logger logger = LoggerFactory.getLogger(CarreraService.class);

    // Consultas SQL para procedimientos almacenados
    private static final String INSERTAR_CARRERA = "{call insertarCarrera(?,?,?)}";
    private static final String MODIFICAR_CARRERA = "{call modificarCarrera(?,?,?,?)}";
    private static final String ELIMINAR_CARRERA = "{call eliminarCarrera(?)}";
    private static final String LISTAR_CARRERAS = "{?=call listarCarreras()}";
    private static final String BUSCAR_CARRERA_POR_CODIGO = "{?=call buscarCarreraPorCodigo(?)}";
    private static final String BUSCAR_CARRERA_POR_NOMBRE = "{?=call buscarCarreraPorNombre(?)}";
    private static final String INSERTAR_CURSO_A_CARRERA = "{call insertarCursoACarrera(?,?,?)}";
    private static final String ELIMINAR_CURSO_DE_CARRERA = "{call eliminarCursoDeCarrera(?,?)}";
    private static final String MODIFICAR_ORDEN_CURSO_CARRERA = "{call modificarOrdenCursoCarrera(?,?,?)}";

    private final DataSource dataSource;

    private final AlumnoService alumnoService;
    private final CarreraCursoService carreraCursoService;
    private final GrupoService grupoService;

    @Autowired
    public CarreraService(DataSource dataSource, AlumnoService alumnoService, CarreraCursoService carreraCursoService, GrupoService grupoService) {
        this.dataSource = dataSource;
        this.alumnoService = alumnoService;
        this.carreraCursoService = carreraCursoService;
        this.grupoService = grupoService;
    }

    public void insertarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CARRERA)) {
            pstmt.setString(1, carrera.getCodigo());
            pstmt.setString(2, carrera.getNombre());
            pstmt.setString(3, carrera.getTitulo());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new NoDataException("No se realizó la inserción");
            }
        } catch (SQLException e) {
            logger.error("Error al insertar carrera: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar carrera");
        }
    }

    public void modificarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_CARRERA)) {
            pstmt.setLong(1, carrera.getIdCarrera());
            pstmt.setString(2, carrera.getCodigo());
            pstmt.setString(3, carrera.getNombre());
            pstmt.setString(4, carrera.getTitulo());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
        } catch (SQLException e) {
            logger.error("Error al modificar carrera: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar carrera");
        }
    }

    public void eliminarCarrera(Long idCarrera) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CARRERA)) {
            pstmt.setLong(1, idCarrera);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new NoDataException("No se realizó el borrado: la carrera no existe");
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar carrera: {}", e.getMessage(), e);
            handleDeleteSQLException(e, "Error al eliminar carrera");
        }
    }

    public List<Carrera> listarCarreras() throws GlobalException, NoDataException {
        List<Carrera> carreras = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_CARRERAS)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    carreras.add(mapResultSetToCarrera(rs));
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al listar carreras: " + e.getMessage());
        }
        if (carreras.isEmpty()) {
            throw new NoDataException("No hay carreras registradas");
        }
        return carreras;
    }

    public Carrera buscarCarreraPorCodigo(String codigo) throws GlobalException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CARRERA_POR_CODIGO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, codigo);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToCarrera(rs);
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al buscar carrera por código: " + e.getMessage());
        }
        return null;
    }

    public Carrera buscarCarreraPorNombre(String nombre) throws GlobalException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_CARRERA_POR_NOMBRE)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, nombre);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToCarrera(rs);
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al buscar carrera por nombre: " + e.getMessage());
        }
        return null;
    }

    public void insertarCursoACarrera(Long carreraId, Long cursoId, Long cicloId) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CURSO_A_CARRERA)) {
            pstmt.setLong(1, carreraId);
            pstmt.setLong(2, cursoId);
            pstmt.setLong(3, cicloId);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new NoDataException("No se realizó la inserción");
            }
        } catch (SQLException e) {
            logger.error("Error al insertar curso en carrera: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar curso en carrera");
        }
    }

    public void eliminarCursoDeCarrera(Long carreraId, Long cursoId) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CURSO_DE_CARRERA)) {
            pstmt.setLong(1, carreraId);
            pstmt.setLong(2, cursoId);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: relación no encontrada");
            }
        } catch (SQLException e) {
            handleDeleteSQLException(e, "Error al eliminar curso de carrera");
        }
    }

    public void modificarOrdenCursoCarrera(Long carreraId, Long cursoId, Long nuevoCicloId) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_ORDEN_CURSO_CARRERA)) {
            pstmt.setLong(1, carreraId);
            pstmt.setLong(2, cursoId);
            pstmt.setLong(3, nuevoCicloId);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
        } catch (SQLException e) {
            logger.error("Error al modificar ciclo de curso en carrera: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar ciclo de curso en carrera");
        }
    }

    public boolean tieneCursosAsociados(Long idCarrera) throws GlobalException {
        try {
            carreraCursoService.buscarCursosPorCarreraYCiclo(idCarrera, null);
            return true;
        } catch (NoDataException e) {
            return false;
        }
    }

    public boolean tieneAlumnosAsociados(Long idCarrera) throws GlobalException {
        try {
            alumnoService.buscarAlumnosPorCarrera(idCarrera);
            return true;
        } catch (NoDataException e) {
            return false;
        }
    }

    public boolean tieneGruposAsociados(Long idCarrera, Long idCurso) throws GlobalException {
        try {
            grupoService.buscarGruposPorCarreraCurso(idCarrera, idCurso);
            return true;
        } catch (NoDataException e) {
            return false;
        }
    }

    private Carrera mapResultSetToCarrera(ResultSet rs) throws SQLException {
        return new Carrera(
                rs.getLong("id_carrera"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("titulo")
        );
    }

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage = switch (errorCode) {
            case -20026 -> "Ya existe una asociación de ese curso en esa carrera y ciclo.";
            case 1 -> "Código de carrera duplicado.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }

    private void handleDeleteSQLException(SQLException e, String message) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage = switch (errorCode) {
            case -20001 -> "No se puede eliminar la carrera: tiene cursos asociados.";
            case -20002 -> "No se puede eliminar la carrera: tiene alumnos inscritos.";
            case -20035 -> "No se puede eliminar la relación carrera-curso: tiene grupos asociados.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
