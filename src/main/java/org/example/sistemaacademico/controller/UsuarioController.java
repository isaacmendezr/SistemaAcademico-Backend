package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.UsuarioService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con usuarios.
 * Proporciona endpoints para CRUD y autenticación de usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el servicio de usuarios.
     *
     * @param usuarioService El servicio de usuarios.
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario El objeto Usuario a crear.
     * @return ResponseEntity con el estado 201 Created.
     */
    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Usuario usuario) {
        logger.debug("Creando usuario con cédula: {}", usuario.getCedula());
        try {
            usuarioService.insertar(usuario);
            logger.info("Usuario creado exitosamente: cédula {}", usuario.getCedula());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al crear usuario: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param usuario El objeto Usuario con los datos actualizados.
     * @return ResponseEntity con el estado 200 OK.
     */
    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Usuario usuario) {
        logger.debug("Actualizando usuario con id: {}", usuario.getIdUsuario());
        try {
            usuarioService.modificar(usuario);
            logger.info("Usuario actualizado exitosamente: id {}", usuario.getIdUsuario());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al actualizar usuario: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id El ID del usuario a eliminar.
     * @return ResponseEntity con el estado 204 No Content.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando usuario con id: {}", id);
        try {
            usuarioService.eliminar(id);
            logger.info("Usuario eliminado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lista todos los usuarios registrados.
     *
     * @return ResponseEntity con la lista de usuarios y estado 200 OK.
     */
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listar() {
        logger.debug("Listando todos los usuarios");
        try {
            List<Usuario> usuarios = usuarioService.listar();
            logger.info("Listado de usuarios obtenido: total {}", usuarios.size());
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al listar usuarios: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Busca un usuario por su cédula.
     *
     * @param cedula La cédula del usuario.
     * @return ResponseEntity con el usuario encontrado y estado 200 OK.
     */
    @GetMapping("/buscarPorCedula")
    public ResponseEntity<Usuario> buscarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Buscando usuario por cédula: {}", cedula);
        try {
            Usuario usuario = usuarioService.buscarPorCedula(cedula);
            if (usuario == null) {
                logger.info("No se encontró usuario con cédula: {}", cedula);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Usuario encontrado: cédula {}", cedula);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar usuario por cédula: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Autentica un usuario mediante su cédula y clave.
     *
     * @param cedula La cédula del usuario.
     * @param clave La clave del usuario.
     * @return ResponseEntity con el usuario autenticado y estado 200 OK.
     */
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam("cedula") String cedula, @RequestParam("clave") String clave) {
        logger.debug("Autenticando usuario con cédula: {}", cedula);
        try {
            Usuario usuario = usuarioService.login(cedula, clave);
            if (usuario == null) {
                logger.info("Credenciales inválidas para cédula: {}", cedula);
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            logger.info("Usuario autenticado exitosamente: cédula {}", cedula);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al autenticar usuario: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
