package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.EquipamentoDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;

import java.sql.ResultSet;
import java.util.List;

public class EquipamentoService {

    private EquipamentoDAO equipamentoDAO = new EquipamentoDAO();

    public void cadastrar(Equipamento equipamento) {
        if (equipamento.getNome() == null || equipamento.getNome().isBlank()) {
            throw new RuntimeException("Nome do equipamento obrigatório!");
        }
        if (equipamento.getNumeroSerie() <= 0) {
            throw new RuntimeException("Número de série do equipamento inválido!");
        }
        if (equipamento.getPreco() < 0) {
            throw new RuntimeException("Preço do equipamento inválido!");
        }
        if (equipamento.getQuantidadeEstoque() < 0) {
            throw new RuntimeException("Quantidade em estoque inválida!");
        }
        equipamentoDAO.inserir(equipamento);
    }

    public void alterar(Equipamento equipamento) {
        if (equipamento.getIdEquipamento() <= 0) {
            throw new RuntimeException("Id do equipamento inválido!");
        }
        equipamentoDAO.alterar(equipamento);
    }

    public void deletar(Equipamento equipamento) {
        if (equipamento.getIdEquipamento() <= 0) {
            throw new RuntimeException("Id do equipamento inválido!");
        }
        equipamentoDAO.deletar(equipamento);
    }

    public List<Equipamento> listar() {
        return equipamentoDAO.listar();
    }

    public ResultSet buscar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("Nome obrigatório para busca!");
        }
        return equipamentoDAO.buscar(nome);
    }
}

