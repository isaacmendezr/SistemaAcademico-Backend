package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Ciclo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CicloService {

    private static final String INSERTAR_CICLO = "{call insertarCiclo(?,?,?,?,?)}";
    private static final String MODIFICAR_CICLO = "{call modificarCiclo(?,?,?,?,?,?)}";
    private static final String ELIMINAR_CICLO = "{call eliminarCiclo(?)}";
    private static final String LISTAR_CICLOS = "{?=call listarCiclos()}";
    private static final String BUSCAR_POR_ANNIO = "{?=call buscarCicloPorAnnio(?)}";
    private static final String ACTIVAR_CICLO = "{call activarCiclo(?)}";
    private static final String BUSCAR_POR_ID = "{?=call buscarCicloPorId(?)}";

    private final DataSource dataSource;

    @Autowired
    public CicloService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertarCiclo(Ciclo ciclo) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_CICLO)) {
            pstmt.setLong(1, ciclo.getAnio());
            pstmt.setLong(2, ciclo.getNumero());
            pstmt.setDate(3, Date.valueOf(ciclo.getFechaInicio()));
            pstmt.setDate(4, Date.valueOf(ciclo.getFechaFin()));
            pstmt.setString(5, ciclo.getEstado());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al insertar ciclo: llave duplicada o sentencia inválida: " + e.getMessage());
        }
    }

    public void modificarCiclo(Ciclo ciclo) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_CICLO)) {
            pstmt.setLong(1, ciclo.getIdCiclo());
            pstmt.setLong(2, ciclo.getAnio());
            pstmt.setLong(3, ciclo.getNumero());
            pstmt.setDate(4, Date.valueOf(ciclo.getFechaInicio()));
            pstmt.setDate(5, Date.valueOf(ciclo.getFechaFin()));
            pstmt.setString(6, ciclo.getEstado());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al modificar ciclo: sentencia inválida: " + e.getMessage());
        }
    }

    public void eliminarCiclo(Long idCiclo) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_CICLO)) {
            pstmt.setLong(1, idCiclo);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: el ciclo no existe");
            }
        } catch (SQLException e) {
            handleDeleteSQLException(e);
        }
    }

    public List<Ciclo> listarCiclos() throws GlobalException, NoDataException {
        List<Ciclo> ciclos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_CICLOS)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    ciclos.add(mapResultSetToCiclo(rs));
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al listar ciclos: " + e.getMessage());
        }
        if (ciclos.isEmpty()) {
            throw new NoDataException("No hay ciclos registrados");
        }
        return ciclos;
    }

    public Ciclo buscarCicloPorAnio(Long anio) throws GlobalException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_ANNIO)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, anio);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToCiclo(rs);
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al buscar ciclo por año: " + e.getMessage());
        }
        return null;
    }

    public Ciclo buscarCicloPorId(Long id) throws GlobalException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_ID)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setLong(2, id);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToCiclo(rs);
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al buscar ciclo por ID: " + e.getMessage());
        }
        return null;
    }

    public void activarCiclo(Long idCiclo) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ACTIVAR_CICLO)) {
            pstmt.setLong(1, idCiclo);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la activación: el ciclo no existe");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al activar ciclo: sentencia inválida: " + e.getMessage());
        }
    }

    // Métodos utilitarios
    private Ciclo mapResultSetToCiclo(ResultSet rs) throws SQLException {
        return new Ciclo(
                rs.getLong("id_ciclo"),
                rs.getLong("anio"),
                rs.getLong("numero"),
                rs.getDate("fecha_inicio").toLocalDate(),
                rs.getDate("fecha_fin").toLocalDate(),
                rs.getString("estado")
        );
    }

    private void handleDeleteSQLException(SQLException e) throws GlobalException {
        int errorCode = e.getErrorCode();
        String errorMessage = switch (errorCode) {
            case -20005 -> "No se puede eliminar el ciclo: tiene cursos asociados.";
            case -20006 -> "No se puede eliminar el ciclo: tiene matrículas asociadas.";
            default -> "Error al eliminar ciclo: " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
