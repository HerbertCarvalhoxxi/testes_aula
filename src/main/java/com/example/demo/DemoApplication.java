package com.example.demo;

import com.example.demo.model.Viagem;
import com.example.demo.repository.ViagemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // Este Bean é executado automaticamente assim que a aplicação inicia
    @Bean
    public CommandLineRunner initDatabase(ViagemRepository viagemRepository) {
        return args -> {
            // Cria a viagem inicial (Que receberá o ID 1 no banco H2) com capacidade para 40 pessoas
            Viagem viagemIncial = new Viagem("Paris - Aula de Testes", 40);
            viagemRepository.save(viagemIncial);
            
            System.out.println("✅ Banco inicializado: Viagem ID 1 criada com sucesso para os testes E2E e manuais!");
        };
    }
}