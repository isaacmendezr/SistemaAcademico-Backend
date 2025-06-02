package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CicloService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Ciclo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con ciclos.
 * Proporciona endpoints para CRUD, búsqueda y activación de ciclos.
 */
@RestController
@RequestMapping("/api/ciclos")
public class CicloController {

    private static final Logger logger = LoggerFactory.getLogger(CicloController.class);
    private final CicloService cicloService;

    /**
     * Constructor que utiliza inyección de dependencias para inicializar el servicio de ciclos.
     *
     * @param cicloService El servicio de ciclos.
     */
    public CicloController(CicloService cicloService) {
        this.cicloService = cicloService;
    }

    /**
     * Crea un nuevo ciclo.
     *
     * @param ciclo El objeto Ciclo a crear.
     * @return ResponseEntity con el estado 201 Created.
     */
    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Ciclo ciclo) {
        logger.debug("Creando ciclo: año {}, número {}", ciclo.getAnio(), ciclo.getNumero());
        try {
            cicloService.insertarCiclo(ciclo);
            logger.info("Ciclo creado exitosamente: año {}, número {}", ciclo.getAnio(), ciclo.getNumero());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al crear ciclo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un ciclo existente.
     *
     * @param ciclo El objeto Ciclo con los datos actualizados.
     * @return ResponseEntity con el estado 200 OK.
     */
    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Ciclo ciclo) {
        logger.debug("Actualizando ciclo con id: {}", ciclo.getIdCiclo());
        try {
            cicloService.modificarCiclo(ciclo);
            logger.info("Ciclo actualizado exitosamente: id {}", ciclo.getIdCiclo());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al actualizar ciclo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina un ciclo por su ID.
     *
     * @param id El ID del ciclo a eliminar.
     * @return ResponseEntity con el estado 204 No Content.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando ciclo con id: {}", id);
        try {
            cicloService.eliminarCiclo(id);
            logger.info("Ciclo eliminado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al eliminar ciclo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lista todos los ciclos registrados.
     *
     * @return ResponseEntity con la lista de ciclos y estado 200 OK.
     */
    @GetMapping("/listar")
    public ResponseEntity<List<Ciclo>> listar() {
        logger.debug("Listando todos los ciclos");
        try {
            List<Ciclo> ciclos = cicloService.listarCiclos();
            logger.info("Listado de ciclos obtenido: total {}", ciclos.size());
            return new ResponseEntity<>(ciclos, HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al listar ciclos: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Busca un ciclo por su año.
     *
     * @param anio El año del ciclo.
     * @return ResponseEntity con el ciclo encontrado y estado 200 OK.
     */
    @GetMapping("/buscarPorAnnio")
    public ResponseEntity<Ciclo> buscarPorAnio(@RequestParam("annio") Long anio) {
        logger.debug("Buscando ciclo por año: {}", anio);
        try {
            Ciclo ciclo = cicloService.buscarCicloPorAnio(anio);
            if (ciclo == null) {
                logger.info("No se encontró ciclo con año: {}", anio);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Ciclo encontrado: año {}", anio);
            return new ResponseEntity<>(ciclo, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar ciclo por año: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca un ciclo por su ID.
     *
     * @param id El ID del ciclo.
     * @return ResponseEntity con el ciclo encontrado y estado 200 OK.
     */
    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<Ciclo> buscarPorId(@PathVariable("id") Long id) {
        logger.debug("Buscando ciclo por id: {}", id);
        try {
            Ciclo ciclo = cicloService.buscarCicloPorId(id);
            if (ciclo == null) {
                logger.info("No se encontró ciclo con id: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            logger.info("Ciclo encontrado: id {}", id);
            return new ResponseEntity<>(ciclo, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al buscar ciclo por id: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Activa un ciclo por su ID.
     *
     * @param id El ID del ciclo a activar.
     * @return ResponseEntity con el estado 200 OK.
     */
    @PostMapping("/activarCiclo/{id}")
    public ResponseEntity<Void> activar(@PathVariable("id") Long id) {
        logger.debug("Activando ciclo con id: {}", id);
        try {
            cicloService.activarCiclo(id);
            logger.info("Ciclo activado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException | NoDataException e) {
            logger.error("Error al activar ciclo: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
