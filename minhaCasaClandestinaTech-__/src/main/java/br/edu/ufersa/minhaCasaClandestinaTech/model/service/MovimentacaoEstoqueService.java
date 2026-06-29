package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.MovimentacaoEstoqueDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.MovimentacaoEstoque;

import java.sql.ResultSet;
import java.util.List;

public class MovimentacaoEstoqueService {

    private MovimentacaoEstoqueDAO movimentacaoEstoqueDAO = new MovimentacaoEstoqueDAO();

    public void cadastrar(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getEquipamento() == null) {
            throw new RuntimeException("Equipamento obrigatório para movimentação de estoque!");
        }
        if (movimentacao.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade da movimentação deve ser maior que zero!");
        }
        if (movimentacao.getTipo() == null) {
            throw new RuntimeException("Tipo de movimentação obrigatório!");
        }
        if (movimentacao.getData() == null) {
            throw new RuntimeException("Data da movimentação obrigatória!");
        }
        movimentacaoEstoqueDAO.inserir(movimentacao);
    }

    public void alterar(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getIdMovimentacao() <= 0) {
            throw new RuntimeException("Id da movimentação inválido!");
        }
        movimentacaoEstoqueDAO.alterar(movimentacao);
    }

    public void deletar(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getIdMovimentacao() <= 0) {
            throw new RuntimeException("Id da movimentação inválido!");
        }
        movimentacaoEstoqueDAO.deletar(movimentacao);
    }

    public List<MovimentacaoEstoque> listar() {
        return movimentacaoEstoqueDAO.listar();
    }

    public ResultSet buscar(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new RuntimeException("Tipo de movimentação obrigatório para busca!");
        }
        return movimentacaoEstoqueDAO.buscar(tipo);
    }

    public List<MovimentacaoEstoque> buscarPorEquipamento(int idEquipamento) {
        if (idEquipamento <= 0) {
            throw new RuntimeException("Id do equipamento inválido!");
        }
        return movimentacaoEstoqueDAO.buscarPorEquipamento(idEquipamento);
    }
}
