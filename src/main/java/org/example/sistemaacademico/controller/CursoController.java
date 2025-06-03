package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CursoService;
import org.example.sistemaacademico.logic.Curso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.example.sistemaacademico.database.NoDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Curso curso) {
        logger.debug("Creando curso con código: {}", curso.getCodigo());
        cursoService.insertarCurso(curso);
        logger.info("Curso creado exitosamente: código {}", curso.getCodigo());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Curso curso) {
        logger.debug("Actualizando curso con id: {}", curso.getIdCurso());
        cursoService.modificarCurso(curso);
        logger.info("Curso actualizado exitosamente: id {}", curso.getIdCurso());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando curso con id: {}", id);
        cursoService.verificarEliminar(id); // Verificación proactiva
        cursoService.eliminarCurso(id);
        logger.info("Curso eliminado exitosamente: id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Curso>> listar() {
        logger.debug("Listando todos los cursos");
        List<Curso> cursos = cursoService.listarCursos();
        logger.info("Listado de cursos obtenido: total {}", cursos.size());
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    @GetMapping("/buscarPorCodigo")
    public ResponseEntity<Curso> buscarPorCodigo(@RequestParam("codigo") String codigo) {
        logger.debug("Buscando curso por código: {}", codigo);
        Curso curso = cursoService.buscarCursoPorCodigo(codigo);
        if (curso == null) {
            logger.info("No se encontró curso con código: {}", codigo);
            throw new NoDataException("No se encontró curso con código: " + codigo);
        }
        logger.info("Curso encontrado: código {}", codigo);
        return new ResponseEntity<>(curso, HttpStatus.OK);
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<Curso> buscarPorNombre(@RequestParam("nombre") String nombre) {
        logger.debug("Buscando curso por nombre: {}", nombre);
        Curso curso = cursoService.buscarCursoPorNombre(nombre);
        if (curso == null) {
            logger.info("No se encontró curso con nombre: {}", nombre);
            throw new NoDataException("No se encontró curso con nombre: " + nombre);
        }
        logger.info("Curso encontrado: nombre {}", nombre);
        return new ResponseEntity<>(curso, HttpStatus.OK);
    }

    @GetMapping("/buscarCursosPorCarrera")
    public ResponseEntity<List<CursoDto>> buscarCursosPorCarrera(@RequestParam("idCarrera") Long idCarrera) {
        logger.debug("Buscando cursos por carrera: {}", idCarrera);
        List<CursoDto> cursos = cursoService.buscarCursosPorCarrera(idCarrera);
        logger.info("Cursos encontrados para carrera {}: {}", idCarrera, cursos.size());
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    @GetMapping("/buscarCursosPorCarreraYCiclo/{idCarrera}/{idCiclo}")
    public ResponseEntity<List<CursoDto>> buscarCursosPorCarreraYCiclo(
            @PathVariable("idCarrera") Long idCarrera,
            @PathVariable("idCiclo") Long idCiclo) {
        logger.debug("Buscando cursos por carrera: {} y ciclo: {}", idCarrera, idCiclo);
        List<CursoDto> cursos = cursoService.buscarCursosPorCarreraYCiclo(idCarrera, idCiclo);
        logger.info("Cursos encontrados para carrera: {} y ciclo: {}: {}", idCarrera, idCiclo, cursos.size());
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    @GetMapping("/buscarCursosPorCiclo/{idCiclo}")
    public ResponseEntity<List<CursoDto>> buscarCursosPorCiclo(@PathVariable("idCiclo") Long idCiclo) {
        logger.debug("Buscando cursos por ciclo: {}", idCiclo);
        List<CursoDto> cursos = cursoService.buscarCursosPorCiclo(idCiclo);
        logger.info("Cursos encontrados para ciclo {}: {}", idCiclo, cursos.size());
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }
}
