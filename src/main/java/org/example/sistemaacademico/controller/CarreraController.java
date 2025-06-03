package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CarreraService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Carrera;
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
        try {
            carreraService.insertarCarrera(carrera);
            logger.info("Carrera creada exitosamente: código {}", carrera.getCodigo());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al crear carrera: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Carrera carrera) {
        logger.debug("Actualizando carrera con id: {}", carrera.getIdCarrera());
        try {
            carreraService.modificarCarrera(carrera);
            logger.info("Carrera actualizada exitosamente: id {}", carrera.getIdCarrera());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al actualizar carrera: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando carrera con id: {}", id);
        try {
            carreraService.eliminarCarrera(id);
            logger.info("Carrera eliminada exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al eliminar carrera: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Carrera>> listar() {
        logger.debug("Listando todas las carreras");
        try {
            List<Carrera> carreras = carreraService.listarCarreras();
            logger.info("Listado de carreras obtenido: total {}", carreras.size());
            return new ResponseEntity<>(carreras, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al listar carreras: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscarPorCodigo")
    public ResponseEntity<Carrera> buscarPorCodigo(@RequestParam("codigo") String codigo) {
        logger.debug("Buscando carrera por código: {}", codigo);
        try {
            Carrera carrera = carreraService.buscarCarreraPorCodigo(codigo);
            if (carrera == null) {
                logger.info("No se encontró carrera con código: {}", codigo);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Carrera encontrada: código {}", codigo);
            return new ResponseEntity<>(carrera, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar carrera por código: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<Carrera> buscarPorNombre(@RequestParam("nombre") String nombre) {
        logger.debug("Buscando carrera por nombre: {}", nombre);
        try {
            Carrera carrera = carreraService.buscarCarreraPorNombre(nombre);
            if (carrera == null) {
                logger.info("No se encontró carrera con nombre: {}", nombre);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Carrera encontrada: nombre {}", nombre);
            return new ResponseEntity<>(carrera, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar carrera por nombre: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/insertarCursoACarrera/{idCarrera}/{idCurso}/{idCiclo}")
    public ResponseEntity<Void> insertarCursoACarrera(
            @PathVariable("idCarrera") Long idCarrera,
            @PathVariable("idCurso") Long idCurso,
            @PathVariable("idCiclo") Long idCiclo) {
        logger.debug("Insertando curso {} en carrera {} con ciclo: {}", idCurso, idCarrera, idCiclo);
        try {
            carreraService.insertarCursoACarrera(idCarrera, idCurso, idCiclo);
            logger.info("Curso {} insertado en carrera {} con ciclo: {}", idCurso, idCarrera, idCiclo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al insertar curso en carrera: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminarCursoDeCarrera/{idCarrera}/{idCurso}")
    public ResponseEntity<Void> eliminarCursoDeCarrera(
            @PathVariable("idCarrera") Long idCarrera,
            @PathVariable("idCurso") Long idCurso) {
        logger.debug("Eliminando curso {} de carrera: {}", idCurso, idCarrera);
        try {
            carreraService.eliminarCursoDeCarrera(idCarrera, idCurso);
            logger.info("Curso {} eliminado de carrera: {}", idCurso, idCarrera);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al eliminar curso de carrera: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/modificarOrdenCursoCarrera/{idCarrera}/{idCurso}/{nuevoIdCiclo}")
    public ResponseEntity<Void> modificarOrdenCursoCarrera(
            @PathVariable("idCarrera") Long idCarrera,
            @PathVariable("idCurso") Long idCurso,
            @PathVariable("nuevoIdCiclo") Long nuevoIdCiclo) {
        logger.debug("Modificando ciclo de curso {} en carrera {}: nuevo ciclo {}", idCurso, idCarrera, nuevoIdCiclo);
        try {
            carreraService.modificarOrdenCursoCarrera(idCarrera, idCurso, nuevoIdCiclo);
            logger.info("Ciclo de curso {} en carrera {} modificado: nuevo ciclo {}", idCurso, idCarrera, nuevoIdCiclo);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al modificar ciclo de curso en carrera: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
