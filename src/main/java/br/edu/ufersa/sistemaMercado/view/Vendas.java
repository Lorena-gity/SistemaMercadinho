package br.edu.ufersa.sistemaMercado.view;

import br.edu.ufersa.sistemaMercado.controller.VendasController;
import br.edu.ufersa.sistemaMercado.controller.VendasController.ItemCarrinho;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import br.edu.ufersa.sistemaMercado.model.entities.TipoProduto;
import br.edu.ufersa.sistemaMercado.model.entities.FormaDeVenda;
import br.edu.ufersa.sistemaMercado.model.service.TipoProdutoService;
import br.edu.ufersa.sistemaMercado.model.session.SessaoUsuario;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.List;

public class Vendas extends Application {

    // ✅ busca o usuário da sessão global
    private Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado();
    private VBox listaProdutosRecentes;
    private VBox listaProdutosCarrinho;
    private Label lblQtdValor;
    private Label lblTotalValor;
    private TextField txtBusca;

    private VendasController controller;
    private ObservableList<ItemCarrinho> carrinho = FXCollections.observableArrayList();

    public Vendas() {
        this.controller = new VendasController();
    }

    public Vendas(Usuario usuario) {
        this();
        this.usuarioLogado = usuario;
    }

    private boolean isGerente() {
        if (usuarioLogado == null) return false;
        return usuarioLogado.getClass().getSimpleName().equalsIgnoreCase("Gerente");
    }

