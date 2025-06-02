package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CursoService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Curso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con cursos.
 * Proporciona endpoints para CRUD y búsqueda de cursos por código, nombre, carrera y ciclo.
 */
@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);
    private final CursoService cursoService;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el servicio de cursos.
     *
     * @param cursoService El servicio de cursos.
     */
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    /**
     * Crea un nuevo curso.
     *
     * @param curso El objeto Curso a crear.
     * @return ResponseEntity con el estado 201 Created.
     */
    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Curso curso) {
        logger.debug("Creando curso con código: {}", curso.getCodigo());
        try {
            cursoService.insertarCurso(curso);
            logger.info("Curso creado exitosamente: código {}", curso.getCodigo());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al crear curso: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un curso existente.
     *
     * @param curso El objeto Curso con los datos actualizados.
     * @return ResponseEntity con el estado 200 OK.
     */
    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Curso curso) {
        logger.debug("Actualizando curso con id: {}", curso.getIdCurso());
        try {
            cursoService.modificarCurso(curso);
            logger.info("Curso actualizado exitosamente: id {}", curso.getIdCurso());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al actualizar curso: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina un curso por su ID.
     *
     * @param id El ID del curso a eliminar.
     * @return ResponseEntity con el estado 204 No Content.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando curso con id: {}", id);
        try {
            cursoService.eliminarCurso(id);
            logger.info("Curso eliminado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al eliminar curso: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lista todos los cursos registrados.
     *
     * @return ResponseEntity con la lista de cursos y estado 200 OK.
     */
    @GetMapping("/listar")
    public ResponseEntity<List<Curso>> listar() {
        logger.debug("Listando todos los cursos");
        try {
            List<Curso> cursos = cursoService.listarCursos();
            logger.info("Listado de cursos obtenido: total {}", cursos.size());
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al listar cursos: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Busca un curso por su código.
     *
     * @param codigo El código del curso.
     * @return ResponseEntity con el curso encontrado y estado 200 OK.
     */
    @GetMapping("/buscarPorCodigo")
    public ResponseEntity<Curso> buscarPorCodigo(@RequestParam("codigo") String codigo) {
        logger.debug("Buscando curso por código: {}", codigo);
        try {
            Curso curso = cursoService.buscarCursoPorCodigo(codigo);
            if (curso == null) {
                logger.info("No se encontró curso con código: {}", codigo);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Curso encontrado: código {}", codigo);
            return new ResponseEntity<>(curso, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar curso por código: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca un curso por su nombre.
     *
     * @param nombre El nombre del curso.
     * @return ResponseEntity con el curso encontrado y estado 200 OK.
     */
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<Curso> buscarPorNombre(@RequestParam("nombre") String nombre) {
        logger.debug("Buscando curso por nombre: {}", nombre);
        try {
            Curso curso = cursoService.buscarCursoPorNombre(nombre);
            if (curso == null) {
                logger.info("No se encontró curso con nombre: {}", nombre);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Curso encontrado: nombre {}", nombre);
            return new ResponseEntity<>(curso, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar curso por nombre: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca cursos asociados a una carrera.
     *
     * @param idCarrera El ID de la carrera.
     * @return ResponseEntity con la lista de cursos y estado 200 OK.
     */
    @GetMapping("/buscarCursosPorCarrera")
    public ResponseEntity<List<CursoDto>> buscarCursosPorCarrera(@RequestParam("idCarrera") Long idCarrera) {
        logger.debug("Buscando cursos por carrera: {}", idCarrera);
        try {
            List<CursoDto> cursos = cursoService.buscarCursosPorCarrera(idCarrera);
            logger.info("Cursos encontrados para carrera {}: {}", idCarrera, cursos.size());
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al buscar cursos por carrera: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Busca cursos asociados a una carrera y ciclo específicos.
     *
     * @param idCarrera El ID de la carrera.
     * @param idCiclo El ID del ciclo.
     * @return ResponseEntity con la lista de cursos y estado 200 OK.
     */
    @GetMapping("/buscarCursosPorCarreraYCiclo/{idCarrera}/{idCiclo}")
    public ResponseEntity<List<CursoDto>> buscarCursosPorCarreraYCiclo(
            @PathVariable("idCarrera") Long idCarrera, @PathVariable("idCiclo") Long idCiclo) {
        logger.debug("Buscando cursos por carrera: {} y ciclo: {}", idCarrera, idCiclo);
        try {
            List<CursoDto> cursos = cursoService.buscarCursosPorCarreraYCiclo(idCarrera, idCiclo);
            logger.info("Cursos encontrados para carrera: {} y ciclo: {}: {}", idCarrera, idCiclo, cursos.size());
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al buscar cursos por carrera y ciclo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Busca cursos asociados a un ciclo específico.
     *
     * @param idCiclo El ID del ciclo.
     * @return ResponseEntity con la lista de cursos y estado 200 OK.
     */
    @GetMapping("/buscarCursosPorCiclo/{idCiclo}")
    public ResponseEntity<List<CursoDto>> buscarCursosPorCiclo(@PathVariable("idCiclo") Long idCiclo) {
        logger.debug("Buscando cursos por ciclo: {}", idCiclo);
        try {
            List<CursoDto> cursos = cursoService.buscarCursosPorCiclo(idCiclo);
            logger.info("Cursos encontrados para ciclo {}: {}", idCiclo, cursos.size());
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al buscar cursos por ciclo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
