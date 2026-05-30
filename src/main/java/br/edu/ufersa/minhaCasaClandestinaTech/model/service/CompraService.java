package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.CompraDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.ItemCompraDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Compra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemCompra;

import java.util.List;

public class CompraService {

    private CompraDAO compraDAO = new CompraDAO();
    private ItemCompraDAO itemCompraDAO = new ItemCompraDAO();

    public void cadastrar(Compra compra) {
        if (compra.getFornecedor() == null || compra.getFornecedor().isBlank()) {
            throw new RuntimeException("Fornecedor obrigatório!");
        }
        if (compra.getDataCompra() == null) {
            throw new RuntimeException("Data da compra obrigatória!");
        }
        if (compra.getListaDeItens().isEmpty()) {
            throw new RuntimeException("A compra deve ter pelo menos um item!");
        }
        compraDAO.inserir(compra);

        // Insere os itens vinculados à compra
        for (ItemCompra item : compra.getListaDeItens()) {
            item.setIdCompra(compra.getIdCompra());
            itemCompraDAO.inserir(item);
        }
    }

    public void alterar(Compra compra) {
        if (compra.getIdCompra() <= 0) {
            throw new RuntimeException("Id da compra inválido!");
        }
        compraDAO.alterar(compra);
    }

    public void deletar(Compra compra) {
        if (compra.getIdCompra() <= 0) {
            throw new RuntimeException("Id da compra inválido!");
        }
        compraDAO.deletar(compra);
    }

    public List<Compra> listar() {
        return compraDAO.listar();
    }

    public java.sql.ResultSet buscar(String fornecedor) {
        if (fornecedor == null || fornecedor.isBlank()) {
            throw new RuntimeException("Fornecedor obrigatório para busca!");
        }
        return compraDAO.buscar(fornecedor);
    }
}
