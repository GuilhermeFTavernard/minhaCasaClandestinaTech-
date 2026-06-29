package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.connection.ConnectionFactory;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Cliente;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

public class VendaDAO implements BaseDAO<Venda> {

    private final Connection con = ConnectionFactory.getInstance().getConnection();
    @Override
    public Venda inserir(Venda entity) {
        String sql = "INSERT INTO venda (data_venda, valor_total, id_cliente) VALUES (?, ?, ?)";

        try (
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(entity.getDataVenda()));
            ps.setBigDecimal(2, entity.getValorTotal());
            ps.setInt(3, entity.getCliente().getIdCliente());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setIdVenda(rs.getInt(1));
            }

            System.out.println("Venda inserida com sucesso! Id: " + entity.getIdVenda());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM venda WHERE id_venda = ?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void alterar(Venda entity) {
        String sql = "UPDATE venda SET valor_total = ? WHERE id_venda = ?";

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, entity.getValorTotal());
            ps.setInt(2, entity.getIdVenda());
            ps.executeUpdate();

            System.out.println("Venda alterada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Venda entity) {
        String sql = "DELETE FROM venda WHERE id_venda = ?";

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdVenda());
            ps.executeUpdate();

            System.out.println("Venda deletada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Venda> listar() {
        String sql = "SELECT v.id_venda, v.data_venda, v.valor_total, " +
                "c.id_cliente, c.nome, c.endereco, c.cpf " +
                "FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente";

        List<Venda> lista = new ArrayList<>();

        try (
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = montarCliente(rs);

                Venda venda = new Venda(rs.getInt("id_venda"), cliente);
                venda.setDataVenda(rs.getDate("data_venda").toLocalDate());
                venda.setValorTotal(rs.getBigDecimal("valor_total"));

                lista.add(venda);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Venda> buscarPorCliente(String cpf) {
        String sql = "SELECT v.id_venda, v.data_venda, v.valor_total, " +
                "c.id_cliente, c.nome, c.endereco, c.cpf " +
                "FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "WHERE c.cpf = ?";

        List<Venda> lista = new ArrayList<>();

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = montarCliente(rs);

                    Venda venda = new Venda(rs.getInt("id_venda"), cliente);
                    venda.setDataVenda(rs.getDate("data_venda").toLocalDate());
                    venda.setValorTotal(rs.getBigDecimal("valor_total"));

                    lista.add(venda);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Venda> buscarPorPeriodo(String dataInicio, String dataFim) {
        String sql = "SELECT v.id_venda, v.data_venda, v.valor_total, " +
                "c.id_cliente, c.nome, c.endereco, c.cpf " +
                "FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "WHERE v.data_venda BETWEEN ? AND ?";

        List<Venda> lista = new ArrayList<>();

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(dataInicio));
            ps.setDate(2, Date.valueOf(dataFim));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = montarCliente(rs);

                    Venda venda = new Venda(rs.getInt("id_venda"), cliente);
                    venda.setDataVenda(rs.getDate("data_venda").toLocalDate());
                    venda.setValorTotal(rs.getBigDecimal("valor_total"));

                    lista.add(venda);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    private Cliente montarCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente(
                rs.getString("nome"),
                rs.getString("endereco"),
                rs.getString("cpf")
        );
        cliente.setIdCliente(rs.getInt("id_cliente"));
        return cliente;
    }
}
