package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.ItemVendaDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemVenda;

import java.sql.ResultSet;
import java.util.List;

public class ItemVendaService {

    private ItemVendaDAO itemVendaDAO = new ItemVendaDAO();

    public void cadastrar(ItemVenda item) {
        if (item.getEquipamento() == null) {
            throw new RuntimeException("Equipamento obrigatório!");
        }
        if (item.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero!");
        }
        if (item.getPrecoUnitario() == null) {
            throw new RuntimeException("Preço unitário obrigatório!");
        }
        itemVendaDAO.inserir(item);
    }

    public void alterar(ItemVenda item) {
        if (item.getIdItemVenda() <= 0) {
            throw new RuntimeException("Id do item inválido!");
        }
        itemVendaDAO.alterar(item);
    }

    public void deletar(ItemVenda item) {
        if (item.getIdItemVenda() <= 0) {
            throw new RuntimeException("Id do item inválido!");
        }
        itemVendaDAO.deletar(item);
    }

    public List<ItemVenda> listar() {
        return itemVendaDAO.listar();
    }

    public ResultSet buscar(String idItemVenda) {
        if (idItemVenda == null || idItemVenda.isBlank()) {
            throw new RuntimeException("Id do item obrigatório para busca!");
        }
        return itemVendaDAO.buscar(idItemVenda);
    }
}
