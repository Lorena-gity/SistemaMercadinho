package br.edu.ufersa.sistemaMercado.model.DAO;

import br.edu.ufersa.sistemaMercado.model.entities.FormaDeVenda;
import br.edu.ufersa.sistemaMercado.model.entities.Produto;
import br.edu.ufersa.sistemaMercado.model.entities.TipoProduto;
import br.edu.ufersa.sistemaMercado.util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements DAO<Produto> {

    private final TipoProdutoDAO tipoDAO = new TipoProdutoDAO();

    @Override
    public void inserir(Produto produto) {
        String sql = "INSERT INTO produto (codigo_barras, nome, quantidade_estoque, preco, forma_venda, id_tipo) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, produto.getCodigoBarras());
            ps.setString(2, produto.getNome());
            ps.setInt(3, produto.getQuantidadeEstoque());
            ps.setDouble(4, produto.getPreco());
            ps.setString(5, produto.getFormaDeVenda().name());
            ps.setInt(6, produto.getTipo().getIdTipo());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                produto.setIdProduto(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto.", e);
        }
    }

    @Override
    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET codigo_barras = ?, nome = ?, quantidade_estoque = ?, " +
                     "preco = ?, forma_venda = ?, id_tipo = ? WHERE id_produto = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, produto.getCodigoBarras());
            ps.setString(2, produto.getNome());
            ps.setInt(3, produto.getQuantidadeEstoque());
            ps.setDouble(4, produto.getPreco());
            ps.setString(5, produto.getFormaDeVenda().name());
            ps.setInt(6, produto.getTipo().getIdTipo());
            ps.setInt(7, produto.getIdProduto());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto.", e);
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM produto WHERE id_produto = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar produto.", e);
        }
    }

    @Override
    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produto WHERE id_produto = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return montarProduto(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto.", e);
        }
        return null;
    }

    public Produto buscarPorCodigoBarras(String codigoBarras) {
        String sql = "SELECT * FROM produto WHERE codigo_barras = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoBarras);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return montarProduto(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por código de barras.", e);
        }
        return null;
    }

    public List<Produto> buscarPorNome(String nome) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE nome LIKE ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                produtos.add(montarProduto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos por nome.", e);
        }
        return produtos;
    }

    @Override
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto ORDER BY nome";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                produtos.add(montarProduto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos.", e);
        }
        return produtos;
    }

    // Monta um Produto a partir da linha atual do ResultSet.
    // O tipo é uma FK, então busco ele pelo id_tipo da própria linha.
    private Produto montarProduto(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_produto");
        String codigo = rs.getString("codigo_barras");
        String nome = rs.getString("nome");
        int estoque = rs.getInt("quantidade_estoque");
        double preco = rs.getDouble("preco");
        FormaDeVenda forma = FormaDeVenda.valueOf(rs.getString("forma_venda"));
        TipoProduto tipo = tipoDAO.buscarPorId(rs.getInt("id_tipo"));

        return new Produto(id, codigo, nome, estoque, preco, tipo, forma);
    }
}
