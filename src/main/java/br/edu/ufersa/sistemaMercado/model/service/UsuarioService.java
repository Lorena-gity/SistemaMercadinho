package br.edu.ufersa.sistemaMercado.model.service;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.exceptions.ElementoNaoEncontradoException;
import br.edu.ufersa.sistemaMercado.exceptions.RegistroDuplicadoException;
import br.edu.ufersa.sistemaMercado.model.DAO.UsuarioDAO;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;

import java.util.List;

public class UsuarioService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario login(String nome, String senha) throws DadosInvalidosException {
        Usuario usuario = usuarioDAO.buscarPorNome(nome);
        if (usuario == null || !usuario.autenticar(senha)) {
            throw new DadosInvalidosException("Nome ou senha incorretos.");
        }
        return usuario;
    }

    public void cadastrarUsuario(Usuario usuario) throws DadosInvalidosException, RegistroDuplicadoException {
        if (usuario == null) {
            throw new DadosInvalidosException("Usuário inválido.");
        }
        if (usuarioDAO.buscarPorNome(usuario.getNome()) != null) {
            throw new RegistroDuplicadoException("Usuário já cadastrado");
        }
        usuarioDAO.inserir(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    public void alterarDados(Usuario usuario, String novaSenha, String novoNome) throws DadosInvalidosException {
        if (usuario == null) {
            throw new DadosInvalidosException("Usuário inválido.");
        }
        if (novoNome != null && !novoNome.isEmpty()) {
            usuario.setNome(novoNome);
        }
        if (novaSenha != null && !novaSenha.isEmpty()) {
            usuario.setSenha(novaSenha);
        }
        usuarioDAO.atualizar(usuario);
    }

    public void removerUsuario(Usuario usuario) throws ElementoNaoEncontradoException {
        if (usuario == null) {
            throw new ElementoNaoEncontradoException("Usuário não encontrado.");
        }
        usuarioDAO.deletar(usuario.getIdUsuario());
    }
}
