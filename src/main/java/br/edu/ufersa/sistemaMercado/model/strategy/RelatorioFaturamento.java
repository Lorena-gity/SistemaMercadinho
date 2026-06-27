package br.edu.ufersa.sistemaMercado.model.strategy;

import br.edu.ufersa.sistemaMercado.model.DAO.NotaCompraDAO;
import br.edu.ufersa.sistemaMercado.model.entities.NotaCompra;

import java.util.ArrayList;
import java.util.List;

// Relatório de faturamento: lista todas as vendas e soma o total.
public class RelatorioFaturamento implements RelatorioStrategy {

    private final NotaCompraDAO notaDAO = new NotaCompraDAO();

    @Override
    public String titulo() {
        return "Faturamento (todas as vendas)";
    }

    @Override
    public String[] colunas() {
        return new String[]{"Nº da Venda", "Data", "Total"};
    }

    @Override
    public List<String[]> linhas() {
        List<String[]> linhas = new ArrayList<>();
        double total = 0;
        for (NotaCompra nota : notaDAO.listarTodos()) {
            linhas.add(new String[]{
                    String.valueOf(nota.getNumeroNota()),
                    String.valueOf(nota.getDataHora()),
                    "R$ " + String.format("%.2f", nota.getValorTotal())
            });
            total += nota.getValorTotal();
        }
        linhas.add(new String[]{"", "TOTAL", "R$ " + String.format("%.2f", total)});
        return linhas;
    }
}
