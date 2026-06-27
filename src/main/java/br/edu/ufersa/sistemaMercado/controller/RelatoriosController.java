package br.edu.ufersa.sistemaMercado.controller;

import br.edu.ufersa.sistemaMercado.model.strategy.RelatorioEstoque;
import br.edu.ufersa.sistemaMercado.model.strategy.RelatorioFaturamento;
import br.edu.ufersa.sistemaMercado.model.strategy.RelatorioMaisVendidos;
import br.edu.ufersa.sistemaMercado.model.strategy.RelatorioStrategy;

import java.util.List;

public class RelatoriosController {

    private final List<RelatorioStrategy> estrategias = List.of(
            new RelatorioFaturamento(),
            new RelatorioMaisVendidos(),
            new RelatorioEstoque()
    );

    public List<RelatorioStrategy> getEstrategias() {
        return estrategias;
    }
}
