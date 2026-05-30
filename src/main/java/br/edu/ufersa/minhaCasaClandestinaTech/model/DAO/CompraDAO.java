package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Compra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO implements BaseDAO<Compra> {

    @Override
    public Compra inserir(Compra entity) {
        String sql = "INSERT INTO compra(data_compra, fornecedor) VALUES (?, ?)";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(entity.getDataCompra()));
            ps.setString(2, entity.getFornecedor());
            ps.execute();

            //Pega o id gerado pelo banco e coloca na entidade
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setIdCompra(rs.getInt(1));
            }

            System.out.println("Compra inserida com sucesso! Id: " + entity.getIdCompra());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM compra WHERE fornecedor = ?";

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
    public void alterar(Compra entity) {
        String sql = "UPDATE compra SET data_compra = ?, fornecedor = ? WHERE id_compra = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(entity.getDataCompra()));
            ps.setString(2, entity.getFornecedor());
            ps.setInt(3, entity.getIdCompra());
            ps.executeUpdate();
            System.out.println("Compra alterada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Compra> listar() {
        String sql = "SELECT * FROM compra";
        List<Compra> lista = new ArrayList<>();

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Compra c = new Compra(
                        rs.getInt("id_compra"),
                        rs.getDate("data_compra").toLocalDate(),
                        rs.getString("fornecedor")
                );
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    @Override
    public void deletar(Compra entity) {
        String sql = "DELETE FROM compra WHERE id_compra = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdCompra());
            ps.executeUpdate();
            System.out.println("Compra deletada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}







