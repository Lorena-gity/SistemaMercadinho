package br.edu.ufersa.sistemaMercado.teste;

import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;
import br.edu.ufersa.sistemaMercado.model.DAO.TipoProdutoDAO;
import br.edu.ufersa.sistemaMercado.model.entities.FormaDeVenda;
import br.edu.ufersa.sistemaMercado.model.entities.NotaCompra;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import br.edu.ufersa.sistemaMercado.model.entities.TipoProduto;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;
import br.edu.ufersa.sistemaMercado.model.service.NotaCompraService;
import br.edu.ufersa.sistemaMercado.model.service.ProdutoService;
import br.edu.ufersa.sistemaMercado.model.service.TipoProdutoService;
import br.edu.ufersa.sistemaMercado.model.service.UsuarioService;

public class TesteDAO {

    public static void main(String[] args) {
        TipoProdutoService tipoService = new TipoProdutoService();
        ProdutoService produtoService = new ProdutoService();
        UsuarioService usuarioService = new UsuarioService();
        NotaCompraService vendaService = new NotaCompraService();

        System.out.println("\n=== 1) Cadastrar tipo ===");
        try {
            tipoService.criarTipoProduto(new TipoProduto(0, "Limpeza"));
            System.out.println("Tipo 'Limpeza' cadastrado.");
        } catch (DadosInvalidosException e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        System.out.println("\n=== 2) Cadastrar produto ===");
        try {
            TipoProduto alimentos = new TipoProdutoDAO().buscarPorNome("Alimentos");
            Produto arroz = new Produto(0, "7891111111111", "Arroz 5kg",
                    30, 24.90, alimentos, FormaDeVenda.UNIDADE);
            produtoService.criarProduto(arroz);
            System.out.println("Produto cadastrado com id = " + arroz.getIdProduto());
        } catch (DadosInvalidosException e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        System.out.println("\n=== 3) Produtos cadastrados ===");
        for (Produto p : produtoService.listarProdutos()) {
            System.out.println("  #" + p.getIdProduto() + " " + p.getNome()
                    + " | R$ " + p.getPreco() + " | estoque: " + p.getQuantidadeEstoque());
        }

        System.out.println("\n=== 4) Buscar por código ===");
        try {
            Produto achado = produtoService.pesquisarPorCodigo("7891111111111");
            System.out.println("Encontrado: " + achado.getNome());
        } catch (DadosInvalidosException e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        System.out.println("\n=== 5) Alterar produto ===");
        try {
            Produto arroz = produtoService.pesquisarPorCodigo("7891111111111");
            produtoService.alterarDados(arroz, "Arroz 5kg (Promoção)", 19.90);
            System.out.println("Produto alterado: " + arroz.getNome() + " - R$ " + arroz.getPreco());
        } catch (DadosInvalidosException e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        System.out.println("\n=== 6) Login ===");
        try {
            Usuario logado = usuarioService.login("admin", "1234");
            System.out.println("Logado: " + logado.getNome() + " (" + logado.getPerfil() + ")");
        } catch (DadosInvalidosException e) {
            System.out.println("Falha no login: " + e.getMessage());
        }

        System.out.println("\n=== 7) Venda ===");
        try {
            Produto arroz = produtoService.pesquisarPorCodigo("7891111111111");
            int estoqueAntes = arroz.getQuantidadeEstoque();

            NotaCompra nota = new NotaCompra();
            vendaService.adicionarItem(nota, arroz, 2);  
            vendaService.finalizarVenda(nota);        

            Produto depois = produtoService.pesquisarPorCodigo("7891111111111");
            System.out.println("Venda registrada! Nota nº " + nota.getNumeroNota());
            System.out.println("Total: R$ " + nota.getValorTotal());
            System.out.println("Estoque: " + estoqueAntes + " -> " + depois.getQuantidadeEstoque());
        } catch (DadosInvalidosException e) {
            System.out.println("Falha na venda: " + e.getMessage());
        }

        System.out.println("\n=== Fim dos testes ===");
    }
}
