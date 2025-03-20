package com.assistente_Rotina.assistente_Rotina.controller;

import com.assistente_Rotina.assistente_Rotina.service.GoogleCalendarService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

//Controller REST para acessar os eventos
@RestController
@RequestMapping("/api/calendar")
public class GoogleCalendarController {

    @GetMapping("/eventos")
    public String getEventosDoDia() {
        try {
            GoogleCalendarService.listarEventosDoDia();
            return "Eventos carregados.";
        } catch (IOException | GeneralSecurityException e) {
            return "Erro ao buscar eventos: " + e.getMessage();
        }
    }
}

