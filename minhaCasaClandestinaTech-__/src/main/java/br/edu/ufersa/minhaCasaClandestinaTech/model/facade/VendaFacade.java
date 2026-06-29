package br.edu.ufersa.minhaCasaClandestinaTech.model.facade;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemVenda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.MovimentacaoEstoque;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.EquipamentoService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.MovimentacaoEstoqueService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.VendaService;

public class VendaFacade {

    private final VendaService vendaService = new VendaService();
    private final EquipamentoService equipamentoService = new EquipamentoService();
    private final MovimentacaoEstoqueService movimentacaoService = new MovimentacaoEstoqueService();

    public void realizarVenda(Venda venda) {
        if (venda == null) {
            throw new RuntimeException("Venda inválida.");
        }

        for (ItemVenda item : venda.getItensVenda()) {
            Equipamento equipamento = item.getEquipamento();

            if (equipamento == null) {
                throw new RuntimeException("Equipamento não informado no item da venda.");
            }

            if (equipamento.getQuantidadeEstoque() < item.getQuantidade()) {
                throw new RuntimeException(
                        "Estoque insuficiente para o equipamento: " + equipamento.getNome()
                );
            }
        }

        vendaService.cadastrar(venda);

        for (ItemVenda item : venda.getItensVenda()) {
            Equipamento equipamento = item.getEquipamento();

            equipamento.setQuantidadeEstoque(
                    equipamento.getQuantidadeEstoque() - item.getQuantidade()
            );

            equipamentoService.alterar(equipamento);

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(equipamento, item.getQuantidade());
            movimentacao.setTipo(MovimentacaoEstoque.TipoMovimentacao.SAIDA);

            movimentacaoService.cadastrar(movimentacao);
        }
    }

    public void cancelarVenda(Venda venda) {
        if (venda == null) {
            throw new RuntimeException("Venda inválida.");
        }

        for (ItemVenda item : venda.getItensVenda()) {
            Equipamento equipamento = item.getEquipamento();

            if (equipamento == null) {
                throw new RuntimeException("Equipamento não informado no item da venda.");
            }

            equipamento.setQuantidadeEstoque(
                    equipamento.getQuantidadeEstoque() + item.getQuantidade()
            );

            equipamentoService.alterar(equipamento);

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(equipamento, item.getQuantidade());
            movimentacao.setTipo(MovimentacaoEstoque.TipoMovimentacao.ENTRADA);

            movimentacaoService.cadastrar(movimentacao);
        }

        vendaService.deletar(venda);
    }
}
