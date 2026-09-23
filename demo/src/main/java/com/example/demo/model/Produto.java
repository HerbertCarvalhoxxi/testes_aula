package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "produtos")
@Data
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private double preco;
    private String imagemUrl;
    private int estoque;

    public boolean temEstoque(int quantidadeDesejada) {
        return this.estoque >= quantidadeDesejada;
    }

    public void decrementarEstoque(int quantidade) {
        this.estoque -= quantidade;
    }
}