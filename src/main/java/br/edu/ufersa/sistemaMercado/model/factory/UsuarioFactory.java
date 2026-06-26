package br.edu.ufersa.sistemaMercado.model.factory;
import br.edu.ufersa.sistemaMercado.model.entities.*;

/* Implementação de padrão de projeto FACTORY METHOD.
 * Garantindo centralizar a criação de objetos Usuario,
 * sem expor para o resto do sistema qual classe concreta será instanciada */

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