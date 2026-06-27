package br.edu.ufersa.sistemaMercado.view;

import br.edu.ufersa.sistemaMercado.controller.VendasController;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
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

public class ModalEditarProduto {

    private final VendasController controller;
    private final Produto produto;

    public ModalEditarProduto(
            VendasController controller,
            Produto produto
    ) {
        this.controller = controller;
        this.produto = produto;
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
        // CONTAINER
        VBox containerModal = new VBox(25);
        containerModal.getStyleClass().add("modal-container");
        containerModal.setPadding(new Insets(30,40,35,40));
        containerModal.setPrefWidth(480);
        // FECHAR
        HBox boxFechar = new HBox();
        boxFechar.setAlignment(Pos.CENTER_RIGHT);
        Button btnFecharX = new Button("✕");
        btnFecharX.getStyleClass().add("btn-fechar-modal");
        btnFecharX.setOnAction(e -> modalStage.close());
        boxFechar.getChildren().add(btnFecharX);
        // TÍTULO
        Label lblTitulo = new Label("Editar Produto");
        VBox.setMargin(lblTitulo, new Insets(0, 0, 20, 0));
        lblTitulo.getStyleClass().add("modal-titulo");
        // GRID
        GridPane gridCampos = new GridPane();
        gridCampos.setHgap(20);
        gridCampos.setVgap(15);
        // NOME
        Label lblNome = new Label("Nome do Produto");
        lblNome.getStyleClass().add("modal-label");
        TextField txtNome = new TextField(produto.getNome());
        txtNome.getStyleClass().add("modal-input");
        gridCampos.add(lblNome,0,0);
        gridCampos.add(txtNome,0,1);
        // PREÇO
        Label lblPreco = new Label("Valor Unitário");
        lblPreco.getStyleClass().add("modal-label");
        TextField txtPreco = new TextField(String.valueOf(produto.getPreco()));
        txtPreco.getStyleClass().add("modal-input");
        gridCampos.add(lblPreco,1,0);
        gridCampos.add(txtPreco,1,1);
        // CATEGORIA
        Label lblCategoria = new Label("Categoria");
        lblCategoria.getStyleClass().add("modal-label");
        ComboBox<String> cbCategoria = new ComboBox<>(controller.listarCategorias());

        cbCategoria.getStyleClass().add("modal-combo");
        cbCategoria.setPrefWidth(200);
        if(produto.getTipo() != null){
            cbCategoria.setValue(produto.getTipo().getNome());
        }
        gridCampos.add(lblCategoria,0,2);
        gridCampos.add(cbCategoria,0,3);
        // FORMA DE VENDA
        Label lblForma = new Label("Tipo");
        lblForma.getStyleClass().add("modal-label");
        ComboBox<String> cbForma = new ComboBox<>(controller.listarFormasVenda());
        cbForma.getStyleClass().add("modal-combo");
        cbForma.setPrefWidth(200);

        if(produto.getFormaDeVenda() != null){
            cbForma.setValue(produto.getFormaDeVenda().name());
        }
        gridCampos.add(lblForma,1,2);
        gridCampos.add(cbForma,1,3);
        // SALVAR
        Button btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("btn-salvar-modal");
        btnSalvar.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String precoStr = txtPreco.getText().trim();
            String categoria = cbCategoria.getValue();
            String forma = cbForma.getValue();

            if(nome.isEmpty() || precoStr.isEmpty() || categoria == null || forma == null){
                Vendas.mostrarAlerta("Campos Incompletos", "Preencha todas as informações.", Alert.AlertType.ERROR);
                return;
            }
            try {
                double preco = Double.parseDouble(precoStr.replace(",", "."));
                boolean sucesso = controller.editarProduto(produto, nome, preco, categoria, forma);

                if(sucesso){
                    Vendas.mostrarAlerta("Sucesso", "Produto atualizado com sucesso.", Alert.AlertType.INFORMATION);
                    modalStage.close();
                    if(onSuccess != null){
                        onSuccess.run();
                    }
                } else {
                    Vendas.mostrarAlerta("Erro", "Não foi possível atualizar o produto.", Alert.AlertType.ERROR);
                }
            } catch(NumberFormatException ex){
                Vendas.mostrarAlerta("Dados Inválidos", "Preço inválido.", Alert.AlertType.ERROR);
            }
        });
        containerModal.getChildren().addAll(boxFechar, lblTitulo, gridCampos, btnSalvar);
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