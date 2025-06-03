package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.ProfesorService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Profesor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con profesores.
 * Proporciona endpoints para CRUD y búsquedas de profesores.
 */
@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    private static final Logger logger = LoggerFactory.getLogger(ProfesorController.class);
    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Profesor profesor) {
        logger.debug("Creando profesor con cédula: {}", profesor.getCedula());
        try {
            profesorService.insertar(profesor);
            logger.info("Profesor creado exitosamente: cédula {}", profesor.getCedula());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al crear profesor: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Profesor profesor) {
        logger.debug("Actualizando profesor con id: {}", profesor.getIdProfesor());
        try {
            profesorService.modificar(profesor);
            logger.info("Profesor actualizado exitosamente: id {}", profesor.getIdProfesor());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al actualizar profesor: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando profesor con id: {}", id);
        try {
            profesorService.eliminar(id);
            logger.info("Profesor eliminado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al eliminar profesor: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminarPorCedula")
    public ResponseEntity<Void> eliminarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Eliminando profesor por cédula: {}", cedula);
        try {
            profesorService.eliminarPorCedula(cedula);
            logger.info("Profesor eliminado exitosamente por cédula: {}", cedula);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al eliminar profesor por cédula: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Profesor>> listar() {
        logger.debug("Listando todos los profesores");
        try {
            List<Profesor> profesores = profesorService.listar();
            logger.info("Listado de profesores obtenido: total {}", profesores.size());
            return new ResponseEntity<>(profesores, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al listar profesores: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscarPorCedula")
    public ResponseEntity<Profesor> buscarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Buscando profesor por cédula: {}", cedula);
        try {
            Profesor profesor = profesorService.buscarPorCedula(cedula);
            if (profesor == null) {
                logger.info("No se encontró profesor con cédula: {}", cedula);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Profesor encontrado: cédula {}", cedula);
            return new ResponseEntity<>(profesor, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar profesor por cédula: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<Profesor> buscarPorNombre(@RequestParam("nombre") String nombre) {
        logger.debug("Buscando profesor por nombre: {}", nombre);
        try {
            Profesor profesor = profesorService.buscarPorNombre(nombre);
            if (profesor == null) {
                logger.info("No se encontró profesor con nombre: {}", nombre);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Profesor encontrado: nombre {}", nombre);
            return new ResponseEntity<>(profesor, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar profesor por nombre: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
