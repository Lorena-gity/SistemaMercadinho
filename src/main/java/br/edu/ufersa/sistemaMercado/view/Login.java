package br.edu.ufersa.sistemaMercado.view;

import br.edu.ufersa.sistemaMercado.controller.LoginController;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {
        // ---- CONTÊINER PRINCIPAL ----
        HBox root = new HBox(60);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #F5EBE2;");

        // Deixa a tela invisível inicialmente para o efeito Fade-in
        root.setOpacity(0.0);

        // ---- LADO ESQUERDO: LOGO ----
        VBox leftSection = new VBox();
        leftSection.setAlignment(Pos.CENTER);

        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/LOGO.png"));
            ImageView logo = new ImageView(logoImage);
            logo.setFitWidth(350);
            logo.setPreserveRatio(true);
            leftSection.getChildren().add(logo);
        } catch (Exception e) {
            System.out.println("Erro ao carregar a imagem. Verifique se o caminho /images/LOGO.png está correto.");
        }

        // ---- LADO DIREITO: CARD DE LOGIN ----
        VBox loginCard = new VBox(25);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPrefSize(380, 450);
        loginCard.setMaxSize(380, 450);

        // Estilização do Card com a fonte Roboto do Google
        loginCard.setStyle(
                "-fx-font-family: 'Roboto', sans-serif;" +
                        "-fx-background-color: #FAF6F0;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 40;"
        );

        // Sombra suave para o Card
        DropShadow cardShadow = new DropShadow();
        cardShadow.setRadius(10);
        cardShadow.setOffsetX(0);
        cardShadow.setOffsetY(4);
        cardShadow.setColor(Color.web("#000000", 0.08));
        loginCard.setEffect(cardShadow);

        // Componentes do Card
        Label lblWelcome = new Label("Bem-Vindo!");
        lblWelcome.setFont(Font.font("Roboto", FontWeight.LIGHT, 26));
        lblWelcome.setStyle("-fx-text-fill: #111111; -fx-font-weight: medium;");
        VBox.setMargin(lblWelcome, new Insets(0, 0, 15, 0));

        // Input de Usuário (Estilizado para manter o placeholder visível ao focar)
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario");
        txtUsuario.setPrefHeight(45);
        txtUsuario.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-color: #E2DCD5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-padding: 0 12 0 12;" +
                        "-fx-prompt-text-visible: true;" // Faz o placeholder sumir só ao digitar
        );

        // Input de Senha (Estilizado para manter o placeholder visível ao focar)
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Senha");
        txtSenha.setPrefHeight(45);
        txtSenha.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-color: #E2DCD5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-padding: 0 12 0 12;" +
                        "-fx-prompt-text-visible: true;" // Faz o placeholder sumir só ao digitar
        );

        Button btnEntrar = new Button("Entrar");
        btnEntrar.setPrefHeight(45);
        btnEntrar.setMaxWidth(Double.MAX_VALUE);
        btnEntrar.setStyle(
                "-fx-background-color: #033020;" +
                        "-fx-text-fill: #FFFFFF;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        VBox.setMargin(btnEntrar, new Insets(10, 0, 0, 0));

        LoginController controller = new LoginController();

        // Definindo o evento de clique do botão
        btnEntrar.setOnAction(event -> {
            String usuarioDigitado = txtUsuario.getText();
            String senhaDigitada = txtSenha.getText();

            // Passa os dados e a janela atual (primaryStage) para o controller
            controller.autenticar(usuarioDigitado, senhaDigitada, primaryStage);
        });

        // Adiciona os componentes ao Card
        loginCard.getChildren().addAll(lblWelcome, txtUsuario, txtSenha, btnEntrar);

        // Adiciona as duas seções ao layout principal
        root.getChildren().addAll(leftSection, loginCard);

        // ---- CONFIGURAÇÃO DA JANELA ----
        Scene scene = new Scene(root, 1000, 650);

        // Importação da fonte Roboto diretamente do Google Fonts para o app
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");

        primaryStage.setTitle("Sr. Pedrinho - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Animação de entrada (Fade In)
        FadeTransition ftIn = new FadeTransition(Duration.millis(300), root);
        ftIn.setFromValue(0.0);
        ftIn.setToValue(1.0);
        ftIn.play();
    }

    // TRANSIÇÃO DE TELAS (ESTÁTICO PARA O CONTROLLER USAR)
    public static void mudarDeTela(Stage stage, Application novaTela) {
        javafx.scene.Node rootNode = stage.getScene().getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), rootNode);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            try {
                novaTela.start(stage);
            } catch (Exception ex) {
                System.out.println("Erro ao mudar de tela: " + ex.getMessage());
            }
        });

        fadeOut.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}