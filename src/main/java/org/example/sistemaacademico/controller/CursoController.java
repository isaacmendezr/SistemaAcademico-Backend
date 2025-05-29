package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.CursoService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Curso;
import org.example.sistemaacademico.logic.CursoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;

    @PostMapping("/insertar")
    public void insertar(@RequestBody Curso Curso) throws NoDataException, GlobalException {
        cursoService.insertarCurso(Curso);
    }

    @PutMapping("/modificar")
    public void modificar(@RequestBody Curso Curso) throws NoDataException, GlobalException {
        cursoService.modificarCurso(Curso);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable("id") Long id) throws NoDataException, GlobalException {
        cursoService.eliminarCurso(id);
    }

    @GetMapping("/listar")
    public List<Curso> listar() throws NoDataException, GlobalException {
        return cursoService.listarCursos();
    }

    @GetMapping("/buscarPorCodigo")
    public Curso buscarPorCedula(@RequestParam String codigo) throws NoDataException, GlobalException {
        return cursoService.buscarCursoPorCodigo(codigo);
    }

    @GetMapping("/buscarPorNombre")
    public Curso buscarPorNombre(@RequestParam String nombre) throws NoDataException, GlobalException {
        return cursoService.buscarCursoPorNombre(nombre);
    }
    @GetMapping("/buscarCursosPorCarrera")
    public List<CursoDto> buscarCursosPorCarrera(@RequestParam Long idCarrera) throws NoDataException, GlobalException {
        return cursoService.buscarCursosPorCarrera(idCarrera);
    }
    @GetMapping("/buscarCursosPorCarreraYCiclo/{pkCarrera}/{pkCiclo}")
    public List<CursoDto> listarCursosPorCarreraYCiclo(
            @PathVariable Long pkCarrera,
            @PathVariable Long pkCiclo) throws NoDataException, GlobalException {
        return cursoService.buscarCursosPorCarreraYCiclo(pkCarrera, pkCiclo);
    }
}
