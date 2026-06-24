package br.edu.ufersa.sistemaMercado.controller;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.exceptions.ElementoNaoEncontradoException;
import br.edu.ufersa.sistemaMercado.exceptions.EstoqueInsuficienteException;
import br.edu.ufersa.sistemaMercado.exceptions.RegistroDuplicadoException;
import br.edu.ufersa.sistemaMercado.model.entities.*;
import br.edu.ufersa.sistemaMercado.model.service.NotaCompraService;
import br.edu.ufersa.sistemaMercado.model.service.ProdutoService;
import br.edu.ufersa.sistemaMercado.model.service.TipoProdutoService;
import br.edu.ufersa.sistemaMercado.view.Login;
import br.edu.ufersa.sistemaMercado.view.Vendas;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class VendasController {

    private final NotaCompraService notaCompraService = new NotaCompraService();
    private final ProdutoService produtoService = new ProdutoService();
    private final TipoProdutoService tipoProdutoService = new TipoProdutoService();

    public boolean finalizarVenda(List<ItemCarrinho> itens) {
        if (itens.isEmpty()) return false;
        try {
            NotaCompra nota = new NotaCompra();
            for (ItemCarrinho item : itens) {
                notaCompraService.adicionarItem(nota, item.getProduto(), item.getQuantidade());
            }
            notaCompraService.finalizarVenda(nota);
            return true;
        } catch (EstoqueInsuficienteException e) {
            System.out.println("Estoque insuficiente: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Erro ao finalizar venda: " + e.getMessage());
            return false;
        }
    }

    public boolean salvarProdutoComprado(String nome, double preco, String categoriaNome, int quantidade) {
        try {
            TipoProduto tipo = tipoProdutoService.listarTipos().stream()
                    .filter(t -> t.getNome().equals(categoriaNome))
                    .findFirst()
                    .orElseThrow(() -> new DadosInvalidosException("Categoria não encontrada"));

            Produto novoProduto = new Produto();
            novoProduto.setNome(nome);
            novoProduto.setPreco(preco);
            novoProduto.setQuantidadeEstoque(quantidade);
            novoProduto.setFormaDeVenda(FormaDeVenda.UNIDADE);
            novoProduto.setTipo(tipo);
            // codigoBarras não é obrigatório na compra, mas o DAO insere deixa null ou gera um
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
                boolean bateCodigo = p.getCodigoBarras() != null
                        && p.getCodigoBarras().contains(termo);
                boolean bateNome = p.getNome() != null
                        && p.getNome().toLowerCase().contains(termo.toLowerCase());
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

    public boolean editarProduto(Produto produto, String novoNome, double novoPreco,
                                 String categoriaNome, String formaVendaNome) {
        try {
            TipoProduto tipo = tipoProdutoService.listarTipos().stream()
                    .filter(t -> t.getNome().equals(categoriaNome))
                    .findFirst()
                    .orElseThrow(() -> new DadosInvalidosException("Categoria não encontrada"));

            FormaDeVenda formaDeVenda = FormaDeVenda.valueOf(formaVendaNome);

            produto.setNome(novoNome);
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

    private void abrirTelaGerenciarFuncionarios(Stage stageAtual, Usuario usuario) {
        try {
            Login.mudarDeTela(stageAtual, new Vendas(usuario));
        } catch (Exception e) {
            mostrarAlertaErro("Erro de Inicialização", "Não foi possível carregar a tela.");
        }
    }

    private void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}