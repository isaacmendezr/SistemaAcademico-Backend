package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.ProfesorService;
import org.example.sistemaacademico.logic.Profesor;
import org.example.sistemaacademico.database.NoDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        profesorService.insertar(profesor);
        logger.info("Profesor creado exitosamente: cédula {}", profesor.getCedula());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Profesor profesor) {
        logger.debug("Actualizando profesor con id: {}", profesor.getIdProfesor());
        profesorService.modificar(profesor);
        logger.info("Profesor actualizado exitosamente: id {}", profesor.getIdProfesor());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando profesor con id: {}", id);
        profesorService.verificarEliminar(id); // Verificación proactiva
        profesorService.eliminar(id);
        logger.info("Profesor eliminado exitosamente: id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/eliminarPorCedula")
    public ResponseEntity<Void> eliminarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Eliminando profesor por cédula: {}", cedula);
        profesorService.verificarEliminarPorCedula(cedula); // Verificación proactiva
        profesorService.eliminarPorCedula(cedula);
        logger.info("Profesor eliminado exitosamente por cédula: {}", cedula);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Profesor>> listar() {
        logger.debug("Listando todos los profesores");
        List<Profesor> profesores = profesorService.listar();
        logger.info("Listado de profesores obtenido: total {}", profesores.size());
        return new ResponseEntity<>(profesores, HttpStatus.OK);
    }

    @GetMapping("/buscarPorCedula")
    public ResponseEntity<Profesor> buscarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Buscando profesor por cédula: {}", cedula);
        Profesor profesor = profesorService.buscarPorCedula(cedula);
        if (profesor == null) {
            logger.info("No se encontró profesor con cédula: {}", cedula);
            throw new NoDataException("No se encontró profesor con cédula: " + cedula);
        }
        logger.info("Profesor encontrado: cédula {}", cedula);
        return new ResponseEntity<>(profesor, HttpStatus.OK);
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<Profesor> buscarPorNombre(@RequestParam("nombre") String nombre) {
        logger.debug("Buscando profesor por nombre: {}", nombre);
        Profesor profesor = profesorService.buscarPorNombre(nombre);
        if (profesor == null) {
            logger.info("No se encontró profesor con nombre: {}", nombre);
            throw new NoDataException("No se encontró profesor con nombre: " + nombre);
        }
        logger.info("Profesor encontrado: nombre {}", nombre);
        return new ResponseEntity<>(profesor, HttpStatus.OK);
    }
}
