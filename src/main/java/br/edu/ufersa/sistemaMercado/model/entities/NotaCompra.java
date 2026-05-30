package br.edu.ufersa.sistemaMercado.model.entities;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class NotaCompra {
    private int numeroNota;
    private LocalDate dataHora;
    private double valorTotal;
    private List<ItemNota> listaItens;

    // Construtores
    public NotaCompra() {
        this.listaItens = new ArrayList<>();
        this.dataHora = LocalDate.now();
    }
    
    public NotaCompra(int numeroNota, LocalDate dataHora, double valorTotal) {
		super();
		this.numeroNota = numeroNota;
		this.dataHora = dataHora;
		this.valorTotal = valorTotal;
	}

    // Getters e Setters
    public int getNumeroNota() {
        return numeroNota;
    }

    public List<ItemNota> getListaItens() {
        return listaItens;
    }
	
    public void adicionarItem(Produto produto, int quantidade) {
        ItemNota item = new ItemNota();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        listaItens.add(item);
    }

    public boolean cancelarItem(String codigoBarras) {
        Iterator<ItemNota> iterator = listaItens.iterator();
        while (iterator.hasNext()) {
            ItemNota item = iterator.next();
            if (item.getProduto().getCodigoBarras().equals(codigoBarras)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public boolean trocarItem(String codigoAntigo, Produto novoProduto, int quantidade) {
        boolean removido = cancelarItem(codigoAntigo);
        if (removido) {
            adicionarItem(novoProduto, quantidade);
            return true;
        } else {
            return false;
        }
    }

 	public double calcularTotal() {
        double total = 0;
        for (ItemNota item : listaItens) {
            total += item.calcularSubTotal();
        }
        this.valorTotal = total;
        return total;
    }
   
    public void mostrarNota() {
        System.out.println("Número da Nota:" + numeroNota);
        System.out.println("Data: " + dataHora);
        System.out.println("Itens: ");

        for (ItemNota item : listaItens) {
            System.out.println(item.getProduto().getNome() + " | Quantidade: " + item.getQuantidade() +
            " | Unidade: " + item.getPrecoUnitario() + " | SubTotal: " + item.calcularSubTotal());
        }
        System.out.println("Total: " + calcularTotal());
    }
}