package br.edu.ufersa.sistemaMercado.view;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.exceptions.RegistroDuplicadoException;
import br.edu.ufersa.sistemaMercado.model.entities.Gerente;
import br.edu.ufersa.sistemaMercado.model.entities.PerfilUsuario;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;
import br.edu.ufersa.sistemaMercado.model.factory.UsuarioFactory;
import br.edu.ufersa.sistemaMercado.model.service.UsuarioService;
import br.edu.ufersa.sistemaMercado.model.session.SessaoUsuario;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.List;

public class GerenciarFuncionarios extends Application {

    // busca o usuário da sessão global
    private Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado();
    private VBox tabelaFuncionarios;

    // usa o Service, não dados mock
    private final UsuarioService usuarioService = new UsuarioService();

    public GerenciarFuncionarios() {}

    public GerenciarFuncionarios(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    @Override
    public void start(Stage primaryStage) {
        // fallback correto — usa Gerente, não Usuario abstrato
        if (usuarioLogado == null) {
            this.usuarioLogado = new Gerente(0, "Gerente Teste", "123");
        }

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F4F5F4; -fx-font-family: 'Roboto', sans-serif;");

        // Deixa a tela invisível no começo para o efeito de Fade-in
        root.setOpacity(0.0);

        // ==========================================
        // CABEÇALHO
        // ==========================================
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setStyle("-fx-background-color: #02261A;");

        HBox logoETitulo = new HBox(12);
        logoETitulo.setAlignment(Pos.CENTER_LEFT);

        try {
            Image imgLogoSec = new Image(getClass().getResourceAsStream("/images/SEC_LOGO.png"));
            ImageView viewLogoSec = new ImageView(imgLogoSec);
            viewLogoSec.setFitWidth(35);
            viewLogoSec.setPreserveRatio(true);
            logoETitulo.getChildren().add(viewLogoSec);
        } catch (Exception e) {}

        VBox titleBox = new VBox(2);
        Label lblTitulo = new Label("Mercadinho do Seu Pedrinho");
        lblTitulo.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
        lblTitulo.setStyle("-fx-text-fill: #FFFFFF;");
        Label lblSubtitulo = new Label("Gerencie suas vendas");
        lblSubtitulo.setFont(Font.font("Roboto", FontWeight.NORMAL, 13));
        lblSubtitulo.setStyle("-fx-text-fill: #A3B8B0;");
        titleBox.getChildren().addAll(lblTitulo, lblSubtitulo);
        logoETitulo.getChildren().add(titleBox);

        HBox spacerHeader = new HBox();
        HBox.setHgrow(spacerHeader, Priority.ALWAYS);

        HBox usuarioBox = new HBox(15);
        usuarioBox.setAlignment(Pos.CENTER_RIGHT);

        Label lblFuncionario = new Label(usuarioLogado.getNome() + " (Gerente)");
        lblFuncionario.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #033B29; -fx-padding: 8 15 8 15; -fx-background-radius: 20; -fx-font-weight: bold;");
        // ADICIONADO: Ícone de Usuário
        try {
            Image imgUser = new Image(getClass().getResourceAsStream("/images/iconUsuario.png"));
            ImageView viewUser = new ImageView(imgUser);
            viewUser.setFitWidth(14);
            viewUser.setPreserveRatio(true);
            lblFuncionario.setGraphic(viewUser);
        } catch (Exception e) {}

        Button btnSair = new Button("Sair");
        btnSair.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #02261A; -fx-background-radius: 20; -fx-padding: 8 20 8 20; -fx-font-weight: bold; -fx-cursor: hand;");
        // ADICIONADO: Ícone de Sair
        try {
            Image imgSair = new Image(getClass().getResourceAsStream("/images/iconSair.png"));
            ImageView viewSair = new ImageView(imgSair);
            viewSair.setFitWidth(14);
            viewSair.setPreserveRatio(true);
            btnSair.setGraphic(viewSair);
        } catch (Exception e) {}

        // encerra a sessão ao sair e muda com transição
        btnSair.setOnAction(e -> {
            SessaoUsuario.getInstancia().encerrarSessao();
            mudarDeTela(primaryStage, new Login());
        });

        usuarioBox.getChildren().addAll(lblFuncionario, btnSair);
        header.getChildren().addAll(logoETitulo, spacerHeader, usuarioBox);
        root.setTop(header);

        // ==========================================
        // CONTEÚDO CENTRAL
        // ==========================================
        VBox centroContainer = new VBox(20);
        centroContainer.setPadding(new Insets(20, 30, 20, 30));

        // --- NAVIGATION BAR ---
        HBox navBar = new HBox(20);
        navBar.setStyle("-fx-border-color: #EAEAEA; -fx-border-width: 0 0 1 0; -fx-padding: 0 0 10 0;");

        Label tabProdutos = new Label("Produtos");
        tabProdutos.setStyle("-fx-text-fill: #A0A5A2; -fx-font-weight: bold; -fx-padding: 0 10 5 10; -fx-cursor: hand;");
        try {
            Image imgProd = new Image(getClass().getResourceAsStream("/images/iconProduto_OFF.png"));
            ImageView viewProd = new ImageView(imgProd);
            viewProd.setFitWidth(16);
            viewProd.setPreserveRatio(true);
            tabProdutos.setGraphic(viewProd);
        } catch (Exception e) {}

        // Transição para a tela de Vendas
        tabProdutos.setOnMouseClicked(e -> mudarDeTela(primaryStage, new Vendas(usuarioLogado)));

        Label tabFuncionarios = new Label("Funcionários");
        tabFuncionarios.setStyle("-fx-text-fill: #02261A; -fx-font-weight: bold; -fx-border-color: #02261A; -fx-border-width: 0 0 3 0; -fx-padding: 0 10 5 10;");
        try {
            Image imgFunc = new Image(getClass().getResourceAsStream("/images/iconFuncionario_ON.png"));
            ImageView viewFunc = new ImageView(imgFunc);
            viewFunc.setFitWidth(16);
            viewFunc.setPreserveRatio(true);
            tabFuncionarios.setGraphic(viewFunc);
        } catch (Exception e) {}

        navBar.getChildren().addAll(tabProdutos, tabFuncionarios);

        // --- BARRA DE AÇÕES ---
        HBox acoesBar = new HBox();
        acoesBar.setAlignment(Pos.CENTER_RIGHT);

        Button btnAdicionarFuncionario = new Button("Adicionar Funcionário");
        btnAdicionarFuncionario.setStyle("-fx-background-color: #012417; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        btnAdicionarFuncionario.setOnAction(e -> abrirModalAdicionar(primaryStage));
        acoesBar.getChildren().add(btnAdicionarFuncionario);

        // --- TABELA DE FUNCIONÁRIOS ---
        DropShadow cardShadow = new DropShadow();
        cardShadow.setRadius(15);
        cardShadow.setColor(Color.web("#000000", 0.04));

        VBox cardTabela = new VBox();
        cardTabela.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #EAEAEA; -fx-border-radius: 12; -fx-border-width: 1;");
        cardTabela.setEffect(cardShadow);
        VBox.setVgrow(cardTabela, Priority.ALWAYS);

        HBox headerTabela = new HBox();
        headerTabela.setStyle("-fx-padding: 20 25 15 25; -fx-background-color: #FAFAFA; -fx-background-radius: 12 12 0 0; -fx-border-color: #EAEAEA; -fx-border-width: 0 0 1 0;");

        Label hNome = new Label("Nome");
        hNome.setStyle("-fx-font-weight: bold; -fx-text-fill: #111;");
        hNome.setPrefWidth(400);

        Label hCargo = new Label("Cargo");
        hCargo.setStyle("-fx-font-weight: bold; -fx-text-fill: #111;");
        hCargo.setPrefWidth(300);

        Label hAcoes = new Label("Ações");
        hAcoes.setStyle("-fx-font-weight: bold; -fx-text-fill: #111;");
        hAcoes.setPrefWidth(100);
        hAcoes.setAlignment(Pos.CENTER);

        headerTabela.getChildren().addAll(hNome, hCargo, hAcoes);

        this.tabelaFuncionarios = new VBox();

        // carrega do banco de verdade
        carregarFuncionarios(primaryStage);

        cardTabela.getChildren().addAll(headerTabela, tabelaFuncionarios);
        centroContainer.getChildren().addAll(navBar, acoesBar, cardTabela);
        root.setCenter(centroContainer);

        Scene scene = new Scene(root, 1200, 750);
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        primaryStage.setTitle("Sr. Pedrinho - Gerenciar Funcionários");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Animação de entrada (Fade In)
        FadeTransition ftIn = new FadeTransition(Duration.millis(300), root);
        ftIn.setFromValue(0.0);
        ftIn.setToValue(1.0);
        ftIn.play();
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

    private void carregarFuncionarios(Stage ownerStage) {
        tabelaFuncionarios.getChildren().clear();
        try {
            List<Usuario> funcionarios = usuarioService.listarUsuarios();
            for (Usuario u : funcionarios) {
                tabelaFuncionarios.getChildren().add(criarLinhaFuncionario(u, ownerStage));
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar funcionários: " + e.getMessage());
        }
    }

    private HBox criarLinhaFuncionario(Usuario func, Stage ownerStage) {
        HBox linha = new HBox();
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setStyle("-fx-padding: 15 25 15 25; -fx-border-color: #EAEAEA; -fx-border-width: 0 0 1 0;");

        Label lblNome = new Label(func.getNome());
        lblNome.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        lblNome.setPrefWidth(400);

        HBox boxCargo = new HBox();
        boxCargo.setPrefWidth(300);
        boxCargo.setAlignment(Pos.CENTER_LEFT);

        Label lblCargo = new Label(func.getPerfil().name());
        if (func.getPerfil() == PerfilUsuario.GERENTE) {
            lblCargo.setStyle("-fx-background-color: #F8D7DA; -fx-text-fill: #721C24; -fx-padding: 4 12 4 12; -fx-background-radius: 12; -fx-font-weight: bold; -fx-font-size: 11;");
        } else {
            lblCargo.setStyle("-fx-background-color: #E2D9F3; -fx-text-fill: #603E99; -fx-padding: 4 12 4 12; -fx-background-radius: 12; -fx-font-weight: bold; -fx-font-size: 11;");
        }
        boxCargo.getChildren().add(lblCargo);

        HBox acaoBox = new HBox(10);
        acaoBox.setPrefWidth(100);
        acaoBox.setAlignment(Pos.CENTER);

        Button btnEditar = new Button();
        btnEditar.getStyleClass().addAll("botao-acao", "botao-acao-verde");
        btnEditar.setGraphic(criarIcone("/images/iconLapis.png", 14));
        btnEditar.setOnAction(e -> abrirModalEditar(ownerStage, func));

        Button btnDeletar = new Button();
        btnDeletar.getStyleClass().addAll("botao-acao", "botao-acao-vermelho");
        btnDeletar.setGraphic(criarIcone("/images/iconLixo.png", 14));
        btnDeletar.setOnAction(e -> {
            try {
                usuarioService.removerUsuario(func);
                carregarFuncionarios(ownerStage); // atualiza a tabela após deletar
            } catch (Exception ex) {
                mostrarAlerta("Erro", "Não foi possível remover o funcionário: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        acaoBox.getChildren().addAll(btnEditar, btnDeletar);
        linha.getChildren().addAll(lblNome, boxCargo, acaoBox);
        return linha;
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

    private void abrirModalAdicionar(Stage ownerStage) {
        Stage modalStage = prepararModal(ownerStage);

        VBox containerModal = new VBox(25);
        containerModal.setPadding(new Insets(30, 40, 35, 40));
        containerModal.setStyle("-fx-background-color: #EFEFEF; -fx-background-radius: 16; -fx-alignment: top-center;");
        containerModal.setPrefWidth(450);

        HBox boxFechar = criarBotaoFechar(modalStage);

        Label lblTituloModal = new Label("Cadastrar Funcionário");
        lblTituloModal.setFont(Font.font("Roboto", FontWeight.BOLD, 22));

        GridPane gridCampos = new GridPane();
        gridCampos.setHgap(20);
        gridCampos.setVgap(10);

        Label lblNome = new Label("Nome");
        lblNome.setStyle("-fx-font-weight: bold;");
        TextField txtNome = new TextField();
        txtNome.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6; -fx-padding: 8;");

        Label lblSenha = new Label("Senha");
        lblSenha.setStyle("-fx-font-weight: bold;");
        PasswordField txtSenha = new PasswordField();
        txtSenha.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6; -fx-padding: 8;");

        Label lblCargo = new Label("Cargo");
        lblCargo.setStyle("-fx-font-weight: bold;");

        ObservableList<String> cargos = FXCollections.observableArrayList();
        for (PerfilUsuario p : PerfilUsuario.values()) {
            cargos.add(p.name());
        }
        ComboBox<String> cbCargo = new ComboBox<>(cargos);
        cbCargo.setValue("Selecione");
        cbCargo.setPrefWidth(200);
        cbCargo.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6;");

        gridCampos.add(lblNome, 0, 0);
        gridCampos.add(txtNome, 0, 1);
        gridCampos.add(lblSenha, 1, 0);
        gridCampos.add(txtSenha, 1, 1);
        gridCampos.add(lblCargo, 0, 2);
        gridCampos.add(cbCargo, 0, 3);

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setStyle("-fx-background-color: #012417; -fx-text-fill: #FFFFFF; -fx-background-radius: 8; -fx-padding: 10 45 10 45; -fx-font-weight: bold; -fx-cursor: hand;");

        btnSalvar.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            String senha = txtSenha.getText().trim();
            String cargo = cbCargo.getValue();

            if (nome.isEmpty() || senha.isEmpty() || "Selecione".equals(cargo)) {
                mostrarAlerta("Campos Incompletos", "Por favor, preencha todos os campos.", Alert.AlertType.ERROR);
                return;
            }

            try {
                PerfilUsuario perfil = PerfilUsuario.valueOf(cargo);
                Usuario novoUsuario = UsuarioFactory.criarUsuario(perfil, 0, nome, senha);
                usuarioService.cadastrarUsuario(novoUsuario);

                mostrarAlerta("Sucesso", "Funcionário cadastrado com sucesso!", Alert.AlertType.INFORMATION);
                modalStage.close();
                carregarFuncionarios(ownerStage); // atualiza a tabela
            } catch (RegistroDuplicadoException ex) {
                mostrarAlerta("Erro", "Já existe um funcionário com esse nome.", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                mostrarAlerta("Erro", "Não foi possível cadastrar o funcionário: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        containerModal.getChildren().addAll(boxFechar, lblTituloModal, gridCampos, btnSalvar);
        Scene modalScene = new Scene(containerModal);
        modalScene.setFill(Color.TRANSPARENT);
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
        removerEfeitoFundo(ownerStage);
    }

    private void abrirModalEditar(Stage ownerStage, Usuario func) {
        Stage modalStage = prepararModal(ownerStage);

        VBox containerModal = new VBox(25);
        containerModal.setPadding(new Insets(30, 40, 35, 40));
        containerModal.setStyle("-fx-background-color: #EFEFEF; -fx-background-radius: 16; -fx-alignment: top-center;");
        containerModal.setPrefWidth(450);

        HBox boxFechar = criarBotaoFechar(modalStage);

        Label lblTituloModal = new Label("Editar Funcionário");
        lblTituloModal.setFont(Font.font("Roboto", FontWeight.BOLD, 22));

        GridPane gridCampos = new GridPane();
        gridCampos.setHgap(20);
        gridCampos.setVgap(10);

        Label lblNome = new Label("Nome");
        lblNome.setStyle("-fx-font-weight: bold;");
        TextField txtNome = new TextField(func.getNome());
        txtNome.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6; -fx-padding: 8;");

        Label lblSenha = new Label("Nova Senha");
        lblSenha.setStyle("-fx-font-weight: bold;");
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Deixe em branco para não alterar");
        txtSenha.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6; -fx-padding: 8;");

        gridCampos.add(lblNome, 0, 0);
        gridCampos.add(txtNome, 0, 1);
        gridCampos.add(lblSenha, 1, 0);
        gridCampos.add(txtSenha, 1, 1);

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setStyle("-fx-background-color: #012417; -fx-text-fill: #FFFFFF; -fx-background-radius: 8; -fx-padding: 10 45 10 45; -fx-font-weight: bold; -fx-cursor: hand;");

        btnSalvar.setOnAction(e -> {
            String novoNome = txtNome.getText().trim();
            String novaSenha = txtSenha.getText().trim();

            if (novoNome.isEmpty()) {
                mostrarAlerta("Campo Incompleto", "O nome não pode ser vazio.", Alert.AlertType.ERROR);
                return;
            }

            try {
                usuarioService.alterarDados(func, novaSenha.isEmpty() ? null : novaSenha, novoNome);

                mostrarAlerta("Sucesso", "Funcionário atualizado com sucesso!", Alert.AlertType.INFORMATION);
                modalStage.close();
                carregarFuncionarios(ownerStage); // atualiza a tabela
            } catch (Exception ex) {
                mostrarAlerta("Erro", "Não foi possível atualizar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        containerModal.getChildren().addAll(boxFechar, lblTituloModal, gridCampos, btnSalvar);
        Scene modalScene = new Scene(containerModal);
        modalScene.setFill(Color.TRANSPARENT);
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
        removerEfeitoFundo(ownerStage);
    }

    private Stage prepararModal(Stage ownerStage) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(ownerStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);

        ColorAdjust escurecerFundo = new ColorAdjust();
        escurecerFundo.setBrightness(-0.5);
        GaussianBlur desfoqueFundo = new GaussianBlur(5);
        desfoqueFundo.setInput(escurecerFundo);
        ownerStage.getScene().getRoot().setEffect(desfoqueFundo);

        return modalStage;
    }

    private HBox criarBotaoFechar(Stage modalStage) {
        HBox boxFechar = new HBox();
        boxFechar.setAlignment(Pos.CENTER_RIGHT);
        Button btnFecharX = new Button("✕");
        btnFecharX.setStyle("-fx-background-color: transparent; -fx-font-size: 16; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        btnFecharX.setOnAction(e -> modalStage.close());
        boxFechar.getChildren().add(btnFecharX);
        return boxFechar;
    }

    private void removerEfeitoFundo(Stage ownerStage) {
        ownerStage.getScene().getRoot().setEffect(null);
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}