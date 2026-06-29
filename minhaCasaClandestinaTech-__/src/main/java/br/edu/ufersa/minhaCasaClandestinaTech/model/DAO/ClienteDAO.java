package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.exception.BancoDeDadosException;
import br.edu.ufersa.minhaCasaClandestinaTech.model.connection.ConnectionFactory;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

public class ClienteDAO implements BaseDAO<Cliente> {

    private final Connection con =
            ConnectionFactory.getInstance().getConnection();

    @Override
    public Cliente inserir(Cliente entity) {
        String sql = "INSERT INTO cliente(nome, endereco, cpf) VALUES (?, ?, ?)";

        try (
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getCpf());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setIdCliente(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao inserir cliente.", e);
    }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM cliente WHERE nome = ?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar cliente.", e);
        }
    }

    public Cliente buscarCliente(String nome) {
        String sql = "SELECT * FROM cliente WHERE nome = ?";

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getString("nome"),
                            rs.getString("endereco"),
                            rs.getString("cpf")
                    );
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    return cliente;
                }
            }

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar cliente.", e);
        }

        return null;
    }

    @Override
    public void alterar(Cliente entity) {
        String sql = "UPDATE cliente SET nome = ?, endereco = ?, cpf = ? WHERE id_cliente = ?";

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getCpf());
            ps.setInt(4, entity.getIdCliente());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao alterar cliente.", e);
        }
    }

    @Override
    public List<Cliente> listar() {
        String sql = "SELECT * FROM cliente";
        List<Cliente> lista = new ArrayList<>();

        try (
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("cpf")
                );
                cliente.setIdCliente(rs.getInt("id_cliente"));
                lista.add(cliente);
            }

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao listar clientes.", e);
        }

        return lista;
    }

    @Override
    public void deletar(Cliente entity) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdCliente());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao excluir cliente.", e);
        }
    }
}
