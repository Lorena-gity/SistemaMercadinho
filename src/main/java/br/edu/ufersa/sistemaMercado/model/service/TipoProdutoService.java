package br.edu.ufersa.sistemaMercado.model.service;

import br.edu.ufersa.sistemaMercado.exceptions.DadosIncorretosException;
import br.edu.ufersa.sistemaMercado.model.dao.TipoProdutoDAO;
import br.edu.ufersa.sistemaMercado.model.entities.TipoProduto;

import java.util.List;

public class TipoProdutoService {
    private final TipoProdutoDAO tipoDAO = new TipoProdutoDAO();

    public void criarTipoProduto(TipoProduto tipo) throws DadosIncorretosException {
        if (tipo == null) {
            throw new DadosIncorretosException("Tipo inválido");
        }
        if (tipoDAO.buscarPorNome(tipo.getNome()) != null) {
            throw new DadosIncorretosException("Tipo já cadastrado");
        }
        tipoDAO.inserir(tipo);
    }

    public List<TipoProduto> listarTipos() {
        return tipoDAO.listarTodos();
    }

    public void removerTipo(String nome) throws DadosIncorretosException {
        for (TipoProduto t : tipoDAO.listarTodos()) {
            if (t.getNome().equals(nome)) {
                tipoDAO.deletar(t.getIdTipo());
                return;
            }
        }
        throw new DadosIncorretosException("Tipo não encontrado.");
    }

    public void atualizarTipo(TipoProduto tipo, String novoNome) throws DadosIncorretosException {
        if (tipo == null) {
            throw new DadosIncorretosException("Tipo não encontrado");
        }
        if (novoNome != null && !novoNome.isEmpty()) {
            tipo.setNome(novoNome);
        }
        tipoDAO.atualizar(tipo);
    }
}
