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
        // CARREGAMENTO DA FONTE
        Font.loadFont(getClass().getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/Roboto-Bold.ttf").toExternalForm(), 12);

        HBox root = new HBox(60);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.getStyleClass().add("login-root");
        root.setOpacity(0.0); // Deixa a tela invisível inicialmente para o efeito Fade-in
        // LOGO
        VBox leftSection = new VBox();
        leftSection.setAlignment(Pos.CENTER);
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/LOGO.png"));
            ImageView logo = new ImageView(logoImage);
            logo.setTranslateY(-35);
            logo.setFitWidth(350);
            logo.setPreserveRatio(true);

            leftSection.getChildren().add(logo);
        } catch (Exception e) {
            System.out.println("Erro ao carregar a imagem. Verifique se o caminho /images/LOGO.png está correto.");
        }
        // CARD DE LOGIN
        VBox loginCard = new VBox(25);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPrefSize(380, 450);
        loginCard.setMaxSize(380, 450);
        loginCard.getStyleClass().add("login-card");
        // SOMBRA DO CARD
        DropShadow cardShadow = new DropShadow();
        cardShadow.setRadius(10);
        cardShadow.setOffsetX(0);
        cardShadow.setOffsetY(4);
        cardShadow.setColor(Color.web("#000000", 0.08));
        loginCard.setEffect(cardShadow);
        // COMPONENTES DO CARD
        Label lblWelcome = new Label("Bem-Vindo!");
        lblWelcome.setFont(Font.font("Roboto", FontWeight.LIGHT, 26));
        lblWelcome.getStyleClass().add("title-label");
        VBox.setMargin(lblWelcome, new Insets(0, 0, 15, 0));
        // INPUT USUARIO
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario");
        txtUsuario.getStyleClass().add("input-field");
        // INPUT SENHA
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Senha");
        txtSenha.getStyleClass().add("input-field");
        // BOTÃO
        Button btnEntrar = new Button("Entrar");
        btnEntrar.setMaxWidth(Double.MAX_VALUE);
        btnEntrar.getStyleClass().add("login-button");

        VBox.setMargin(btnEntrar, new Insets(10, 0, 0, 0));
        // Iniciando controller de login
        LoginController controller = new LoginController();
        // Definindo o evento de clique do botão
        btnEntrar.setOnAction(event -> {
            String usuarioDigitado = txtUsuario.getText();
            String senhaDigitada = txtSenha.getText();
            // Passando dados e a janela atual (primaryStage) para o controller
            controller.autenticar(usuarioDigitado, senhaDigitada, primaryStage);
        });
        // Adicionando componentes ao Card
        loginCard.getChildren().addAll(
                lblWelcome,
                txtUsuario,
                txtSenha,
                btnEntrar
        );
        // Adicionando as duas seções ao layout principal
        root.getChildren().addAll(leftSection, loginCard);
        // CONFIGURAÇÃO DA JANELA
        Scene scene = new Scene(root, 1200, 750);
        scene.getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm()
        );
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
        try {
            novaTela.start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}