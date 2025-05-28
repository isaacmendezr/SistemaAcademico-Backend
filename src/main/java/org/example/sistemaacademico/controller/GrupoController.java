package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.GrupoService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Grupo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {
    @Autowired
    private GrupoService grupoService;
    @PostMapping("/insertar")
    public void insertar(@RequestBody Grupo grupo) throws NoDataException, GlobalException {
        grupoService.insertarGrupo(grupo);
    }

    @PutMapping("/modificar")
    public void modificar(@RequestBody Grupo grupo) throws NoDataException, GlobalException {
        grupoService.modificarGrupo(grupo);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable("id") Long id) throws NoDataException, GlobalException {
        grupoService.eliminarGrupo(id);
    }

    @GetMapping("/listar")
    public List<Grupo> listar() throws NoDataException, GlobalException {
        return grupoService.listarGrupos();
    }
    @GetMapping("/buscarGruposPorCarreraCurso/{pkCarreraCurso}")
    public List<GrupoDto> buscarGruposPorCarreraCurso(@PathVariable Long pkCarreraCurso) throws NoDataException, GlobalException {
        return grupoService.buscarGruposPorCarreraCurso(pkCarreraCurso);
    }

}
