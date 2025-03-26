package com.assistente_Rotina.assistente_Rotina.controller;

import com.assistente_Rotina.assistente_Rotina.service.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

//Controller REST para acessar os eventos
@RestController
@RequestMapping("/api/calendar")
public class GoogleCalendarController {

    @Autowired
    private GoogleCalendarService googleCalendarService;  // Injeção do GoogleCalendarService

    // Endpoint para obter os eventos do dia
    @GetMapping("/eventos")
    public List<String> getEventosDoDia() {
        try {
            // Chama o serviço para obter os eventos do dia
            List<String> eventos = googleCalendarService.getEventosDoDia();

            // Retorna os eventos como uma lista
            return eventos;
        } catch (IOException | GeneralSecurityException e) {
            // Retorna uma mensagem de erro se ocorrer uma exceção
            return List.of("Erro ao buscar eventos: " + e.getMessage());
        }
    }
}
