package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EstufaService {
    
    private static final Logger log = LoggerFactory.getLogger(EstufaService.class);

    public String avaliarTemperatura(int temperatura) {
        log.info("[SERVICE] Analisando temperatura: {}°C", temperatura);
        
        if (temperatura >= 15 && temperatura <= 30) {
            log.info("[SERVICE] Regra Aprovada: Temperatura dentro do limite (15 a 30).");
            return "OK";
        }
        
        log.warn("[SERVICE] Regra Violada: Temperatura FORA do limite!");
        return "ALERTA";
    }
}