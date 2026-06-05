package br.edu.ufersa.sistemaMercado.model.entities;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;

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
	
	public void setProduto(Produto produto) throws DadosInvalidosException {
        if (produto == null) {
            throw new DadosInvalidosException("Produto inválido!");
        } else {
            this.produto = produto;
        }
    }

    public void setQuantidade(int quantidade) throws DadosInvalidosException {
        if (quantidade <= 0) {
            throw new DadosInvalidosException("Quantidade inválida");
        } else {
            this.quantidade = quantidade;
        }
    }

    public void setPrecoUnitario(double precoUnitario) throws DadosInvalidosException {
        if (precoUnitario <= 0) {
            throw new DadosInvalidosException("Preço inválido");
        } else {
            this.precoUnitario = precoUnitario;
        }
    }

	// métodos públicos
    public double calcularSubTotal() {
    	return quantidade * precoUnitario;
    }
}