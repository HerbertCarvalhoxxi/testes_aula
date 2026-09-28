package com.example.demo.service;

import com.example.demo.dto.VeiculoRequestDTO;
import com.example.demo.dto.VeiculoResponseDTO;
import com.example.demo.dto.VeiculoMapper;
import com.example.demo.model.Veiculo;
import com.example.demo.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeiculoService {

    private final VeiculoRepository repository;
    private final VeiculoMapper mapper;

    public VeiculoService(VeiculoRepository repository, VeiculoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public VeiculoResponseDTO cadastrar(VeiculoRequestDTO request) {
        repository.findByPlaca(request.placa()).ifPresent(v -> {
            throw new IllegalArgumentException("Veículo com placa já cadastrada.");
        });

        Veiculo veiculo = mapper.toEntity(request);
        Veiculo salvo = repository.save(veiculo);
        return mapper.toResponseDTO(salvo);
    }

    public VeiculoResponseDTO buscarPorPlaca(String placa) {
        Veiculo veiculo = repository.findByPlaca(placa)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado."));
        return mapper.toResponseDTO(veiculo);
    }

    public void desativarVeiculo(String placa) {
        Veiculo veiculo = repository.findByPlaca(placa)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado."));
        
        if (!veiculo.getAtivo()) {
            throw new IllegalStateException("O veículo já está inativo.");
        }

        veiculo.setAtivo(false);
        repository.save(veiculo);
    }

    public List<VeiculoResponseDTO> buscarPorModelo(String termo) {
        return repository.findByModeloContainingIgnoreCase(termo).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}