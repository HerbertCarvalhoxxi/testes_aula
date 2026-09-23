package com.example.demo.controller;

import com.example.demo.service.EstufaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estufa")
@CrossOrigin(origins = "*")
public class EstufaController {

    private static final Logger log = LoggerFactory.getLogger(EstufaController.class);
    private final EstufaService estufaService;

    public EstufaController(EstufaService estufaService) {
        this.estufaService = estufaService;
    }

    @GetMapping("/leitura")
    public String registrarLeitura(@RequestParam int temperatura) {
        log.info("[CONTROLLER] Requisição HTTP recebida. Temperatura informada: {}°C", temperatura);
        return estufaService.avaliarTemperatura(temperatura);
    }
}