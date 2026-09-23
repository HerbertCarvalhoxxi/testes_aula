package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.service.EcommerceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private EcommerceService ecommerceService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario credentials) {
        try {
            Usuario usuario = ecommerceService.autenticar(credentials.getEmail(), credentials.getSenha());
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // Adicionado para atender à rota de cadastro do front-end
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario novoUsuario) {
        try {
            Usuario usuario = ecommerceService.cadastrar(novoUsuario);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}