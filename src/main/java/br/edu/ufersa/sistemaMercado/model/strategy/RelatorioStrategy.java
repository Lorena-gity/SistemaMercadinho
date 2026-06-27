package br.edu.ufersa.sistemaMercado.model.strategy;

import java.util.List;

// Padrão Strategy: cada tipo de relatório gera seus próprios dados,
// mas todos seguem o mesmo contrato (título, colunas e linhas).
public interface RelatorioStrategy {

    String titulo();

    String[] colunas();

    List<String[]> linhas();
}
