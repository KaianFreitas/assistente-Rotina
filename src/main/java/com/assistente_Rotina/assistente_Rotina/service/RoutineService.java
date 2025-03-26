package com.assistente_Rotina.assistente_Rotina.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.io.IOException;
import java.security.GeneralSecurityException;

// A anotação @Service indica que esta classe é um serviço gerenciado pelo Spring.
@Service
public class RoutineService {

    // O Spring irá injetar automaticamente uma instância de GoogleCalendarService
    @Autowired
    private GoogleCalendarService googleCalendarService;

    // O Spring também irá injetar automaticamente uma instância de WeatherService
    @Autowired
    private WeatherService weatherService;

    // Método responsável por gerar o resumo diário da rotina com base nos eventos e previsão do tempo.
    public String getResumoDiario(String cidade) {
        // Inicializa as variáveis de eventos e previsão
        List<String> eventos = null;
        String previsao = null;

        try {
            // Obtém os eventos do dia a partir do Google Calendar
            eventos = googleCalendarService.getEventosDoDia();
        } catch (IOException | GeneralSecurityException e) {
            // Lida com a exceção e exibe uma mensagem de erro
            return "Erro ao buscar eventos: " + e.getMessage();
        }

        // Obtém a previsão do tempo para a cidade especificada
        previsao = weatherService.getPrevisaoDoTempo(cidade);

        // Utiliza um StringBuilder para construir a resposta final
        StringBuilder resumo = new StringBuilder();

        // Adiciona o título para o resumo do dia
        resumo.append("📅 Seu resumo do dia:\n\n");

        // Se não houver eventos, avisa ao usuário
        if (eventos.isEmpty()) {
            resumo.append("✅ Você não tem eventos programados para hoje.\n\n");
        } else {
            // Caso contrário, exibe os eventos do dia
            resumo.append("📌 Seus compromissos de hoje:\n");
            for (String evento : eventos) {
                resumo.append("   - ").append(evento).append("\n");
            }
            resumo.append("\n");
        }

        // Adiciona a previsão do tempo ao resumo
        resumo.append(previsao);

        // Retorna o resumo formatado
        return resumo.toString();
    }
}
