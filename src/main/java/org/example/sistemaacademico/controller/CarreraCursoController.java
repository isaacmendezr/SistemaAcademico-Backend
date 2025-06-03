package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CarreraCursoService;
import org.example.sistemaacademico.logic.CarreraCurso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.example.sistemaacademico.database.GlobalException;
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

        carreraCursoService.insertar(carreraCurso);

        logger.info("Relación Carrera-Curso creada exitosamente: carrera {}, curso {}, ciclo {}",
                carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody CarreraCurso carreraCurso) {
        logger.debug("Actualizando relación Carrera-Curso: carrera {}, curso {}, ciclo {}",
                carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());

        carreraCursoService.modificar(carreraCurso);

        logger.info("Relación Carrera-Curso actualizada exitosamente: carrera {}, curso {}, ciclo {}",
                carreraCurso.getPkCarrera(), carreraCurso.getPkCurso(), carreraCurso.getPkCiclo());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<Void> eliminar(@RequestParam("idCarrera") Long idCarrera,
                                         @RequestParam("idCurso") Long idCurso) {
        logger.debug("Eliminando relación Carrera-Curso: carrera {}, curso {}", idCarrera, idCurso);

        if (carreraCursoService.tieneGruposAsociados(idCarrera, idCurso)) {
            logger.warn("No se puede eliminar relación Carrera-Curso: tiene grupos asociados");
            throw new GlobalException("No se puede eliminar: la relación Carrera-Curso tiene grupos asociados.");
        }

        carreraCursoService.eliminar(idCarrera, idCurso);
        logger.info("Relación Carrera-Curso eliminada exitosamente: carrera {}, curso {}", idCarrera, idCurso);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CarreraCurso>> listar() {
        logger.debug("Listando todas las relaciones Carrera-Curso");
        List<CarreraCurso> relaciones = carreraCursoService.listar();
        logger.info("Listado de relaciones Carrera-Curso obtenido: total {}", relaciones.size());
        return new ResponseEntity<>(relaciones, HttpStatus.OK);
    }

    @GetMapping("/cursos")
    public ResponseEntity<List<CursoDto>> buscarCursosPorCarreraYCiclo(
            @RequestParam("idCarrera") Long idCarrera, @RequestParam("idCiclo") Long idCiclo) {
        logger.debug("Buscando cursos por carrera: {} y ciclo: {}", idCarrera, idCiclo);
        List<CursoDto> cursos = carreraCursoService.buscarCursosPorCarreraYCiclo(idCarrera, idCiclo);
        logger.info("Cursos encontrados para carrera: {} y ciclo: {}: {}", idCarrera, idCiclo, cursos.size());
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    @GetMapping("/tiene-grupos")
    public ResponseEntity<Boolean> tieneGruposAsociados(
            @RequestParam("idCarrera") Long idCarrera,
            @RequestParam("idCurso") Long idCurso) {
        logger.debug("Verificando grupos asociados para carrera: {} y curso: {}", idCarrera, idCurso);
        boolean tieneGrupos = carreraCursoService.tieneGruposAsociados(idCarrera, idCurso);
        logger.info("Resultado de verificación de grupos asociados para carrera: {} y curso: {}: {}", idCarrera, idCurso, tieneGrupos);
        return new ResponseEntity<>(tieneGrupos, HttpStatus.OK);
    }
}
