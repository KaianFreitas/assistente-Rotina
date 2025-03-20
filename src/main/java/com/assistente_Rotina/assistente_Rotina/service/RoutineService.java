package com.assistente_Rotina.assistente_Rotina.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

 //Serviço responsável por fornecer um resumo diário da rotina do usuário.

@Service
public class RoutineService {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private WeatherService weatherService;


     //Obtém resumo da rotina diária com base nos eventos e previsão do tempo.

    public String getResumoDiario(String cidade) {
        // Obtém eventos do dia
        List<String> eventos = googleCalendarService.getEventosDoDia();

        // Obtém previsão do tempo
        String previsao = weatherService.getPrevisaoDoTempo(cidade);

        // Formata resposta final
        StringBuilder resumo = new StringBuilder();
        resumo.append("📅 Seu resumo do dia:\n\n");

        // Adiciona os eventos
        if (eventos.isEmpty()) {
            resumo.append("✅ Você não tem eventos programados para hoje.\n\n");
        } else {
            resumo.append("📌 Seus compromissos de hoje:\n");
            for (String evento : eventos) {
                resumo.append("   - ").append(evento).append("\n");
            }
            resumo.append("\n");
        }

        // Adiciona previsão do tempo
        resumo.append(previsao);

        return resumo.toString();
    }
}