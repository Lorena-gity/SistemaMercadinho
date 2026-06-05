package br.edu.ufersa.sistemaMercado.model.entities;

public class Caixa extends Usuario {
    public Caixa(int idUsuario, String nome, String senha) {
        super(idUsuario, nome, senha);
    }

    @Override
    public PerfilUsuario getPerfil() {
        return PerfilUsuario.CAIXA;
    }
}
