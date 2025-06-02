package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.AlumnoService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Alumno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con alumnos.
 * Proporciona endpoints para CRUD y búsqueda de alumnos por cédula, nombre y carrera.
 */
@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoController.class);
    private final AlumnoService alumnoService;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el servicio de alumnos.
     *
     * @param alumnoService El servicio de alumnos.
     */
    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    /**
     * Crea un nuevo alumno.
     *
     * @param alumno El objeto Alumno a crear.
     * @return ResponseEntity con el estado 201 Created.
     */
    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Alumno alumno) {
        logger.debug("Creando alumno con cédula: {}", alumno.getCedula());
        try {
            alumnoService.insertarAlumno(alumno);
            logger.info("Alumno creado exitosamente: cédula {}", alumno.getCedula());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al crear alumno: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un alumno existente.
     *
     * @param alumno El objeto Alumno con los datos actualizados.
     * @return ResponseEntity con el estado 200 OK.
     */
    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Alumno alumno) {
        logger.debug("Actualizando alumno con id: {}", alumno.getIdAlumno());
        try {
            alumnoService.modificarAlumno(alumno);
            logger.info("Alumno actualizado exitosamente: id {}", alumno.getIdAlumno());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al actualizar alumno: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina un alumno por su ID.
     *
     * @param id El ID del alumno a eliminar.
     * @return ResponseEntity con el estado 204 No Content.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando alumno con id: {}", id);
        try {
            alumnoService.eliminarAlumno(id);
            logger.info("Alumno eliminado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al eliminar alumno: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina un alumno por su cédula.
     *
     * @param cedula La cédula del alumno a eliminar.
     * @return ResponseEntity con el estado 204 No Content.
     */
    @DeleteMapping("/eliminarPorCedula")
    public ResponseEntity<Void> eliminarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Eliminando alumno con cédula: {}", cedula);
        try {
            alumnoService.eliminarAlumnoPorCedula(cedula);
            logger.info("Alumno eliminado exitosamente: cédula {}", cedula);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException e) {
            logger.error("Error al eliminar alumno por cédula: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lista todos los alumnos registrados.
     *
     * @return ResponseEntity con la lista de alumnos y estado 200 OK.
     */
    @GetMapping("/listar")
    public ResponseEntity<List<Alumno>> listar() {
        logger.debug("Listando todos los alumnos");
        try {
            List<Alumno> alumnos = alumnoService.listarAlumnos();
            logger.info("Listado de alumnos obtenido: total {}", alumnos.size());
            return new ResponseEntity<>(alumnos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al listar alumnos: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Busca un alumno por su cédula.
     *
     * @param cedula La cédula del alumno.
     * @return ResponseEntity con el alumno encontrado y estado 200 OK.
     */
    @GetMapping("/buscarPorCedula")
    public ResponseEntity<Alumno> buscarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Buscando alumno por cédula: {}", cedula);
        try {
            Alumno alumno = alumnoService.buscarAlumnoPorCedula(cedula);
            if (alumno == null) {
                logger.info("No se encontró alumno con cédula: {}", cedula);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Alumno encontrado: cédula {}", cedula);
            return new ResponseEntity<>(alumno, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar alumno por cédula: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca un alumno por su nombre.
     *
     * @param nombre El nombre del alumno.
     * @return ResponseEntity con el alumno encontrado y estado 200 OK.
     */
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<Alumno> buscarPorNombre(@RequestParam("nombre") String nombre) {
        logger.debug("Buscando alumno por nombre: {}", nombre);
        try {
            Alumno alumno = alumnoService.buscarAlumnoPorNombre(nombre);
            if (alumno == null) {
                logger.info("No se encontró alumno con nombre: {}", nombre);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Alumno encontrado: nombre {}", nombre);
            return new ResponseEntity<>(alumno, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar alumno por nombre: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca alumnos asociados a una carrera.
     *
     * @param idCarrera El ID de la carrera.
     * @return ResponseEntity con la lista de alumnos y estado 200 OK.
     */
    @GetMapping("/buscarPorCarrera")
    public ResponseEntity<List<Alumno>> buscarPorCarrera(@RequestParam("carrera") Long idCarrera) {
        logger.debug("Buscando alumnos por carrera: {}", idCarrera);
        try {
            List<Alumno> alumnos = alumnoService.buscarAlumnosPorCarrera(idCarrera);
            logger.info("Alumnos encontrados para carrera {}: {}", idCarrera, alumnos.size());
            return new ResponseEntity<>(alumnos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al buscar alumnos por carrera: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
