package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.MatriculaService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Matricula;
import org.example.sistemaacademico.logic.Profesor;
import org.example.sistemaacademico.logic.dto.MatriculaAlumnoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matricular")
public class MatriculaController {
    @Autowired
    private MatriculaService matriculaService;
    @PostMapping("/insertar")
    public void insertar(@RequestBody Matricula matricula) throws NoDataException, GlobalException {
        matriculaService.insertarMatricula(matricula);
    }
    @PutMapping("/modificar")
    public void modificar(@RequestBody Matricula matricula) throws NoDataException, GlobalException {
        matriculaService.modificarMatricula(matricula);
    }
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable("id") Long id) throws NoDataException, GlobalException {
        matriculaService.eliminarMatricula(id);
    }
    @GetMapping("/listarMatriculasPorAlumno/{cedula}")
    public List<MatriculaAlumnoDto> listarMatriculasPorAlumno(
            @PathVariable String cedula) throws NoDataException, GlobalException {
        return matriculaService.listarMatriculasPorAlumno(cedula);
    }
    @GetMapping("/listarMatriculasPorAlumnoYCiclo/{idAlumno}/{idCiclo}")
    public List<MatriculaAlumnoDto> listarMatriculasPorAlumnoYCiclo(
            @PathVariable Long idAlumno,
            @PathVariable Long idCiclo) throws NoDataException, GlobalException {
        return matriculaService.listarMatriculasPorAlumnoYCiclo(idAlumno,idCiclo);
    }
}
