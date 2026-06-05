package br.edu.ufersa.sistemaMercado.model.service;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.exceptions.ElementoNaoEncontradoException;
import br.edu.ufersa.sistemaMercado.exceptions.EstoqueInsuficienteException;
import br.edu.ufersa.sistemaMercado.exceptions.OperacaoInvalidaException;
import br.edu.ufersa.sistemaMercado.model.DAO.NotaCompraDAO;
import br.edu.ufersa.sistemaMercado.model.DAO.ProdutoDAO;
import br.edu.ufersa.sistemaMercado.model.entities.ItemNota;
import br.edu.ufersa.sistemaMercado.model.entities.NotaCompra;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import java.util.Iterator;

public class NotaCompraService {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final NotaCompraDAO notaDAO = new NotaCompraDAO();

    public void adicionarItem(NotaCompra nota, Produto produto, int quantidade) throws DadosInvalidosException {
        if (nota == null) throw new DadosInvalidosException("Nota inválida.");
        if (produto == null) throw new DadosInvalidosException("Produto inválido.");
        if (quantidade <= 0) throw new DadosInvalidosException("Quantidade inválida.");

        ItemNota item = new ItemNota();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        nota.getListaItens().add(item);
    }

    public boolean cancelarItem(NotaCompra nota, String codigoBarras) throws DadosInvalidosException, ElementoNaoEncontradoException {
        if (nota == null) throw new DadosInvalidosException("Nota inválida.");
        if (codigoBarras == null || codigoBarras.isEmpty()) throw new DadosInvalidosException("Código inválido.");

        Iterator<ItemNota> iterator = nota.getListaItens().iterator();
        while (iterator.hasNext()) {
            ItemNota item = iterator.next();
            if (item.getProduto().getCodigoBarras().equals(codigoBarras)) {
                iterator.remove();
                return true;
            }
        }
        throw new ElementoNaoEncontradoException("Item não encontrado na nota.");
    }

    public boolean trocarItem(NotaCompra nota, String codigoAntigo, Produto novoProduto, int quantidade) throws DadosInvalidosException, ElementoNaoEncontradoException {
        cancelarItem(nota, codigoAntigo);
        adicionarItem(nota, novoProduto, quantidade);
        return true;
    }

    public double calcularTotal(NotaCompra nota) throws DadosInvalidosException, OperacaoInvalidaException {
        if (nota == null) throw new DadosInvalidosException("Nota inválida.");
        if (nota.getListaItens().isEmpty()) throw new OperacaoInvalidaException("Nota sem itens.");

        double total = 0;
        for (ItemNota item : nota.getListaItens()) {
            total += item.calcularSubTotal();
        }
        nota.setValorTotal(total);
        return total;
    }

    // Fecha a venda: confere o estoque de cada item, dá baixa e grava a nota no banco
    public void finalizarVenda(NotaCompra nota) throws DadosInvalidosException, ElementoNaoEncontradoException, OperacaoInvalidaException, EstoqueInsuficienteException {
        if (nota == null) throw new DadosInvalidosException("Nota inválida.");
        if (nota.getListaItens().isEmpty()) throw new OperacaoInvalidaException("Nota sem itens.");

        // valida o estoque antes de mexer em qualquer coisa
        for (ItemNota item : nota.getListaItens()) {
            Produto atual = produtoDAO.buscarPorId(item.getProduto().getIdProduto());
            if (atual == null) {
                throw new ElementoNaoEncontradoException("Produto não encontrado: " + item.getProduto().getNome());
            }
            if (item.getQuantidade() > atual.getQuantidadeEstoque()) {
                throw new EstoqueInsuficienteException("Estoque insuficiente para " + atual.getNome());
            }
        }

        calcularTotal(nota);

        // dá baixa no estoque de cada produto vendido
        for (ItemNota item : nota.getListaItens()) {
            Produto atual = produtoDAO.buscarPorId(item.getProduto().getIdProduto());
            atual.setQuantidadeEstoque(atual.getQuantidadeEstoque() - item.getQuantidade());
            produtoDAO.atualizar(atual);
        }

        notaDAO.inserir(nota);
    }
}
