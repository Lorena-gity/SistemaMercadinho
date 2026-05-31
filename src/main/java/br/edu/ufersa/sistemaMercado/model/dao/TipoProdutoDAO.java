package br.edu.ufersa.sistemaMercado.model.DAO;

import br.edu.ufersa.sistemaMercado.model.entities.TipoProduto;
import br.edu.ufersa.sistemaMercado.util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoProdutoDAO implements DAO<TipoProduto> {

    @Override
    public void inserir(TipoProduto tipo) {
        String sql = "INSERT INTO tipo_produto (nome) VALUES (?)";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tipo.getNome());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                tipo.setIdTipo(rs.getInt(1)); // guarda o id que o banco gerou
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir tipo de produto.", e);
        }
    }

    @Override
    public void atualizar(TipoProduto tipo) {
        String sql = "UPDATE tipo_produto SET nome = ? WHERE id_tipo = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo.getNome());
            ps.setInt(2, tipo.getIdTipo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar tipo de produto.", e);
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM tipo_produto WHERE id_tipo = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar tipo de produto.", e);
        }
    }

    @Override
    public TipoProduto buscarPorId(int id) {
        String sql = "SELECT * FROM tipo_produto WHERE id_tipo = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return montarTipo(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar tipo de produto.", e);
        }
        return null;
    }

    public TipoProduto buscarPorNome(String nome) {
        String sql = "SELECT * FROM tipo_produto WHERE nome = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return montarTipo(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar tipo de produto.", e);
        }
        return null;
    }

    @Override
    public List<TipoProduto> listarTodos() {
        List<TipoProduto> tipos = new ArrayList<>();
        String sql = "SELECT * FROM tipo_produto ORDER BY nome";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tipos.add(montarTipo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tipos de produto.", e);
        }
        return tipos;
    }

    // Monta um TipoProduto a partir da linha atual do ResultSet
    private TipoProduto montarTipo(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_tipo");
        String nome = rs.getString("nome");
        return new TipoProduto(id, nome);
    }
}
