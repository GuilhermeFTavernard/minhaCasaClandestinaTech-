package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.exception.BancoDeDadosException;
import br.edu.ufersa.minhaCasaClandestinaTech.model.connection.ConnectionFactory;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Local;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDAO implements BaseDAO<Local> {

    private final Connection con = ConnectionFactory.getInstance().getConnection();

    @Override
    public Local inserir(Local entity) {
        String sql = "INSERT INTO local(nome_casa, nome_compartimento, id_responsavel) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getNomeCasa());
            ps.setString(2, entity.getNomeCompartimento());
            if (entity.getResponsavel() != null) {
                ps.setInt(3, entity.getResponsavel().getIdResponsavel());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.execute();
            System.out.println("Local inserido com sucesso!");
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao inserir local.", e);
        }

        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM local WHERE nome_casa = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar local.", e);
        }
    }

    @Override
    public void alterar(Local entity) {
        String sql = "UPDATE local SET nome_casa = ?, nome_compartimento = ?, id_responsavel = ? WHERE id_local = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getNomeCasa());
            ps.setString(2, entity.getNomeCompartimento());
            if (entity.getResponsavel() != null) {
                ps.setInt(3, entity.getResponsavel().getIdResponsavel());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setInt(4, entity.getIdLocal());
            ps.executeUpdate();
            System.out.println("Local alterado com sucesso!");
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao alterar local.", e);
        }
    }

    @Override
    public List<Local> listar() {
        String sql = """
                SELECT l.id_local, l.nome_casa, l.nome_compartimento,
                       r.id_responsavel, r.nome_resp, r.endereco, r.telefone
                FROM local l
                LEFT JOIN responsavel r ON l.id_responsavel = r.id_responsavel
                """;

        List<Local> lista = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Local l = new Local(
                        rs.getString("nome_casa"),
                        rs.getString("nome_compartimento")
                );
                l.setIdLocal(rs.getInt("id_local"));

                // Mapeia o responsável (pode ser null se não houver)
                int idResp = rs.getInt("id_responsavel");
                if (!rs.wasNull()) {
                    Responsavel r = new Responsavel(
                            rs.getString("nome_resp"),
                            rs.getString("endereco"),
                            rs.getString("telefone")
                    );
                    r.setIdResponsavel(idResp);
                    l.setResponsavel(r);
                }

                lista.add(l);
            }

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao listar local.", e);
        }

        return lista;
    }

    @Override
    public void deletar(Local entity) {
        String sql = "DELETE FROM local WHERE id_local = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdLocal());
            ps.executeUpdate();
            System.out.println("Local deletado com sucesso!");
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao excluir local.", e);
        }
    }
}