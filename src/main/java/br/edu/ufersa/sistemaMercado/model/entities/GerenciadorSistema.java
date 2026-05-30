package br.edu.ufersa.sistemaMercado.model.entities;
import java.util.List;
import java.util.ArrayList;

public class GerenciadorSistema {
    private Usuario usuarioLogado;
    private List<Produto> bancoProdutos;
    private List<TipoProduto> bancoTipos;

    public GerenciadorSistema(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.bancoProdutos = new ArrayList<>();
        this.bancoTipos = new ArrayList<>();
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public List<Produto> getBancoProdutos() {
        return bancoProdutos;
    }

    public List<TipoProduto> getBancoTipos() {
        return bancoTipos;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            System.out.println("Insira um usuário válido.");
        } else {
            this.usuarioLogado = usuarioLogado;
        }
    }

    public boolean login (String nome, String senha) {
        if (usuarioLogado != null && usuarioLogado.getNome().equals(nome) && usuarioLogado.autenticar(senha) == true) {
            System.out.println("Login realizado com sucesso!");
            return true;
        } else {
            System.out.println("Nome ou senha incorretos.");
            return false;
        }
    }

    public Produto pesquisarPorCodigo(String codigoBarras) {
        for (Produto produto : bancoProdutos) {
            if(produto.getCodigoBarras().equals(codigoBarras)) {
                return produto;
            }
        }
        return null;
    }

    public List<Produto> pesquisarPorNome(String nome) {
        List<Produto> encontrados = new ArrayList<>();
        for(Produto produto : bancoProdutos) {
            if (produto.getNome().contains(nome)) {
                encontrados.add(produto);
            } else {
                System.out.println("Produto não encontrado.");
            }
        }
        return encontrados;
    }

    public boolean verificarPermissaoGerente() {
        return usuarioLogado != null && usuarioLogado.getPerfil() == PerfilUsuario.GERENTE;
    }
}