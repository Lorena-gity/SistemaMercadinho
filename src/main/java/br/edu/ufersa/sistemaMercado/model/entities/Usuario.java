package br.edu.ufersa.sistemaMercado.model.entities;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.exceptions.RegistroDuplicadoException;

public abstract class Usuario {
    private int idUsuario;
    private String nome;
    private String senha;

    // Construtores
    public Usuario() {}
    
    public Usuario(int idUsuario, String nome, String senha) {
		super();
		this.idUsuario = idUsuario;
		this.nome = nome;
		this.senha = senha;
	}

	public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
    	return nome;
    }

    public String getSenha() {
        return senha;
    }

    public abstract PerfilUsuario getPerfil();

    public boolean autenticar(String senhaDigitada) {
        return this.senha != null && this.senha.equals(senhaDigitada);
    }

    public void setNome(String novoNome) throws DadosInvalidosException, RegistroDuplicadoException { // para alterar o nome atual do usuário é necessário que o novo nome seja preenchido e que não seja igual ao anterior
        if (novoNome == null || novoNome.isEmpty()) {
            throw new DadosInvalidosException("Nome não pode ser vazio.");
        } else if (this.nome != null && novoNome.equals(this.nome)) { // .equals é usado para comparar o texto ( "==" iria comparar endereço de memória)
            throw new RegistroDuplicadoException("Novo nome deve ser diferente do nome atual.");
        } else {
            this.nome = novoNome;
        }
    }

    public void setSenha(String novaSenha) throws DadosInvalidosException, RegistroDuplicadoException {
        if (novaSenha == null || novaSenha.isEmpty()) {
            throw new DadosInvalidosException("Senha não pode ser vazia.");
        } else if (this.senha != null && novaSenha.equals(this.senha)) {
            throw new RegistroDuplicadoException("Nova senha deve ser diferente da senha atual.");
        } else {
            this.senha = novaSenha;
        }
    }
}