package br.edu.ufersa.sistemaMercado.view;

import br.edu.ufersa.sistemaMercado.controller.VendasController;
import br.edu.ufersa.sistemaMercado.controller.VendasController.ItemCarrinho;
import br.edu.ufersa.sistemaMercado.model.entities.*;
import br.edu.ufersa.sistemaMercado.model.session.SessaoUsuario;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class Vendas extends Application {
    private Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado(); // busca o usuário da sessão global
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
        return usuarioLogado instanceof Gerente;
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
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: SEC_LOGO.png");
        }
        VBox titleBox = new VBox(2);
        Label lblTitulo = new Label("Mercadinho do Seu Pedrinho");
        lblTitulo.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        lblTitulo.setStyle("-fx-text-fill: #FFFFFF;");

        Label lblSubtitulo = new Label("Gerencie suas vendas");
        lblSubtitulo.setFont(Font.font("Roboto", FontWeight.NORMAL, 10));
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
            lblFuncionario.setGraphic(criarIcone("/images/iconUsuario.png", 14));
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: iconUsuario.png");
        }

        Button btnSair = new Button("Sair");
        btnSair.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #02261A; -fx-background-radius: 20; -fx-padding: 8 20 8 20; -fx-font-weight: bold; -fx-cursor: hand;");
        try {
            btnSair.setGraphic(criarIcone("/images/iconSair.png", 14));
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: iconSair.png");
        }
        // encerra a sessão ao sair
        btnSair.setOnAction(e -> controller.logout(primaryStage));

        usuarioBox.getChildren().addAll(lblFuncionario, btnSair);
        header.getChildren().addAll(logoETituloContainer, spacerHeader, usuarioBox);
        root.setTop(header);
        // CONTEÚDO CENTRAL
        VBox centroContainer = new VBox(20);
        centroContainer.setPadding(new Insets(20, 30, 20, 30));
        // NAVBAR
        HBox navBar = new HBox(20);

        Label tabProdutos = new Label("Produtos");
        tabProdutos.setStyle("-fx-text-fill: #02261A; -fx-font-weight: bold; -fx-border-color: #02261A; -fx-border-width: 0 0 3 0; -fx-padding: 0 10 5 10;");
        try {
            tabProdutos.setGraphic(criarIcone("/images/iconProduto.png", 14));
        } catch (Exception e) {}
        navBar.getChildren().add(tabProdutos);

        if (isGerente()) {
            Label tabFuncionarios = new Label("Funcionários");
            tabFuncionarios.setStyle("-fx-text-fill: #A0A5A2; -fx-font-weight: bold; -fx-padding: 0 10 5 10; -fx-cursor: hand;");
            try {
                tabFuncionarios.setGraphic(criarIcone("/images/iconFuncionario.png", 14));
            } catch (Exception e) {}
            tabFuncionarios.setOnMouseClicked(e -> {
                Login.mudarDeTela(primaryStage, new GerenciarFuncionarios(usuarioLogado));
            });
            navBar.getChildren().add(tabFuncionarios);
        }
        // AÇÕES
        HBox acoesBar = new HBox(15);
        acoesBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(acoesBar, Priority.ALWAYS);

        Button btnFinalizar = new Button("Finalizar Venda");
        btnFinalizar.setStyle("-fx-background-color: #02261A; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        try {
            btnFinalizar.setGraphic(criarIcone("/images/iconVenda.png", 14));
        } catch (Exception e) {}
        btnFinalizar.setOnAction(e -> acaoFinalizarVenda());

        Button btnCancelar = new Button("Cancelar Venda");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-border-color: #A0A5A2; -fx-border-radius: 8; -fx-text-fill: #333333; -fx-padding: 10 20 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        try {
            btnCancelar.setGraphic(criarIcone("/images/iconCancelarVenda.png", 14));
        } catch (Exception e) {}
        btnCancelar.setOnAction(e -> acaoCancelarVenda());

        HBox spacerAcoes = new HBox();
        HBox.setHgrow(spacerAcoes, Priority.ALWAYS);

        Button btnComprar = new Button("Comprar Produtos");
        btnComprar.setStyle("-fx-background-color: #02261A; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        btnComprar.setOnAction(e -> new ModalCompraProduto(controller).abrir(primaryStage, this::atualizarPainelLateral));

        acoesBar.getChildren().addAll(btnFinalizar, btnCancelar, spacerAcoes, btnComprar);
        // CARDS PRINCIPAIS
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

        HBox tabelaHeader = new HBox(20);
        tabelaHeader.setStyle("-fx-padding: 0 0 10 0; -fx-border-color: #EAEAEA; -fx-border-width: 0 0 1 0;");

        Label hProduto = new Label("Produto"); hProduto.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
        HBox.setHgrow(hProduto, Priority.ALWAYS);
        hProduto.setMaxWidth(Double.MAX_VALUE);
        Label hUnitario = new Label("V. Unitário"); hUnitario.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
        hUnitario.setPrefWidth(100);
        Label hQuantidade = new Label("Quantidade"); hQuantidade.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
        hQuantidade.setPrefWidth(140);
        hQuantidade.setAlignment(Pos.CENTER);
        Label hTotal = new Label("V. Total"); hTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
        hTotal.setPrefWidth(100);
        Label hAcoes = new Label("Ações"); hAcoes.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
        hAcoes.setPrefWidth(60);
        hAcoes.setAlignment(Pos.CENTER);

        tabelaHeader.getChildren().addAll(hProduto, hUnitario, hQuantidade, hTotal, hAcoes);

        this.listaProdutosCarrinho = new VBox(14);

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
            ImageView iconePesquisa = criarIcone("/images/iconPesquisa.png", 14);
            if (iconePesquisa != null) {
                campoBuscaContainer.getChildren().add(iconePesquisa);
            }
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

        this.listaProdutosRecentes = new VBox(18);

        this.carrinho.addListener((ListChangeListener<ItemCarrinho>) change -> atualizarVisualizacaoCarrinho());

        atualizarPainelLateral();

        cardDireita.getChildren().addAll(lblBuscarTitulo, campoBuscaContainer, lblRecentesTitulo, listaProdutosRecentes);
        cardsContainer.getChildren().addAll(cardEsquerda, cardDireita);
        centroContainer.getChildren().addAll(navBar, acoesBar, cardsContainer);
        root.setCenter(centroContainer);

        Scene scene = new Scene(root, 1200, 750);
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
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
            if (item.getProduto().getIdProduto() == produto.getIdProduto())  {
                item.setQuantidade(item.getQuantidade() + 1);
                atualizarVisualizacaoCarrinho();
                return;
            }
        }
        carrinho.add(new ItemCarrinho(produto, 1));
    }

    private String formatarMoeda(double valor) {
        return "R$ " + String.format("%.2f", valor);
    }

    private void atualizarVisualizacaoCarrinho() {
        listaProdutosCarrinho.getChildren().clear();
        int qtdTotalItens = 0;
        double valorTotalVenda = 0.0;

        for (ItemCarrinho item : carrinho) {
            Produto produto = item.getProduto();
            String categoria = produto.getTipo() != null ? produto.getTipo().getNome() : "Geral";

            HBox linha = criarLinhaProdutoCarrinho(produto.getNome(), categoria, formatarMoeda(produto.getPreco()), item, formatarMoeda(item.getTotal()));
            listaProdutosCarrinho.getChildren().add(linha);

            qtdTotalItens += item.getQuantidade();
            valorTotalVenda += item.getTotal();
        }
        lblQtdValor.setText(String.valueOf(qtdTotalItens));
        lblTotalValor.setText(formatarMoeda(valorTotalVenda));
    }

    private void acaoCancelarVenda() {
        carrinho.clear();
        txtBusca.clear();
        atualizarVisualizacaoCarrinho();
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
            atualizarVisualizacaoCarrinho();
            atualizarPainelLateral();
        } else {
            mostrarAlerta("Erro", "Não foi possível finalizar a venda.", Alert.AlertType.ERROR);
        }
    }

    public static void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private HBox criarLinhaProdutoCarrinho(String nome, String categoria, String vUnitario, ItemCarrinho item, String vTotal) {
        HBox linha = new HBox(20);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.getStyleClass().add("linha-carrinho");

        VBox prodInfo = new VBox(2);
        HBox.setHgrow(prodInfo, Priority.ALWAYS);

        Label lblNome = new Label(nome);
        lblNome.getStyleClass().add("carrinho-produto-nome");

        Label lblCat = new Label(categoria);
        lblCat.getStyleClass().add("carrinho-produto-categoria");

        prodInfo.getChildren().addAll(lblNome, lblCat);

        Label lblUnitario = new Label(vUnitario);
        lblUnitario.setPrefWidth(100);
        lblUnitario.getStyleClass().add("carrinho-preco-unitario");

        HBox seletorQtd = new HBox(10);
        seletorQtd.setPrefWidth(140);
        seletorQtd.setAlignment(Pos.CENTER);

        Button btnMenos = new Button("<");
        btnMenos.getStyleClass().add("btn-qtd");

        btnMenos.setOnAction(e -> {
            if (item.getQuantidade() > 1) {
                item.setQuantidade(item.getQuantidade() - 1);
                atualizarVisualizacaoCarrinho();
            }
        });
        Label lblQtd = new Label(String.valueOf(item.getQuantidade()));
        lblQtd.getStyleClass().add("label-qtd");

        Button btnMais = new Button(">");
        btnMais.getStyleClass().add("btn-qtd");

        btnMais.setOnAction(e -> {
            item.setQuantidade(item.getQuantidade() + 1);
            atualizarVisualizacaoCarrinho();
        });
        seletorQtd.getChildren().addAll(btnMenos, lblQtd, btnMais);

        Label lblTotal = new Label(vTotal);
        lblTotal.setPrefWidth(100);
        lblTotal.getStyleClass().add("carrinho-total");

        HBox acaoBox = new HBox();
        acaoBox.setPrefWidth(60);
        acaoBox.setAlignment(Pos.CENTER);

        Button btnDeletar = new Button();
        btnDeletar.getStyleClass().addAll("botao-acao", "botao-acao-vermelho");
        btnDeletar.setOnAction(e -> {carrinho.remove(item);atualizarVisualizacaoCarrinho();});
        try {
            btnDeletar.setGraphic(criarIcone("/images/iconLixo.png", 14));
        } catch (Exception ignored) {}
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
        card.getStyleClass().add("produto-card");

        VBox infoBox = new VBox(3);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label lblNome = new Label(prod.getNome());
        lblNome.getStyleClass().add("produto-nome");
        Label lblInfo = new Label(infoPreco);
        lblInfo.getStyleClass().add("produto-info");
        Label lblEstoque = new Label(estoqueText);
        lblEstoque.setStyle("-fx-text-fill: #6BB759; -fx-font-size: 11px; -fx-font-weight: bold;");
        infoBox.getChildren().addAll(lblNome, lblInfo, lblEstoque);

        infoBox.getStyleClass().add("clicavel");
        infoBox.setOnMouseClicked(e -> adicionarProdutoAoCarrinho(prod));

        if (isGerente()) {
            Button btnEditar = new Button();
            btnEditar.getStyleClass().addAll("botao-acao", "botao-acao-verde");
            try {
                btnEditar.setGraphic(criarIcone("/images/iconLapis.png", 14));
            } catch (Exception e) {}
            btnEditar.setOnAction(e -> new ModalEditarProduto(controller, prod).abrir((Stage) card.getScene().getWindow(), this::atualizarPainelLateral));

            Button btnExcluir = new Button();
            btnExcluir.getStyleClass().addAll("botao-acao", "botao-acao-vermelho");
            try {
                btnExcluir.setGraphic(criarIcone("/images/iconLixo.png", 14));
            } catch (Exception e) {}
            btnExcluir.setOnAction(e -> excluirProduto(prod));

            HBox acoesCard = new HBox(8, btnEditar, btnExcluir);
            acoesCard.setAlignment(Pos.CENTER);
            card.getChildren().addAll(infoBox, acoesCard);
        } else {
            card.getChildren().add(infoBox);
        }
        return card;
    }

    private void excluirProduto(Produto prod) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir o produto \"" + prod.getNome() + "\"?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.setTitle("Confirmar exclusão");
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.YES) {
            if (controller.excluirProduto(prod)) {
                atualizarPainelLateral();
            } else {
                mostrarAlerta("Erro", "Não foi possível excluir o produto.", Alert.AlertType.ERROR);
            }
        }
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

    public static void main(String[] args) {
        launch(args);
    }
}