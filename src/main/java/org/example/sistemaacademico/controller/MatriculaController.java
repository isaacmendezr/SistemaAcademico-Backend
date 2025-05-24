package org.example.sistemaacademico.controller;

import org.example.sistemaacademico.data.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matricular")
public class MatriculaController {
    @Autowired
    private MatriculaService matriculaService;
}
