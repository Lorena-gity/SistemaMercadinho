package br.edu.ufersa.sistemaMercado.controller;

import br.edu.ufersa.sistemaMercado.model.DAO.UsuarioDAO;
import br.edu.ufersa.sistemaMercado.model.entities.PerfilUsuario;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class LoginController {

    private final UsuarioDAO usuarioDAO;

    public LoginController() {
        // Inicializa o DAO para comunicação com o banco de dados
        this.usuarioDAO = new UsuarioDAO();
    }

    public void autenticar(String nomeUsuario, String senha, Stage stageAtual) {

        // Verificação básica de campos vazios
        if (nomeUsuario.trim().isEmpty() || senha.trim().isEmpty()) {
            mostrarAlertaErro("Aviso", "Por favor, preencha o usuário e a senha.");
            return;
        }

        try {
            // Tenta buscar o usuário no banco de dados pelo nome
            Usuario usuario = usuarioDAO.buscarPorNome(nomeUsuario);
            // Verifica se o usuário existe e se a senha está correta. Usando o método autenticar() já criada
            if (usuario == null || !usuario.autenticar(senha)) {
                mostrarAlertaErro("Falha no Login", "Usuário ou senha incorretos!");
                return;
            }
            PerfilUsuario perfil = usuario.getPerfil();

            if (perfil == PerfilUsuario.GERENTE) {
                abrirTelaGerente(stageAtual);
            } else if (perfil == PerfilUsuario.CAIXA) {
                abrirTelaCaixa(stageAtual);
            }

        } catch (RuntimeException e) {
            mostrarAlertaErro("Erro de Conexão",
                    "Não foi possível conectar ao banco de dados.\nVerifique sua conexão.\n\nDetalhes: " + e.getMessage());
        }
    }

    private void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void abrirTelaGerente(Stage stageAtual) {
        try {
            System.out.println("Login bem-sucedido! Abrindo painel do Gerente...");
            stageAtual.close();
        } catch (Exception e) {
            mostrarAlertaErro("Erro de Inicialização", "Não foi possível carregar a tela do Gerente.");
        }
    }

    private void abrirTelaCaixa(Stage stageAtual) {
        try {
            System.out.println("Login bem-sucedido! Abrindo painel do Caixa...");
            stageAtual.close();
        } catch (Exception e) {
            mostrarAlertaErro("Erro de Inicialização", "Não foi possível carregar a tela do Caixa.");
        }
    }
}