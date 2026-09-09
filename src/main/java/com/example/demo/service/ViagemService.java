package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.model.Viagem;
import org.springframework.stereotype.Service;

@Service
public class ViagemService {

    public void adicionarPassageiro(Viagem viagem, Usuario usuario) {
        // Regra 1: Idade mínima
        if (usuario.getIdade() < 18) {
            throw new IllegalArgumentException("Usuário deve ter 18 anos ou mais.");
        }

        // Regra 2: Capacidade máxima
        if (viagem.getPassageiros().size() >= viagem.getCapacidadeMaxima()) {
            throw new IllegalStateException("A viagem atingiu a capacidade máxima.");
        }

        viagem.getPassageiros().add(usuario);
    }
}