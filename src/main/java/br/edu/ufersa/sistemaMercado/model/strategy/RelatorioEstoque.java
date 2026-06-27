package br.edu.ufersa.sistemaMercado.model.strategy;

import br.edu.ufersa.sistemaMercado.model.DAO.ProdutoDAO;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Relatório de estoque: lista os produtos do menor para o maior estoque.
public class RelatorioEstoque implements RelatorioStrategy {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    public String titulo() {
        return "Estoque dos produtos";
    }

    @Override
    public String[] colunas() {
        return new String[]{"Produto", "Estoque", "Preço"};
    }

    @Override
    public List<String[]> linhas() {
        List<Produto> produtos = new ArrayList<>(produtoDAO.listarTodos());
        produtos.sort(Comparator.comparingInt(Produto::getQuantidadeEstoque));

        List<String[]> linhas = new ArrayList<>();
        for (Produto p : produtos) {
            linhas.add(new String[]{
                    p.getNome(),
                    String.valueOf(p.getQuantidadeEstoque()),
                    "R$ " + String.format("%.2f", p.getPreco())
            });
        }
        return linhas;
    }
}
