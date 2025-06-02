package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.MatriculaService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Matricula;
import org.example.sistemaacademico.logic.dto.MatriculaAlumnoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con matrículas.
 * Proporciona endpoints para CRUD y búsqueda de matrículas por alumno y ciclo.
 */
@RestController
@RequestMapping("/api/matricular")
public class MatriculaController {

    private static final Logger logger = LoggerFactory.getLogger(MatriculaController.class);
    private final MatriculaService matriculaService;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el servicio de matrículas.
     *
     * @param matriculaService El servicio de matrículas.
     */
    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    /**
     * Crea una nueva matrícula.
     *
     * @param matricula El objeto Matricula a crear.
     * @return ResponseEntity con el estado 201 Created.
     */
    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Matricula matricula) {
        logger.debug("Creando matrícula para alumno: {}, grupo: {}", matricula.getPkAlumno(), matricula.getPkGrupo());
        try {
            matriculaService.insertarMatricula(matricula);
            logger.info("Matrícula creada exitosamente para alumno: {}, grupo: {}", matricula.getPkAlumno(), matricula.getPkGrupo());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al crear matrícula: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza una matrícula existente.
     *
     * @param matricula El objeto Matricula con los datos actualizados.
     * @return ResponseEntity con el estado 200 OK.
     */
    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Matricula matricula) {
        logger.debug("Actualizando matrícula con id: {}", matricula.getIdMatricula());
        try {
            matriculaService.modificarMatricula(matricula);
            logger.info("Matrícula actualizada exitosamente: id {}", matricula.getIdMatricula());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al actualizar matrícula: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina una matrícula por su ID.
     *
     * @param id El ID de la matrícula a eliminar.
     * @return ResponseEntity con el estado 204 No Content.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando matrícula con id: {}", id);
        try {
            matriculaService.eliminarMatricula(id);
            logger.info("Matrícula eliminada exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al eliminar matrícula: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lista las matrículas de un alumno por su cédula.
     *
     * @param cedula La cédula del alumno.
     * @return ResponseEntity con la lista de matrículas y estado 200 OK.
     */
    @GetMapping("/listarMatriculasPorAlumno/{cedula}")
    public ResponseEntity<List<MatriculaAlumnoDto>> listarMatriculasPorAlumno(@PathVariable("cedula") String cedula) {
        logger.debug("Listando matrículas para alumno con cédula: {}", cedula);
        try {
            List<MatriculaAlumnoDto> matriculas = matriculaService.listarMatriculasPorAlumno(cedula);
            logger.info("Matrículas encontradas para alumno con cédula {}: {}", cedula, matriculas.size());
            return new ResponseEntity<>(matriculas, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al listar matrículas por alumno: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Lista las matrículas de un alumno en un ciclo específico.
     *
     * @param idAlumno El ID del alumno.
     * @param idCiclo El ID del ciclo.
     * @return ResponseEntity con la lista de matrículas y estado 200 OK.
     */
    @GetMapping("/listarMatriculasPorAlumnoYCiclo/{idAlumno}/{idCiclo}")
    public ResponseEntity<List<MatriculaAlumnoDto>> listarMatriculasPorAlumnoYCiclo(
            @PathVariable("idAlumno") Long idAlumno, @PathVariable("idCiclo") Long idCiclo) {
        logger.debug("Listando matrículas para alumno {} y ciclo: {}", idAlumno, idCiclo);
        try {
            List<MatriculaAlumnoDto> matriculas = matriculaService.listarMatriculasPorAlumnoYCiclo(idAlumno, idCiclo);
            logger.info("Matrículas encontradas para alumno {} y ciclo {}: {}", idAlumno, idCiclo, matriculas.size());
            return new ResponseEntity<>(matriculas, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al listar matrículas por alumno y ciclo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
