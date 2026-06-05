package br.edu.ufersa.sistemaMercado.model.service;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.exceptions.ElementoNaoEncontradoException;
import br.edu.ufersa.sistemaMercado.exceptions.RegistroDuplicadoException;
import br.edu.ufersa.sistemaMercado.model.DAO.TipoProdutoDAO;
import br.edu.ufersa.sistemaMercado.model.entities.TipoProduto;

import java.util.List;

public class TipoProdutoService {
    private final TipoProdutoDAO tipoDAO = new TipoProdutoDAO();

    public void criarTipoProduto(TipoProduto tipo) throws DadosInvalidosException, RegistroDuplicadoException {
        if (tipo == null) {
            throw new DadosInvalidosException("Tipo inválido");
        }
        if (tipoDAO.buscarPorNome(tipo.getNome()) != null) {
            throw new RegistroDuplicadoException("Tipo já cadastrado");
        }
        tipoDAO.inserir(tipo);
    }

    public List<TipoProduto> listarTipos() {
        return tipoDAO.listarTodos();
    }

    public void removerTipo(String nome) throws ElementoNaoEncontradoException {
        for (TipoProduto t : tipoDAO.listarTodos()) {
            if (t.getNome().equals(nome)) {
                tipoDAO.deletar(t.getIdTipo());
                return;
            }
        }
        throw new ElementoNaoEncontradoException("Tipo não encontrado.");
    }

    public void atualizarTipo(TipoProduto tipo, String novoNome) throws ElementoNaoEncontradoException {
        if (tipo == null) {
            throw new ElementoNaoEncontradoException("Tipo não encontrado");
        }
        if (novoNome != null && !novoNome.isEmpty()) {
            tipo.setNome(novoNome);
        }
        tipoDAO.atualizar(tipo);
    }
}
