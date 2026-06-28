package br.edu.ufersa.sistemaMercado.controller;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.exceptions.ElementoNaoEncontradoException;
import br.edu.ufersa.sistemaMercado.exceptions.EstoqueInsuficienteException;
import br.edu.ufersa.sistemaMercado.exceptions.RegistroDuplicadoException;
import br.edu.ufersa.sistemaMercado.model.entities.*;
import br.edu.ufersa.sistemaMercado.model.service.NotaCompraService;
import br.edu.ufersa.sistemaMercado.model.service.ProdutoService;
import br.edu.ufersa.sistemaMercado.model.service.TipoProdutoService;
import br.edu.ufersa.sistemaMercado.model.session.SessaoUsuario;
import br.edu.ufersa.sistemaMercado.view.GerenciarFuncionarios;
import br.edu.ufersa.sistemaMercado.view.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class VendasController {
    private final NotaCompraService notaCompraService = new NotaCompraService();
    private final ProdutoService produtoService = new ProdutoService();
    private final TipoProdutoService tipoProdutoService = new TipoProdutoService();

    // Retorna a nota gerada (com número e itens) em caso de sucesso, ou null se falhar.
    public NotaCompra finalizarVenda(List<ItemCarrinho> itens) {
        if (itens.isEmpty()) return null;
        try {
            NotaCompra nota = new NotaCompra();
            for (ItemCarrinho item : itens) {
                notaCompraService.adicionarItem(nota, item.getProduto(), item.getQuantidade());
            }
            notaCompraService.finalizarVenda(nota);
            return nota;
        } catch (EstoqueInsuficienteException e) {
            System.out.println("Estoque insuficiente: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Erro ao finalizar venda: " + e.getMessage());
            return null;
        }
    }

    public boolean salvarProdutoComprado(String nome, String marca, double preco, String categoriaNome, String forma, double quantidade) {
        try {
            TipoProduto tipo = tipoProdutoService.listarTipos().stream()
                    .filter(t -> t.getNome().equals(categoriaNome))
                    .findFirst()
                    .orElseThrow(() ->
                            new DadosInvalidosException("Categoria não encontrada"));

            Produto novoProduto = new Produto();
            novoProduto.setNome(nome);
            novoProduto.setMarca(marca);
            novoProduto.setPreco(preco);
            novoProduto.setQuantidadeEstoque(quantidade);
            novoProduto.setFormaDeVenda(FormaDeVenda.valueOf(forma));
            novoProduto.setTipo(tipo);

            produtoService.criarProduto(novoProduto);
            return true;
        } catch (DadosInvalidosException | RegistroDuplicadoException e) {
            System.out.println("Erro de validação: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
            return false;
        }
    }

    public List<Produto> obterProdutosRecentes() {
        try {
            return produtoService.listarProdutos();
        } catch (Exception e) {
            System.out.println("Erro ao buscar produtos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Produto> pesquisarProdutos(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return obterProdutosRecentes();
        }
        try {
            List<Produto> todos = produtoService.listarProdutos();
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
            System.out.println("Erro ao pesquisar produtos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static class ItemCarrinho {
        private final Produto produto;
        private double quantidade;

        public ItemCarrinho(Produto produto, double quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }

        public Produto getProduto() { return produto; }
        public double getQuantidade() { return quantidade; }
        public void setQuantidade(double quantidade) { this.quantidade = quantidade; }
        public double getTotal() { return produto.getPreco() * quantidade; }
    }

    public boolean editarProduto(Produto produto, String novoNome, String novaMarca, double novoPreco, String categoriaNome, String formaVendaNome) {
        try {
            TipoProduto tipo = tipoProdutoService.listarTipos().stream()
                    .filter(t -> t.getNome().equals(categoriaNome))
                    .findFirst()
                    .orElseThrow(() -> new DadosInvalidosException("Categoria não encontrada"));

            FormaDeVenda formaDeVenda = FormaDeVenda.valueOf(formaVendaNome);

            produto.setNome(novoNome);
            produto.setMarca(novaMarca);
            produto.setPreco(novoPreco);
            produto.setTipo(tipo);
            produto.setFormaDeVenda(formaDeVenda);

            produtoService.alterarDados(produto, novoNome, novoPreco);
            return true;
        } catch (DadosInvalidosException | ElementoNaoEncontradoException e) {
            System.out.println("Erro de validação: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Erro ao editar produto: " + e.getMessage());
            return false;
        }
    }

    public boolean excluirProduto(Produto produto) {
        try {
            produtoService.removerPorId(produto.getIdProduto());
            return true;
        } catch (Exception e) {
            // normalmente cai aqui quando o produto já tem vendas registradas (chave estrangeira)
            System.out.println("Erro ao excluir produto: " + e.getMessage());
            return false;
        }
    }

    public void logout(Stage stage){
        SessaoUsuario.getInstancia().encerrarSessao();
        try{
            new Login().start(stage);
        } catch(Exception e){
            System.out.println("Erro ao abrir login: " + e.getMessage());
        }
    }

    public ObservableList<String> listarCategorias() {
        ObservableList<String> categorias = FXCollections.observableArrayList();
        try {
            TipoProdutoService tipoService = new TipoProdutoService();
            for (TipoProduto tipo : tipoService.listarTipos()) {
                categorias.add(tipo.getNome());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public ObservableList<String> listarFormasVenda() {
        ObservableList<String> formas = FXCollections.observableArrayList();
        for (FormaDeVenda forma : FormaDeVenda.values()) {
            formas.add(forma.name());
        }
        return formas;
    }
}