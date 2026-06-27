package br.edu.ufersa.sistemaMercado.controller;

import br.edu.ufersa.sistemaMercado.model.entities.TipoProduto;
import br.edu.ufersa.sistemaMercado.model.service.TipoProdutoService;

import java.util.ArrayList;
import java.util.List;

public class TiposController {

    private final TipoProdutoService service = new TipoProdutoService();

    public List<TipoProduto> listar() {
        try {
            return service.listarTipos();
        } catch (Exception e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean criar(String nome) {
        try {
            service.criarTipoProduto(new TipoProduto(0, nome));
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao criar categoria: " + e.getMessage());
            return false;
        }
    }

    public boolean renomear(TipoProduto tipo, String novoNome) {
        try {
            service.atualizarTipo(tipo, novoNome);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao renomear categoria: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(String nome) {
        try {
            service.removerTipo(nome);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao excluir categoria: " + e.getMessage());
            return false;
        }
    }
}
