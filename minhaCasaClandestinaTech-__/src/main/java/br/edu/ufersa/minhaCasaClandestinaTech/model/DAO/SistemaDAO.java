package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.connection.ConnectionFactory;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Local;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Sistema;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

public class SistemaDAO implements BaseDAO<Sistema> {

    private final Connection con = ConnectionFactory.getInstance().getConnection();
    @Override
    public Sistema inserir(Sistema entity) {
        // Sistema é uma entidade agregadora; cada sub-entidade é persistida pelos seus próprios DAOs.
        // Este método persiste os equipamentos, responsáveis e locais do sistema.
        EquipamentoDAO equipamentoDAO = new EquipamentoDAO();
        ResponsavelDAO responsavelDAO = new ResponsavelDAO();
        LocalDAO localDAO = new LocalDAO();

        for (Equipamento e : entity.getEquipamentos()) {
            equipamentoDAO.inserir(e);
        }
        for (Responsavel r : entity.getResponsaveis()) {
            responsavelDAO.inserir(r);
        }
        for (Local l : entity.getLocais()) {
            localDAO.inserir(l);
        }

        System.out.println("Sistema persistido com sucesso!");
        return entity;
    }

    @Override
    public ResultSet buscar(String param) {
        // Busca equipamentos do sistema pelo nome
        String sql = "SELECT * FROM equipamento WHERE nome = ?";

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
    public void alterar(Sistema entity) {
        // Atualiza cada sub-entidade pelos seus próprios DAOs
        EquipamentoDAO equipamentoDAO = new EquipamentoDAO();
        ResponsavelDAO responsavelDAO = new ResponsavelDAO();
        LocalDAO localDAO = new LocalDAO();

        for (Equipamento e : entity.getEquipamentos()) {
            equipamentoDAO.alterar(e);
        }
        for (Responsavel r : entity.getResponsaveis()) {
            responsavelDAO.alterar(r);
        }
        for (Local l : entity.getLocais()) {
            localDAO.alterar(l);
        }

        System.out.println("Sistema alterado com sucesso!");
    }

    @Override
    public void deletar(Sistema entity) {
        // Remove cada sub-entidade pelos seus próprios DAOs
        EquipamentoDAO equipamentoDAO = new EquipamentoDAO();
        ResponsavelDAO responsavelDAO = new ResponsavelDAO();
        LocalDAO localDAO = new LocalDAO();

        for (Equipamento e : entity.getEquipamentos()) {
            equipamentoDAO.deletar(e);
        }
        for (Responsavel r : entity.getResponsaveis()) {
            responsavelDAO.deletar(r);
        }
        for (Local l : entity.getLocais()) {
            localDAO.deletar(l);
        }

        System.out.println("Sistema deletado com sucesso!");
    }

    @Override
    public List<Sistema> listar() {
        EquipamentoDAO equipamentoDAO = new EquipamentoDAO();
        ResponsavelDAO responsavelDAO = new ResponsavelDAO();
        LocalDAO localDAO = new LocalDAO();

        Sistema sistema = new Sistema();

        for (Equipamento e : equipamentoDAO.listar()) {
            sistema.adicionarEquipamento(e);
        }
        for (Responsavel r : responsavelDAO.listar()) {
            sistema.adicionarResponsavel(r);
        }
        for (Local l : localDAO.listar()) {
            sistema.adicionarLocal(l);
        }

        List<Sistema> lista = new ArrayList<>();
        lista.add(sistema);
        return lista;
    }
}

