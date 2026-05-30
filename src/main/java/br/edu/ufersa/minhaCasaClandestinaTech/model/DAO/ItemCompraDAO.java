package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemCompra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemCompraDAO implements BaseDAO<ItemCompra> {

    @Override
    public ItemCompra inserir(ItemCompra entity) {
        String sql = "INSERT INTO item_compra(quantidade, preco_unitario, categoria, id_compra) VALUES (?, ?, ?, ?)";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getQuantidade());
            ps.setFloat(2, entity.getPrecoUnitario());
            ps.setString(3, entity.getCategoria());
            ps.setInt(4, entity.getIdCompra());
            ps.execute();
            System.out.println("Item inserido com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM item_compra WHERE categoria = ?";

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
    public void alterar(ItemCompra entity) {
        String sql = "UPDATE item_compra SET quantidade = ?, preco_unitario = ?, categoria = ? WHERE id_item_compra = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getQuantidade());
            ps.setFloat(2, entity.getPrecoUnitario());
            ps.setString(3, entity.getCategoria());
            ps.setInt(4, entity.getIdItemCompra());
            ps.executeUpdate();
            System.out.println("Item alterado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ItemCompra> listar() {
        String sql = "SELECT * FROM item_compra";
        List<ItemCompra> lista = new ArrayList<>();

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemCompra item = new ItemCompra(
                        rs.getInt("id_item_compra"),
                        rs.getInt("id_compra"),
                        rs.getInt("quantidade"),
                        rs.getFloat("preco_unitario"),
                        rs.getString("categoria")
                );
                lista.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void deletar(ItemCompra entity) {
        String sql = "DELETE FROM item_compra WHERE id_item_compra = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdItemCompra());
            ps.executeUpdate();
            System.out.println("Item deletado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet listarPorCompra(int idCompra) {
        String sql = "SELECT * FROM item_compra WHERE id_compra = ?";

        try {
            Connection con = BaseDAO.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idCompra);
            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
