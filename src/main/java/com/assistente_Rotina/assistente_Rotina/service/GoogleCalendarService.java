package com.assistente_Rotina.assistente_Rotina.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Responsável por autenticar e manipular eventos no Google Calendar.
@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "Assistente de Rotina";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    // Escopos para acessar o Google Calendar.
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/client_secret_59356497731-bp92kll0u6ec1ed04726i1urv9vq86ev.apps.googleusercontent.com.json";

    // Retorna as credenciais para autenticação na API.
    private static Credential getCredentials(HttpTransport httpTransport) throws IOException {
        InputStream in = GoogleCalendarService.class.getClassLoader().getResourceAsStream("client_secret_59356497731-bp92kll0u6ec1ed04726i1urv9vq86ev.apps.googleusercontent.com.json");
        if (in == null) {
            throw new FileNotFoundException("Arquivo de credenciais não encontrado: " + CREDENTIALS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    // Metodo para listar eventos do Google Calendar
    public List<String> listarEventosDoDia() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(httpTransport);

        Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Define o período para buscar eventos
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary") // Calendário principal
                .setTimeMin(now)
                .setMaxResults(10)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> items = events.getItems();
        List<String> eventos = new ArrayList<>();

        if (items.isEmpty()) {
            eventos.add("Nenhum evento encontrado para hoje.");
        } else {
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                eventos.add(start + " - " + event.getSummary());
            }
        }

        return eventos;
    }

    // Metodo para criar um evento no Google Calendar
    public static void createEvent(String eventName) throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(httpTransport);

        Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Cria o evento
        Event event = new Event()
                .setSummary(eventName) // Nome do evento
                .setLocation("Localização do evento") // Local do evento
                .setDescription("Descrição do evento");

        // Define a data e a hora de início e término
        DateTime startDateTime = new DateTime("2025-04-01T09:00:00-07:00"); // Data e hora de início
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Sao_Paulo"); // Defina o fuso horário
        event.setStart(start);

        DateTime endDateTime = new DateTime("2025-04-01T10:00:00-07:00"); // Data e hora de término
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Sao_Paulo"); // Defina o fuso horário
        event.setEnd(end);

        // Insere o evento no Google Calendar
        service.events().insert("primary", event).execute();
        System.out.println("Evento criado: " + event.getSummary());
    }

    // Metodo para retornar eventos do dia (usando a API)
    public List<String> getEventosDoDia() throws IOException, GeneralSecurityException {
        return listarEventosDoDia();
    }
}
