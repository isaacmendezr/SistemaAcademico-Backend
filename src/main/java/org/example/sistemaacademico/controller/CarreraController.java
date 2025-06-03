package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CarreraService;
import org.example.sistemaacademico.logic.Carrera;
import org.example.sistemaacademico.database.NoDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    private static final Logger logger = LoggerFactory.getLogger(CarreraController.class);
    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Carrera carrera) {
        logger.debug("Creando carrera con código: {}", carrera.getCodigo());
        carreraService.insertarCarrera(carrera);
        logger.info("Carrera creada exitosamente: código {}", carrera.getCodigo());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Carrera carrera) {
        logger.debug("Actualizando carrera con id: {}", carrera.getIdCarrera());
        carreraService.modificarCarrera(carrera);
        logger.info("Carrera actualizada exitosamente: id {}", carrera.getIdCarrera());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando carrera con id: {}", id);
        if (carreraService.tieneCursosAsociados(id)) {
            logger.warn("No se puede eliminar carrera con id {}: tiene cursos asociados", id);
            throw new RuntimeException("No se puede eliminar carrera: tiene cursos asociados.");
        }
        if (carreraService.tieneAlumnosAsociados(id)) {
            logger.warn("No se puede eliminar carrera con id {}: tiene alumnos asociados", id);
            throw new RuntimeException("No se puede eliminar carrera: tiene alumnos inscritos.");
        }
        carreraService.eliminarCarrera(id);
        logger.info("Carrera eliminada exitosamente: id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Carrera>> listar() {
        logger.debug("Listando todas las carreras");
        List<Carrera> carreras = carreraService.listarCarreras();
        logger.info("Listado de carreras obtenido: total {}", carreras.size());
        return new ResponseEntity<>(carreras, HttpStatus.OK);
    }

    @GetMapping("/buscarPorCodigo")
    public ResponseEntity<Carrera> buscarPorCodigo(@RequestParam("codigo") String codigo) {
        logger.debug("Buscando carrera por código: {}", codigo);
        Carrera carrera = carreraService.buscarCarreraPorCodigo(codigo);
        if (carrera == null) {
            logger.info("No se encontró carrera con código: {}", codigo);
            throw new NoDataException("No se encontró carrera con código: " + codigo);
        }
        logger.info("Carrera encontrada: código {}", codigo);
        return new ResponseEntity<>(carrera, HttpStatus.OK);
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<Carrera> buscarPorNombre(@RequestParam("nombre") String nombre) {
        logger.debug("Buscando carrera por nombre: {}", nombre);
        Carrera carrera = carreraService.buscarCarreraPorNombre(nombre);
        if (carrera == null) {
            logger.info("No se encontró carrera con nombre: {}", nombre);
            throw new NoDataException("No se encontró carrera con nombre: " + nombre);
        }
        logger.info("Carrera encontrada: nombre {}", nombre);
        return new ResponseEntity<>(carrera, HttpStatus.OK);
    }

    @PostMapping("/insertarCursoACarrera/{idCarrera}/{idCurso}/{idCiclo}")
    public ResponseEntity<Void> insertarCursoACarrera(
            @PathVariable("idCarrera") Long idCarrera,
            @PathVariable("idCurso") Long idCurso,
            @PathVariable("idCiclo") Long idCiclo) {
        logger.debug("Insertando curso {} en carrera {} con ciclo: {}", idCurso, idCarrera, idCiclo);
        carreraService.insertarCursoACarrera(idCarrera, idCurso, idCiclo);
        logger.info("Curso {} insertado en carrera {} con ciclo: {}", idCurso, idCarrera, idCiclo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminarCursoDeCarrera/{idCarrera}/{idCurso}")
    public ResponseEntity<Void> eliminarCursoDeCarrera(
            @PathVariable("idCarrera") Long idCarrera,
            @PathVariable("idCurso") Long idCurso) {
        logger.debug("Eliminando curso {} de carrera: {}", idCurso, idCarrera);
        if (carreraService.tieneGruposAsociados(idCarrera, idCurso)) {
            logger.warn("No se puede eliminar curso {} de carrera {}: tiene grupos asociados", idCurso, idCarrera);
            throw new RuntimeException("No se puede eliminar: el curso tiene grupos asociados.");
        }
        carreraService.eliminarCursoDeCarrera(idCarrera, idCurso);
        logger.info("Curso {} eliminado de carrera: {}", idCurso, idCarrera);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/modificarOrdenCursoCarrera/{idCarrera}/{idCurso}/{nuevoIdCiclo}")
    public ResponseEntity<Void> modificarOrdenCursoCarrera(
            @PathVariable("idCarrera") Long idCarrera,
            @PathVariable("idCurso") Long idCurso,
            @PathVariable("nuevoIdCiclo") Long nuevoIdCiclo) {
        logger.debug("Modificando ciclo de curso {} en carrera {}: nuevo ciclo {}", idCurso, idCarrera, nuevoIdCiclo);
        carreraService.modificarOrdenCursoCarrera(idCarrera, idCurso, nuevoIdCiclo);
        logger.info("Ciclo de curso {} en carrera {} modificado: nuevo ciclo {}", idCurso, idCarrera, nuevoIdCiclo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
