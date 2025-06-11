package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.AlumnoService;
import org.example.sistemaacademico.logic.Alumno;
import org.example.sistemaacademico.database.NoDataException;
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
        alumnoService.insertarAlumno(alumno);
        logger.info("Alumno creado exitosamente: cédula {}", alumno.getCedula());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Alumno alumno) {
        logger.debug("Actualizando alumno con id: {}", alumno.getIdAlumno());
        alumnoService.modificarAlumno(alumno);
        logger.info("Alumno actualizado exitosamente: id {}", alumno.getIdAlumno());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando alumno con id: {}", id);
        alumnoService.verificarEliminar(id); // Nueva verificación
        alumnoService.eliminarAlumno(id);
        logger.info("Alumno eliminado exitosamente: id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/eliminarPorCedula")
    public ResponseEntity<Void> eliminarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Eliminando alumno con cédula: {}", cedula);
        alumnoService.verificarEliminarPorCedula(cedula); // Nueva verificación
        alumnoService.eliminarAlumnoPorCedula(cedula);
        logger.info("Alumno eliminado exitosamente: cédula {}", cedula);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Alumno>> listar() {
        logger.debug("Listando todos los alumnos");
        List<Alumno> alumnos = alumnoService.listarAlumnos();
        logger.info("Listado de alumnos obtenido: total {}", alumnos.size());
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<Alumno> buscarPorId(@PathVariable("id") Long id) {
        logger.debug("Buscando alumno por id: {}", id);
        Alumno alumno = alumnoService.buscarAlumnoPorId(id);
        logger.info("Alumno encontrado: id {}", id);
        return new ResponseEntity<>(alumno, HttpStatus.OK);
    }

    @GetMapping("/buscarPorCedula")
    public ResponseEntity<Alumno> buscarPorCedula(@RequestParam("cedula") String cedula) {
        logger.debug("Buscando alumno por cédula: {}", cedula);
        Alumno alumno = alumnoService.buscarAlumnoPorCedula(cedula);
        if (alumno == null) {
            logger.info("No se encontró alumno con cédula: {}", cedula);
            throw new NoDataException("No se encontró alumno con cédula: " + cedula);
        }
        logger.info("Alumno encontrado: cédula {}", cedula);
        return new ResponseEntity<>(alumno, HttpStatus.OK);
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<Alumno> buscarPorNombre(@RequestParam("nombre") String nombre) {
        logger.debug("Buscando alumno por nombre: {}", nombre);
        Alumno alumno = alumnoService.buscarAlumnoPorNombre(nombre);
        if (alumno == null) {
            logger.info("No se encontró alumno con nombre: {}", nombre);
            throw new NoDataException("No se encontró alumno con nombre: " + nombre);
        }
        logger.info("Alumno encontrado: nombre {}", nombre);
        return new ResponseEntity<>(alumno, HttpStatus.OK);
    }

    @GetMapping("/buscarPorCarrera")
    public ResponseEntity<List<Alumno>> buscarPorCarrera(@RequestParam("carrera") Long idCarrera) {
        logger.debug("Buscando alumnos por carrera: {}", idCarrera);
        List<Alumno> alumnos = alumnoService.buscarAlumnosPorCarrera(idCarrera);
        logger.info("Alumnos encontrados para carrera {}: {}", idCarrera, alumnos.size());
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    @GetMapping("/alumnosConOfertaEnCiclo")
    public ResponseEntity<List<Alumno>> alumnosConOfertaEnCiclo(@RequestParam("idCiclo") Long idCiclo) {
        logger.debug("Buscando alumnos con oferta en ciclo: {}", idCiclo);
        List<Alumno> alumnos = alumnoService.alumnosConOfertaEnCiclo(idCiclo);
        logger.info("Alumnos con oferta encontrados para ciclo {}: {}", idCiclo, alumnos.size());
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }
}
