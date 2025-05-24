package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CicloService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Ciclo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ciclos")
public class CicloController {
    @Autowired
    private CicloService cicloService;

    @PostMapping("/insertar")
    public void insertar(@RequestBody Ciclo ciclo) throws NoDataException, GlobalException {
        cicloService.insertarCiclo(ciclo);
    }

    @PutMapping("/modificar")
    public void modificar(@RequestBody Ciclo ciclo) throws NoDataException, GlobalException {
        cicloService.modificarCiclo(ciclo);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable("id") Long id) throws NoDataException, GlobalException {
        cicloService.eliminarCiclo(id);
    }

    @GetMapping("/listar")
    public List<Ciclo> listar() throws NoDataException, GlobalException {
        return cicloService.listarCiclos();
    }

    @GetMapping("/buscarPorAnnio")
    public Ciclo buscarPorAnnio(@RequestParam Long annio) throws NoDataException, GlobalException {
        return cicloService.buscarCicloPorAnnio(annio);
    }

    @PostMapping("/activarCiclo/{id}")
    public ResponseEntity<Void> activarCiclo(@PathVariable Long id) throws NoDataException, GlobalException {
        cicloService.activarCiclo(id);
        return ResponseEntity.ok().build();
    }
}
