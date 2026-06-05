package br.edu.ufersa.sistemaMercado.exceptions;

public class OperacaoInvalidaException extends RuntimeException {
    public OperacaoInvalidaException(String message) {
        super(message);
    }
}
