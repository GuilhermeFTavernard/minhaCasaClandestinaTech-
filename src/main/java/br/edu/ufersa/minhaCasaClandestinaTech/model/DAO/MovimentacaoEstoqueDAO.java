package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.MovimentacaoEstoque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoEstoqueDAO implements BaseDAO<MovimentacaoEstoque> {

    @Override
    public MovimentacaoEstoque inserir(MovimentacaoEstoque entity) {
        String sql = "INSERT INTO movimentacao_estoque(data, quantidade, tipo, id_equipamento) VALUES (?, ?, ?, ?)";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(entity.getData()));
            ps.setInt(2, entity.getQuantidade());
            ps.setString(3, entity.getTipo().name());
            ps.setInt(4, entity.getEquipamento().getIdEquipamento());
            ps.execute();
            System.out.println("Movimentação de estoque inserida com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM movimentacao_estoque WHERE tipo = ?";

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
    public void alterar(MovimentacaoEstoque entity) {
        String sql = "UPDATE movimentacao_estoque SET data = ?, quantidade = ?, tipo = ?, id_equipamento = ? WHERE id_movimentacao = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(entity.getData()));
            ps.setInt(2, entity.getQuantidade());
            ps.setString(3, entity.getTipo().name());
            ps.setInt(4, entity.getEquipamento().getIdEquipamento());
            ps.setInt(5, entity.getIdMovimentacao());
            ps.executeUpdate();
            System.out.println("Movimentação de estoque alterada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MovimentacaoEstoque> listar() {
        String sql = "SELECT m.id_movimentacao, m.data, m.quantidade, m.tipo, " +
                "e.id_equipamento, e.nome, e.numero_serie, e.preco, e.quantidade_estoque " +
                "FROM movimentacao_estoque m " +
                "JOIN equipamento e ON m.id_equipamento = e.id_equipamento";

        List<MovimentacaoEstoque> lista = new ArrayList<>();

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

                MovimentacaoEstoque m = new MovimentacaoEstoque(equipamento, rs.getInt("quantidade"));
                m.setIdMovimentacao(rs.getInt("id_movimentacao"));
                m.setData(rs.getDate("data").toLocalDate());
                m.setTipo(MovimentacaoEstoque.TipoMovimentacao.valueOf(rs.getString("tipo")));
                lista.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void deletar(MovimentacaoEstoque entity) {
        String sql = "DELETE FROM movimentacao_estoque WHERE id_movimentacao = ?";

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getIdMovimentacao());
            ps.executeUpdate();
            System.out.println("Movimentação de estoque deletada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MovimentacaoEstoque> buscarPorEquipamento(int idEquipamento) {
        String sql = "SELECT m.id_movimentacao, m.data, m.quantidade, m.tipo, " +
                "e.id_equipamento, e.nome, e.numero_serie, e.preco, e.quantidade_estoque " +
                "FROM movimentacao_estoque m " +
                "JOIN equipamento e ON m.id_equipamento = e.id_equipamento " +
                "WHERE m.id_equipamento = ?";

        List<MovimentacaoEstoque> lista = new ArrayList<>();

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEquipamento);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Equipamento equipamento = new Equipamento(
                        rs.getInt("id_equipamento"),
                        rs.getString("nome"),
                        rs.getInt("numero_serie"),
                        rs.getFloat("preco"),
                        rs.getInt("quantidade_estoque")
                );

                MovimentacaoEstoque m = new MovimentacaoEstoque(equipamento, rs.getInt("quantidade"));
                m.setIdMovimentacao(rs.getInt("id_movimentacao"));
                m.setData(rs.getDate("data").toLocalDate());
                m.setTipo(MovimentacaoEstoque.TipoMovimentacao.valueOf(rs.getString("tipo")));
                lista.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}

