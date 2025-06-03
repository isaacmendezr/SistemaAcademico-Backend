package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private static final String INSERTAR_USUARIO = "{call insertarUsuario(?,?,?)}";
    private static final String MODIFICAR_USUARIO = "{call modificarUsuario(?,?,?,?)}";
    private static final String ELIMINAR_USUARIO = "{call eliminarUsuario(?)}";
    private static final String ELIMINAR_ALUMNO_POR_CEDULA = "{call eliminarAlumnoPorCedula(?)}";
    private static final String ELIMINAR_PROFESOR_POR_CEDULA = "{call eliminarProfesorPorCedula(?)}";
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

    /*public void eliminar(Long idUsuario) throws GlobalException, NoDataException {
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
    }*/

    public void eliminarUsuarioYEntidadAsociada(Long idUsuario) throws GlobalException, NoDataException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement("SELECT id_usuario, cedula, tipo FROM Usuario WHERE id_usuario = ?")) {
                pstmt.setLong(1, idUsuario);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new NoDataException("Usuario no encontrado con id: " + idUsuario);
                    }
                    String cedula = rs.getString("cedula");
                    String tipo = rs.getString("tipo");

                    if ("Alumno".equals(tipo)) {
                        try (PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM Matricula WHERE pk_alumno = (SELECT id_alumno FROM Alumno WHERE cedula = ?)")) {
                            checkStmt.setString(1, cedula);
                            try (ResultSet checkRs = checkStmt.executeQuery()) {
                                if (checkRs.next() && checkRs.getInt(1) > 0) {
                                    throw new GlobalException("No se puede eliminar: el alumno tiene matrículas asociadas.");
                                }
                            }
                        }
                    } else if ("Profesor".equals(tipo)) {
                        try (PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM Grupo WHERE pk_profesor = (SELECT id_profesor FROM Profesor WHERE cedula = ?)")) {
                            checkStmt.setString(1, cedula);
                            try (ResultSet checkRs = checkStmt.executeQuery()) {
                                if (checkRs.next() && checkRs.getInt(1) > 0) {
                                    throw new GlobalException("No se puede eliminar: el profesor tiene grupos asignados.");
                                }
                            }
                        }
                    }

                    try (CallableStatement deleteUserStmt = conn.prepareCall(ELIMINAR_USUARIO)) {
                        deleteUserStmt.setLong(1, idUsuario);
                        int rowsAffected = deleteUserStmt.executeUpdate();
                        if (rowsAffected == 0) {
                            throw new NoDataException("No se realizó el borrado: el usuario no existe");
                        }
                    }

                    if ("Alumno".equals(tipo)) {
                        try (CallableStatement deleteAlumnoStmt = conn.prepareCall(ELIMINAR_ALUMNO_POR_CEDULA)) {
                            deleteAlumnoStmt.setString(1, cedula);
                            deleteAlumnoStmt.executeUpdate();
                        }
                    } else if ("Profesor".equals(tipo)) {
                        try (CallableStatement deleteProfesorStmt = conn.prepareCall(ELIMINAR_PROFESOR_POR_CEDULA)) {
                            deleteProfesorStmt.setString(1, cedula);
                            deleteProfesorStmt.executeUpdate();
                        }
                    }

                    conn.commit();
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    logger.error("Error during rollback: {}", rollbackEx.getMessage(), rollbackEx);
                }
            }
            handleSQLException(e, "Error al eliminar usuario y entidad asociada");
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    logger.error("Error closing connection: {}", closeEx.getMessage(), closeEx);
                }
            }
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
            case 20011 -> "No se puede eliminar el alumno: tiene matrículas asociadas.";
            case 20030 -> "No se puede eliminar el profesor: tiene grupos asignados.";
            default -> message + ": " + e.getMessage();
        };
        throw new GlobalException(errorMessage);
    }
}
