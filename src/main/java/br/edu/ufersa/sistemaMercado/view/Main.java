package br.edu.ufersa.sistemaMercado.view;

import br.edu.ufersa.sistemaMercado.model.dao.ProdutoDAO;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final ObservableList<String> itens = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        Label titulo = new Label("Produtos cadastrados");
        ListView<String> lista = new ListView<>(itens);
        Button carregar = new Button("Carregar produtos");
        
        carregar.setOnAction(e -> carregarProdutos());

        VBox raiz = new VBox(10, titulo, lista, carregar);
        raiz.setPadding(new Insets(15));

        stage.setTitle("Sistema Mercadinho");
        stage.setScene(new Scene(raiz, 400, 300));
        stage.show();
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            itens.clear();
            for (Produto p : produtos) {
                itens.add(p.getNome() + " - R$ " + p.getPreco() + " (estoque: " + p.getQuantidadeEstoque() + ")");
            }
            if (produtos.isEmpty()) {
                itens.add("Nenhum produto cadastrado.");
            }
        } catch (RuntimeException erro) {
            new Alert(Alert.AlertType.ERROR, "Erro ao acessar o banco: " + erro.getMessage()).showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
