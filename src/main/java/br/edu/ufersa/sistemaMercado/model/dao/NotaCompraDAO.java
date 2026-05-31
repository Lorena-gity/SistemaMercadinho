package br.edu.ufersa.sistemaMercado.model.DAO;

import br.edu.ufersa.sistemaMercado.model.entities.ItemNota;
import br.edu.ufersa.sistemaMercado.model.entities.NotaCompra;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import br.edu.ufersa.sistemaMercado.util.ConexaoBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NotaCompraDAO implements DAO<NotaCompra> {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    // Grava a nota e todos os seus itens de uma vez só.
    // Se algo falhar no meio, faz rollback para não deixar nota sem itens no banco.
    @Override
    public void inserir(NotaCompra nota) {
        String sqlNota = "INSERT INTO nota_compra (data_hora, valor_total) VALUES (?, ?)";
        String sqlItem = "INSERT INTO item_nota (numero_nota, id_produto, quantidade, preco_unitario) " +
                         "VALUES (?, ?, ?, ?)";

        Connection con = null;
        try {
            con = ConexaoBD.getConnection();
            con.setAutoCommit(false);

            // salva o cabeçalho da nota e pega o número gerado
            int numeroNota;
            try (PreparedStatement ps = con.prepareStatement(sqlNota, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, Date.valueOf(nota.getDataHora()));
                ps.setDouble(2, nota.getValorTotal());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                numeroNota = rs.getInt(1);
                nota.setNumeroNota(numeroNota);
            }

            // salva cada item ligado a essa nota
            try (PreparedStatement ps = con.prepareStatement(sqlItem)) {
                for (ItemNota item : nota.getListaItens()) {
                    ps.setInt(1, numeroNota);
                    ps.setInt(2, item.getProduto().getIdProduto());
                    ps.setInt(3, item.getQuantidade());
                    ps.setDouble(4, item.getPrecoUnitario());
                    ps.executeUpdate();
                }
            }

            con.commit();
        } catch (SQLException e) {
            desfazer(con);
            throw new RuntimeException("Erro ao salvar a nota de compra.", e);
        } finally {
            fechar(con);
        }
    }

    @Override
    public void atualizar(NotaCompra nota) {
        String sql = "UPDATE nota_compra SET data_hora = ?, valor_total = ? WHERE numero_nota = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(nota.getDataHora()));
            ps.setDouble(2, nota.getValorTotal());
            ps.setInt(3, nota.getNumeroNota());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar nota de compra.", e);
        }
    }

    // Apaga primeiro os itens (por causa da chave estrangeira) e depois a nota
    @Override
    public void deletar(int numeroNota) {
        Connection con = null;
        try {
            con = ConexaoBD.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement("DELETE FROM item_nota WHERE numero_nota = ?")) {
                ps.setInt(1, numeroNota);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM nota_compra WHERE numero_nota = ?")) {
                ps.setInt(1, numeroNota);
                ps.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            desfazer(con);
            throw new RuntimeException("Erro ao deletar nota de compra.", e);
        } finally {
            fechar(con);
        }
    }

    @Override
    public NotaCompra buscarPorId(int numeroNota) {
        String sql = "SELECT * FROM nota_compra WHERE numero_nota = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroNota);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NotaCompra nota = new NotaCompra(
                        rs.getInt("numero_nota"),
                        rs.getDate("data_hora").toLocalDate(),
                        rs.getDouble("valor_total"));
                nota.getListaItens().addAll(buscarItens(numeroNota));
                return nota;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar nota de compra.", e);
        }
        return null;
    }

    @Override
    public List<NotaCompra> listarTodos() {
        List<NotaCompra> notas = new ArrayList<>();
        String sql = "SELECT * FROM nota_compra ORDER BY numero_nota";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NotaCompra nota = new NotaCompra(
                        rs.getInt("numero_nota"),
                        rs.getDate("data_hora").toLocalDate(),
                        rs.getDouble("valor_total"));
                nota.getListaItens().addAll(buscarItens(nota.getNumeroNota()));
                notas.add(nota);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar notas de compra.", e);
        }
        return notas;
    }

    // Carrega os itens de uma nota e remonta cada ItemNota com seu Produto
    private List<ItemNota> buscarItens(int numeroNota) {
        List<ItemNota> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_nota WHERE numero_nota = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroNota);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Produto produto = produtoDAO.buscarPorId(rs.getInt("id_produto"));
                int quantidade = rs.getInt("quantidade");
                double precoUnitario = rs.getDouble("preco_unitario");
                itens.add(new ItemNota(quantidade, precoUnitario, produto));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens da nota.", e);
        }
        return itens;
    }

    private void desfazer(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ignored) {
            }
        }
    }

    private void fechar(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
