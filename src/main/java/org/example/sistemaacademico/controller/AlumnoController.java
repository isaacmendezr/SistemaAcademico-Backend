package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.AlumnoService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Alumno;
import org.example.sistemaacademico.logic.MatriculaAlumnoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {
    @Autowired
    private AlumnoService alumnoService;
    @PostMapping("/insertar")
    public void insertar(@RequestBody Alumno alumno) throws NoDataException, GlobalException {
        alumnoService.insertarAlumno(alumno);
    }

    @PutMapping("/modificar")
    public void modificar(@RequestBody Alumno alumno) throws NoDataException, GlobalException {
        alumnoService.modificarAlumno(alumno);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) throws NoDataException, GlobalException {
        alumnoService.eliminarAlumno(id);
    }

    @GetMapping("/listar")
    public List<Alumno> listar() throws NoDataException, GlobalException {
        return alumnoService.listarAlumnos();
    }

    @GetMapping("/buscarPorCedula")
    public Alumno buscarPorCedula(@RequestParam String cedula) throws NoDataException, GlobalException {
        return alumnoService.buscarAlumnoPorCedula(cedula);
    }

    @GetMapping("/buscarPorNombre")
    public Alumno buscarPorNombre(@RequestParam String nombre) throws NoDataException, GlobalException {
        return alumnoService.buscarAlumnoPorNombre(nombre);
    }

    @GetMapping("/buscarPorCarrera")
    public List<Alumno> buscarPorCarrera(@RequestParam Long carrera) throws NoDataException, GlobalException {
        return alumnoService.buscarAlumnosPorCarrera(carrera);
    }

    @GetMapping("/historialAlumno/{id}")
    public List<MatriculaAlumnoDto> listarMatriculasPorAlumno(@PathVariable Long id) throws NoDataException, GlobalException {
        return alumnoService.listarMatriculasPorAlumno(id);
    }
}
