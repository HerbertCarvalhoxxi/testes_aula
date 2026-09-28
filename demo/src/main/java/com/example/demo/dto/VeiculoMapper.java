package com.example.demo.dto;

import com.example.demo.dto.VeiculoRequestDTO;
import com.example.demo.dto.VeiculoResponseDTO;
import com.example.demo.model.Veiculo;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    public Veiculo toEntity(VeiculoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Veiculo(dto.placa(), dto.modelo(), dto.proprietario());
    }

    public VeiculoResponseDTO toResponseDTO(Veiculo veiculo) {
        if (veiculo == null) {
            return null;
        }
        return new VeiculoResponseDTO(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getProprietario(),
                veiculo.getAtivo()
        );
    }
}