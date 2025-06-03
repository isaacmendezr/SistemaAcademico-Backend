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

@RestController
@RequestMapping("/api/ciclos")
public class CicloController {

    private static final Logger logger = LoggerFactory.getLogger(CicloController.class);
    private final CicloService cicloService;

    public CicloController(CicloService cicloService) {
        this.cicloService = cicloService;
    }

    @PostMapping("/insertar")
    public ResponseEntity<Void> insertar(@RequestBody Ciclo ciclo) {
        logger.debug("Creando ciclo: año {}, número {}", ciclo.getAnio(), ciclo.getNumero());
        try {
            cicloService.insertarCiclo(ciclo);
            logger.info("Ciclo creado exitosamente: año {}, número {}", ciclo.getAnio(), ciclo.getNumero());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (GlobalException e) {
            logger.error("Error al crear ciclo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.error("Error al crear ciclo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Ciclo ciclo) {
        logger.debug("Actualizando ciclo con id: {}", ciclo.getIdCiclo());
        try {
            cicloService.modificarCiclo(ciclo);
            logger.info("Ciclo actualizado exitosamente: id {}", ciclo.getIdCiclo());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al actualizar ciclo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.error("Error al actualizar ciclo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando ciclo con id: {}", id);
        try {
            cicloService.verificarEliminar(id); // Verificación proactiva
            cicloService.eliminarCiclo(id);
            logger.info("Ciclo eliminado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GlobalException e) {
            logger.error("Error al eliminar ciclo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.error("Error al eliminar ciclo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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

    @PostMapping("/activarCiclo/{id}")
    public ResponseEntity<Void> activar(@PathVariable("id") Long id) {
        logger.debug("Activando ciclo con id: {}", id);
        try {
            cicloService.activarCiclo(id);
            logger.info("Ciclo activado exitosamente: id {}", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Error al activar ciclo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoDataException e) {
            logger.error("Error al activar ciclo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
