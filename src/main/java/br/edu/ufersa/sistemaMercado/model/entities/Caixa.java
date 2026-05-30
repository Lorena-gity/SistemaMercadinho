package br.edu.ufersa.sistemaMercado.model.entities;

public class Caixa extends Usuario {
    public Caixa(int idUsuario, String nome, String senha) {
        super(idUsuario, nome, senha);
    }

    /*public void comprarProduto(String codigoBarras, int quantidade) {
        for (Produto produto : sistema.getBancoProdutos()) {
            if (produto.getCodigoBarras().equals(codigoBarras)) {
                produto.adicionarProduto(quantidade);
                System.out.println("Compra realizada! Estoque atualizado com sucesso!");
                return;
            }
        }
        System.out.println("Produto não encontrado.");
    }


    public boolean realizarVenda(NotaCompra nota) {
        for (ItemNota itens : nota.getListaItens()) {
            Produto produto = itens.getProduto();
            int quantidade = itens.getQuantidade();
            boolean conseguiu =  produto.removerEstoque(quantidade);
            if (!conseguiu) {
                System.out.println("Produto " + produto.getNome() + " em falta no estoque.");
                return false;
            }
        }
        System.out.println("Venda realizada! Estoque atualizado com sucesso!");
        return true;
    }*/
}