    @Override
    public void start(Stage primaryStage) {
        if (usuarioLogado == null) {
            System.out.println("Aviso: Tela aberta sem usuário logado. Usando perfil temporário de CAIXA.");
            this.usuarioLogado = new br.edu.ufersa.sistemaMercado.model.entities.Caixa(0, "Caixa", "123");
        }

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F4F5F4; -fx-font-family: 'Roboto', sans-serif;");

        // === HEADER ===
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setStyle("-fx-background-color: #02261A;");

        HBox logoETituloContainer = new HBox(12);
        logoETituloContainer.setAlignment(Pos.CENTER_LEFT);

        try {
            Image imgLogoSec = new Image(getClass().getResourceAsStream("/images/SEC_LOGO.png"));
            ImageView viewLogoSec = new ImageView(imgLogoSec);
            viewLogoSec.setFitWidth(35);
            viewLogoSec.setPreserveRatio(true);
            logoETituloContainer.getChildren().add(viewLogoSec);
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: SEC_LOGO.png");
        }

        VBox titleBox = new VBox(2);
        Label lblTitulo = new Label("Mercadinho do Seu Pedrinho");
        lblTitulo.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
        lblTitulo.setStyle("-fx-text-fill: #FFFFFF;");

        Label lblSubtitulo = new Label("Gerencie suas vendas");
        lblSubtitulo.setFont(Font.font("Roboto", FontWeight.NORMAL, 13));
        lblSubtitulo.setStyle("-fx-text-fill: #A3B8B0;");
        titleBox.getChildren().addAll(lblTitulo, lblSubtitulo);
        logoETituloContainer.getChildren().add(titleBox);

        HBox spacerHeader = new HBox();
        HBox.setHgrow(spacerHeader, Priority.ALWAYS);

        HBox usuarioBox = new HBox(15);
        usuarioBox.setAlignment(Pos.CENTER_RIGHT);

        Label lblFuncionario = new Label(usuarioLogado.getNome() + (isGerente() ? " (Gerente)" : ""));
        lblFuncionario.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #033B29; -fx-padding: 8 15 8 15; -fx-background-radius: 20; -fx-font-weight: bold;");
        try {
            Image imgUser = new Image(getClass().getResourceAsStream("/images/iconUsuario.png"));
            ImageView viewUser = new ImageView(imgUser);
            viewUser.setFitWidth(14);
            viewUser.setPreserveRatio(true);
            lblFuncionario.setGraphic(viewUser);
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: iconUsuario.png");
        }

        Button btnSair = new Button("Sair");
        btnSair.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #02261A; -fx-background-radius: 20; -fx-padding: 8 20 8 20; -fx-font-weight: bold; -fx-cursor: hand;");
        try {
            Image imgSair = new Image(getClass().getResourceAsStream("/images/iconSair.png"));
            ImageView viewSair = new ImageView(imgSair);
            viewSair.setFitWidth(14);
            viewSair.setPreserveRatio(true);
            btnSair.setGraphic(viewSair);
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: iconSair.png");
        }

        // ✅ encerra a sessão ao sair
        btnSair.setOnAction(e -> {
            SessaoUsuario.getInstancia().encerrarSessao();
            primaryStage.close();
            try {
                new Login().start(new Stage());
            } catch (Exception ex) {
                System.out.println("Erro ao abrir login: " + ex.getMessage());
            }
        });

        usuarioBox.getChildren().addAll(lblFuncionario, btnSair);
        header.getChildren().addAll(logoETituloContainer, spacerHeader, usuarioBox);
        root.setTop(header);

        // === CONTEÚDO CENTRAL ===
        VBox centroContainer = new VBox(20);
        centroContainer.setPadding(new Insets(20, 30, 20, 30));

        // --- NAVIGATION BAR ---
        HBox navBar = new HBox(20);

        Label tabProdutos = new Label("Produtos");
        tabProdutos.setStyle("-fx-text-fill: #02261A; -fx-font-weight: bold; -fx-border-color: #02261A; -fx-border-width: 0 0 3 0; -fx-padding: 0 10 5 10;");
        try {
            Image imgProd = new Image(getClass().getResourceAsStream("/images/iconProduto.png"));
            ImageView viewProd = new ImageView(imgProd);
            viewProd.setFitWidth(16);
            viewProd.setPreserveRatio(true);
            tabProdutos.setGraphic(viewProd);
        } catch (Exception e) {}
        navBar.getChildren().add(tabProdutos);

        if (isGerente()) {
            Label tabFuncionarios = new Label("Funcionários");
            tabFuncionarios.setStyle("-fx-text-fill: #A0A5A2; -fx-font-weight: bold; -fx-padding: 0 10 5 10; -fx-cursor: hand;");
            try {
                Image imgFunc = new Image(getClass().getResourceAsStream("/images/iconFuncionario.png"));
                ImageView viewFunc = new ImageView(imgFunc);
                viewFunc.setFitWidth(16);
                viewFunc.setPreserveRatio(true);
                tabFuncionarios.setGraphic(viewFunc);
            } catch (Exception e) {}

            tabFuncionarios.setOnMouseClicked(e -> {
                primaryStage.close();
                try {
                    new GerenciarFuncionarios(usuarioLogado).start(new Stage());
                } catch (Exception ex) {
                    System.out.println("Erro ao abrir tela de funcionários.");
                }
            });
            navBar.getChildren().add(tabFuncionarios);
        }

        // --- AÇÕES ---
        HBox acoesBar = new HBox(15);
        acoesBar.setAlignment(Pos.CENTER_LEFT);

        Button btnFinalizar = new Button("Finalizar Venda");
        btnFinalizar.setStyle("-fx-background-color: #02261A; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        try {
            Image imgCheck = new Image(getClass().getResourceAsStream("/images/iconVenda.png"));
            ImageView viewCheck = new ImageView(imgCheck);
            viewCheck.setFitWidth(14);
            viewCheck.setPreserveRatio(true);
            btnFinalizar.setGraphic(viewCheck);
        } catch (Exception e) {}
        btnFinalizar.setOnAction(e -> acaoFinalizarVenda());

        Button btnCancelar = new Button("Cancelar Venda");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-border-color: #A0A5A2; -fx-border-radius: 8; -fx-text-fill: #333333; -fx-padding: 10 20 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        try {
            Image imgCancel = new Image(getClass().getResourceAsStream("/images/iconCancelarVenda.png"));
            ImageView viewCancel = new ImageView(imgCancel);
            viewCancel.setFitWidth(12);
            viewCancel.setPreserveRatio(true);
            btnCancelar.setGraphic(viewCancel);
        } catch (Exception e) {}
        btnCancelar.setOnAction(e -> acaoCancelarVenda());

        HBox spacerAcoes = new HBox();
        HBox.setHgrow(spacerAcoes, Priority.ALWAYS);

