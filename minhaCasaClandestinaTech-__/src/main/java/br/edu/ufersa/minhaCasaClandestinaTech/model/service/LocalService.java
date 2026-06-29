package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.LocalDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Local;

import java.util.List;

public class LocalService {

    private LocalDAO localDAO = new LocalDAO();

    public void cadastrar(Local local) {
        if (local.getNomeCasa() == null || local.getNomeCasa().isBlank()) {
            throw new RuntimeException("Nome da casa obrigatório!");
        }
        if (local.getNomeCompartimento() == null || local.getNomeCompartimento().isBlank()) {
            throw new RuntimeException("Nome do compartimento obrigatório!");
        }
        localDAO.inserir(local);
    }

    public void alterar(Local local) {
        if (local.getIdLocal() <= 0) {
            throw new RuntimeException("Id do local inválido!");
        }
        localDAO.alterar(local);
    }

    public void deletar(Local local) {
        if (local.getIdLocal() <= 0) {
            throw new RuntimeException("Id do local inválido!");
        }
        localDAO.deletar(local);
    }

    public List<Local> listar() {
        return localDAO.listar();
    }

    public java.sql.ResultSet buscar(String nomeCasa) {
        if (nomeCasa == null || nomeCasa.isBlank()) {
            throw new RuntimeException("Nome da casa obrigatório para busca!");
        }
        return localDAO.buscar(nomeCasa);
    }
}
