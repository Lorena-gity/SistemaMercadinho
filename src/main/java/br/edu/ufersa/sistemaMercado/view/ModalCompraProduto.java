package br.edu.ufersa.sistemaMercado.view;

import br.edu.ufersa.sistemaMercado.controller.VendasController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class ModalCompraProduto {
    private final VendasController controller;

    public ModalCompraProduto(VendasController controller) {
        this.controller = controller;
    }

    public void abrir(Stage ownerStage, Runnable onSuccess) {
        Stage modalStage = new Stage();
        Login.aplicarIcone(modalStage);

        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(ownerStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);
        // ESCURECER FUNDO
        Parent rootDaTelaPrincipal = ownerStage.getScene().getRoot();
        ColorAdjust escurecerFundo = new ColorAdjust();
        escurecerFundo.setBrightness(-0.5);
        GaussianBlur desfoqueFundo = new GaussianBlur(5);
        desfoqueFundo.setInput(escurecerFundo);
        rootDaTelaPrincipal.setEffect(desfoqueFundo);
        // CONTAINER PRINCIPAL
        VBox containerModal = new VBox(25);
        containerModal.getStyleClass().add("modal-container");
        containerModal.setPadding(new Insets(30, 40, 35, 40));
        containerModal.setPrefWidth(480);
        // FECHAR
        HBox boxFechar = new HBox();
        boxFechar.setAlignment(Pos.CENTER_RIGHT);
        Button btnFecharX = new Button("✕");
        btnFecharX.getStyleClass().add("btn-fechar-modal");
        btnFecharX.setOnAction(e -> modalStage.close());
        boxFechar.getChildren().add(btnFecharX);
        // TITULO
        Label lblTitulo = new Label("Compra de Produtos");
        lblTitulo.getStyleClass().add("modal-titulo");
        VBox.setMargin(lblTitulo, new Insets(0, 0, 20, 0));
        // GRID
        GridPane gridCampos = new GridPane();
        gridCampos.setHgap(20);
        gridCampos.setVgap(15);
        // NOME
        Label lblNome = new Label("Nome do Produto");
        lblNome.getStyleClass().add("modal-label");
        TextField txtNome = new TextField();
        txtNome.getStyleClass().add("modal-input");
        gridCampos.add(lblNome, 0, 0);
        gridCampos.add(txtNome, 0, 1);
        // PREÇO
        Label lblPreco = new Label("Valor Unitário");
        lblPreco.getStyleClass().add("modal-label");
        TextField txtPreco = new TextField();
        txtPreco.getStyleClass().add("modal-input");
        gridCampos.add(lblPreco, 1, 0);
        gridCampos.add(txtPreco, 1, 1);
        // CATEGORIA
        Label lblCategoria = new Label("Categoria");
        lblCategoria.getStyleClass().add("modal-label");
        ComboBox<String> cbCategoria = new ComboBox<>(controller.listarCategorias());
        cbCategoria.getStyleClass().add("modal-combo");
        cbCategoria.setPrefWidth(200);
        gridCampos.add(lblCategoria, 0, 2);
        gridCampos.add(cbCategoria, 0, 3);
        // TIPO
        Label lblTipo = new Label("Tipo de Venda");
        lblTipo.getStyleClass().add("modal-label");
        ComboBox<String> cbTipo = new ComboBox<>(controller.listarFormasVenda());
        cbTipo.getStyleClass().add("modal-combo");
        cbTipo.setPrefWidth(200);
        gridCampos.add(lblTipo, 1, 2);
        gridCampos.add(cbTipo, 1, 3);
        // QUANTIDADE
        VBox boxQuantidade = new VBox(5);
        Label lblQtd = new Label("Quantidade");
        lblQtd.getStyleClass().add("modal-label");
        TextField txtQuantidade = new TextField();
        txtQuantidade.getStyleClass().add("modal-input");
        boxQuantidade.getChildren().addAll(lblQtd, txtQuantidade);
        // SALVAR
        Button btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("btn-salvar-modal");
        btnSalvar.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String precoStr = txtPreco.getText().trim();
            String categoria = cbCategoria.getValue();
            String tipo = cbTipo.getValue();
            String qtdStr = txtQuantidade.getText().trim();
            if (nome.isEmpty() || precoStr.isEmpty() || qtdStr.isEmpty() || categoria == null || tipo == null) {
                Vendas.mostrarAlerta("Campos Incompletos", "Preencha todas as informações.", Alert.AlertType.ERROR);
                return;
            }
            try {
                double preco = Double.parseDouble(precoStr.replace(",", "."));
                double quantidade = Double.parseDouble(qtdStr.replace(",", "."));
                boolean sucesso = controller.salvarProdutoComprado(nome, preco, categoria, tipo, quantidade);

                if (sucesso) {
                    Vendas.mostrarAlerta("Sucesso", "Produto cadastrado com sucesso.", Alert.AlertType.INFORMATION);
                    modalStage.close();

                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Vendas.mostrarAlerta("Erro", "Não foi possível salvar o produto.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException ex) {
                Vendas.mostrarAlerta("Dados Inválidos", "Preço e quantidade devem ser numéricos.", Alert.AlertType.ERROR);
            }
        });
        containerModal.getChildren().addAll(boxFechar, lblTitulo, gridCampos, boxQuantidade, btnSalvar);

        Scene scene = new Scene(containerModal);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        modalStage.setScene(scene);
        modalStage.setOnShown(e -> {modalStage.setX(ownerStage.getX() + (ownerStage.getWidth() - modalStage.getWidth()) / 2);
            modalStage.setY(ownerStage.getY() + (ownerStage.getHeight() - modalStage.getHeight()) / 2);
        });
        modalStage.setOnHidden(e -> rootDaTelaPrincipal.setEffect(null));
        modalStage.showAndWait();
    }
}