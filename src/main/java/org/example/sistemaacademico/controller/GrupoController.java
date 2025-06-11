package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.GrupoService;
import org.example.sistemaacademico.logic.Grupo;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.example.sistemaacademico.logic.dto.GrupoDto;
import org.example.sistemaacademico.logic.dto.GrupoProfesorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    private static final Logger logger = LoggerFactory.getLogger(GrupoController.class);
    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Grupo grupo) {
        logger.debug("Creando grupo para carrera-curso: {}, número: {}, profesor: {}",
                grupo.getIdCarreraCurso(), grupo.getNumeroGrupo(), grupo.getIdProfesor());
        grupoService.insertarGrupo(grupo);
        logger.info("Grupo creado exitosamente para carrera-curso: {}, número: {}, profesor: {}",
                grupo.getIdCarreraCurso(), grupo.getNumeroGrupo(), grupo.getIdProfesor());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Grupo grupo) {
        logger.debug("Actualizando grupo con id: {}", grupo.getIdGrupo());
        grupoService.modificarGrupo(grupo);
        logger.info("Grupo actualizado exitosamente: id {}", grupo.getIdGrupo());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando grupo con id: {}", id);
        grupoService.verificarEliminar(id); // Verificación proactiva
        grupoService.eliminarGrupo(id);
        logger.info("Grupo eliminado exitosamente: id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Grupo>> listar() {
        logger.debug("Listando todos los grupos");
        List<Grupo> grupos = grupoService.listarGrupos();
        logger.info("Listado de grupos obtenido: total {}", grupos.size());
        return new ResponseEntity<>(grupos, HttpStatus.OK);
    }

    @GetMapping("/buscarGruposPorCarreraCurso/{idCarrera}/{idCurso}")
    public ResponseEntity<List<GrupoDto>> buscarGruposPorCarreraCurso(
            @PathVariable("idCarrera") Long idCarrera, @PathVariable("idCurso") Long idCurso) {
        logger.debug("Buscando grupos por carrera: {}, curso: {}", idCarrera, idCurso);
        List<GrupoDto> grupos = grupoService.buscarGruposPorCarreraCurso(idCarrera, idCurso);
        logger.info("Grupos encontrados para carrera: {}, curso: {}: {}", idCarrera, idCurso, grupos.size());
        return new ResponseEntity<>(grupos, HttpStatus.OK);
    }

    @GetMapping("/buscarGruposPorCursoCicloCarrera/{idCurso}/{idCiclo}/{idCarrera}")
    public ResponseEntity<List<GrupoDto>> buscarGruposPorCursoCicloCarrera(
            @PathVariable("idCurso") Long idCurso, @PathVariable("idCiclo") Long idCiclo, @PathVariable("idCarrera") Long idCarrera) {
        logger.debug("Buscando grupos por curso: {}, ciclo: {}, carrera: {}", idCurso, idCiclo, idCarrera);
        List<GrupoDto> grupos = grupoService.buscarGruposPorCursoCicloCarrera(idCurso, idCiclo, idCarrera);
        logger.info("Grupos encontrados para curso: {}, ciclo: {}, carrera: {}: {}", idCurso, idCiclo, idCarrera, grupos.size());
        return new ResponseEntity<>(grupos, HttpStatus.OK);
    }

    @GetMapping("/buscarGruposPorProfesor")
    public ResponseEntity<List<GrupoDto>> buscarGruposPorProfesor(@RequestParam("cedula") String cedula) {
        logger.debug("Buscando grupos por profesor con cédula: {}", cedula);
        List<GrupoDto> grupos = grupoService.buscarGruposPorProfesor(cedula);
        logger.info("Grupos encontrados para profesor con cédula {}: {}", cedula, grupos.size());
        return new ResponseEntity<>(grupos, HttpStatus.OK);
    }

    @GetMapping("/buscarGruposPorProfesorEnCicloActivo/{cedula}")
    public ResponseEntity<List<GrupoProfesorDto>> buscarGruposPorProfesorEnCicloActivo(
            @PathVariable String cedula) {
        logger.debug("Buscando grupos por profesor con cédula {} en ciclo activo", cedula);
        List<GrupoProfesorDto> grupos = grupoService.buscarGruposPorProfesorCicloActivo(cedula);
        logger.info("Grupos encontrados para profesor con cédula {} en ciclo activo: {}", cedula, grupos.size());
        return new ResponseEntity<>(grupos, HttpStatus.OK);
    }

    @GetMapping("/buscarPorMatricula/{idMatricula}")
    public ResponseEntity<GrupoDto> buscarGrupoPorMatricula(@PathVariable("idMatricula") Long idMatricula) {
        logger.debug("Buscando grupo por matrícula: {}", idMatricula);
        GrupoDto grupo = grupoService.buscarGrupoPorMatricula(idMatricula);
        logger.info("Grupo encontrado para matrícula: {}", idMatricula);
        return new ResponseEntity<>(grupo, HttpStatus.OK);
    }

    @GetMapping("/buscarCursoPorGrupo/{idGrupo}")
    public ResponseEntity<CursoDto> buscarCursoPorGrupo(@PathVariable("idGrupo") Long idGrupo) {
        logger.debug("Buscando curso por grupo: {}", idGrupo);
        CursoDto curso = grupoService.buscarCursoPorGrupo(idGrupo);
        logger.info("Curso encontrado para grupo: {}", idGrupo);
        return new ResponseEntity<>(curso, HttpStatus.OK);
    }
}
