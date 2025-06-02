package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.GrupoService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Grupo;
import org.example.sistemaacademico.logic.dto.GrupoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con grupos.
 * Proporciona endpoints para CRUD y búsqueda de grupos por carrera, curso, ciclo y profesor.
 */
@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    private static final Logger logger = LoggerFactory.getLogger(GrupoController.class);
    private final GrupoService grupoService;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el servicio de grupos.
     *
     * @param grupoService El servicio de grupos.
     */
    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    /**
     * Crea un nuevo grupo.
     *
     * @param grupo El objeto Grupo a crear.
     * @return ResponseEntity con el estado 201 Created.
     */
    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Grupo grupo) {
        logger.debug("Creando grupo para carrera-curso: {}, número: {}, profesor: {}",
                grupo.getIdCarreraCurso(), grupo.getNumeroGrupo(), grupo.getIdProfesor());
        try {
            grupoService.insertarGrupo(grupo);
            logger.info("Grupo creado exitosamente para carrera-curso: {}, número: {}, profesor: {}",
                    grupo.getIdCarreraCurso(), grupo.getNumeroGrupo(), grupo.getIdProfesor());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al crear grupo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un grupo existente.
     *
     * @param grupo El objeto Grupo con los datos actualizados.
     * @return ResponseEntity con el estado 200 OK.
     */
    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Grupo grupo) {
        logger.debug("Actualizando grupo con id: {}", grupo.getIdGrupo());
        try {
            grupoService.modificarGrupo(grupo);
            logger.info("Grupo actualizado exitosamente: id {}", grupo.getIdGrupo());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al actualizar grupo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina un grupo por su ID.
     *
     * @param id El ID del grupo a eliminar.
     * @return ResponseEntity con el estado 204 No Content.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando grupo con id: {}", id);
        try {
            grupoService.eliminarGrupo(id);
            logger.info("Grupo eliminado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al eliminar grupo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lista todos los grupos registrados.
     *
     * @return ResponseEntity con la lista de grupos y estado 200 OK.
     */
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

    /**
     * Busca grupos asociados a una carrera y curso específicos.
     *
     * @param idCarrera El ID de la carrera.
     * @param idCurso El ID del curso.
     * @return ResponseEntity con la lista de grupos y estado 200 OK.
     */
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

    /**
     * Busca grupos asociados a un curso, ciclo y carrera específicos.
     *
     * @param idCurso El ID del curso.
     * @param idCiclo El ID del ciclo.
     * @param idCarrera El ID de la carrera.
     * @return ResponseEntity con la lista de grupos y estado 200 OK.
     */
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

    /**
     * Busca grupos asignados a un profesor por su cédula.
     *
     * @param cedula La cédula del profesor.
     * @return ResponseEntity con la lista de grupos y estado 200 OK.
     */
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
}
