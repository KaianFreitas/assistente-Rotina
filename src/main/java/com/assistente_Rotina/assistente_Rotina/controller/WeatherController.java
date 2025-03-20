package com.assistente_Rotina.assistente_Rotina.controller;

import com.assistente_Rotina.assistente_Rotina.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;


 //Controller REST para obter a previsão do tempo.

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;


     //obter a previsão do tempo para uma cidade.
     //Exemplo de uso: GET /api/weather?cidade=Brasilia

    @GetMapping
    public String getPrevisao(@RequestParam String cidade) {
        return weatherService.getPrevisaoDoTempo(cidade);
    }
}