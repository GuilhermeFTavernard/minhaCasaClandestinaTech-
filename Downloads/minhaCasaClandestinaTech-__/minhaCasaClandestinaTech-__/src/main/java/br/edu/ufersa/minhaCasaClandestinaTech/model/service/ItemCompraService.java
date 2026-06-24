package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.ItemCompraDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemCompra;

import java.util.List;

public class ItemCompraService {

    private ItemCompraDAO itemCompraDAO = new ItemCompraDAO();

    public void cadastrar(ItemCompra item) {
        if (item.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero!");
        }
        if (item.getPrecoUnitario() <= 0) {
            throw new RuntimeException("Preço unitário deve ser maior que zero!");
        }
        if (item.getCategoria() == null || item.getCategoria().isBlank()) {
            throw new RuntimeException("Categoria obrigatória!");
        }
        if (item.getIdCompra() <= 0) {
            throw new RuntimeException("Item deve estar vinculado a uma compra!");
        }
        itemCompraDAO.inserir(item);
    }

    public void alterar(ItemCompra item) {
        if (item.getIdItemCompra() <= 0) {
            throw new RuntimeException("Id do item inválido!");
        }
        itemCompraDAO.alterar(item);
    }

    public void deletar(ItemCompra item) {
        if (item.getIdItemCompra() <= 0) {
            throw new RuntimeException("Id do item inválido!");
        }
        itemCompraDAO.deletar(item);
    }

    public List<ItemCompra> listar() {
        return itemCompraDAO.listar();
    }

    public List<ItemCompra> listarPorCompra(int idCompra) {
        if (idCompra <= 0) {
            throw new RuntimeException("Id da compra inválido!");
        }
        return itemCompraDAO.listarPorCompra(idCompra);
    }
}
