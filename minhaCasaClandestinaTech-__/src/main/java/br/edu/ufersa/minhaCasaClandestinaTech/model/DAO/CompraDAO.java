package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.exception.BancoDeDadosException;
import br.edu.ufersa.minhaCasaClandestinaTech.model.connection.ConnectionFactory;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Compra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemCompra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO implements BaseDAO<Compra> {

    private final Connection con = ConnectionFactory.getInstance().getConnection();
    private final ItemCompraDAO itemCompraDAO = new ItemCompraDAO();

    @Override
    public Compra inserir(Compra entity) {
        String sql = "INSERT INTO compra(data_compra, fornecedor) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(entity.getDataCompra()));
            ps.setString(2, entity.getFornecedor());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setIdCompra(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao inserir compra.", e);
        }
        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM compra WHERE fornecedor = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar compra.", e);
        }
    }

    @Override
    public void alterar(Compra entity) {
        String sql = "UPDATE compra SET data_compra = ?, fornecedor = ? WHERE id_compra = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(entity.getDataCompra()));
            ps.setString(2, entity.getFornecedor());
            ps.setInt(3, entity.getIdCompra());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao alterar compra.", e);
        }
    }

    @Override
    public List<Compra> listar() {
        String sql = "SELECT * FROM compra";
        List<Compra> lista = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Compra compra = new Compra(
                        rs.getInt("id_compra"),
                        rs.getDate("data_compra").toLocalDate(),
                        rs.getString("fornecedor")
                );

                // ← carrega os itens da compra
                List<ItemCompra> itens = itemCompraDAO.listarPorCompra(compra.getIdCompra());
                compra.setListaDeItens(itens);

                lista.add(compra);
            }

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao listar compras.", e);
        }

        return lista;
    }

    @Override
    public void deletar(Compra entity) {
        String sql = "DELETE FROM compra WHERE id_compra = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdCompra());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao excluir compra.", e);
        }
    }
}