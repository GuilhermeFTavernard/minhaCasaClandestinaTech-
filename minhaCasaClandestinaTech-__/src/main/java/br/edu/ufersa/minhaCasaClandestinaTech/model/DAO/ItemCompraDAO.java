package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.connection.ConnectionFactory;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemCompra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemCompraDAO implements BaseDAO<ItemCompra> {

    private final Connection con = ConnectionFactory.getInstance().getConnection();

    @Override
    public ItemCompra inserir(ItemCompra entity) {
        String sql = "INSERT INTO item_compra(quantidade, preco_unitario, categoria, id_compra, id_equipamento) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getQuantidade());
            ps.setFloat(2, entity.getPrecoUnitario());
            ps.setString(3, entity.getCategoria());
            ps.setInt(4, entity.getIdCompra());

            if (entity.getEquipamento() != null) {
                ps.setInt(5, entity.getEquipamento().getIdEquipamento());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM item_compra WHERE categoria = ?";

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
    public void alterar(ItemCompra entity) {
        String sql = "UPDATE item_compra SET quantidade = ?, preco_unitario = ?, categoria = ? WHERE id_item_compra = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getQuantidade());
            ps.setFloat(2, entity.getPrecoUnitario());
            ps.setString(3, entity.getCategoria());
            ps.setInt(4, entity.getIdItemCompra());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ItemCompra> listar() {
        String sql = "SELECT * FROM item_compra";
        List<ItemCompra> lista = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdItemCompra());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ItemCompra> listarPorCompra(int idCompra) {
        String sql = "SELECT ic.*, e.nome AS nome_equipamento FROM item_compra ic " +
                "LEFT JOIN equipamento e ON ic.id_equipamento = e.id_equipamento " +
                "WHERE ic.id_compra = ?";
        List<ItemCompra> lista = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCompra);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ItemCompra item = new ItemCompra(
                            rs.getInt("id_item_compra"),
                            rs.getInt("id_compra"),
                            rs.getInt("quantidade"),
                            rs.getFloat("preco_unitario"),
                            rs.getString("categoria")
                    );

                    int idEquip = rs.getInt("id_equipamento");
                    if (idEquip > 0) {
                        Equipamento equip = new Equipamento();
                        equip.setIdEquipamento(idEquip);
                        equip.setNome(rs.getString("nome_equipamento"));
                        item.setEquipamento(equip);
                    }

                    lista.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}