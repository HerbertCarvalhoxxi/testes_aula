package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String email;
    private String senha;
    private double saldo;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> carrinho = new ArrayList<>();

    public boolean temSaldo(double valor) {
        return this.saldo >= valor;
    }
    
    public void debitarSaldo(double valor) {
        this.saldo -= valor;
    }
}