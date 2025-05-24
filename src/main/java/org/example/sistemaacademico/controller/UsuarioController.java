package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.UsuarioService;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/insertar")
    public void insertar(@RequestBody Usuario usuario) throws NoDataException, GlobalException {
        usuarioService.insertarUsuario(usuario);
    }
    @PutMapping("/modificar")
    public void modificar(@RequestBody Usuario usuario) throws NoDataException, GlobalException {
        usuarioService.modificarUsuario(usuario);
    }
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable("id") Long id) throws NoDataException, GlobalException {
        usuarioService.eliminarUsuario(id);
    }

    @GetMapping("/listar")
    public List<Usuario> listar() throws NoDataException, GlobalException {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/buscarPorCedula")
    public Usuario buscarPorCedula(@RequestParam String cedula) throws NoDataException, GlobalException {
        return usuarioService.buscarUsuarioPorCedula(cedula);
    }
    @PostMapping("/login")
    public Usuario login(@RequestParam String cedula, @RequestParam String clave) throws NoDataException, GlobalException {
        return usuarioService.loginUsuario(cedula, clave);
    }
}
