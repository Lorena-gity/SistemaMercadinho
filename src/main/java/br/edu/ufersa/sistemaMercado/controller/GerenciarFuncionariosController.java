package br.edu.ufersa.sistemaMercado.controller;

import br.edu.ufersa.sistemaMercado.model.entities.PerfilUsuario;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;
import br.edu.ufersa.sistemaMercado.model.factory.UsuarioFactory;
import br.edu.ufersa.sistemaMercado.model.service.UsuarioService;
import br.edu.ufersa.sistemaMercado.model.session.SessaoUsuario;
import br.edu.ufersa.sistemaMercado.view.Login;
import javafx.stage.Stage;

import java.util.List;

public class GerenciarFuncionariosController {

    private final UsuarioService usuarioService;

    public GerenciarFuncionariosController() {
        this.usuarioService = new UsuarioService();
    }

    public List<Usuario> listarFuncionarios() {
        return usuarioService.listarUsuarios();
    }

    public void cadastrarFuncionario(Usuario usuario) throws Exception {
        usuarioService.cadastrarUsuario(usuario);
    }

    public void editarFuncionario(Usuario funcionario, String novoNome, String novaSenha) throws Exception {
        usuarioService.alterarDados(funcionario, novaSenha.isBlank() ? null : novaSenha, novoNome);
    }

    public void removerFuncionario(Usuario funcionario) throws Exception {
        usuarioService.removerUsuario(funcionario);
    }

    public void logout(Stage stage) {
        SessaoUsuario.getInstancia().encerrarSessao();
        try {
            new Login().start(stage);
        } catch (Exception e) {
            System.out.println("Erro ao abrir login: " + e.getMessage());
        }
    }
}