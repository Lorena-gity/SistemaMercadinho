package br.edu.ufersa.sistemaMercado.view;

import br.edu.ufersa.sistemaMercado.controller.RelatoriosController;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;
import br.edu.ufersa.sistemaMercado.model.session.SessaoUsuario;
import br.edu.ufersa.sistemaMercado.model.strategy.RelatorioStrategy;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Relatorios extends Application {

    private final RelatoriosController controller = new RelatoriosController();
    private Usuario usuarioLogado;
    private TableView<String[]> tabela;

    public Relatorios() {
        this.usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado();
    }

    public Relatorios(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F4F5F4; -fx-font-family: 'Roboto', sans-serif;");
        // HEADER
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setStyle("-fx-background-color: #02261A;");

        HBox logoETituloContainer = new HBox(12);
        logoETituloContainer.setAlignment(Pos.CENTER_LEFT);
        try {
            ImageView logo = criarIcone("/images/SEC_LOGO.png", 47);
            if (logo != null) {
                logo.setTranslateY(-5);
                logoETituloContainer.getChildren().add(logo);
            }
        } catch (Exception ignored) {}
        VBox titleBox = new VBox(2);

        Label lblTitulo = new Label("Mercadinho do Seu Pedrinho");
        lblTitulo.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        lblTitulo.setStyle("-fx-text-fill: white;");

        Label lblSubtitulo = new Label("Gerencie suas vendas");
        lblSubtitulo.setFont(Font.font("Roboto", FontWeight.NORMAL, 10));
        lblSubtitulo.setStyle("-fx-text-fill: #A3B8B0;");

        titleBox.getChildren().addAll(lblTitulo, lblSubtitulo);
        logoETituloContainer.getChildren().add(titleBox);

        Region spacerHeader = new Region();
        HBox.setHgrow(spacerHeader, Priority.ALWAYS);

        HBox usuarioBox = new HBox(15);
        usuarioBox.setAlignment(Pos.CENTER_RIGHT);

        Label lblFuncionario = new Label(usuarioLogado.getNome() + " (Gerente)");
        lblFuncionario.setStyle("-fx-text-fill: white;" + "-fx-background-color: #033B29;" + "-fx-padding: 8 15 8 15;" + "-fx-background-radius: 20;" + "-fx-font-weight: bold;");
        try {
            lblFuncionario.setGraphic(criarIcone("/images/iconUsuario.png",14));
        } catch (Exception ignored){}
        Button btnSair = new Button("Sair");
        btnSair.setStyle("-fx-background-color: white;" + "-fx-text-fill: #02261A;" + "-fx-background-radius: 20;" + "-fx-padding: 8 20 8 20;" + "-fx-font-weight: bold;" + "-fx-cursor: hand;");
        btnSair.setOnAction(e -> Login.mudarDeTela(primaryStage,new Login()));

        usuarioBox.getChildren().addAll(lblFuncionario, btnSair);
        header.getChildren().addAll(logoETituloContainer, spacerHeader, usuarioBox);
        root.setTop(header);
        // CENTRO
        VBox centroContainer = new VBox(20);
        centroContainer.setPadding(new Insets(20,30,20,30));
        // NAVBAR
        HBox navBar = new HBox(20);
        navBar.setStyle("-fx-border-color: #EAEAEA;" + "-fx-border-width: 0 0 1 0;" + "-fx-padding: 0 0 10 0;");

        Label tabProdutos = new Label("Produtos");
        tabProdutos.setStyle("-fx-text-fill: #A0A5A2;" + "-fx-font-weight: bold;" + "-fx-padding: 0 10 5 10;" + "-fx-cursor: hand;");
        try {
            tabProdutos.setGraphic(criarIcone("/images/iconProduto_OFF.png",14));
        } catch (Exception ignored){}
        tabProdutos.setOnMouseClicked(e -> Login.mudarDeTela(primaryStage, new Vendas(usuarioLogado)));

        Label tabFuncionarios = new Label("Funcionários");
        tabFuncionarios.setStyle("-fx-text-fill: #A0A5A2;" + "-fx-font-weight: bold;" + "-fx-padding: 0 10 5 10;" + "-fx-cursor: hand;");
        try {
            tabFuncionarios.setGraphic(criarIcone("/images/iconFuncionario.png",14));
        } catch (Exception ignored){}
        tabFuncionarios.setOnMouseClicked(e -> Login.mudarDeTela(primaryStage, new GerenciarFuncionarios(usuarioLogado)));

        Label tabRelatorios = new Label("Relatórios");
        tabRelatorios.setStyle("-fx-text-fill: #02261A;" + "-fx-font-weight: bold;" + "-fx-border-color: #02261A;" + "-fx-border-width: 0 0 3 0;" + "-fx-padding: 0 10 5 10;");
        try {
            tabRelatorios.setGraphic(criarIcone("/images/iconRelatorio_ON.png",14));
        } catch (Exception ignored){}
        navBar.getChildren().addAll(tabProdutos, tabFuncionarios, tabRelatorios);
        // CARD
        VBox card = new VBox(18);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white;" + "-fx-background-radius: 12;");

        ComboBox<RelatorioStrategy> combo = new ComboBox<>();
        combo.getItems().addAll(controller.getEstrategias());
        combo.setConverter(new StringConverter<>() {
            @Override
            public String toString(RelatorioStrategy r) {
                return r == null ? "" : r.titulo();
            }

            @Override
            public RelatorioStrategy fromString(String s) {
                return null;
            }
        });

        combo.setPrefWidth(300);
        combo.setOnAction(e -> mostrar(combo.getValue()));

        tabela = new TableView<>();
        VBox.setVgrow(tabela, Priority.ALWAYS);

        Label lblEscolha = new Label("Escolha o relatório:");
        lblEscolha.setStyle("-fx-font-weight: bold;");

        card.getChildren().addAll(lblEscolha, combo, tabela);

        VBox.setVgrow(card, Priority.ALWAYS);

        centroContainer.getChildren().addAll(navBar, card);
        root.setCenter(centroContainer);

        combo.getSelectionModel().selectFirst();
        mostrar(combo.getValue());

        Scene scene = new Scene(root,1200,750);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        primaryStage.setTitle("Sr. Pedrinho - Relatórios");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView criarIcone(String caminho, double largura) {
        try {
            Image img = new Image(getClass().getResourceAsStream(caminho));
            ImageView view = new ImageView(img);
            view.setFitWidth(largura);
            view.setPreserveRatio(true);
            return view;
        } catch (Exception e) {
            return null;
        }
    }

    private void mostrar(RelatorioStrategy estrategia) {
        tabela.getColumns().clear();
        if (estrategia == null) {
            return;
        }
        String[] colunas = estrategia.colunas();
        for (int i = 0; i < colunas.length; i++) {
            final int indice = i;
            TableColumn<String[], String> coluna = new TableColumn<>(colunas[i]);
            coluna.setCellValueFactory(dados -> new SimpleStringProperty(dados.getValue()[indice]));
            tabela.getColumns().add(coluna);
        }
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setItems(FXCollections.observableArrayList(estrategia.linhas()));
    }
}