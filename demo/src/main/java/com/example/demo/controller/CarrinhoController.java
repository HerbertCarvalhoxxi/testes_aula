package com.example.demo.controller;

import com.example.demo.service.EcommerceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/carrinho")
@CrossOrigin(origins = "*")
public class CarrinhoController {

    @Autowired
    private EcommerceService ecommerceService;

    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarAoCarrinho(@RequestBody Map<String, Object> payload) {
        try {
            Long usuarioId = Long.valueOf(payload.get("usuarioId").toString());
            Long produtoId = Long.valueOf(payload.get("produtoId").toString());
            int quantidade = Integer.parseInt(payload.get("quantidade").toString());

            ecommerceService.adicionarAoCarrinho(usuarioId, produtoId, quantidade);
            return ResponseEntity.ok(Map.of("mensagem", "Produto adicionado ao carrinho com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/comprar/{usuarioId}")
    public ResponseEntity<?> efetuarCompra(@PathVariable Long usuarioId) {
        try {
            String resultado = ecommerceService.efetuarCompra(usuarioId);
            return ResponseEntity.ok(Map.of("mensagem", resultado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}