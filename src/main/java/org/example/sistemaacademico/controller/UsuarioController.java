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

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Usuario usuario) {
        logger.debug("Creando usuario con cédula: {}", usuario.getCedula());
        try {
            usuarioService.insertar(usuario);
            logger.info("Usuario creado exitosamente: cédula {}", usuario.getCedula());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Usuario usuario) {
        logger.debug("Actualizando usuario con id: {}", usuario.getIdUsuario());
        try {
            usuarioService.modificar(usuario);
            logger.info("Usuario actualizado exitosamente: id {}", usuario.getIdUsuario());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al actualizar usuario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.error("Error al actualizar usuario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando usuario con id: {}", id);
        try {
            usuarioService.verificarEliminar(id); // Verificación proactiva
            usuarioService.eliminar(id);
            logger.info("Usuario eliminado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam("cedula") String cedula, @RequestParam("clave") String clave) {
        logger.debug("Autenticando usuario con cédula: {}", cedula);
        try {
            Usuario usuario = usuarioService.login(cedula, clave);
            logger.info("Usuario autenticado exitosamente: cédula {}", cedula);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al autenticar usuario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.info("Credenciales inválidas para cédula: {}", cedula);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
