package com.example.demo.controller;

import com.example.demo.service.EstufaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstufaController.class)
@Import(EstufaService.class)
class EstufaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveTestarIntegracaoNoLimiteInferior() throws Exception {
        System.out.println("\n[TESTE DE INTEGRAÇÃO] Simulando chamada na URL /api/estufa/leitura?temperatura=15");
        
        mockMvc.perform(get("/api/estufa/leitura").param("temperatura", "15"))
               .andDo(print()) // ISSO AQUI GERA UM LOG INCRÍVEL PARA AULA!
               .andExpect(status().isOk())
               .andExpect(content().string("OK"));
    }
}