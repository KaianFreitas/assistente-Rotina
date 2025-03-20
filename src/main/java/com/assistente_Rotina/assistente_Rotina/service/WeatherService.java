package com.assistente_Rotina.assistente_Rotina.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

//Serviço para obter a previsão do tempo usando a API OpenWeatherMap.
@Service
public class WeatherService {

    private static final String API_KEY = "c930961a207400f541f0cdae9c19eacb";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

     // Obtém a previsão do tempo para uma cidade específica.
     //@param cidade Nome da cidade
     //@return Descrição do clima e temperatura

    public String getPrevisaoDoTempo(String cidade) {
        RestTemplate restTemplate = new RestTemplate();

        // Monta a URL da requisição
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", cidade)
                .queryParam("appid", API_KEY)
                .queryParam("units", "metric") // Temperatura em Celsius
                .queryParam("lang", "pt") // Respostas em português
                .toUriString();

        try {
            // Faz a requisição HTTP e obtém a resposta como String
            String response = restTemplate.getForObject(url, String.class);

            // Converte a resposta JSON em um objeto manipulável
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            // Extrai informações do JSON
            String descricao = root.path("weather").get(0).path("description").asText();
            double temperatura = root.path("main").path("temp").asDouble();

            return String.format("🌤️ O tempo em %s está %s com temperatura de %.1f°C.", cidade, descricao, temperatura);

        } catch (IOException e) {
            return "❌ Erro ao buscar previsão do tempo.";
        }
    }
}