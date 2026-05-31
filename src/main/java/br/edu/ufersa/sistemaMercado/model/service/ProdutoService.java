package br.edu.ufersa.sistemaMercado.model.service;

import br.edu.ufersa.sistemaMercado.exceptions.DadosIncorretosException;
import br.edu.ufersa.sistemaMercado.model.dao.ProdutoDAO;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;

import java.util.List;

public class ProdutoService {
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public void criarProduto(Produto produto) throws DadosIncorretosException {
        if (produto == null) {
            throw new DadosIncorretosException("Produto inválido");
        }
        if (produtoDAO.buscarPorCodigoBarras(produto.getCodigoBarras()) != null) {
            throw new DadosIncorretosException("Produto já cadastrado");
        }
        produtoDAO.inserir(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoDAO.listarTodos();
    }

    public void removerProduto(String nome) throws DadosIncorretosException {
        for (Produto p : produtoDAO.listarTodos()) {
            if (p.getNome().equals(nome)) {
                produtoDAO.deletar(p.getIdProduto());
                return;
            }
        }
        throw new DadosIncorretosException("Produto não encontrado.");
    }

    public void alterarDados(Produto produto, String novoNome, double novoPreco) throws DadosIncorretosException {
        if (produto == null) {
            throw new DadosIncorretosException("Produto não encontrado.");
        }
        if (novoNome != null && !novoNome.isEmpty()) {
            produto.setNome(novoNome);
        }
        if (novoPreco > 0) {
            produto.setPreco(novoPreco);
        }
        produtoDAO.atualizar(produto);
    }

    public Produto pesquisarPorCodigo(String codigoBarras) throws DadosIncorretosException {
        Produto produto = produtoDAO.buscarPorCodigoBarras(codigoBarras);
        if (produto == null) {
            throw new DadosIncorretosException("Produto não encontrado.");
        }
        return produto;
    }

    public List<Produto> pesquisarPorNome(String nome) throws DadosIncorretosException {
        List<Produto> encontrados = produtoDAO.buscarPorNome(nome);
        if (encontrados.isEmpty()) {
            throw new DadosIncorretosException("Nenhum produto encontrado com esse nome.");
        }
        return encontrados;
    }
}
