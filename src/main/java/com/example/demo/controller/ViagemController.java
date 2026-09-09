package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.model.Viagem;
import com.example.demo.repository.ViagemRepository;
import com.example.demo.service.ViagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/viagens")
public class ViagemController {

    private final ViagemService viagemService;
    private final ViagemRepository viagemRepository;

    public ViagemController(ViagemService viagemService, ViagemRepository viagemRepository) {
        this.viagemService = viagemService;
        this.viagemRepository = viagemRepository;
    }

    @PostMapping("/{id}/passageiros")
    public ResponseEntity<String> adicionarPassageiro(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Viagem viagem = viagemRepository.findById(id).orElseThrow();
            
            // Chama a regra de negócio
            viagemService.adicionarPassageiro(viagem, usuario);
            
            // Salva no banco (H2)
            viagemRepository.save(viagem);
            
            return ResponseEntity.ok("Passageiro adicionado com sucesso!");
            
        } catch (IllegalArgumentException e) {
            // Regra de idade falhou
            return ResponseEntity.badRequest().body(e.getMessage()); 
        } catch (IllegalStateException e) {
            // Regra de capacidade falhou
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}