package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.connection.ConnectionFactory;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Local;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoDAO implements BaseDAO<Equipamento> {

    private final Connection con = ConnectionFactory.getInstance().getConnection();

    @Override
    public Equipamento inserir(Equipamento entity) {
        String sql = "INSERT INTO equipamento(nome, numero_serie, preco, quantidade_estoque, id_local, id_responsavel) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNome());
            ps.setInt(2, entity.getNumeroSerie());
            ps.setFloat(3, entity.getPreco());
            ps.setInt(4, entity.getQuantidadeEstoque());
            if (entity.getLocal() != null)
                ps.setInt(5, entity.getLocal().getIdLocal());
            else
                ps.setNull(5, Types.INTEGER);
            if (entity.getResponsavel() != null)
                ps.setInt(6, entity.getResponsavel().getIdResponsavel());
            else
                ps.setNull(6, Types.INTEGER);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) entity.setIdEquipamento(rs.getInt(1));

        } catch (SQLException e) { e.printStackTrace(); }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM equipamento WHERE nome = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            return ps.executeQuery();
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void alterar(Equipamento entity) {
        String sql = "UPDATE equipamento SET nome = ?, numero_serie = ?, preco = ?, quantidade_estoque = ?, id_local = ?, id_responsavel = ? WHERE id_equipamento = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getNome());
            ps.setInt(2, entity.getNumeroSerie());
            ps.setFloat(3, entity.getPreco());
            ps.setInt(4, entity.getQuantidadeEstoque());
            if (entity.getLocal() != null)
                ps.setInt(5, entity.getLocal().getIdLocal());
            else
                ps.setNull(5, Types.INTEGER);
            if (entity.getResponsavel() != null)
                ps.setInt(6, entity.getResponsavel().getIdResponsavel());
            else
                ps.setNull(6, Types.INTEGER);
            ps.setInt(7, entity.getIdEquipamento());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Equipamento> listar() {
        String sql = """
                SELECT e.id_equipamento, e.nome, e.numero_serie, e.preco, e.quantidade_estoque,
                       l.id_local, l.nome_casa, l.nome_compartimento,
                       r.id_responsavel, r.nome_resp, r.endereco, r.telefone
                FROM equipamento e
                LEFT JOIN local l ON e.id_local = l.id_local
                LEFT JOIN responsavel r ON e.id_responsavel = r.id_responsavel
                """;

        List<Equipamento> lista = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Equipamento e = new Equipamento(
                        rs.getInt("id_equipamento"),
                        rs.getString("nome"),
                        rs.getInt("numero_serie"),
                        rs.getFloat("preco"),
                        rs.getInt("quantidade_estoque")
                );

                int idLocal = rs.getInt("id_local");
                if (!rs.wasNull()) {
                    Local l = new Local(rs.getString("nome_casa"), rs.getString("nome_compartimento"));
                    l.setIdLocal(idLocal);
                    e.setLocal(l);
                }

                int idResp = rs.getInt("id_responsavel");
                if (!rs.wasNull()) {
                    Responsavel r = new Responsavel(rs.getString("nome_resp"), rs.getString("endereco"), rs.getString("telefone"));
                    r.setIdResponsavel(idResp);
                    e.setResponsavel(r);
                }

                lista.add(e);
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return lista;
    }

    @Override
    public void deletar(Equipamento entity) {
        String sql = "DELETE FROM equipamento WHERE id_equipamento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdEquipamento());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}