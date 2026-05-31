package br.edu.ufersa.sistemaMercado.model.service;

import br.edu.ufersa.sistemaMercado.exceptions.DadosIncorretosException;
import br.edu.ufersa.sistemaMercado.model.dao.UsuarioDAO;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;

import java.util.List;

public class UsuarioService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario login(String nome, String senha) throws DadosIncorretosException {
        Usuario usuario = usuarioDAO.buscarPorNome(nome);
        if (usuario == null || !usuario.autenticar(senha)) {
            throw new DadosIncorretosException("Nome ou senha incorretos.");
        }
        return usuario;
    }

    public void cadastrarUsuario(Usuario usuario) throws DadosIncorretosException {
        if (usuario == null) {
            throw new DadosIncorretosException("Usuário inválido.");
        }
        if (usuarioDAO.buscarPorNome(usuario.getNome()) != null) {
            throw new DadosIncorretosException("Usuário já cadastrado");
        }
        usuarioDAO.inserir(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    public void alterarDados(Usuario usuario, String novaSenha, String novoNome) throws DadosIncorretosException {
        if (usuario == null) {
            throw new DadosIncorretosException("Usuário inválido.");
        }
        if (novoNome != null && !novoNome.isEmpty()) {
            usuario.setNome(novoNome);
        }
        if (novaSenha != null && !novaSenha.isEmpty()) {
            usuario.setSenha(novaSenha);
        }
        usuarioDAO.atualizar(usuario);
    }

    public void removerUsuario(Usuario usuario) throws DadosIncorretosException {
        if (usuario == null) {
            throw new DadosIncorretosException("Usuário não encontrado.");
        }
        usuarioDAO.deletar(usuario.getIdUsuario());
    }
}
