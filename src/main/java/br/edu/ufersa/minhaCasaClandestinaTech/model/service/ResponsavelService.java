package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.ResponsavelDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;

import java.util.List;

public class ResponsavelService {

    private ResponsavelDAO responsavelDAO = new ResponsavelDAO();

    public void cadastrar(Responsavel responsavel) {
        if (responsavel.getNome() == null || responsavel.getNome().isBlank()) {
            throw new RuntimeException("Nome obrigatório!");
        }
        if (responsavel.getTelefone() == null || responsavel.getTelefone().isBlank()) {
            throw new RuntimeException("Telefone obrigatório!");
        }
        if (responsavel.getEndereco() == null || responsavel.getEndereco().isBlank()) {
            throw new RuntimeException("Endereço obrigatório!");
        }
        responsavelDAO.inserir(responsavel);
    }

    public void alterar(Responsavel responsavel) {
        if (responsavel.getIdResponsavel() <= 0) {
            throw new RuntimeException("Id do responsável inválido!");
        }
        responsavelDAO.alterar(responsavel);
    }

    public void deletar(Responsavel responsavel) {
        if (responsavel.getIdResponsavel() <= 0) {
            throw new RuntimeException("Id do responsável inválido!");
        }
        responsavelDAO.deletar(responsavel);
    }

    public List<Responsavel> listar() {
        return responsavelDAO.listar();
    }

    public java.sql.ResultSet buscar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("Nome obrigatório para busca!");
        }
        return responsavelDAO.buscar(nome);
    }
}
