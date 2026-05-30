package br.edu.ufersa.sistemaMercado.model.service;
import br.edu.ufersa.sistemaMercado.exceptions.DadosIncorretosException;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import java.util.ArrayList;
import java.util.List;

public class ProdutoService {
    private List<Produto> bancoProdutos;

    public ProdutoService() {
        bancoProdutos = new ArrayList<>();
    }

    public List<Produto> getBancoProdutos() {
        return bancoProdutos;
    }


    // métodos públicos
    public void criarProduto(Produto produto) throws DadosIncorretosException {
        if (produto == null) {
            throw new DadosIncorretosException("Produto inválido");
        }
        for (Produto p : bancoProdutos) {
            if (p.getCodigoBarras().equals(produto.getCodigoBarras())) {
                throw new DadosIncorretosException("Produto já cadastrado");
            }
        }
        bancoProdutos.add(produto);
        System.out.println("Produto cadastrado com sucesso.");
    }

    public void listarProduto(Produto produto) {
        if (bancoProdutos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado");
            return;
        } else {
            for (Produto p : bancoProdutos) {
                System.out.println("ID: " + p.getIdProduto());
                System.out.println("Nome: " + p.getNome());
                System.out.println("Código de barras: " + p.getCodigoBarras());
                System.out.println("Quantidade em estoque: " + p.getQuantidadeEstoque());
                System.out.println("Preço: " + p.getPreco());
                System.out.println("----------------------------------");
            }
        }
    }

    public boolean removerProduto(String codigoDeBarras) throws DadosIncorretosException {
        if (codigoDeBarras == null || codigoDeBarras.isEmpty()) {
            throw new DadosIncorretosException("Código de barras inválido");
        } else {
            boolean removido = bancoProdutos.removeIf(p -> p.getCodigoBarras().equals(codigoDeBarras)); //muda isso mds
            if (!removido) {
                throw new DadosIncorretosException("Produto não encontrado.");
            } else {
                System.out.println("Produto removido com sucesso!");
                return true;
            }
        }
    }

    public void alterarDados(Produto produto, String novoNome, double novoPreco) throws DadosIncorretosException {
        if (produto == null || !bancoProdutos.contains(produto)) {
            throw new DadosIncorretosException("Produto não encontrado.");
        } if (novoNome != null && !novoNome.isEmpty()) {
            produto.setNome(novoNome);
        } if (novoPreco > 0) {
            produto.setPreco(novoPreco);
        }
    }

    public Produto pesquisarPorCodigo(String codigoBarras) throws DadosIncorretosException {
        for (Produto p : bancoProdutos) {
            if (p.getCodigoBarras().equals(codigoBarras)) {
                return p;
            }
        }
        throw new DadosIncorretosException("Produto não encontrado.");
    }

    public List<Produto> pesquisarPorNome(String nome) throws DadosIncorretosException {
        List<Produto> encontrados = new ArrayList<>();
        for (Produto p : bancoProdutos) {
            if (p.getNome().toLowerCase().contains(nome.toLowerCase())) {
                encontrados.add(p);
            }
        }
        if (encontrados.isEmpty()) {
            throw new DadosIncorretosException("Nenhum produto encontrado com esse nome.");
        }
        return encontrados;
    }
}
