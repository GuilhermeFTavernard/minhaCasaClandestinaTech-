package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Local;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDAO implements BaseDAO<Local> {

    @Override
    public Local inserir(Local entity) {
        String sql = "INSERT INTO local(nome_casa, nome_compartimento) VALUES (?, ?)";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entity.getNomeCasa());
            ps.setString(2, entity.getNomeCompartimento());
            ps.execute();
            System.out.println("Local inserido com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM local WHERE nome_casa = ?";

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
    public void alterar(Local entity) {
        String sql = "UPDATE local SET nome_casa = ?, nome_compartimento = ? WHERE id_local = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entity.getNomeCasa());
            ps.setString(2, entity.getNomeCompartimento());
            ps.setInt(3, entity.getIdLocal());
            ps.executeUpdate();
            System.out.println("Local alterado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Local> listar() {
        String sql = "SELECT * FROM local";
        List<Local> lista = new ArrayList<>();

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Local l = new Local(
                        rs.getString("nome_casa"),
                        rs.getString("nome_compartimento")
                );
                l.setIdLocal(rs.getInt("id_local"));
                lista.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void deletar(Local entity) {
        String sql = "DELETE FROM local WHERE id_local = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdLocal());
            ps.executeUpdate();
            System.out.println("Local deletado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