        Button btnComprar = new Button("Comprar Produtos");
        btnComprar.setStyle("-fx-background-color: #02261A; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        btnComprar.setOnAction(e -> abrirModalCompra(primaryStage));

        acoesBar.getChildren().addAll(btnFinalizar, btnCancelar, spacerAcoes, btnComprar);

        // --- CARDS PRINCIPAIS ---
        HBox cardsContainer = new HBox(25);
        HBox.setHgrow(cardsContainer, Priority.ALWAYS);

        DropShadow cardShadow = new DropShadow();
        cardShadow.setRadius(15);
        cardShadow.setColor(Color.web("#000000", 0.04));

        // CARD ESQUERDO (Carrinho)
        VBox cardEsquerda = new VBox(20);
        HBox.setHgrow(cardEsquerda, Priority.ALWAYS);
        cardEsquerda.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-padding: 25;");
        cardEsquerda.setEffect(cardShadow);

        HBox tabelaHeader = new HBox();
        tabelaHeader.setStyle("-fx-padding: 0 0 10 0; -fx-border-color: #EAEAEA; -fx-border-width: 0 0 1 0;");

        Label hProduto = new Label("Produto"); hProduto.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;"); hProduto.setPrefWidth(220);
        Label hUnitario = new Label("V. Unitário"); hUnitario.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;"); hUnitario.setPrefWidth(100);
        Label hQuantidade = new Label("Quantidade"); hQuantidade.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;"); hQuantidade.setPrefWidth(110); hQuantidade.setAlignment(Pos.CENTER);
        Label hTotal = new Label("V. Total"); hTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;"); hTotal.setPrefWidth(100);
        Label hAcoes = new Label("Ações"); hAcoes.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;"); hAcoes.setPrefWidth(60); hAcoes.setAlignment(Pos.CENTER);

        tabelaHeader.getChildren().addAll(hProduto, hUnitario, hQuantidade, hTotal, hAcoes);

        this.listaProdutosCarrinho = new VBox();

        VBox resumenBox = new VBox(15);
        resumenBox.setStyle("-fx-padding: 20 0 0 0; -fx-border-color: #EAEAEA; -fx-border-width: 1 0 0 0;");

        HBox qtdItensBox = new HBox();
        Label lblQtdTexto = new Label("Quantidade de Itens:");
        lblQtdTexto.setFont(Font.font("Roboto", 15));
        HBox spacerQtd = new HBox(); HBox.setHgrow(spacerQtd, Priority.ALWAYS);
        this.lblQtdValor = new Label("0");
        this.lblQtdValor.setFont(Font.font("Roboto", FontWeight.BOLD, 15));
        qtdItensBox.getChildren().addAll(lblQtdTexto, spacerQtd, this.lblQtdValor);

        HBox totalBox = new HBox();
        totalBox.setAlignment(Pos.BOTTOM_LEFT);
        Label lblTotalTexto = new Label("Total:");
        lblTotalTexto.setFont(Font.font("Roboto", 26));
        HBox spacerTotal = new HBox(); HBox.setHgrow(spacerTotal, Priority.ALWAYS);
        this.lblTotalValor = new Label("R$ 0.00");
        this.lblTotalValor.setFont(Font.font("Roboto", FontWeight.BOLD, 28));
        this.lblTotalValor.setStyle("-fx-text-fill: #6BB759;");
        totalBox.getChildren().addAll(lblTotalTexto, spacerTotal, this.lblTotalValor);

        resumenBox.getChildren().addAll(qtdItensBox, totalBox);
        cardEsquerda.getChildren().addAll(tabelaHeader, listaProdutosCarrinho, resumenBox);

        // CARD DIREITO (Busca e Recentes)
        VBox cardDireita = new VBox(20);
        cardDireita.setPrefWidth(350);
        cardDireita.setMinWidth(350);
        cardDireita.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-padding: 25;");
        cardDireita.setEffect(cardShadow);

        Label lblBuscarTitulo = new Label("Buscar Produto");
        lblBuscarTitulo.setFont(Font.font("Roboto", FontWeight.BOLD, 16));

        HBox campoBuscaContainer = new HBox(10);
        campoBuscaContainer.setAlignment(Pos.CENTER_LEFT);
        campoBuscaContainer.setPrefHeight(45);
        campoBuscaContainer.setStyle("-fx-background-color: #EFEFEF; -fx-background-radius: 25; -fx-padding: 0 15 0 15;");

        try {
            Image imgLupa = new Image(getClass().getResourceAsStream("/images/iconPesquisa.png"));
            ImageView viewLupa = new ImageView(imgLupa);
            viewLupa.setFitWidth(16);
            viewLupa.setPreserveRatio(true);
            campoBuscaContainer.getChildren().add(viewLupa);
        } catch (Exception e) {}

        this.txtBusca = new TextField();
        this.txtBusca.setPromptText("Inserir código de barras ou nome");
        this.txtBusca.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0;");
        HBox.setHgrow(this.txtBusca, Priority.ALWAYS);
        campoBuscaContainer.getChildren().add(this.txtBusca);

        this.txtBusca.textProperty().addListener((observable, oldValue, newValue) -> filtrarPainelLateral(newValue));

        Label lblRecentesTitulo = new Label("Produtos Recentes");
        lblRecentesTitulo.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
        VBox.setMargin(lblRecentesTitulo, new Insets(10, 0, 0, 0));

        this.listaProdutosRecentes = new VBox(12);

        this.carrinho.addListener((ListChangeListener<ItemCarrinho>) change -> atualizarVisualizacaoCarrinho());

        atualizarPainelLateral();

        cardDireita.getChildren().addAll(lblBuscarTitulo, campoBuscaContainer, lblRecentesTitulo, listaProdutosRecentes);
        cardsContainer.getChildren().addAll(cardEsquerda, cardDireita);
        centroContainer.getChildren().addAll(navBar, acoesBar, cardsContainer);
        root.setCenter(centroContainer);

        Scene scene = new Scene(root, 1200, 750);
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");

        primaryStage.setTitle("Sr. Pedrinho - Módulo de Vendas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void atualizarPainelLateral() {
        filtrarPainelLateral("");
    }

    private void filtrarPainelLateral(String termo) {
        this.listaProdutosRecentes.getChildren().clear();
        List<Produto> produtos = controller.pesquisarProdutos(termo);
        for (Produto prod : produtos) {
            HBox cardVisual = criarCardProdutoRecente(prod);
            this.listaProdutosRecentes.getChildren().add(cardVisual);
        }
    }

    private void adicionarProdutoAoCarrinho(Produto produto) {
        for (ItemCarrinho item : carrinho) {
            if (item.getProduto().getNome().equals(produto.getNome())) {
                item.setQuantidade(item.getQuantidade() + 1);
                atualizarVisualizacaoCarrinho();
                return;
            }
        }
        carrinho.add(new ItemCarrinho(produto, 1));
    }

    private void atualizarVisualizacaoCarrinho() {
        this.listaProdutosCarrinho.getChildren().clear();
        int qtdTotalItens = 0;
        double valorTotalVenda = 0.0;

        for (ItemCarrinho item : carrinho) {
            Produto p = item.getProduto();
            String categoria = (p.getTipo() != null) ? p.getTipo().getNome() : "Geral";

            HBox linha = criarLinhaProdutoCarrinho(
                    p.getNome(),
                    categoria,
                    "R$ " + String.format("%.2f", p.getPreco()),
                    item,
                    "R$ " + String.format("%.2f", item.getTotal())
            );

            this.listaProdutosCarrinho.getChildren().add(linha);
            qtdTotalItens += item.getQuantidade();
            valorTotalVenda += item.getTotal();
        }

        this.lblQtdValor.setText(String.valueOf(qtdTotalItens));
        this.lblTotalValor.setText("R$ " + String.format("%.2f", valorTotalVenda));
    }

    private void acaoCancelarVenda() {
        if (!carrinho.isEmpty()) {
            carrinho.clear();
            txtBusca.clear();
        }
    }

    private void acaoFinalizarVenda() {
        if (carrinho.isEmpty()) {
            mostrarAlerta("Carrinho Vazio", "Adicione produtos antes de finalizar a venda.", Alert.AlertType.WARNING);
            return;
        }

        boolean sucesso = controller.finalizarVenda(carrinho);
        if (sucesso) {
            mostrarAlerta("Sucesso", "Venda finalizada com sucesso!", Alert.AlertType.INFORMATION);
            carrinho.clear();
            txtBusca.clear();
            atualizarPainelLateral();
        } else {
            mostrarAlerta("Erro", "Não foi possível finalizar a venda.", Alert.AlertType.ERROR);
        }
    }

    // ==========================================
    // MODAL DE COMPRA
    // ==========================================
    private void abrirModalCompra(Stage ownerStage) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(ownerStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);

        javafx.scene.Parent rootDaTelaPrincipal = ownerStage.getScene().getRoot();

        javafx.scene.effect.ColorAdjust escurecerFundo = new javafx.scene.effect.ColorAdjust();
        escurecerFundo.setBrightness(-0.5);
        javafx.scene.effect.GaussianBlur desfoqueFundo = new javafx.scene.effect.GaussianBlur(5);
        desfoqueFundo.setInput(escurecerFundo);
        rootDaTelaPrincipal.setEffect(desfoqueFundo);

        VBox containerModal = new VBox(25);
        containerModal.setPadding(new Insets(30, 40, 35, 40));
        containerModal.setStyle("-fx-background-color: #EFEFEF; -fx-background-radius: 16; -fx-alignment: top-center;");
        containerModal.setPrefWidth(480);

        HBox boxFechar = new HBox();
        boxFechar.setAlignment(Pos.CENTER_RIGHT);
        Button btnFecharX = new Button("✕");
        btnFecharX.setStyle("-fx-background-color: transparent; -fx-text-fill: #000000; -fx-font-size: 16; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnFecharX.setOnAction(e -> modalStage.close());
        boxFechar.getChildren().add(btnFecharX);

        Label lblTituloModal = new Label("Compra de Produtos");
        lblTituloModal.setFont(Font.font("Roboto", FontWeight.BOLD, 22));
        lblTituloModal.setStyle("-fx-text-fill: #000000;");

        GridPane gridCampos = new GridPane();
        gridCampos.setHgap(20);
        gridCampos.setVgap(15);

        Label lblNome = new Label("Nome do Produto");
        lblNome.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");
        TextField txtNome = new TextField();
        txtNome.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6; -fx-padding: 8; -fx-border-width: 0;");
        gridCampos.add(lblNome, 0, 0);
        gridCampos.add(txtNome, 0, 1);

        Label lblPreco = new Label("Valor Unitário");
        lblPreco.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");
        TextField txtPreco = new TextField();
        txtPreco.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6; -fx-padding: 8; -fx-border-width: 0;");
        gridCampos.add(lblPreco, 1, 0);
        gridCampos.add(txtPreco, 1, 1);

        Label lblCategoria = new Label("Categoria");
        lblCategoria.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");

        TipoProdutoService tipoService = new TipoProdutoService();
        ObservableList<String> categoriasObs = FXCollections.observableArrayList();
        try {
            for (TipoProduto tp : tipoService.listarTipos()) {
                categoriasObs.add(tp.getNome());
            }
        } catch (Exception ex) {}

        ComboBox<String> cbCategoria = new ComboBox<>(categoriasObs);
        cbCategoria.setValue(categoriasObs.isEmpty() ? "Sem categorias cadastradas" : "Selecione");
        cbCategoria.setPrefWidth(200);
        cbCategoria.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6;");
        gridCampos.add(lblCategoria, 0, 2);
        gridCampos.add(cbCategoria, 0, 3);

        Label lblFormaVenda = new Label("Tipo");
        lblFormaVenda.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");

        ObservableList<String> formasDeVendaObs = FXCollections.observableArrayList();
        for (FormaDeVenda forma : FormaDeVenda.values()) {
            formasDeVendaObs.add(forma.name());
        }

        ComboBox<String> cbTipo = new ComboBox<>(formasDeVendaObs);
        cbTipo.setValue("Selecione");
        cbTipo.setPrefWidth(200);
        cbTipo.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6;");
        gridCampos.add(lblFormaVenda, 1, 2);
        gridCampos.add(cbTipo, 1, 3);

        VBox boxQuantidade = new VBox(5);
        Label lblQtd = new Label("Quantidade");
        lblQtd.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");
        TextField txtQuantidade = new TextField();
        txtQuantidade.setPrefWidth(200);
        txtQuantidade.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6; -fx-padding: 8; -fx-border-width: 0;");
        boxQuantidade.getChildren().addAll(lblQtd, txtQuantidade);

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setStyle("-fx-background-color: #012417; -fx-text-fill: #FFFFFF; -fx-background-radius: 8; -fx-padding: 10 45 10 45; -fx-font-weight: bold; -fx-cursor: hand;");

        btnSalvar.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String precoStr = txtPreco.getText().trim();
            String categoria = cbCategoria.getValue();
            String tipo = cbTipo.getValue();
            String qtdStr = txtQuantidade.getText().trim();

            if (nome.isEmpty() || precoStr.isEmpty() || qtdStr.isEmpty() ||
                    "Selecione".equals(categoria) || "Sem categorias cadastradas".equals(categoria) ||
                    "Selecione".equals(tipo)) {
                mostrarAlerta("Campos Incompletos", "Por favor, preencha todas as informações do produto.", Alert.AlertType.ERROR);
                return;
            }

            try {
                double preco = Double.parseDouble(precoStr.replace("R$", "").trim().replace(",", "."));
                int quantidade = Integer.parseInt(qtdStr);

                boolean sucesso = controller.salvarProdutoComprado(nome, preco, categoria, quantidade);

                if (sucesso) {
                    mostrarAlerta("Sucesso", "Produto inserido no banco com sucesso!", Alert.AlertType.INFORMATION);
                    modalStage.close();
                    atualizarPainelLateral();
                } else {
                    mostrarAlerta("Erro", "Ocorreu um erro interno ao salvar no banco.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Dados Inválidos", "Valor Unitário e Quantidade devem conter apenas números válidos.", Alert.AlertType.ERROR);
            }
        });

        containerModal.getChildren().addAll(boxFechar, lblTituloModal, gridCampos, boxQuantidade, btnSalvar);

        Scene modalScene = new Scene(containerModal);
        modalScene.setFill(Color.TRANSPARENT);
        modalStage.setScene(modalScene);

        modalStage.showAndWait();
        rootDaTelaPrincipal.setEffect(null);
    }

