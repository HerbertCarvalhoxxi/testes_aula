package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String placa;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private String proprietario;

    @Column(nullable = false)
    private Boolean ativo = true;

    public Veiculo() {}

    public Veiculo(String placa, String modelo, String proprietario) {
        this.placa = placa;
        this.modelo = modelo;
        this.proprietario = proprietario;
        this.ativo = true;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getProprietario() { return proprietario; }
    public void setProprietario(String proprietario) { this.proprietario = proprietario; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}