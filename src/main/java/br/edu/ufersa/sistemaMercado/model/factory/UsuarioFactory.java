package br.edu.ufersa.sistemaMercado.model.factory;
import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.model.entities.*;

public class UsuarioFactory {
    public static Usuario criarUsuario(
            PerfilUsuario perfil,
            int id,
            String nome,
            String senha) {

        switch (perfil) {
            case GERENTE:
                return new Gerente(id, nome, senha);
            case CAIXA:
                return new Caixa(id, nome, senha);
            default:
                throw new IllegalArgumentException("Perfil inválido.");
        }
    }
}