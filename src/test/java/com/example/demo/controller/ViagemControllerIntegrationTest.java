package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.model.Viagem;
import com.example.demo.repository.ViagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional // <- Importante: Desfaz as alterações no banco H2 após cada teste!
class ViagemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private ObjectMapper objectMapper; // Para transformar o objeto Usuario em JSON

    private Viagem viagemSalva;

    @BeforeEach
    void setUp() {
        // Prepara o banco H2 antes de cada teste com uma viagem de capacidade 2
        Viagem viagem = new Viagem("Tóquio", 2);
        viagemSalva = viagemRepository.save(viagem);
    }

    // ==========================================
    // CLASSE VÁLIDA: Idade >= 18 e Capacidade OK
    // ==========================================
    @Test
    void deveRetornarStatus200AoAdicionarPassageiroValido() throws Exception {
        Usuario usuarioValido = new Usuario("Maria", 18); // Valor Limite Válido
        String jsonPayload = objectMapper.writeValueAsString(usuarioValido);

        mockMvc.perform(post("/api/viagens/" + viagemSalva.getId() + "/passageiros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk()) // Espera HTTP 200
                .andExpect(content().string("Passageiro adicionado com sucesso!"));
    }

    // ==========================================
    // CLASSE INVÁLIDA: Idade < 18
    // ==========================================
    @Test
    void deveRetornarStatus400AoAdicionarPassageiroMenorDeIdade() throws Exception {
        Usuario usuarioInvalido = new Usuario("Joãozinho", 17); // Valor Limite Inválido
        String jsonPayload = objectMapper.writeValueAsString(usuarioInvalido);

        mockMvc.perform(post("/api/viagens/" + viagemSalva.getId() + "/passageiros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isBadRequest()) // Espera HTTP 400
                .andExpect(content().string("Usuário deve ter 18 anos ou mais."));
    }

    // ==========================================
    // CLASSE INVÁLIDA: Capacidade Excedida
    // ==========================================
    @Test
    void deveRetornarStatus409QuandoViagemEstiverCheia() throws Exception {
        // 1. Enche a viagem até o limite (Capacidade = 2)
        viagemSalva.getPassageiros().add(new Usuario("Ana", 25));
        viagemSalva.getPassageiros().add(new Usuario("Carlos", 30));
        viagemRepository.save(viagemSalva);

        // 2. Tenta adicionar o 3º passageiro
        Usuario usuarioExcedente = new Usuario("Pedro", 22);
        String jsonPayload = objectMapper.writeValueAsString(usuarioExcedente);

        mockMvc.perform(post("/api/viagens/" + viagemSalva.getId() + "/passageiros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isConflict()) // Espera HTTP 409 (Conflito de estado)
                .andExpect(content().string("A viagem atingiu a capacidade máxima."));
    }
}