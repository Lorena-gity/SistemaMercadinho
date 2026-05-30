package br.edu.ufersa.sistemaMercado.model.entities;
import java.util.Iterator;

public class Gerente extends Usuario {
    public Gerente(int idUsuario, String nome, String senha) {
        super(idUsuario, nome, senha);
    }

    /*public boolean cadastrarProduto(Produto produto) {
        if (sistema.getBancoProdutos().contains(produto)) {
            System.out.println("Produto já cadastrado");
            return false;
        } else {
            sistema.getBancoProdutos().add(produto);
            System.out.println("Produto cadastrado com sucesso!");
            return true;
        }
    }

    public boolean alterarProduto(String codigoBarras, Produto novosDados) {
        for (Produto produto : sistema.getBancoProdutos()) {
            if (produto.getCodigoBarras().equals(codigoBarras)) {
                produto.setNome(novosDados.getNome());
                produto.setTipo(novosDados.getTipo());
                return true;
            }
        }
        return false;
    }

    public boolean deletarProduto(String codigoBarras) {
        Iterator<Produto> iterator = sistema.getBancoProdutos().iterator();
        while (iterator.hasNext()) {
            Produto produto = iterator.next();
            if (produto.getCodigoBarras().equals(codigoBarras)) {
                iterator.remove();
                return true;
        }
    }
    return false;
}

    public boolean cadastrarTipo(TipoProduto tipo) {
        for (TipoProduto t : sistema.getBancoTipos()) {
            if (t.getNome().equals(tipo.getNome())) {
                return false;
            }
        }
        sistema.getBancoTipos().add(tipo);
        return true;
    }

    public boolean alterarTipo(int idTipo, TipoProduto novosDados) {
        for (TipoProduto t : sistema.getBancoTipos()) {
            if (t.getIdTipo() == idTipo) {
                t.setNome(novosDados.getNome());
                t.setFormaVenda(novosDados.getFormaDeVenda());
                return true;
            }
        }
        return false;
    }

    public boolean deletarTipo(int idTipo) {
        Iterator<TipoProduto> iterator = sistema.getBancoTipos().iterator();
        while (iterator.hasNext()) {
            TipoProduto t = iterator.next();
            if (t.getIdTipo() == idTipo) {
                iterator.remove();
                return true;
            }
        }
    return false;
    }*/
}
