package br.edu.ufersa.sistemaMercado.model.service;
import br.edu.ufersa.sistemaMercado.exceptions.DadosIncorretosException;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;
import java.util.List;
import java.util.ArrayList;

public class UsuarioService {
    private List<Usuario> bancoUsuarios;

    public UsuarioService() {
        bancoUsuarios = new ArrayList<>();
    }


    public List<Usuario> getBancoUsuarios() {
        return bancoUsuarios;
    }


    public boolean autenticar(Usuario usuario, String senhaDigitada) {
        if (usuario == null || senhaDigitada == null || usuario.getSenha() == null) {
            return false;
        } else {
            return usuario.getSenha().equals(senhaDigitada);
        }
    }

    public void cadastrarUsuario(Usuario usuario) throws DadosIncorretosException {
        if (usuario == null) {
            throw new DadosIncorretosException("Usuário inválido.");
        } else {
            for (Usuario u : bancoUsuarios) {
                if (u.getIdUsuario() == usuario.getIdUsuario()) {
                    throw new DadosIncorretosException("Usuário já cadastrado");
                }
            }
            bancoUsuarios.add(usuario);
            System.out.println("Usuário cadastrado com sucesso.");
        }
    }

    public void listarUsuario() {
        if (bancoUsuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        } else {
            for (Usuario u : bancoUsuarios) {
                System.out.println("ID: " + u.getIdUsuario());
                System.out.println("Nome: " + u.getNome());
                System.out.println("--------------------------------");
            }
        }
    }

    public void alterarDados(Usuario usuario, String novaSenha, String novoNome) throws DadosIncorretosException {
        if (usuario != null && bancoUsuarios.contains(usuario)) {
            throw new DadosIncorretosException("Usuário inválido.");
        } if (novoNome != null && !novoNome.isEmpty()) {
            usuario.setNome(novoNome);
            System.out.println("Nome atualizado com sucesso!");
        } if (novaSenha != null && !novaSenha.isEmpty()) {
            usuario.setSenha(novaSenha);
            System.out.println("Senha atualizada com sucesso!");
        }
    }

    public void removerUsuario(Usuario usuario) throws DadosIncorretosException {
        boolean removido = bancoUsuarios.remove(usuario);
        if (removido) {
            System.out.println("Usuário removido com sucesso!");
        } else {
            throw new DadosIncorretosException("Usuário não encontrado.");
        }
    }
}