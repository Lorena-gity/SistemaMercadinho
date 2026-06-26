package br.edu.ufersa.sistemaMercado.controller;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.model.entities.PerfilUsuario;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;
import br.edu.ufersa.sistemaMercado.model.service.UsuarioService;
import br.edu.ufersa.sistemaMercado.model.session.SessaoUsuario;
import br.edu.ufersa.sistemaMercado.view.Login;
import br.edu.ufersa.sistemaMercado.view.Vendas;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class LoginController {

    private final UsuarioService usuarioService = new UsuarioService();

    public void autenticar(String nomeUsuario, String senha, Stage stageAtual) {
        if (nomeUsuario.trim().isEmpty() || senha.trim().isEmpty()) {
            mostrarAlertaErro("Aviso", "Por favor, preencha o usuário e a senha.");
            return;
        }
        try {
            Usuario usuario = usuarioService.login(nomeUsuario, senha); // Autentica via Service
            SessaoUsuario.getInstancia().setUsuarioLogado(usuario); // Salva na sessão global
            PerfilUsuario perfil = usuario.getPerfil(); // Abre a tela correta
            if (perfil == PerfilUsuario.GERENTE) {
                abrirTelaVendas(stageAtual, usuario);
            } else if (perfil == PerfilUsuario.CAIXA) {
                abrirTelaVendas(stageAtual, usuario);
            }
        } catch (DadosInvalidosException e) {
            mostrarAlertaErro("Falha no Login", e.getMessage());
        } catch (RuntimeException e) {
            mostrarAlertaErro("Erro de Conexão",
                    "Não foi possível conectar ao banco de dados.\n\nDetalhes: " + e.getMessage());
        }
    }

    private void abrirTelaVendas(Stage stageAtual, Usuario usuario) {
        try {
            Login.mudarDeTela(stageAtual, new Vendas(usuario));
        } catch (Exception e) {
            mostrarAlertaErro("Erro de Inicialização", "Não foi possível carregar a tela.");
        }
    }

    private void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}