package com.example.demo.dto;

import com.example.demo.dto.VeiculoRequestDTO;
import com.example.demo.dto.VeiculoResponseDTO;
import com.example.demo.model.Veiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VeiculoMapperTest {

    private VeiculoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new VeiculoMapper();
    }

    @Test
    void deveConverterRequestDtoParaEntityComSucesso() {
        VeiculoRequestDTO dto = new VeiculoRequestDTO("ABC-1234", "Fusca", "João");

        Veiculo veiculo = mapper.toEntity(dto);

        assertNotNull(veiculo);
        assertEquals("ABC-1234", veiculo.getPlaca());
        assertEquals("Fusca", veiculo.getModelo());
        assertEquals("João", veiculo.getProprietario());
    }

    @Test
    void deveRetornarNullAoConverterRequestDtoNulo() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void deveConverterEntityParaResponseDtoComSucesso() {
        Veiculo veiculo = new Veiculo("XYZ-9876", "Civic", "Maria");
        veiculo.setId(1L);

        VeiculoResponseDTO response = mapper.toResponseDTO(veiculo);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("XYZ-9876", response.placa());
        assertEquals("Civic", response.modelo());
        assertEquals("Maria", response.proprietario());
    }

    @Test
    void deveRetornarNullAoConverterEntityNula() {
        assertNull(mapper.toResponseDTO(null));
    }
}