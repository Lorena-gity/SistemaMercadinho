package br.edu.ufersa.sistemaMercado.model.service;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.exceptions.ElementoNaoEncontradoException;
import br.edu.ufersa.sistemaMercado.exceptions.EstoqueInsuficienteException;
import br.edu.ufersa.sistemaMercado.exceptions.RegistroDuplicadoException;
import br.edu.ufersa.sistemaMercado.model.DAO.ProdutoDAO;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;

import java.util.List;

public class ProdutoService {
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public void criarProduto(Produto produto) throws DadosInvalidosException, RegistroDuplicadoException {
        if (produto == null) {
            throw new DadosInvalidosException("Produto inválido");
        }
        if (produtoDAO.buscarPorCodigoBarras(produto.getCodigoBarras()) != null) {
            throw new RegistroDuplicadoException("Produto já cadastrado");
        }
        produtoDAO.inserir(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoDAO.listarTodos();
    }

    public void removerProduto(String nome) throws ElementoNaoEncontradoException {
        for (Produto p : produtoDAO.listarTodos()) {
            if (p.getNome().equals(nome)) {
                produtoDAO.deletar(p.getIdProduto());
                return;
            }
        }
        throw new ElementoNaoEncontradoException("Produto não encontrado.");
    }

    public void alterarDados(Produto produto, String novoNome, double novoPreco) throws DadosInvalidosException, ElementoNaoEncontradoException {
        if (produto == null) {
            throw new ElementoNaoEncontradoException("Produto não encontrado.");
        }
        if (novoNome != null && !novoNome.isEmpty()) {
            produto.setNome(novoNome);
        }
        if (novoPreco > 0) {
            produto.setPreco(novoPreco);
        }
        produtoDAO.atualizar(produto);
    }

    public Produto pesquisarPorCodigo(String codigoBarras) throws ElementoNaoEncontradoException {
        Produto produto = produtoDAO.buscarPorCodigoBarras(codigoBarras);
        if (produto == null) {
            throw new ElementoNaoEncontradoException("Produto não encontrado.");
        }
        return produto;
    }

    public List<Produto> pesquisarPorNome(String nome) throws ElementoNaoEncontradoException {
        List<Produto> encontrados = produtoDAO.buscarPorNome(nome);
        if (encontrados.isEmpty()) {
            throw new ElementoNaoEncontradoException("Nenhum produto encontrado com esse nome.");
        }
        return encontrados;
    }
}
