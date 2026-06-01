package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoDAO implements BaseDAO<Equipamento> {

    @Override
    public Equipamento inserir(Equipamento entity) {
        String sql = "INSERT INTO equipamento(nome, numero_serie, preco, quantidade_estoque) VALUES (?, ?, ?, ?)";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entity.getNome());
            ps.setInt(2, entity.getNumeroSerie());
            ps.setFloat(3, entity.getPreco());
            ps.setInt(4, entity.getQuantidadeEstoque());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setIdEquipamento(rs.getInt(1));
            }

            System.out.println("Equipamento inserido com sucesso! Id: " + entity.getIdEquipamento());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM equipamento WHERE nome = ?";

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
    public void alterar(Equipamento entity) {
        String sql = "UPDATE equipamento SET nome = ?, numero_serie = ?, preco = ?, quantidade_estoque = ? WHERE id_equipamento = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entity.getNome());
            ps.setInt(2, entity.getNumeroSerie());
            ps.setFloat(3, entity.getPreco());
            ps.setInt(4, entity.getQuantidadeEstoque());
            ps.setInt(5, entity.getIdEquipamento());
            ps.executeUpdate();
            System.out.println("Equipamento alterado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Equipamento> listar() {
        String sql = "SELECT * FROM equipamento";
        List<Equipamento> lista = new ArrayList<>();

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Equipamento e = new Equipamento(
                        rs.getInt("id_equipamento"),
                        rs.getString("nome"),
                        rs.getInt("numero_serie"),
                        rs.getFloat("preco"),
                        rs.getInt("quantidade_estoque")
                );
                lista.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void deletar(Equipamento entity) {
        String sql = "DELETE FROM equipamento WHERE id_equipamento = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdEquipamento());
            ps.executeUpdate();
            System.out.println("Equipamento deletado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
