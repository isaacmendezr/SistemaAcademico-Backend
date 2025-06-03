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

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoController.class);
    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Alumno alumno) {
        logger.debug("Creando alumno con cédula: {}", alumno.getCedula());
        try {
            alumnoService.insertarAlumno(alumno);
            logger.info("Alumno creado exitosamente: cédula {}", alumno.getCedula());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException e) {
            logger.error("Error al crear alumno: {}", e.getMessage());
            if (e.getMessage().contains("nombre") || e.getMessage().contains("correo") ||
                    e.getMessage().contains("fecha") || e.getMessage().contains("cédula") ||
                    e.getMessage().contains("teléfono") || e.getMessage().contains("duplicada")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Alumno alumno) {
        logger.debug("Actualizando alumno con id: {}", alumno.getIdAlumno());
        try {
            alumnoService.modificarAlumno(alumno);
            logger.info("Alumno actualizado exitosamente: id {}", alumno.getIdAlumno());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al actualizar alumno: {}", e.getMessage());
            if (e.getMessage().contains("nombre") || e.getMessage().contains("correo") ||
                    e.getMessage().contains("fecha") || e.getMessage().contains("cédula") ||
                    e.getMessage().contains("teléfono") || e.getMessage().contains("duplicada")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoDataException e) {
            logger.error("Error al actualizar alumno: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando alumno con id: {}", id);
        try {
            // Verificar dependencias proactivamente
            Alumno alumno = alumnoService.buscarAlumnoPorId(id);
            if (alumnoService.tieneUsuarioAsociado(alumno.getCedula())) {
                logger.warn("No se puede eliminar alumno con id {}: tiene usuario asociado", id);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (alumnoService.tieneMatriculasAsociadas(id)) {
                logger.warn("No se puede eliminar alumno con id {}: tiene matrículas asociadas", id);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            alumnoService.eliminarAlumno(id);
            logger.info("Alumno eliminado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException e) {
            logger.error("Error al eliminar alumno: {}", e.getMessage());
            if (e.getMessage().contains("usuario asociado") || e.getMessage().contains("matrículas asociadas")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoDataException e) {
            logger.error("Error al eliminar alumno: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminarPorCedula")
    public ResponseEntity<Void> eliminarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Eliminando alumno con cédula: {}", cedula);
        try {
            // Verificar dependencias proactivamente
            Alumno alumno = alumnoService.buscarAlumnoPorCedula(cedula);
            if (alumno == null) {
                logger.warn("No se encontró alumno con cédula: {}", cedula);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (alumnoService.tieneUsuarioAsociado(cedula)) {
                logger.warn("No se puede eliminar alumno con cédula {}: tiene usuario asociado", cedula);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (alumnoService.tieneMatriculasAsociadas(alumno.getIdAlumno())) {
                logger.warn("No se puede eliminar alumno con cédula {}: tiene matrículas asociadas", cedula);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            alumnoService.eliminarAlumnoPorCedula(cedula);
            logger.info("Alumno eliminado exitosamente: cédula {}", cedula);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException e) {
            logger.error("Error al eliminar alumno por cédula: {}", e.getMessage());
            if (e.getMessage().contains("usuario asociado") || e.getMessage().contains("matrículas asociadas")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoDataException e) {
            logger.error("Error al eliminar alumno por cédula: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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
            logger.error("Error al buscar alumno por cédula: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
