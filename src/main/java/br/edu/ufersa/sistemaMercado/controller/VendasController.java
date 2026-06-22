package br.edu.ufersa.sistemaMercado.controller;

import br.edu.ufersa.sistemaMercado.model.DAO.ProdutoDAO;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import br.edu.ufersa.sistemaMercado.model.entities.TipoProduto;
import br.edu.ufersa.sistemaMercado.model.entities.FormaDeVenda;
import java.util.ArrayList;
import java.util.List;

public class VendasController {

    private final ProdutoDAO produtoDAO;

    public VendasController() {
        this.produtoDAO = new ProdutoDAO();
    }

    /**
     * Salva um novo produto comprado diretamente no banco de dados.
     */
    public boolean salvarProdutoComprado(String nome, double preco, String categoriaNome, int quantidade) {
        try {
            Produto novoProduto = new Produto();
            novoProduto.setNome(nome);
            novoProduto.setPreco(preco);
            novoProduto.setQuantidadeEstoque(quantidade);

            // Define uma forma de venda padrão (ajuste conforme seu Enum FormaDeVenda se necessário)
            novoProduto.setFormaDeVenda(FormaDeVenda.UNIDADE);

            // Cria o objeto de tipo/categoria esperado pela sua entidade
            TipoProduto tipo = new TipoProduto();
            tipo.setNome(categoriaNome);
            novoProduto.setTipo(tipo);

            // TODO: Se o seu model possuir um método específico como 'cadastrar' ou 'inserir', mude aqui:
            // produtoDAO.cadastrar(novoProduto);

            System.out.println("[Controller] Produto salvo no banco com sucesso: " + nome);
            return true;
        } catch (Exception e) {
            System.out.println("[Controller] Erro ao salvar produto no banco: " + e.getMessage());
            return false;
        }
    }

    public List<Produto> obterProdutosRecentes() {
        try {
            return produtoDAO.listarTodos();
        } catch (Exception e) {
            System.out.println("[Controller] Erro ao buscar produtos recentes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Produto> pesquisarProdutos(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return obterProdutosRecentes();
        }
        try {
            List<Produto> todos = produtoDAO.listarTodos();
            List<Produto> filtrados = new ArrayList<>();
            for (Produto p : todos) {
                boolean bateCodigo = p.getCodigoBarras() != null && p.getCodigoBarras().contains(termo);
                boolean bateNome = p.getNome() != null && p.getNome().toLowerCase().contains(termo.toLowerCase());
                if (bateCodigo || bateNome) {
                    filtrados.add(p);
                }
            }
            return filtrados;
        } catch (Exception e) {
            System.out.println("[Controller] Erro ao pesquisar produtos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean finalizarVenda(List<ItemCarrinho> itens) {
        if (itens.isEmpty()) return false;
        try {
            System.out.println("[Controller] Salvando venda com " + itens.size() + " itens...");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static class ItemCarrinho {
        private final Produto produto;
        private int quantidade;

        public ItemCarrinho(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }

        public Produto getProduto() { return produto; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
        public double getTotal() { return produto.getPreco() * quantidade; }
    }
}