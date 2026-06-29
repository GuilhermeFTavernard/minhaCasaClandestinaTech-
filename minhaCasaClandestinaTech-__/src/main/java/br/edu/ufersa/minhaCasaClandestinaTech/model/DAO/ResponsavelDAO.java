package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.connection.ConnectionFactory;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

public class ResponsavelDAO implements BaseDAO<Responsavel> {

    private final Connection con = ConnectionFactory.getInstance().getConnection();
    @Override
    public Responsavel inserir(Responsavel entity) {
        String sql = "INSERT INTO responsavel(nome_resp, endereco, telefone) VALUES (?, ?, ?)";

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getTelefone());
            ps.execute();
            System.out.println("Responsável inserido com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM responsavel WHERE nome_resp = ?";

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
    public void alterar(Responsavel entity) {
        String sql = "UPDATE responsavel SET nome_resp = ?, endereco = ?, telefone = ? WHERE id_responsavel = ?";

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getTelefone());
            ps.setInt(4, entity.getIdResponsavel());
            ps.executeUpdate();
            System.out.println("Responsável alterado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Responsavel> listar() {
        String sql = "SELECT * FROM responsavel";
        List<Responsavel> lista = new ArrayList<>();

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Responsavel r = new Responsavel(
                        rs.getString("nome_resp"),
                        rs.getString("endereco"),
                        rs.getString("telefone")
                );
                r.setIdResponsavel(rs.getInt("id_responsavel"));
                lista.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void deletar(Responsavel entity) {
        String sql = "DELETE FROM responsavel WHERE id_responsavel = ?";

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdResponsavel());
            ps.executeUpdate();
            System.out.println("Responsável deletado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}