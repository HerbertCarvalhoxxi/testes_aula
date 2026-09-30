package com.example.demo.controller;

import com.example.demo.dto.VeiculoRequestDTO;
import com.example.demo.dto.VeiculoResponseDTO;
import com.example.demo.service.VeiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VeiculoResponseDTO> cadastrar(@RequestBody VeiculoRequestDTO request) {
        VeiculoResponseDTO novoVeiculo = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoVeiculo);
    }

    @GetMapping("/{placa}")
    public ResponseEntity<VeiculoResponseDTO> buscarPorPlaca(@PathVariable String placa) {
        VeiculoResponseDTO veiculo = service.buscarPorPlaca(placa);
        return ResponseEntity.ok(veiculo);
    }

    @PatchMapping("/{placa}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable String placa) {
        service.desativarVeiculo(placa);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/modelo")
    public ResponseEntity<List<VeiculoResponseDTO>> buscarPorModelo(@RequestParam String termo) {
        List<VeiculoResponseDTO> veiculos = service.buscarPorModelo(termo);
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping
    public ResponseEntity<List<VeiculoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}