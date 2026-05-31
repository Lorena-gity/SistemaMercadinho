package br.edu.ufersa.sistemaMercado.model.dao;

import br.edu.ufersa.sistemaMercado.model.entities.Caixa;
import br.edu.ufersa.sistemaMercado.model.entities.Gerente;
import br.edu.ufersa.sistemaMercado.model.entities.PerfilUsuario;
import br.edu.ufersa.sistemaMercado.model.entities.Usuario;
import br.edu.ufersa.sistemaMercado.util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements DAO<Usuario> {

    @Override
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, senha, perfil) VALUES (?, ?, ?)";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getSenha());
            ps.setString(3, usuario.getPerfil().name());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuario.setIdUsuario(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário.", e);
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, senha = ?, perfil = ? WHERE id_usuario = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getSenha());
            ps.setString(3, usuario.getPerfil().name());
            ps.setInt(4, usuario.getIdUsuario());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário.", e);
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário.", e);
        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return montarUsuario(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário.", e);
        }
        return null;
    }

    // Usado no login: procura o usuário pelo nome
    public Usuario buscarPorNome(String nome) {
        String sql = "SELECT * FROM usuario WHERE nome = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return montarUsuario(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por nome.", e);
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY nome";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(montarUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários.", e);
        }
        return usuarios;
    }

    // O perfil guardado no banco diz se o usuário é Gerente ou Caixa
    private Usuario montarUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_usuario");
        String nome = rs.getString("nome");
        String senha = rs.getString("senha");
        PerfilUsuario perfil = PerfilUsuario.valueOf(rs.getString("perfil"));

        if (perfil == PerfilUsuario.GERENTE) {
            return new Gerente(id, nome, senha);
        } else {
            return new Caixa(id, nome, senha);
        }
    }
}
