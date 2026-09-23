package com.example.demo.config;

import com.example.demo.model.Produto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ProdutoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepo, ProdutoRepository produtoRepo) {
        return args -> {
            // Usuário com saldo suficiente
            Usuario u1 = new Usuario();
            u1.setNome("Com Saldo");
            u1.setEmail("com_saldo@email.com");
            u1.setSenha("123456");
            u1.setSaldo(3000.0);
            usuarioRepo.save(u1);

            // Usuário sem saldo
            Usuario u2 = new Usuario();
            u2.setNome("Sem Saldo");
            u2.setEmail("sem_saldo@email.com");
            u2.setSenha("123456");
            u2.setSaldo(10.0);
            usuarioRepo.save(u2);

            // Produtos padronizados
            Produto p1 = new Produto();
            p1.setNome("Notebook Gamer");
            p1.setPreco(2500.0);
            p1.setEstoque(5);
            p1.setImagemUrl("https://picsum.photos/200");
            produtoRepo.save(p1);

            Produto p2 = new Produto();
            p2.setNome("Mouse Pad Grande");
            p2.setPreco(50.0);
            p2.setEstoque(15);
            p2.setImagemUrl("https://picsum.photos/200");
            produtoRepo.save(p2);

            // Produto sem estoque (para o fluxo 4)
            Produto p3 = new Produto();
            p3.setNome("Tênis Esgotado");
            p3.setPreco(300.0);
            p3.setEstoque(0);
            p3.setImagemUrl("https://picsum.photos/200");
            produtoRepo.save(p3);
        };
    }
}