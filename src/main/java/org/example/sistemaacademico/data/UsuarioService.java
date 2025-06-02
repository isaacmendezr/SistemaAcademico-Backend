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

/**
 * Servicio para gestionar operaciones relacionadas con Usuarios en la base de datos.
 * Implementa operaciones CRUD, búsquedas y autenticación, asegurando manejo adecuado de excepciones y cierre de recursos.
 */
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

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el DataSource.
     *
     * @param dataSource El DataSource gestionado por Spring Boot.
     */
    @Autowired
    public UsuarioService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuario El objeto Usuario a insertar.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos, como una cédula duplicada o sentencia inválida.
     * @throws NoDataException Si la inserción no se realiza.
     */
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

    /**
     * Modifica un usuario existente en la base de datos.
     *
     * @param usuario El objeto Usuario con los datos actualizados.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos, como una sentencia inválida.
     * @throws NoDataException Si la actualización no se realiza.
     */
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

    /**
     * Elimina un usuario por su ID.
     *
     * @param idUsuario El ID del usuario a eliminar.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si el usuario no existe o no se elimina.
     */
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

    /**
     * Lista todos los usuarios registrados.
     *
     * @return Lista de objetos Usuario.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no hay usuarios registrados.
     */
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

    /**
     * Busca un usuario por su cédula.
     *
     * @param cedula La cédula del usuario.
     * @return El objeto Usuario encontrado.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si no se encuentra un usuario con la cédula especificada.
     */
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

    /**
     * Autentica un usuario mediante su cédula y clave.
     *
     * @param cedula La cédula del usuario.
     * @param clave La clave del usuario.
     * @return El objeto Usuario autenticado.
     * @throws GlobalException Si ocurre un error relacionado con la base de datos.
     * @throws NoDataException Si las credenciales son inválidas o el usuario no se encuentra.
     */
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

    /**
     * Mapea un ResultSet a un objeto Usuario.
     *
     * @param rs El ResultSet con los datos del usuario.
     * @return Un objeto Usuario mapeado.
     * @throws SQLException Si ocurre un error al leer los datos.
     */
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id_usuario"),
                rs.getString("cedula"),
                rs.getString("tipo")
        );
    }

    /**
     * Maneja excepciones SQL genéricas y lanza GlobalException con un mensaje específico.
     * Usado para operaciones como insertar, modificar, eliminar, listar, buscar y autenticar.
     *
     * @param e       La excepción SQL capturada.
     * @param message El mensaje base para la excepción, que describe la operación fallida.
     * @throws GlobalException Si ocurre un error en la base de datos, como una sentencia SQL inválida.
     */
    private void handleSQLException(SQLException e, String message) throws GlobalException {
        throw new GlobalException(message + ": " + e.getMessage());
    }
}
