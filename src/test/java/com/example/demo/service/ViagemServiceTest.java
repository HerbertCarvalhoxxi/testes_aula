package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.model.Viagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ViagemServiceTest {

    private ViagemService service;

    @BeforeEach
    void setUp() {
        service = new ViagemService();
    }

    // ==========================================
    // TESTES DA REGRA DE IDADE (Limite: 18)
    // ==========================================

    @Test
    void deveRecusarUsuarioAbaixoDaIdadePermitida() {
        // BVA (Valor Limite Inferior Inválido): 17 anos
        Usuario usuario = new Usuario("Joãozinho", 17);
        Viagem viagem = new Viagem("Paris", 40);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.adicionarPassageiro(viagem, usuario);
        });

        assertEquals("Usuário deve ter 18 anos ou mais.", exception.getMessage());
    }

    @Test
    void deveAceitarUsuarioComIdadeMinimaExata() {
        // BVA (Valor Limite Inferior Válido): 18 anos
        Usuario usuario = new Usuario("Maria", 18);
        Viagem viagem = new Viagem("Paris", 40);

        assertDoesNotThrow(() -> {
            service.adicionarPassageiro(viagem, usuario);
        });
        
        assertEquals(1, viagem.getPassageiros().size());
    }

    @Test
    void deveAceitarUsuarioBemAcimaDaIdadeMinima() {
        // PCE (Classe de Equivalência Válida): Qualquer valor > 18
        Usuario usuario = new Usuario("Carlos", 35);
        Viagem viagem = new Viagem("Paris", 40);

        assertDoesNotThrow(() -> {
            service.adicionarPassageiro(viagem, usuario);
        });
    }


    // ==========================================
    // TESTES DA REGRA DE CAPACIDADE (Limite: Capacidade Máx)
    // ==========================================

    @Test
    void deveAceitarPassageiroSeEstiverNoLimiteDaCapacidade() {
        // BVA (Limite Válido): Viagem com cap=2, já tem 1, adicionando o 2º
        Viagem viagem = new Viagem("Londres", 2);
        viagem.getPassageiros().add(new Usuario("Ana", 25)); // 1 passageiro

        Usuario novoUsuario = new Usuario("Pedro", 22);

        assertDoesNotThrow(() -> {
            service.adicionarPassageiro(viagem, novoUsuario);
        });
        
        assertEquals(2, viagem.getPassageiros().size());
    }

    @Test
    void deveRecusarPassageiroSeCapacidadeMaximaAtingida() {
        // BVA (Limite Inválido): Viagem com cap=2, já tem 2, tentando adicionar o 3º
        Viagem viagem = new Viagem("Londres", 2);
        viagem.getPassageiros().add(new Usuario("Ana", 25));
        viagem.getPassageiros().add(new Usuario("Pedro", 22));

        Usuario usuarioExcedente = new Usuario("Lucas", 30);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            service.adicionarPassageiro(viagem, usuarioExcedente);
        });

        assertEquals("A viagem atingiu a capacidade máxima.", exception.getMessage());
    }
}