package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int idade;

    // Construtores, Getters e Setters
    public Usuario() {}
    public Usuario(String nome, int idade) { this.nome = nome; this.idade = idade; }
    public int getIdade() { return idade; }
    // ... outros getters/setters
}
