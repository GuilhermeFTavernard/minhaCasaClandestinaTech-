package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.SistemaDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Local;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Sistema;

import java.sql.ResultSet;
import java.util.List;

public class SistemaService {

    private SistemaDAO sistemaDAO = new SistemaDAO();

    public void cadastrar(Sistema sistema) {
        if (sistema.getEquipamentos().isEmpty()
                && sistema.getResponsaveis().isEmpty()
                && sistema.getLocais().isEmpty()) {
            throw new RuntimeException("O sistema deve conter ao menos um equipamento, responsável ou local!");
        }
        sistemaDAO.inserir(sistema);
    }

    public void alterar(Sistema sistema) {
        sistemaDAO.alterar(sistema);
    }

    public void deletar(Sistema sistema) {
        sistemaDAO.deletar(sistema);
    }

    public List<Sistema> listar() {
        return sistemaDAO.listar();
    }

    public ResultSet buscar(String param) {
        if (param == null || param.isBlank()) {
            throw new RuntimeException("Parâmetro de busca obrigatório!");
        }
        return sistemaDAO.buscar(param);
    }

    public Sistema carregarSistema() {
        List<Sistema> lista = sistemaDAO.listar();
        return lista.isEmpty() ? new Sistema() : lista.get(0);
    }
}
