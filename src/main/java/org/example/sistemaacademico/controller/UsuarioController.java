package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.UsuarioService;
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
        usuarioService.insertar(usuario);
        logger.info("Usuario creado exitosamente: cédula {}", usuario.getCedula());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Usuario usuario) {
        logger.debug("Actualizando usuario con id: {}", usuario.getIdUsuario());
        usuarioService.modificar(usuario);
        logger.info("Usuario actualizado exitosamente: id {}", usuario.getIdUsuario());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando usuario y entidad asociada con id: {}", id);
        usuarioService.eliminarUsuarioYEntidadAsociada(id);
        logger.info("Usuario y entidad asociada eliminados exitosamente: id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listar() {
        logger.debug("Listando todos los usuarios");
        List<Usuario> usuarios = usuarioService.listar();
        logger.info("Listado de usuarios obtenido: total {}", usuarios.size());
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/buscarPorCedula")
    public ResponseEntity<Usuario> buscarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Buscando usuario por cédula: {}", cedula);
        Usuario usuario = usuarioService.buscarPorCedula(cedula);
        if (usuario == null) {
            logger.info("No se encontró usuario con cédula: {}", cedula);
            throw new org.example.sistemaacademico.database.NoDataException("No se encontró usuario con cédula: " + cedula);
        }
        logger.info("Usuario encontrado: cédula {}", cedula);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam("cedula") String cedula, @RequestParam("clave") String clave) {
        logger.debug("Autenticando usuario con cédula: {}", cedula);
        Usuario usuario = usuarioService.login(cedula, clave);
        logger.info("Usuario autenticado exitosamente: cédula {}", cedula);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}
