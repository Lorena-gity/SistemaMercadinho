package br.edu.ufersa.sistemaMercado.model.entities;

public class TipoProduto {
    private int idTipo;
    private String nome;
    private FormaDeVenda formaVenda;
    
    // Construtores
    public TipoProduto() {}
    
    public TipoProduto(int idTipo, String nome, FormaDeVenda formaVenda) {
		this.idTipo = idTipo;
		this.nome = nome;
		this.formaVenda = formaVenda;
	}

	public int getIdTipo() {
        return idTipo;
    }
   
    public String getNome() {
        return nome;
    }

    public FormaDeVenda getFormaDeVenda() {
        return formaVenda;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("Nome inválido");
        } else {
            this.nome = nome;
        }
    }

    public void setFormaVenda(FormaDeVenda formaVenda) {
        if (formaVenda == null) {
            this.formaVenda = FormaDeVenda.UNIDADE;
        } else {
            this.formaVenda = formaVenda;
        }
    }
}