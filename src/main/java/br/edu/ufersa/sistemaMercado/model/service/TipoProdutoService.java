package br.edu.ufersa.sistemaMercado.model.service;
import br.edu.ufersa.sistemaMercado.exceptions.DadosIncorretosException;
import br.edu.ufersa.sistemaMercado.model.entities.TipoProduto;
import java.util.List;
import java.util.ArrayList;

public class TipoProdutoService {
    private List<TipoProduto> bancoTipos;

    public TipoProdutoService() {
        bancoTipos = new ArrayList<>();
    }

    public List<TipoProduto> getBancoTipos() {
        return bancoTipos;
    }

    public void criarTipoProduto(TipoProduto tipo) throws DadosIncorretosException {
        if (tipo == null) {
            throw new DadosIncorretosException("Tipo inválido");
        } else {
            for (TipoProduto t : bancoTipos) {
                if (t.getIdTipo() == tipo.getIdTipo()) {
                    throw new DadosIncorretosException("Tipo já cadastrado");
                }
            }
            bancoTipos.add(tipo);
            System.out.println("Tipo cadastrado com sucesso");
        }
    }

    public void listarTipoProduto() throws DadosIncorretosException {
        if (bancoTipos.isEmpty()) {
            System.out.println("Nenhum tipo cadastrado");
            return;
        } else {
            for (TipoProduto t : bancoTipos) {
                System.out.println("Nome: " + t.getNome());
            }
        }
    }

    public boolean removerTipo(String nome) throws DadosIncorretosException {
        for (int i = 0; i < bancoTipos.size(); i++) {
            if (bancoTipos.get(i).getNome().equals(nome)) {
                bancoTipos.remove(i);
                System.out.println("Tipo removido com sucesso");
                return true;
            }
        }
        throw new DadosIncorretosException("Tipo não encontrado.");
    }

    public void atualizarTipo(TipoProduto tipo, String novoNome) throws DadosIncorretosException {
        if (tipo == null || !bancoTipos.contains(tipo)) {
            throw new DadosIncorretosException("Tipo não encontrado");
        } if (novoNome != null && !novoNome.isEmpty()) {
            tipo.setNome(novoNome);
        }
    }
}
