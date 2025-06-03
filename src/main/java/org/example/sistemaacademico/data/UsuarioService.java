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

    // Consultas SQL para procedimientos almacenados
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
        logger.debug("Insertando usuario: cedula {}, tipo {}", usuario.getCedula(), usuario.getTipo());
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(INSERTAR_USUARIO)) {
            pstmt.setString(1, usuario.getCedula());
            pstmt.setString(2, usuario.getClave());
            pstmt.setString(3, usuario.getTipo());
            pstmt.executeUpdate();
            logger.info("Usuario insertado exitosamente: cedula {}", usuario.getCedula());
        } catch (SQLException e) {
            logger.error("Error al insertar usuario: {}", e.getMessage(), e);
            handleSQLException(e, "Error al insertar usuario");
        }
    }

    public void modificar(Usuario usuario) throws GlobalException, NoDataException {
        logger.debug("Modificando usuario: id {}, cedula {}, tipo {}",
                usuario.getIdUsuario(), usuario.getCedula(), usuario.getTipo());
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
            logger.info("Usuario modificado exitosamente: id {}", usuario.getIdUsuario());
        } catch (SQLException e) {
            logger.error("Error al modificar usuario: {}", e.getMessage(), e);
            handleSQLException(e, "Error al modificar usuario");
        }
    }

    public void eliminar(Long idUsuario) throws GlobalException, NoDataException {
        logger.debug("Eliminando usuario: id {}", idUsuario);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(ELIMINAR_USUARIO)) {
            pstmt.setLong(1, idUsuario);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado: el usuario no existe");
            }
            logger.info("Usuario eliminado exitosamente: id {}", idUsuario);
        } catch (SQLException e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage(), e);
            handleSQLException(e, "Error al eliminar usuario");
        }
    }

    public List<Usuario> listar() throws GlobalException, NoDataException {
        logger.debug("Listando todos los usuarios");
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
            logger.error("Error al listar usuarios: {}", e.getMessage(), e);
            handleSQLException(e, "Error al listar usuarios");
        }
        if (usuarios.isEmpty()) {
            throw new NoDataException("No hay usuarios registrados");
        }
        logger.info("Listado de usuarios obtenido exitosamente, total: {}", usuarios.size());
        return usuarios;
    }

    public Usuario buscarPorCedula(String cedula) throws GlobalException, NoDataException {
        logger.debug("Buscando usuario por cédula: {}", cedula);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(BUSCAR_POR_CEDULA)) {
            pstmt.registerOutParameter(1, Types.REF_CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(1)) {
                if (rs.next()) {
                    Usuario usuario = mapResultSetToUsuario(rs);
                    logger.info("Usuario encontrado por cédula: {}", cedula);
                    return usuario;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar usuario por cédula: {}", e.getMessage(), e);
            handleSQLException(e, "Error al buscar usuario por cédula");
        }
        throw new NoDataException("No se encontró un usuario con cédula: " + cedula);
    }

    public Usuario login(String cedula, String clave) throws GlobalException, NoDataException {
        logger.debug("Autenticando usuario con cédula: {}", cedula);
        try (Connection conn = dataSource.getConnection();
             CallableStatement pstmt = conn.prepareCall(LOGIN_USUARIO)) {
            pstmt.setString(1, cedula);
            pstmt.setString(2, clave);
            pstmt.registerOutParameter(3, Types.REF_CURSOR);
            pstmt.execute();
            try (ResultSet rs = (ResultSet) pstmt.getObject(3)) {
                if (rs.next()) {
                    Usuario usuario = mapResultSetToUsuario(rs);
                    logger.info("Usuario autenticado exitosamente: cedula {}", cedula);
                    return usuario;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al autenticar usuario: {}", e.getMessage(), e);
            handleSQLException(e, "Error al autenticar usuario");
        }
        throw new NoDataException("Credenciales inválidas o usuario no encontrado");
    }

    // Métodos utilitarios privados
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id_usuario"),
                rs.getString("cedula"),
                rs.getString("tipo")
        );
    }

    private void handleSQLException(SQLException e, String message) throws GlobalException {
        throw new GlobalException(message + ": " + e.getMessage());
    }
}
