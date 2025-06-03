package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private static final String INSERTAR_USUARIO = "{call insertarUsuario(?,?,?)}";
    private static final String MODIFICAR_USUARIO = "{call modificarUsuario(?,?,?,?)}";
    private static final String ELIMINAR_USUARIO = "{call eliminarUsuario(?)}";
    private static final String LISTAR_USUARIOS = "{?=call listarUsuarios()}";
    private static final String BUSCAR_POR_CEDULA = "{?=call buscarUsuarioPorCedula(?)}";
    private static final String LOGIN_USUARIO = "{call loginUsuario(?,?,?)}";

    private final DataSource dataSource;

    @Autowired
    public UsuarioService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertar(Usuario usuario) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_USUARIO)) {
            pstmt.setString(1, usuario.getCedula());
            pstmt.setString(2, usuario.getClave());
            pstmt.setString(3, usuario.getTipo());
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó la inserción del usuario");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al insertar usuario");
        }
    }

    public void modificar(Usuario usuario) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(MODIFICAR_USUARIO)) {
            pstmt.setLong(1, usuario.getIdUsuario());
            pstmt.setString(2, usuario.getCedula());
            pstmt.setString(3, usuario.getClave());
            pstmt.setString(4, usuario.getTipo());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó la actualización del usuario");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al modificar usuario");
        }
    }

    public void eliminar(Long idUsuario) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_USUARIO)) {
            pstmt.setLong(1, idUsuario);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new NoDataException("No se realizó el borrado: el usuario no existe");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al eliminar usuario");
        }
    }

    public List<Usuario> listar() throws GlobalException, NoDataException {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LISTAR_USUARIOS)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                while (rs.next()) {
                    usuarios.add(mapResultSetToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al listar usuarios");
        }
        if (usuarios.isEmpty()) {
            throw new NoDataException("No hay usuarios registrados");
        }
        return usuarios;
    }

    public Usuario buscarPorCedula(String cedula) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_CEDULA)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al buscar usuario por cédula");
        }
        throw new NoDataException("No se encontró un usuario con cédula: " + cedula);
    }

    public Usuario login(String cedula, String clave) throws GlobalException, NoDataException {
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LOGIN_USUARIO)) {
            pstmt.setString(1, cedula);
            pstmt.setString(2, clave);
            pstmt.registerOutParameter(3, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(3)) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error al autenticar usuario");
        }
        throw new NoDataException("Credenciales inválidas o usuario no encontrado");
    }

    // ========= Métodos utilitarios =========

    public void verificarEliminar(Long idUsuario) throws GlobalException, NoDataException {
        // Verificación proactiva: consultar si el usuario existe
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM Usuario WHERE id_usuario = ?")) {
            pstmt.setLong(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new NoDataException("No se encontró usuario con id: " + idUsuario);
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al verificar usuario: " + e.getMessage());
        }
    }

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id_usuario"),
                rs.getString("cedula"),
                rs.getString("tipo")
        );
    }

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        int errorCode = Math.abs(e.getErrorCode());
        String errorMessage = switch (errorCode) {
            case 20028 -> "Ya existe un usuario con esta cédula.";
            case 20033 -> "No existe un alumno con esta cédula para asignar como usuario.";
            case 20034 -> "No existe un profesor con esta cédula para asignar como usuario.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
