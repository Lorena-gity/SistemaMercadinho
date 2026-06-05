package br.edu.ufersa.sistemaMercado.model.entities;
import br.edu.ufersa.sistemaMercado.exceptions.DadosInvalidosException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
		this.numeroNota = numeroNota;
		this.dataHora = dataHora;
		this.valorTotal = valorTotal;
		this.listaItens = new ArrayList<>();
	}

    public int getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(int numeroNota) {
        this.numeroNota = numeroNota;
    }

    public List<ItemNota> getListaItens() {
        return listaItens;
    }

    public LocalDate getDataHora() {
        return dataHora;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) throws DadosInvalidosException {
        if (valorTotal < 0) {
            throw new DadosInvalidosException("Valor total não pode ser negativo");
        } else {
            this.valorTotal = valorTotal;
        }
    }
}