package br.edu.ufersa.sistemaMercado.model.entities;
import br.edu.ufersa.sistemaMercado.exceptions.DadosIncorretosException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class NotaCompra {
    private int numeroNota;
    private LocalDate dataHora;
    private double valorTotal;
    private List<ItemNota> listaItens;

    // Construtores
    public NotaCompra() {
        this.listaItens = new ArrayList<>();
        this.dataHora = LocalDate.now();
    }

    public NotaCompra(int numeroNota, LocalDate dataHora, double valorTotal) {
		super();
		this.numeroNota = numeroNota;
		this.dataHora = dataHora;
		this.valorTotal = valorTotal;
	}

    public int getNumeroNota() {
        return numeroNota;
    }

    public List<ItemNota> getListaItens() {
        return listaItens;
    }

    public LocalDate getDataHora() {
        return dataHora;
    }

    public void setValorTotal(double valorTotal) throws DadosIncorretosException {
        if (valorTotal < 0) {
            throw new DadosIncorretosException("Valor total não pode ser negativo");
        } else {
            this.valorTotal = valorTotal;
        }
    }
}