package com.example.demo.controller;

import com.example.demo.dto.VeiculoRequestDTO;
import com.example.demo.repository.VeiculoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class VeiculoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        veiculoRepository.deleteAll();
    }

    @Test
    void deveCadastrarEBuscarVeiculoComSucesso() throws Exception {
        VeiculoRequestDTO request = new VeiculoRequestDTO("BRA-2026", "Onix", "Carlos");

        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.placa").value("BRA-2026"))
                .andExpect(jsonPath("$.ativo").value(true));

        mockMvc.perform(get("/api/veiculos/BRA-2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value("BRA-2026"));
    }

    @Test
    void naoDevePermitirCadastrarPlacaDuplicada() throws Exception {
        VeiculoRequestDTO request = new VeiculoRequestDTO("DUP-0001", "Gol", "Ana");

        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveDesativarVeiculoComSucesso() throws Exception {
        VeiculoRequestDTO request = new VeiculoRequestDTO("DES-1234", "Palio", "Marcos");

        // Cadastra
        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Desativa (PATCH)
        mockMvc.perform(patch("/api/veiculos/DES-1234/desativar"))
                .andExpect(status().isNoContent());

        // Valida que ficou inativo
        mockMvc.perform(get("/api/veiculos/DES-1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    void deveBuscarVeiculosPorModeloComSucesso() throws Exception {
        veiculoRepository.save(new com.example.demo.model.Veiculo("MOD-001", "Civic Touring", "João"));
        veiculoRepository.save(new com.example.demo.model.Veiculo("MOD-002", "Civic EXL", "Maria"));
        veiculoRepository.save(new com.example.demo.model.Veiculo("MOD-003", "Corolla", "Pedro"));

        // Busca por termo "Civic"
        mockMvc.perform(get("/api/veiculos/modelo")
                        .param("termo", "Civic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveRetornarErroAoBuscarPlacaInexistente() throws Exception {
        mockMvc.perform(get("/api/veiculos/NAO-EXISTE"))
                .andExpect(status().isNotFound());
    }
}