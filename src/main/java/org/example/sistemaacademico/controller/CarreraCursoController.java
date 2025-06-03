package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CarreraCursoService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.CarreraCurso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrera-curso")
public class CarreraCursoController {

    private static final Logger logger = LoggerFactory.getLogger(CarreraCursoController.class);
    private final CarreraCursoService carreraCursoService;

    public CarreraCursoController(CarreraCursoService carreraCursoService) {
        this.carreraCursoService = carreraCursoService;
    }

    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody CarreraCurso carreraCurso) {
        logger.debug("Creando relación Carrera-Curso: carrera {}, curso {}, ciclo {}",
                carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());
        try {
            carreraCursoService.insertar(carreraCurso);
            logger.info("Relación Carrera-Curso creada exitosamente: carrera {}, curso {}, ciclo {}",
                    carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException e) {
            logger.error("Error al crear relación Carrera-Curso: {}", e.getMessage());
            if (e.getMessage().contains("asociación existente")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoDataException e) {
            logger.error("Error al crear relación Carrera-Curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody CarreraCurso carreraCurso) {
        logger.debug("Actualizando relación Carrera-Curso: carrera {}, curso {}, ciclo {}",
                carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());
        try {
            carreraCursoService.modificar(carreraCurso);
            logger.info("Relación Carrera-Curso actualizada exitosamente: carrera {}, curso {}, ciclo {}",
                    carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al actualizar relación Carrera-Curso: {}", e.getMessage());
            if (e.getMessage().contains("asociación existente")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoDataException e) {
            logger.error("Error al actualizar relación Carrera-Curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<Void> eliminar(@RequestParam("idCarrera") Long idCarrera, @RequestParam("idCurso") Long idCurso) {
        logger.debug("Eliminando relación Carrera-Curso: carrera {}, curso {}", idCarrera, idCurso);
        try {
            // Verificar dependencias proactivamente
            if (carreraCursoService.tieneGruposAsociados(idCarrera, idCurso)) {
                logger.warn("No se puede eliminar relación Carrera-Curso: carrera {}, curso {}: tiene grupos asociados", idCarrera, idCurso);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            carreraCursoService.eliminar(idCarrera, idCurso);
            logger.info("Relación Carrera-Curso eliminada exitosamente: carrera {}, curso {}", idCarrera, idCurso);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException e) {
            logger.error("Error al eliminar relación Carrera-Curso: {}", e.getMessage());
            if (e.getMessage().contains("grupos asociados")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoDataException e) {
            logger.error("Error al eliminar relación Carrera-Curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CarreraCurso>> listar() {
        logger.debug("Listando todas las relaciones Carrera-Curso");
        try {
            List<CarreraCurso> relaciones = carreraCursoService.listar();
            logger.info("Listado de relaciones Carrera-Curso obtenido: total {}", relaciones.size());
            return new ResponseEntity<>(relaciones, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al listar relaciones Carrera-Curso: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/cursos")
    public ResponseEntity<List<CursoDto>> buscarCursosPorCarreraYCiclo(
            @RequestParam("idCarrera") Long idCarrera, @RequestParam("idCiclo") Long idCiclo) {
        logger.debug("Buscando cursos por carrera: {} y ciclo: {}", idCarrera, idCiclo);
        try {
            List<CursoDto> cursos = carreraCursoService.buscarCursosPorCarreraYCiclo(idCarrera, idCiclo);
            logger.info("Cursos encontrados para carrera: {} y ciclo: {}: {}", idCarrera, idCiclo, cursos.size());
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al buscar cursos por carrera y ciclo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
