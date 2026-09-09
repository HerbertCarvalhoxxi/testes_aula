package com.example.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Viagem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destino;
    private int capacidadeMaxima;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Usuario> passageiros = new ArrayList<>();

    // Construtores, Getters e Setters
    public Viagem() {}
    public Viagem(String destino, int capacidadeMaxima) {
        this.destino = destino;
        this.capacidadeMaxima = capacidadeMaxima;
    }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public List<Usuario> getPassageiros() { return passageiros; }
    
    public Long getId() { return id; }
   
}