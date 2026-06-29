package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.CompraDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.EquipamentoDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.ItemCompraDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Compra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemCompra;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class CompraService {

    private final CompraDAO compraDAO = new CompraDAO();
    private final ItemCompraDAO itemCompraDAO = new ItemCompraDAO();
    private final EquipamentoDAO equipamentoDAO = new EquipamentoDAO();

    public void cadastrar(Compra compra) {
        validarCompra(compra);

        compraDAO.inserir(compra);

        for (ItemCompra item : compra.getListaDeItens()) {
            item.setIdCompra(compra.getIdCompra());
            itemCompraDAO.inserir(item);
        }
    }

    public void registrarCompra(LocalDate dataCompra,
                                String fornecedor,
                                Equipamento equipamento,
                                int quantidade,
                                float precoUnitario) {

        if (equipamento == null) {
            throw new RuntimeException("Selecione o equipamento.");
        }
        if (quantidade <= 0) {
            throw new RuntimeException("Informe uma quantidade válida.");
        }
        if (precoUnitario < 0) {
            throw new RuntimeException("Informe um custo válido.");
        }

        Compra compra = new Compra(0, dataCompra, fornecedor);

        ItemCompra item = new ItemCompra(
                0,
                0,
                quantidade,
                precoUnitario,
                equipamento.getNome()
        );

        compra.adicionarItemDeCompra(item);
        cadastrar(compra);

        equipamento.setQuantidadeEstoque(equipamento.getQuantidadeEstoque() + quantidade);
        equipamentoDAO.alterar(equipamento);
    }

    public void alterar(Compra compra) {
        if (compra == null || compra.getIdCompra() <= 0) {
            throw new RuntimeException("Id da compra inválido!");
        }
        validarCamposBasicos(compra);
        compraDAO.alterar(compra);
    }

    public void deletar(Compra compra) {
        if (compra == null || compra.getIdCompra() <= 0) {
            throw new RuntimeException("Id da compra inválido!");
        }
        compraDAO.deletar(compra);
    }

    public List<Compra> listar() {
        return compraDAO.listar();
    }

    public List<Equipamento> listarEquipamentos() {
        return equipamentoDAO.listar();
    }

    public ResultSet buscar(String fornecedor) {
        if (fornecedor == null || fornecedor.isBlank()) {
            throw new RuntimeException("Fornecedor obrigatório para busca!");
        }
        return compraDAO.buscar(fornecedor);
    }

    private void validarCompra(Compra compra) {
        validarCamposBasicos(compra);

        if (compra.getListaDeItens() == null || compra.getListaDeItens().isEmpty()) {
            throw new RuntimeException("A compra deve ter pelo menos um item!");
        }
    }

    private void validarCamposBasicos(Compra compra) {
        if (compra == null) {
            throw new RuntimeException("Compra inválida!");
        }
        if (compra.getFornecedor() == null || compra.getFornecedor().isBlank()) {
            throw new RuntimeException("Fornecedor obrigatório!");
        }
        if (compra.getDataCompra() == null) {
            throw new RuntimeException("Data da compra obrigatória!");
        }
    }
}
