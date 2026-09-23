package com.example.demo.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EstufaServiceTest {

    private final EstufaService estufaService = new EstufaService();

    @ParameterizedTest(name = "[{index}] Temp: {0}°C -> Esperado: {1}")
    @CsvSource({
        "14, ALERTA", // Abaixo do limite
        "15, OK",     // Limite inferior exato
        "16, OK",     // Acima do limite inferior
        "29, OK",     // Abaixo do limite superior
        "30, OK",     // Limite superior exato
        "31, ALERTA"  // Acima do limite superior
    })
    void deveAvaliarLimitesDeTemperatura(int temperatura, String resultadoEsperado) {
        System.out.println("\n======================================================");
        System.out.println("[TESTE DE UNIDADE] Executando Análise de Valor Limite");
        System.out.println("-> Testando entrada: " + temperatura + "°C");
        System.out.println("-> Comportamento esperado: " + resultadoEsperado);
        
        String resultadoReal = estufaService.avaliarTemperatura(temperatura);
        
        System.out.println("-> Comportamento real obtido: " + resultadoReal);
        assertEquals(resultadoEsperado, resultadoReal);
    }
}