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
        root.setStyle("-fx-background-color: #F5EBE2;");

        // ---- Cabeçalho ----
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setStyle("-fx-background-color: #02261A;");

        Label titulo = new Label("Relatórios de Vendas");
        titulo.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        titulo.setStyle("-fx-text-fill: white;");

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);

        Button btnVoltar = new Button("Voltar");
        btnVoltar.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #02261A; -fx-background-radius: 20; -fx-padding: 8 20 8 20; -fx-font-weight: bold; -fx-cursor: hand;");
        btnVoltar.setOnAction(e -> Login.mudarDeTela(primaryStage, new Vendas(usuarioLogado)));

        header.getChildren().addAll(titulo, espaco, btnVoltar);

        // ---- Conteúdo ----
        VBox card = new VBox(18);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12;");

        ComboBox<RelatorioStrategy> combo = new ComboBox<>();
        combo.getItems().addAll(controller.getEstrategias());
        combo.setConverter(new StringConverter<>() {
            @Override public String toString(RelatorioStrategy r) { return r == null ? "" : r.titulo(); }
            @Override public RelatorioStrategy fromString(String s) { return null; }
        });
        combo.setPrefWidth(300);
        combo.setOnAction(e -> mostrar(combo.getValue()));

        this.tabela = new TableView<>();
        VBox.setVgrow(this.tabela, Priority.ALWAYS);

        card.getChildren().addAll(new Label("Escolha o relatório:"), combo, this.tabela);

        VBox centro = new VBox(card);
        centro.setPadding(new Insets(25, 30, 30, 30));
        VBox.setVgrow(card, Priority.ALWAYS);

        root.setTop(header);
        root.setCenter(centro);

        // mostra o primeiro relatório já aberto
        combo.getSelectionModel().selectFirst();
        mostrar(combo.getValue());

        Scene scene = new Scene(root, 1200, 750);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        } catch (Exception ignored) {}
        primaryStage.setTitle("Sr. Pedrinho - Relatórios");
        primaryStage.setScene(scene);
        primaryStage.show();
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
