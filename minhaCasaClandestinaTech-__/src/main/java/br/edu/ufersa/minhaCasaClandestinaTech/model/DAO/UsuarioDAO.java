package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.exception.BancoDeDadosException;
import br.edu.ufersa.minhaCasaClandestinaTech.model.connection.ConnectionFactory;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

public class UsuarioDAO implements BaseDAO<Usuario>{

    private final Connection con =
            ConnectionFactory.getInstance().getConnection();

    public Usuario inserir(Usuario entity){

        String sql = "INSERT INTO usuario(email, senha) VALUES(?,?)"; //precisa terminar
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getSenha());
            ps.execute();
            System.out.println("Usuário inserido com sucesso!");
            ps.close();
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao inserir usuário.", e);
        }
        return entity;
    }

    public ResultSet buscar (String param){

        String sql = "SELECT * FROM usuario AS u WHERE u.email=?";
        ResultSet rs = null;
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar usuário.", e);
        }
        return rs;
    }

    @Override
    public void alterar(Usuario entity) {

        String sql = "UPDATE usuario SET email = ?, senha = ? WHERE id_usuario = ?";

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getSenha());
            ps.setInt(3, entity.getIdUsuario());
            ps.executeUpdate();

            System.out.println("Usuário alterado!");
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao alterar usuário.", e);
        }
    }

    @Override
    public List<Usuario> listar() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> lista = new ArrayList<>();

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("email"),
                        rs.getString("senha")
                );
                lista.add(u);
            }

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao listar usuário.", e);
        }

        return lista;
    }

    @Override
    public void deletar(Usuario entity) {


        String sql =
                "DELETE FROM usuario WHERE id_usuario = ?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, entity.getIdUsuario());

            ps.executeUpdate();

            ps.close();

            System.out.println("Usuário deletado!");
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao excluir usuário.", e);
        }
    }
}
