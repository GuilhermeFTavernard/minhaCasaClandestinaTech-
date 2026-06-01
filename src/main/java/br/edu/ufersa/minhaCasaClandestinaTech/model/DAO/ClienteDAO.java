package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements BaseDAO<Cliente> {

    @Override
    public Cliente inserir(Cliente entity) {
        String sql = "INSERT INTO cliente(nome, endereco, cpf) VALUES (?, ?, ?)";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setInt(3, entity.getCpf());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setIdCliente(rs.getInt(1));
            }

            System.out.println("Cliente inserido com sucesso! Id: " + entity.getIdCliente());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM cliente WHERE nome = ?";

        try {
            Connection con = BaseDAO.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void alterar(Cliente entity) {
        String sql = "UPDATE cliente SET nome = ?, endereco = ?, cpf = ? WHERE id_cliente = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setInt(3, entity.getCpf());
            ps.setInt(4, entity.getIdCliente());
            ps.executeUpdate();
            System.out.println("Cliente alterado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cliente> listar() {
        String sql = "SELECT * FROM cliente";
        List<Cliente> lista = new ArrayList<>();

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getInt("cpf")
                );
                c.setIdCliente(rs.getInt("id_cliente"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void deletar(Cliente entity) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdCliente());
            ps.executeUpdate();
            System.out.println("Cliente deletado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
