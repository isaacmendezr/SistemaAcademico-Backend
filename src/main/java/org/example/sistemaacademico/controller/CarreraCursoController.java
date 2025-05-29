package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CarreraCursoService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.CarreraCurso;
import org.example.sistemaacademico.logic.dto.CursoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrera-curso")
public class CarreraCursoController {

    @Autowired
    private CarreraCursoService carreraCursoService;

    @PostMapping("/insertar")
    public void insertar(@RequestBody CarreraCurso carreraCurso) throws GlobalException, NoDataException {
        carreraCursoService.insertar(carreraCurso);
    }

    @PutMapping("/modificar")
    public void modificar(@RequestBody CarreraCurso carreraCurso) throws GlobalException, NoDataException {
        carreraCursoService.modificar(carreraCurso);
    }

    @DeleteMapping("/eliminar")
    public void eliminar(@RequestParam Long idCarrera, @RequestParam Long idCurso) throws GlobalException, NoDataException {
        carreraCursoService.eliminar(idCarrera, idCurso);
    }

    @GetMapping("/cursos")
    public List<CursoDto> buscarCursosPorCarreraYCiclo(@RequestParam Long idCarrera, @RequestParam Long idCiclo)
            throws GlobalException, NoDataException {
        return carreraCursoService.buscarCursosPorCarreraYCiclo(idCarrera, idCiclo);
    }

    @GetMapping("/listar")
    public List<CarreraCurso> listar() throws GlobalException, NoDataException {
        return carreraCursoService.listar();
    }
}
