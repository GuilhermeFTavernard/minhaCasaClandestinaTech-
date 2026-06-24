package br.edu.ufersa.minhaCasaClandestinaTech.model.facade;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Compra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemCompra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.MovimentacaoEstoque;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.CompraService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.EquipamentoService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.MovimentacaoEstoqueService;

public class CompraFacade {

    private final CompraService compraService = new CompraService();
    private final EquipamentoService equipamentoService = new EquipamentoService();
    private final MovimentacaoEstoqueService movimentacaoService = new MovimentacaoEstoqueService();

    public void realizarCompra(Compra compra) {
        if (compra == null) {
            throw new RuntimeException("Compra inválida.");
        }

        compraService.cadastrar(compra);

        for (ItemCompra item : compra.getListaDeItens()) {
            Equipamento equipamento = item.getEquipamento();

            if (equipamento == null) {
                throw new RuntimeException("Equipamento não informado no item da compra.");
            }

            equipamento.setQuantidadeEstoque(
                    equipamento.getQuantidadeEstoque() + item.getQuantidade()
            );

            equipamentoService.alterar(equipamento);

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(equipamento, item.getQuantidade());
            movimentacao.setTipo(MovimentacaoEstoque.TipoMovimentacao.ENTRADA);

            movimentacaoService.cadastrar(movimentacao);
        }
    }
}
