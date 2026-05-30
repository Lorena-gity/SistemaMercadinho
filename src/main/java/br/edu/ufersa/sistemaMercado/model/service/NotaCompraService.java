package br.edu.ufersa.sistemaMercado.model.service;

import br.edu.ufersa.sistemaMercado.exceptions.DadosIncorretosException;
import br.edu.ufersa.sistemaMercado.model.entities.ItemNota;
import br.edu.ufersa.sistemaMercado.model.entities.NotaCompra;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import java.util.Iterator;

public class NotaCompraService {

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

    public void listarItens(NotaCompra nota) throws DadosIncorretosException {
        if (nota == null) throw new DadosIncorretosException("Nota inválida.");
        if (nota.getListaItens().isEmpty()) {
            System.out.println("Nenhum item na nota.");
            return;
        }
        for (ItemNota item : nota.getListaItens()) {
            System.out.println("Produto: " + item.getProduto().getNome());
            System.out.println("Quantidade: " + item.getQuantidade());
            System.out.println("Preço unitário: " + item.getPrecoUnitario());
            System.out.println("Subtotal: " + item.calcularSubTotal());
            System.out.println("--------------------------------");
        }
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

    public void mostrarNota(NotaCompra nota) throws DadosIncorretosException {
        if (nota == null) throw new DadosIncorretosException("Nota inválida.");

        System.out.println("Número da Nota: " + nota.getNumeroNota());
        System.out.println("Data: " + nota.getDataHora());
        System.out.println("Itens:");
        listarItens(nota);
        System.out.println("Total: " + calcularTotal(nota));
    }
}