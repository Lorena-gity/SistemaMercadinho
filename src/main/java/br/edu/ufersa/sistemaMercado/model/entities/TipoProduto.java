package br.edu.ufersa.sistemaMercado.model.entities;

public class TipoProduto {
    private int idTipo;
    private String nome;
    
    // Construtores
    public TipoProduto() {}
    
    public TipoProduto(int idTipo, String nome) {
		this.idTipo = idTipo;
		this.nome = nome;
	}

	public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("Nome inválido");
        } else {
            this.nome = nome;
        }
    }
}