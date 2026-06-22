package br.edu.ufersa.sistemaMercado.model.DAO;

import java.util.List;

public interface DAO<T> {
    void inserir(T obj);
    void atualizar(T obj);
    void deletar(int id);
    T buscarPorId(int id);
    List<T> listarTodos();
}
