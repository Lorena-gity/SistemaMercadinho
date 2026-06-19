package br.edu.ufersa.sistemaMercado.model.facade;

import br.edu.ufersa.sistemaMercado.model.entities.NotaCompra;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import br.edu.ufersa.sistemaMercado.model.service.NotaCompraService;

public class CompraFacade {

    private final NotaCompraService notaCompraService =
            new NotaCompraService();

    public void adicionarProduto(
            NotaCompra nota,
            Produto produto,
            int quantidade) throws Exception {

        notaCompraService.adicionarItem(
                nota,
                produto,
                quantidade
        );
    }

    public void finalizarCompra(NotaCompra nota) throws Exception {
        notaCompraService.finalizarVenda(nota);
    }
}