    // ==========================================
    // MODAL DE EDIÇÃO
    // ==========================================
    private void abrirModalEditar(Stage ownerStage, Produto produto) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(ownerStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);

        javafx.scene.Parent rootDaTelaPrincipal = ownerStage.getScene().getRoot();

        javafx.scene.effect.ColorAdjust escurecerFundo = new javafx.scene.effect.ColorAdjust();
        escurecerFundo.setBrightness(-0.5);
        javafx.scene.effect.GaussianBlur desfoqueFundo = new javafx.scene.effect.GaussianBlur(5);
        desfoqueFundo.setInput(escurecerFundo);
        rootDaTelaPrincipal.setEffect(desfoqueFundo);

        VBox containerModal = new VBox(25);
        containerModal.setPadding(new Insets(30, 40, 35, 40));
        containerModal.setStyle("-fx-background-color: #EFEFEF; -fx-background-radius: 16; -fx-alignment: top-center;");
        containerModal.setPrefWidth(480);

        HBox boxFechar = new HBox();
        boxFechar.setAlignment(Pos.CENTER_RIGHT);
        Button btnFecharX = new Button("✕");
        btnFecharX.setStyle("-fx-background-color: transparent; -fx-text-fill: #000000; -fx-font-size: 16; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnFecharX.setOnAction(e -> modalStage.close());
        boxFechar.getChildren().add(btnFecharX);

        Label lblTituloModal = new Label("Editar Produto");
        lblTituloModal.setFont(Font.font("Roboto", FontWeight.BOLD, 22));
        lblTituloModal.setStyle("-fx-text-fill: #000000;");

        GridPane gridCampos = new GridPane();
        gridCampos.setHgap(20);
        gridCampos.setVgap(15);

        Label lblNome = new Label("Nome do Produto");
        lblNome.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");
        TextField txtNome = new TextField(produto.getNome());
        txtNome.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6; -fx-padding: 8; -fx-border-width: 0;");
        gridCampos.add(lblNome, 0, 0);
        gridCampos.add(txtNome, 0, 1);

        Label lblPreco = new Label("Valor Unitário");
        lblPreco.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");
        TextField txtPreco = new TextField(String.format("R$ %.2f", produto.getPreco()).replace(",", "."));
        txtPreco.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6; -fx-padding: 8; -fx-border-width: 0;");
        gridCampos.add(lblPreco, 1, 0);
        gridCampos.add(txtPreco, 1, 1);

        Label lblCategoria = new Label("Categoria");
        lblCategoria.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");

        TipoProdutoService tipoService = new TipoProdutoService();
        ObservableList<String> categoriasObs = FXCollections.observableArrayList();
        try {
            for (TipoProduto tp : tipoService.listarTipos()) {
                categoriasObs.add(tp.getNome());
            }
        } catch (Exception ex) {}

        ComboBox<String> cbCategoria = new ComboBox<>(categoriasObs);
        String catAtual = (produto.getTipo() != null) ? produto.getTipo().getNome() : "Selecione";
        cbCategoria.setValue(categoriasObs.contains(catAtual) ? catAtual : "Selecione");
        cbCategoria.setPrefWidth(200);
        cbCategoria.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6;");
        gridCampos.add(lblCategoria, 0, 2);
        gridCampos.add(cbCategoria, 0, 3);

        Label lblFormaVenda = new Label("Tipo");
        lblFormaVenda.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;");

        ObservableList<String> formasDeVendaObs = FXCollections.observableArrayList();
        for (FormaDeVenda forma : FormaDeVenda.values()) {
            formasDeVendaObs.add(forma.name());
        }

        ComboBox<String> cbTipo = new ComboBox<>(formasDeVendaObs);
        // ✅ carrega o tipo atual do produto
        cbTipo.setValue(produto.getFormaDeVenda() != null ? produto.getFormaDeVenda().name() : "Selecione");
        cbTipo.setPrefWidth(200);
        cbTipo.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6;");
        gridCampos.add(lblFormaVenda, 1, 2);
        gridCampos.add(cbTipo, 1, 3);

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setStyle("-fx-background-color: #012417; -fx-text-fill: #FFFFFF; -fx-background-radius: 8; -fx-padding: 10 45 10 45; -fx-font-weight: bold; -fx-cursor: hand;");

        btnSalvar.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String precoStr = txtPreco.getText().trim();
            String categoria = cbCategoria.getValue();
            String tipo = cbTipo.getValue();

            if (nome.isEmpty() || precoStr.isEmpty() ||
                    "Selecione".equals(categoria) || "Sem categorias cadastradas".equals(categoria) ||
                    "Selecione".equals(tipo)) {
                mostrarAlerta("Campos Incompletos", "Por favor, preencha todas as informações.", Alert.AlertType.ERROR);
                return;
            }

            try {
                double novoPreco = Double.parseDouble(precoStr.replace("R$", "").trim().replace(",", "."));

                // ✅ agora chama o controller de verdade
                boolean sucesso = controller.editarProduto(produto, nome, novoPreco, categoria, tipo);

                if (sucesso) {
                    mostrarAlerta("Sucesso", "Produto editado com sucesso!", Alert.AlertType.INFORMATION);
                    modalStage.close();
                    atualizarPainelLateral();
                } else {
                    mostrarAlerta("Erro", "Não foi possível editar o produto.", Alert.AlertType.ERROR);
                }

            } catch (NumberFormatException ex) {
                mostrarAlerta("Dados Inválidos", "Valor Unitário deve conter apenas números válidos.", Alert.AlertType.ERROR);
            }
        });

        containerModal.getChildren().addAll(boxFechar, lblTituloModal, gridCampos, btnSalvar);

        Scene modalScene = new Scene(containerModal);
        modalScene.setFill(Color.TRANSPARENT);
        modalStage.setScene(modalScene);

        modalStage.showAndWait();
        rootDaTelaPrincipal.setEffect(null);
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private HBox criarLinhaProdutoCarrinho(String nome, String categoria, String vUnitario, ItemCarrinho item, String vTotal) {
        HBox linha = new HBox();
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setStyle("-fx-padding: 15 0 15 0; -fx-border-color: #F1F1F1; -fx-border-width: 0 0 1 0;");

        VBox prodInfo = new VBox(2);
        prodInfo.setPrefWidth(220);
        Label lblNome = new Label(nome); lblNome.setStyle("-fx-font-weight: bold; -fx-text-fill: #222;");
        Label lblCat = new Label(categoria); lblCat.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");
        prodInfo.getChildren().addAll(lblNome, lblCat);

        Label lblUnitario = new Label(vUnitario);
        lblUnitario.setPrefWidth(100);
        lblUnitario.setStyle("-fx-text-fill: #444;");

        HBox seletorQtd = new HBox(10);
        seletorQtd.setPrefWidth(110);
        seletorQtd.setAlignment(Pos.CENTER);

        Button btnMenos = new Button("<");
        btnMenos.setStyle("-fx-background-color: transparent; -fx-text-fill: #888; -fx-cursor: hand; -fx-font-weight: bold;");
        btnMenos.setOnAction(e -> {
            if (item.getQuantidade() > 1) {
                item.setQuantidade(item.getQuantidade() - 1);
                atualizarVisualizacaoCarrinho();
            }
        });

        Label lblQtd = new Label(String.valueOf(item.getQuantidade()));
        lblQtd.setStyle("-fx-background-color: #EFEFEF; -fx-padding: 4 12 4 12; -fx-background-radius: 4; -fx-font-weight: bold;");

        Button btnMais = new Button(">");
        btnMais.setStyle("-fx-background-color: transparent; -fx-text-fill: #888; -fx-cursor: hand; -fx-font-weight: bold;");
        btnMais.setOnAction(e -> {
            item.setQuantidade(item.getQuantidade() + 1);
            atualizarVisualizacaoCarrinho();
        });

        seletorQtd.getChildren().addAll(btnMenos, lblQtd, btnMais);

        Label lblTotal = new Label(vTotal);
        lblTotal.setPrefWidth(100);
        lblTotal.setStyle("-fx-text-fill: #222; -fx-font-weight: bold;");

        HBox acaoBox = new HBox();
        acaoBox.setPrefWidth(60);
        acaoBox.setAlignment(Pos.CENTER);

        Button btnDeletar = new Button();
        btnDeletar.setStyle("-fx-background-color: #FFEFEA; -fx-background-radius: 50; -fx-min-width: 32; -fx-min-height: 32; -fx-max-width: 32; -fx-max-height: 32; -fx-cursor: hand;");
        btnDeletar.setOnAction(e -> carrinho.remove(item));

        try {
            Image imgLixo = new Image(getClass().getResourceAsStream("/images/iconLixo.png"));
            ImageView viewLixo = new ImageView(imgLixo);
            viewLixo.setFitWidth(14);
            viewLixo.setPreserveRatio(true);
            btnDeletar.setGraphic(viewLixo);
        } catch (Exception e) {}
        acaoBox.getChildren().add(btnDeletar);

        linha.getChildren().addAll(prodInfo, lblUnitario, seletorQtd, lblTotal, acaoBox);
        return linha;
    }

    private HBox criarCardProdutoRecente(Produto prod) {
        String nomeCategoria = (prod.getTipo() != null) ? prod.getTipo().getNome() : "Geral";
        String infoPreco = nomeCategoria + " - R$ " + String.format("%.2f", prod.getPreco());
        String estoqueText = "Estoque atual: " + prod.getQuantidadeEstoque() + " un";

        HBox card = new HBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #EFEFEF; -fx-background-radius: 8; -fx-padding: 12;");

        VBox infoBox = new VBox(3);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label lblNome = new Label(prod.getNome());
        lblNome.setStyle("-fx-font-weight: bold; -fx-text-fill: #222;");
        Label lblInfo = new Label(infoPreco);
        lblInfo.setStyle("-fx-text-fill: #555; -fx-font-size: 12;");
        Label lblEstoque = new Label(estoqueText);
        lblEstoque.setStyle("-fx-text-fill: #6BB759; -fx-font-size: 11; -fx-font-weight: bold;");

        infoBox.getChildren().addAll(lblNome, lblInfo, lblEstoque);

        infoBox.setStyle("-fx-cursor: hand;");
        infoBox.setOnMouseClicked(e -> adicionarProdutoAoCarrinho(prod));

        if (isGerente()) {
            Button btnEditar = new Button();
            btnEditar.setStyle("-fx-background-color: #E2F0DD; -fx-background-radius: 50; -fx-min-width: 32; -fx-min-height: 32; -fx-max-width: 32; -fx-max-height: 32; -fx-cursor: hand;");

            try {
                Image imgLapis = new Image(getClass().getResourceAsStream("/images/iconLapis.png"));
                ImageView viewLapis = new ImageView(imgLapis);
                viewLapis.setFitWidth(14);
                viewLapis.setPreserveRatio(true);
                btnEditar.setGraphic(viewLapis);
            } catch (Exception e) {}

            btnEditar.setOnAction(e -> abrirModalEditar((Stage) card.getScene().getWindow(), prod));
            card.getChildren().addAll(infoBox, btnEditar);
        } else {
            card.getChildren().add(infoBox);
        }

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #E5E5E5; -fx-background-radius: 8; -fx-padding: 12;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #EFEFEF; -fx-background-radius: 8; -fx-padding: 12;"));

        return card;
    }

    private void mudarDeTela(Stage stage, Application novaTela) {
        // Pega a raiz da tela atual
        javafx.scene.Node rootNode = stage.getScene().getRoot();

        // Cria uma animação de desaparecimento
        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), rootNode);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // Quando a animação terminar, carrega a nova tela NO MESMO STAGE
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