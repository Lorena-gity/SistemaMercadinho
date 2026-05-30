package br.edu.ufersa.sistemaMercado.model.entities;

public class ItemNota {
    private int quantidade;
    private double precoUnitario;
    private Produto produto;

    // Construtores
    public ItemNota() {}
    
    public ItemNota(int quantidade, double precoUnitario, Produto produto) {
		this.quantidade = quantidade;
		this.precoUnitario = precoUnitario;
		this.produto = produto;
	}

    // Getters e Setters
	public int getQuantidade() {
		return quantidade;
	}

	public double getPrecoUnitario() {
		return precoUnitario;
	}

	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {
        if (produto == null) {
            System.out.println("Produto inválido!");
        } else {
            this.produto = produto;
        }
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            System.out.println("Quantidade inválida");
        } else {
            this.quantidade = quantidade;
        }
    }

    public void setPrecoUnitario(double precoUnitario) {
        if (precoUnitario <= 0) {
            System.out.println("Preço inválido");
        } else {
            this.precoUnitario = precoUnitario;
        }
    }

	// métodos públicos
    public double calcularSubTotal() {
    	return quantidade * precoUnitario;
    }
}