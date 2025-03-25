package com.assistente_Rotina.assistente_Rotina.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Layout para os elementos
        VBox vbox = new VBox();

        // Criação de um campo de texto para a entrada do usuário
        Label label = new Label("Digite o evento:");
        TextField eventField = new TextField();

        // Botão para enviar o evento
        Button btnSubmit = new Button("Enviar");

        btnSubmit.setOnAction(event -> {
            String eventName = eventField.getText();
            System.out.println("Evento: " + eventName);
            // A partir daqui, você pode integrar com o Google Calendar
        });

        // Layout horizontal para a label e campo de texto
        HBox hbox = new HBox(label, eventField);
        vbox.getChildren().addAll(hbox, btnSubmit);

        // Configuração da cena e janela
        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setTitle("Assistente de Rotina");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Lança a aplicação JavaFX
    }
}
