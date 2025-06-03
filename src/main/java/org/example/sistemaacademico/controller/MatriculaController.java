package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.MatriculaService;
import org.example.sistemaacademico.logic.Matricula;
import org.example.sistemaacademico.logic.dto.MatriculaAlumnoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matricular")
public class MatriculaController {

    private static final Logger logger = LoggerFactory.getLogger(MatriculaController.class);
    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Matricula matricula) {
        logger.debug("Creando matrícula para alumno: {}, grupo: {}", matricula.getPkAlumno(), matricula.getPkGrupo());
        matriculaService.insertarMatricula(matricula);
        logger.info("Matrícula creada exitosamente para alumno: {}, grupo: {}", matricula.getPkAlumno(), matricula.getPkGrupo());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Matricula matricula) {
        logger.debug("Actualizando matrícula con id: {}", matricula.getIdMatricula());
        matriculaService.modificarMatricula(matricula);
        logger.info("Matrícula actualizada exitosamente: id {}", matricula.getIdMatricula());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando matrícula con id: {}", id);
        matriculaService.verificarEliminar(id); // Verificación proactiva
        matriculaService.eliminarMatricula(id);
        logger.info("Matrícula eliminada exitosamente: id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listarMatriculasPorAlumno/{cedula}")
    public ResponseEntity<List<MatriculaAlumnoDto>> listarMatriculasPorAlumno(@PathVariable("cedula") String cedula) {
        logger.debug("Listando matrículas para alumno con cédula: {}", cedula);
        List<MatriculaAlumnoDto> matriculas = matriculaService.listarMatriculasPorAlumno(cedula);
        logger.info("Matrículas encontradas para alumno con cédula {}: {}", cedula, matriculas.size());
        return new ResponseEntity<>(matriculas, HttpStatus.OK);
    }

    @GetMapping("/listarMatriculasPorAlumnoYCiclo/{idAlumno}/{idCiclo}")
    public ResponseEntity<List<MatriculaAlumnoDto>> listarMatriculasPorAlumnoYCiclo(
            @PathVariable("idAlumno") Long idAlumno,
            @PathVariable("idCiclo") Long idCiclo) {
        logger.debug("Listando matrículas para alumno {} y ciclo: {}", idAlumno, idCiclo);
        List<MatriculaAlumnoDto> matriculas = matriculaService.listarMatriculasPorAlumnoYCiclo(idAlumno, idCiclo);
        logger.info("Matrículas encontradas para alumno {} y ciclo {}: {}", idAlumno, idCiclo, matriculas.size());
        return new ResponseEntity<>(matriculas, HttpStatus.OK);
    }
}
