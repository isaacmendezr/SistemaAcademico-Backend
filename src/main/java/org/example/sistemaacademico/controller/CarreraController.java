package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CarreraService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Carrera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {
    @Autowired
    private CarreraService carreraService;

    @PostMapping("/insertar")
    public void insertar(@RequestBody Carrera carrera) throws NoDataException, GlobalException {
        carreraService.insertarCarrera(carrera);
    }

    @PutMapping("/modificar")
    public void modificar(@RequestBody Carrera carrera) throws NoDataException, GlobalException {
        carreraService.modificarCarrera(carrera);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable("id") Long id) throws NoDataException, GlobalException {
        carreraService.eliminarCarrera(id);
    }

    @GetMapping("/listar")
    public List<Carrera> listar() throws NoDataException, GlobalException {
        return carreraService.listarCarreras();
    }

    @GetMapping("/buscarPorCodigo")
    public Carrera buscarPorCedula(@RequestParam String codigo) throws NoDataException, GlobalException {
        return carreraService.buscarCarreraPorCodigo(codigo);
    }
    @GetMapping("/buscarPorNombre")
    public Carrera buscarPorNombre(@RequestParam String nombre) throws NoDataException, GlobalException {
        return carreraService.buscarCarreraPorNombre(nombre);
    }
    @PostMapping("/insertarCursoACarrera/{pkCarrera}/{pkCurso}/{pkCiclo}")
    public void insertarCursoACarrera(
            @PathVariable Long pkCarrera,
            @PathVariable Long pkCurso,
            @PathVariable Long pkCiclo) throws NoDataException, GlobalException {
        carreraService.insertarCursoACarrera(pkCarrera, pkCurso, pkCiclo);
    }
    @DeleteMapping("/eliminarCursoDeCarrera/{pkCarrera}/{pkCurso}")
    public void eliminarCursoDeCarrera(
            @PathVariable Long pkCarrera,
            @PathVariable Long pkCurso) throws NoDataException, GlobalException {
        carreraService.eliminarCursoDeCarrera(pkCarrera, pkCurso);
    }
    @PutMapping("/modificarOrdenCursoCarrera/{pkCarrera}/{pkCurso}/{nuevoPkCiclo}")
    public void modificarOrdenCursoCarrera(
            @PathVariable Long pkCarrera,
            @PathVariable Long pkCurso,
            @PathVariable Long nuevoPkCiclo) throws NoDataException, GlobalException {
        carreraService.modificarOrdenCursoCarrera(pkCarrera, pkCurso, nuevoPkCiclo);
    }
}
