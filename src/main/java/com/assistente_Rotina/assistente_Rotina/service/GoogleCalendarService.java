package com.assistente_Rotina.assistente_Rotina.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.Model.*;
import com.google.auth.Credentials;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;
import org.w3c.dom.events.Event;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// responsavel por buscar e autenticar eventos no calendario google.
public class GoogleCalendarService {
    private static final String APPLICATION_NAME = "Assistente de rotina";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "TOKENS";

    //escopos necessários para acessar o calendário google
    private static final List<String> SCOPES = Collections.SingletonList(CalendarScopes.CALENDASR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    //retorna as credenciais para autenticação na api
    private static Credential getCredentials(HttpTransport httpTransport) throws IOException {
        InputStream in = GoogleCalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Arquivo de credenciais não encontrado" + CREDENTIALS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new
                GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAcessType("offline")
                .build();

        return AuthorizationCodeInstaledApp (flow, new LocalReceiver()).authorize("user");
    }

    //obtem eventos do dia no calendario google
    public static void listarEventosDoDia() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport();
        Credential credential = getCredentials(httpTransport);
        Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        //define periodo para buscar eventos
        DataTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("Primary")
                .setTimeMin(now).setMaxResults(10)
                .setOrderBy("start time")
                .setSingleEvents(true)
                .execute();

        List<Event> items = events.getItems();

        if (items.isEmpty()) {
            System.out.println("Nenhum evento para hoje");
            for (Event event : items) {
                DateTime start = event.getStart().getDataTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.println("%s - %s%n", start, event.getSummary());
            }
        }
    }
}
