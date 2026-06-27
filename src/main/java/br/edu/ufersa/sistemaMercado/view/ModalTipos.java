package br.edu.ufersa.sistemaMercado.view;

import br.edu.ufersa.sistemaMercado.controller.TiposController;
import br.edu.ufersa.sistemaMercado.model.entities.TipoProduto;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class ModalTipos {

    private final TiposController controller = new TiposController();
    private VBox lista;

    public void abrir(Stage owner, Runnable aoFechar) {
        Stage modal = new Stage();
        Login.aplicarIcone(modal);
        modal.initOwner(owner);
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle("Gerenciar Categorias");

        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.setStyle("-fx-background-color: #FFFFFF;");

        Label titulo = new Label("Categorias");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #02261A;");

        HBox addBox = new HBox(8);
        TextField campoNome = new TextField();
        campoNome.setPromptText("Nova categoria");
        HBox.setHgrow(campoNome, Priority.ALWAYS);
        Button btnAdd = new Button("Adicionar");
        btnAdd.setStyle("-fx-background-color: #02261A; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btnAdd.setOnAction(e -> {
            String nome = campoNome.getText() == null ? "" : campoNome.getText().trim();
            if (nome.isEmpty()) return;
            if (controller.criar(nome)) {
                campoNome.clear();
                recarregar();
            } else {
                alerta("Não foi possível adicionar (categoria já existe?).");
            }
        });
        addBox.getChildren().addAll(campoNome, btnAdd);

        this.lista = new VBox(8);
        ScrollPane scroll = new ScrollPane(lista);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(280);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        Button btnFechar = new Button("Fechar");
        btnFechar.setStyle("-fx-background-color: transparent; -fx-border-color: #02261A; -fx-border-radius: 6; -fx-text-fill: #02261A; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btnFechar.setOnAction(e -> modal.close());

        container.getChildren().addAll(titulo, addBox, scroll, btnFechar);
        recarregar();

        modal.setScene(new Scene(container, 420, 480));
        modal.showAndWait();
        if (aoFechar != null) {
            aoFechar.run();
        }
    }

    private void recarregar() {
        lista.getChildren().clear();
        for (TipoProduto tipo : controller.listar()) {
            HBox linha = new HBox(10);
            linha.setAlignment(Pos.CENTER_LEFT);
            linha.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 6; -fx-padding: 10;");

            Label nome = new Label(tipo.getNome());
            HBox.setHgrow(nome, Priority.ALWAYS);
            nome.setMaxWidth(Double.MAX_VALUE);

            Button btnRenomear = new Button("Renomear");
            btnRenomear.setStyle("-fx-background-color: #8bc34a; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
            btnRenomear.setOnAction(e -> {
                TextInputDialog dialog = new TextInputDialog(tipo.getNome());
                dialog.setHeaderText(null);
                dialog.setTitle("Renomear categoria");
                dialog.setContentText("Novo nome:");
                Optional<String> resposta = dialog.showAndWait();
                if (resposta.isPresent() && !resposta.get().trim().isEmpty()) {
                    if (controller.renomear(tipo, resposta.get().trim())) {
                        recarregar();
                    } else {
                        alerta("Não foi possível renomear.");
                    }
                }
            });

            Button btnExcluir = new Button("Excluir");
            btnExcluir.setStyle("-fx-background-color: #e08a4a; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
            btnExcluir.setOnAction(e -> {
                if (controller.excluir(tipo.getNome())) {
                    recarregar();
                } else {
                    alerta("Não foi possível excluir (categoria em uso por produtos?).");
                }
            });

            linha.getChildren().addAll(nome, btnRenomear, btnExcluir);
            lista.getChildren().add(linha);
        }
    }

    private void alerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING, mensagem);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
