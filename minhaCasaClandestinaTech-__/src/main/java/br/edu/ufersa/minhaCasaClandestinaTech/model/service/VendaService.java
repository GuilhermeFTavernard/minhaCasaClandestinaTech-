package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.ItemVendaDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.VendaDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemVenda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;

import java.sql.ResultSet;
import java.util.List;

public class VendaService {

    private VendaDAO vendaDAO = new VendaDAO();
    private ItemVendaDAO itemVendaDAO = new ItemVendaDAO();

    public void cadastrar(Venda venda) {
        if (venda.getCliente() == null) {
            throw new RuntimeException("Cliente obrigatório!");
        }
        if (venda.getDataVenda() == null) {
            throw new RuntimeException("Data da venda obrigatória!");
        }
        if (venda.getItensVenda().isEmpty()) {
            throw new RuntimeException("A venda deve ter pelo menos um item!");
        }
        vendaDAO.inserir(venda);

        // Insere os itens vinculados à venda
        for (ItemVenda item : venda.getItensVenda()) {
            item.setIdVenda(venda.getIdVenda());
            itemVendaDAO.inserir(item);
        }
    }

    public void alterar(Venda venda) {
        if (venda.getIdVenda() <= 0) {
            throw new RuntimeException("Id da venda inválido!");
        }
        vendaDAO.alterar(venda);
    }

    public void deletar(Venda venda) {
        if (venda.getIdVenda() <= 0) {
            throw new RuntimeException("Id da venda inválido!");
        }

        // Remove os itens primeiro (regra de chave estrangeira no banco)
        List<ItemVenda> itens = itemVendaDAO.buscarPorVenda(venda.getIdVenda());
        for (ItemVenda item : itens) {
            itemVendaDAO.deletar(item);
        }

        vendaDAO.deletar(venda);
    }

    public List<Venda> listar() {
        return vendaDAO.listar();
    }

    public ResultSet buscar(String idVenda) {
        if (idVenda == null || idVenda.isBlank()) {
            throw new RuntimeException("Id da venda obrigatório para busca!");
        }
        return vendaDAO.buscar(idVenda);
    }
}
