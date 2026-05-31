package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemVenda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaDAO implements BaseDAO<ItemVenda> {

    @Override
    public ItemVenda inserir(ItemVenda entity) {
        String sql = "INSERT INTO item_venda (quantidade, preco_unitario, id_venda, id_equipamento) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, entity.getQuantidade());
            ps.setBigDecimal(2, entity.getPrecoUnitario());
            ps.setInt(3, entity.getIdVenda());
            ps.setInt(4, entity.getEquipamento().getIdEquipamento());
            ps.execute();

            // Pega o id gerado pelo banco e coloca na entidade
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setIdItemVenda(rs.getInt(1));
            }

            System.out.println("Item de venda inserido com sucesso! Id: " + entity.getIdItemVenda());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM item_venda WHERE id_item_venda = ?";

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
    public void alterar(ItemVenda entity) {
        String sql = "UPDATE item_venda SET quantidade = ?, preco_unitario = ? " +
                     "WHERE id_item_venda = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getQuantidade());
            ps.setBigDecimal(2, entity.getPrecoUnitario());
            ps.setInt(3, entity.getIdItemVenda());
            ps.executeUpdate();
            System.out.println("Item de venda alterado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(ItemVenda entity) {
        String sql = "DELETE FROM item_venda WHERE id_item_venda = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdItemVenda());
            ps.executeUpdate();
            System.out.println("Item de venda deletado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ItemVenda> listar() {
        String sql = "SELECT iv.id_item_venda, iv.quantidade, iv.preco_unitario, iv.id_venda, " +
                     "e.id_equipamento, e.nome, e.numero_serie, e.preco, e.quantidade_estoque " +
                     "FROM item_venda iv " +
                     "JOIN equipamento e ON iv.id_equipamento = e.id_equipamento";

        List<ItemVenda> lista = new ArrayList<>();

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Equipamento equipamento = new Equipamento(
                        rs.getInt("id_equipamento"),
                        rs.getString("nome"),
                        rs.getInt("numero_serie"),
                        rs.getFloat("preco"),
                        rs.getInt("quantidade_estoque")
                );

                ItemVenda item = new ItemVenda(
                        rs.getInt("quantidade"),
                        rs.getBigDecimal("preco_unitario"),
                        equipamento
                );
                item.setIdItemVenda(rs.getInt("id_item_venda"));
                item.setIdVenda(rs.getInt("id_venda"));

                lista.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Busca todos os itens pertencentes a uma venda específica
    public List<ItemVenda> buscarPorVenda(int idVenda) {
        String sql = "SELECT iv.id_item_venda, iv.quantidade, iv.preco_unitario, iv.id_venda, " +
                     "e.id_equipamento, e.nome, e.numero_serie, e.preco, e.quantidade_estoque " +
                     "FROM item_venda iv " +
                     "JOIN equipamento e ON iv.id_equipamento = e.id_equipamento " +
                     "WHERE iv.id_venda = ?";

        List<ItemVenda> lista = new ArrayList<>();

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVenda);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Equipamento equipamento = new Equipamento(
                        rs.getInt("id_equipamento"),
                        rs.getString("nome"),
                        rs.getInt("numero_serie"),
                        rs.getFloat("preco"),
                        rs.getInt("quantidade_estoque")
                );

                ItemVenda item = new ItemVenda(
                        rs.getInt("quantidade"),
                        rs.getBigDecimal("preco_unitario"),
                        equipamento
                );
                item.setIdItemVenda(rs.getInt("id_item_venda"));
                item.setIdVenda(rs.getInt("id_venda"));

                lista.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
