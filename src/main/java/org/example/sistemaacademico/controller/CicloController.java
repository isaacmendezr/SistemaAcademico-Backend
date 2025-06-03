package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CicloService;
import org.example.sistemaacademico.logic.Ciclo;
import org.example.sistemaacademico.database.NoDataException;
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
        cicloService.insertarCiclo(ciclo);
        logger.info("Ciclo creado exitosamente: año {}, número {}", ciclo.getAnio(), ciclo.getNumero());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Void> modificar(@RequestBody Ciclo ciclo) {
        logger.debug("Actualizando ciclo con id: {}", ciclo.getIdCiclo());
        cicloService.modificarCiclo(ciclo);
        logger.info("Ciclo actualizado exitosamente: id {}", ciclo.getIdCiclo());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.debug("Eliminando ciclo con id: {}", id);
        cicloService.verificarEliminar(id); // Verificación proactiva
        cicloService.eliminarCiclo(id);
        logger.info("Ciclo eliminado exitosamente: id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Ciclo>> listar() {
        logger.debug("Listando todos los ciclos");
        List<Ciclo> ciclos = cicloService.listarCiclos();
        logger.info("Listado de ciclos obtenido: total {}", ciclos.size());
        return new ResponseEntity<>(ciclos, HttpStatus.OK);
    }

    @GetMapping("/buscarPorAnnio")
    public ResponseEntity<Ciclo> buscarPorAnio(@RequestParam("annio") Long anio) {
        logger.debug("Buscando ciclo por año: {}", anio);
        Ciclo ciclo = cicloService.buscarCicloPorAnio(anio);
        if (ciclo == null) {
            logger.info("No se encontró ciclo con año: {}", anio);
            throw new NoDataException("No se encontró ciclo con año: " + anio);
        }
        logger.info("Ciclo encontrado: año {}", anio);
        return new ResponseEntity<>(ciclo, HttpStatus.OK);
    }

    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<Ciclo> buscarPorId(@PathVariable("id") Long id) {
        logger.debug("Buscando ciclo por id: {}", id);
        Ciclo ciclo = cicloService.buscarCicloPorId(id);
        if (ciclo == null) {
            logger.info("No se encontró ciclo con id: {}", id);
            throw new NoDataException("No se encontró ciclo con id: " + id);
        }
        logger.info("Ciclo encontrado: id {}", id);
        return new ResponseEntity<>(ciclo, HttpStatus.OK);
    }

    @PostMapping("/activarCiclo/{id}")
    public ResponseEntity<Void> activar(@PathVariable("id") Long id) {
        logger.debug("Activando ciclo con id: {}", id);
        cicloService.activarCiclo(id);
        logger.info("Ciclo activado exitosamente: id {}", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
