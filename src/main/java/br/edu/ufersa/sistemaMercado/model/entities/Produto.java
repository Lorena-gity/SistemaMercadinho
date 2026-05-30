package br.edu.ufersa.sistemaMercado.model.entities;

import br.edu.ufersa.sistemaMercado.exceptions.DadosIncorretosException;

public class Produto {
    private int idProduto;
    private String codigoBarras;
    private String nome;
    private int quantidadeEstoque;
    private double preco;
    private TipoProduto tipo;
    private Marca marca;
    private FormaDeVenda formaDeVenda;

    // Construtores
    public Produto() {}
    
    public Produto(int idProduto, String codigoBarras, String nome, int quantidadeEstoque, double preco, TipoProduto tipo, Marca marca, FormaDeVenda formaDeVenda) {
		this.idProduto = idProduto;
		this.codigoBarras = codigoBarras;
		this.nome = nome;
		this.quantidadeEstoque = quantidadeEstoque;
		this.preco = preco;
		this.tipo = tipo;
        this.marca = marca;
        this.formaDeVenda = formaDeVenda;
	}   

	// Getters e Setters
    public int getIdProduto() {
        return idProduto;
    }
   
    public String getCodigoBarras() {
        return codigoBarras;
    }

    public String getNome() {
        return nome;
    }
    
    public double getPreco() {
        return preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public TipoProduto getTipo() {
        return tipo;
    }

    public Marca getMarca() { return marca; }

    public FormaDeVenda getFormaDeVenda() { return formaDeVenda; }


    public void setNome(String nome) throws DadosIncorretosException {
    	if (nome == null || nome.isEmpty()) {
            throw new DadosIncorretosException("Nome de produto inválido.");
        } else {
            this.nome = nome;
        }
    }

    public void setCodigoBarras(String codigoBarras) throws DadosIncorretosException {
        if (codigoBarras == null || codigoBarras.length() != 13) {
            throw new DadosIncorretosException("Código de barras inválido.");
        } else {
            this.codigoBarras = codigoBarras;
        }
    }

    public void setTipo(TipoProduto tipo) throws DadosIncorretosException {
        if (tipo == null) {
            throw new DadosIncorretosException("Categoria inválida");
        } else {
            this.tipo = tipo;
        }
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) throws DadosIncorretosException {
        if (quantidadeEstoque < 0) {
            throw new DadosIncorretosException("Quantidade inválida");
        } else {
            this.quantidadeEstoque = quantidadeEstoque;
        }
    }

    public void setPreco(double preco) throws DadosIncorretosException {
        if (preco <= 0) {
            throw new DadosIncorretosException("Preço inválido");
        } else {
            this.preco = preco;
        }
    }

    public void setMarca(Marca marca) throws DadosIncorretosException {
        if (marca == null) {
            throw new DadosIncorretosException("Marca inválida.");
        } else {
            this.marca = marca;
        }
    }

    public void setFormaDeVenda(FormaDeVenda formaDeVenda) throws DadosIncorretosException {
        if (formaDeVenda == null) {
            throw new DadosIncorretosException("Forma de venda inválida");
        } else {
            this.formaDeVenda = formaDeVenda;
        }
    }


}