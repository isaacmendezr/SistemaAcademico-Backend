package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Profesor;
import org.example.sistemaacademico.data.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {
    @Autowired
    private ProfesorService profesorService;

    @PostMapping("/insertar")
    public void insertar(@RequestBody Profesor profesor) throws NoDataException, GlobalException {
        profesorService.insertarProfesor(profesor);
    }

    @PutMapping("/modificar")
    public void modificar(@RequestBody Profesor profesor) throws NoDataException, GlobalException {
        profesorService.modificarProfesor(profesor);
    }
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable("id") Long id) throws NoDataException, GlobalException {
        profesorService.eliminarProfesor(id);
    }

    @GetMapping("/listar")
    public List<Profesor> listar() throws NoDataException, GlobalException {
        return profesorService.listarProfesores();
    }

    @GetMapping("/buscarPorCedula")
    public Profesor buscarPorCedula(@RequestParam String cedula) throws NoDataException, GlobalException {
        return profesorService.buscarProfesorPorCedula(cedula);
    }
    @GetMapping("/buscarPorNombre")
    public Profesor buscarPorNombre(@RequestParam String nombre) throws NoDataException, GlobalException {
        return profesorService.buscarProfesorPorNombre(nombre);
    }

}
