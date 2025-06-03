package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Carrera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarreraService {

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

    @Autowired
    public CarreraService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CARRERA)) {
            pstmt.setString(1, carrera.getCodigo());
            pstmt.setString(2, carrera.getNombre());
            pstmt.setString(3, carrera.getTitulo());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al insertar carrera: llave duplicada o sentencia inválida");
        }
    }

    public void modificarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_CARRERA)) {
            pstmt.setLong(1, carrera.getIdCarrera());
            pstmt.setString(2, carrera.getCodigo());
            pstmt.setString(3, carrera.getNombre());
            pstmt.setString(4, carrera.getTitulo());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al modificar carrera: sentencia no válida");
        }
    }

    public void eliminarCarrera(Long idCarrera) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CARRERA)) {
            pstmt.setLong(1, idCarrera);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: la carrera no existe");
            }
        } catch (SQLException e) {
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
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al insertar curso en carrera: datos inválidos o duplicados");
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
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al modificar ciclo de curso en carrera: datos inválidos");
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
        throw new GlobalException(message + ": " + e.getMessage());
    }

    private void handleDeleteSQLException(SQLException e, String message) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage = switch (errorCode) {
            case -20001 -> "No se puede eliminar la carrera: tiene cursos asociados.";
            case -20002 -> "No se puede eliminar la carrera: tiene alumnos inscritos.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
