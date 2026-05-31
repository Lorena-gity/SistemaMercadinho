package br.edu.ufersa.sistemaMercado.model.service;

import br.edu.ufersa.sistemaMercado.exceptions.DadosIncorretosException;
import br.edu.ufersa.sistemaMercado.model.dao.NotaCompraDAO;
import br.edu.ufersa.sistemaMercado.model.dao.ProdutoDAO;
import br.edu.ufersa.sistemaMercado.model.entities.ItemNota;
import br.edu.ufersa.sistemaMercado.model.entities.NotaCompra;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import java.util.Iterator;

public class NotaCompraService {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final NotaCompraDAO notaDAO = new NotaCompraDAO();

    public void adicionarItem(NotaCompra nota, Produto produto, int quantidade) throws DadosIncorretosException {
        if (nota == null) throw new DadosIncorretosException("Nota inválida.");
        if (produto == null) throw new DadosIncorretosException("Produto inválido.");
        if (quantidade <= 0) throw new DadosIncorretosException("Quantidade inválida.");

        ItemNota item = new ItemNota();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        nota.getListaItens().add(item);
    }

    public boolean cancelarItem(NotaCompra nota, String codigoBarras) throws DadosIncorretosException {
        if (nota == null) throw new DadosIncorretosException("Nota inválida.");
        if (codigoBarras == null || codigoBarras.isEmpty()) throw new DadosIncorretosException("Código inválido.");

        Iterator<ItemNota> iterator = nota.getListaItens().iterator();
        while (iterator.hasNext()) {
            ItemNota item = iterator.next();
            if (item.getProduto().getCodigoBarras().equals(codigoBarras)) {
                iterator.remove();
                return true;
            }
        }
        throw new DadosIncorretosException("Item não encontrado na nota.");
    }

    public boolean trocarItem(NotaCompra nota, String codigoAntigo, Produto novoProduto, int quantidade) throws DadosIncorretosException {
        cancelarItem(nota, codigoAntigo);
        adicionarItem(nota, novoProduto, quantidade);
        return true;
    }

    public double calcularTotal(NotaCompra nota) throws DadosIncorretosException {
        if (nota == null) throw new DadosIncorretosException("Nota inválida.");
        if (nota.getListaItens().isEmpty()) throw new DadosIncorretosException("Nota sem itens.");

        double total = 0;
        for (ItemNota item : nota.getListaItens()) {
            total += item.calcularSubTotal();
        }
        nota.setValorTotal(total);
        return total;
    }

    // Fecha a venda: confere o estoque de cada item, dá baixa e grava a nota no banco
    public void finalizarVenda(NotaCompra nota) throws DadosIncorretosException {
        if (nota == null) throw new DadosIncorretosException("Nota inválida.");
        if (nota.getListaItens().isEmpty()) throw new DadosIncorretosException("Nota sem itens.");

        // valida o estoque antes de mexer em qualquer coisa
        for (ItemNota item : nota.getListaItens()) {
            Produto atual = produtoDAO.buscarPorId(item.getProduto().getIdProduto());
            if (atual == null) {
                throw new DadosIncorretosException("Produto não encontrado: " + item.getProduto().getNome());
            }
            if (item.getQuantidade() > atual.getQuantidadeEstoque()) {
                throw new DadosIncorretosException("Estoque insuficiente para " + atual.getNome());
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
