package br.edu.ufersa.sistemaMercado.model.strategy;

import br.edu.ufersa.sistemaMercado.model.DAO.NotaCompraDAO;
import br.edu.ufersa.sistemaMercado.model.entities.ItemNota;
import br.edu.ufersa.sistemaMercado.model.entities.NotaCompra;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Relatório de produtos mais vendidos: soma a quantidade e o valor vendido de cada produto.
public class RelatorioMaisVendidos implements RelatorioStrategy {

    private final NotaCompraDAO notaDAO = new NotaCompraDAO();

    @Override
    public String titulo() {
        return "Produtos mais vendidos";
    }

    @Override
    public String[] colunas() {
        return new String[]{"Produto", "Qtd. vendida", "Valor total"};
    }

    @Override
    public List<String[]> linhas() {
        Map<String, double[]> acumulado = new LinkedHashMap<>(); // nome -> [quantidade, valor]
        for (NotaCompra nota : notaDAO.listarTodos()) {
            for (ItemNota item : nota.getListaItens()) {
                String nome = item.getProduto() != null ? item.getProduto().getNome() : "(produto removido)";
                double[] dados = acumulado.computeIfAbsent(nome, k -> new double[2]);
                dados[0] += item.getQuantidade();
                dados[1] += item.calcularSubTotal();
            }
        }

        List<Map.Entry<String, double[]>> ordenado = new ArrayList<>(acumulado.entrySet());
        ordenado.sort((a, b) -> Double.compare(b.getValue()[0], a.getValue()[0]));

        List<String[]> linhas = new ArrayList<>();
        for (Map.Entry<String, double[]> e : ordenado) {
            linhas.add(new String[]{
                    e.getKey(),
                    String.valueOf((int) e.getValue()[0]),
                    "R$ " + String.format("%.2f", e.getValue()[1])
            });
        }
        return linhas;
    }
}
