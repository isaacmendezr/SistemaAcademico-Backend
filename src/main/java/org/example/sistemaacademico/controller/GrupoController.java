package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.GrupoService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Grupo;
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
        try {
            grupoService.insertarGrupo(grupo);
            logger.info("Grupo creado exitosamente para carrera-curso: {}, número: {}, profesor: {}",
                    grupo.getIdCarreraCurso(), grupo.getNumeroGrupo(), grupo.getIdProfesor());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException e) {
            logger.error("Error al crear grupo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.error("Error al crear grupo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Grupo grupo) {
        logger.debug("Actualizando grupo con id: {}", grupo.getIdGrupo());
        try {
            grupoService.modificarGrupo(grupo);
            logger.info("Grupo actualizado exitosamente: id {}", grupo.getIdGrupo());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al actualizar grupo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.error("Error al actualizar grupo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando grupo con id: {}", id);
        try {
            grupoService.verificarEliminar(id); // Verificación proactiva
            grupoService.eliminarGrupo(id);
            logger.info("Grupo eliminado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException e) {
            logger.error("Error al eliminar grupo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.error("Error al eliminar grupo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Grupo>> listar() {
        logger.debug("Listando todos los grupos");
        try {
            List<Grupo> grupos = grupoService.listarGrupos();
            logger.info("Listado de grupos obtenido: total {}", grupos.size());
            return new ResponseEntity<>(grupos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al listar grupos: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscarGruposPorCarreraCurso/{idCarrera}/{idCurso}")
    public ResponseEntity<List<GrupoDto>> buscarGruposPorCarreraCurso(
            @PathVariable("idCarrera") Long idCarrera, @PathVariable("idCurso") Long idCurso) {
        logger.debug("Buscando grupos por carrera: {}, curso: {}", idCarrera, idCurso);
        try {
            List<GrupoDto> grupos = grupoService.buscarGruposPorCarreraCurso(idCarrera, idCurso);
            logger.info("Grupos encontrados para carrera: {}, curso: {}: {}", idCarrera, idCurso, grupos.size());
            return new ResponseEntity<>(grupos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al buscar grupos por carrera y curso: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscarGruposPorCursoCicloCarrera/{idCurso}/{idCiclo}/{idCarrera}")
    public ResponseEntity<List<GrupoDto>> buscarGruposPorCursoCicloCarrera(
            @PathVariable("idCurso") Long idCurso, @PathVariable("idCiclo") Long idCiclo, @PathVariable("idCarrera") Long idCarrera) {
        logger.debug("Buscando grupos por curso: {}, ciclo: {}, carrera: {}", idCurso, idCiclo, idCarrera);
        try {
            List<GrupoDto> grupos = grupoService.buscarGruposPorCursoCicloCarrera(idCurso, idCiclo, idCarrera);
            logger.info("Grupos encontrados para curso: {}, ciclo: {}, carrera: {}: {}", idCurso, idCiclo, idCarrera, grupos.size());
            return new ResponseEntity<>(grupos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al buscar grupos por curso, ciclo y carrera: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscarGruposPorProfesor")
    public ResponseEntity<List<GrupoDto>> buscarGruposPorProfesor(@RequestParam("cedula") String cedula) {
        logger.debug("Buscando grupos por profesor con cédula: {}", cedula);
        try {
            List<GrupoDto> grupos = grupoService.buscarGruposPorProfesor(cedula);
            logger.info("Grupos encontrados para profesor con cédula {}: {}", cedula, grupos.size());
            return new ResponseEntity<>(grupos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al buscar grupos por profesor: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscarGruposPorProfesorEnCicloActivo/{cedula}")
    public ResponseEntity<List<GrupoProfesorDto>> buscarGruposPorProfesorEnCicloActivo(
            @PathVariable String cedula) {
        logger.debug("Buscando grupos por profesor con cédula {} en ciclo activo", cedula);
        try {
            List<GrupoProfesorDto> grupos = grupoService.buscarGruposPorProfesorCicloActivo(cedula);
            logger.info("Grupos encontrados para profesor con cédula {} en ciclo activo: {}", cedula, grupos.size());
            return new ResponseEntity<>(grupos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al buscar grupos por profesor en ciclo activo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
