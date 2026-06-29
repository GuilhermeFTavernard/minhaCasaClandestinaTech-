package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.exception.BancoDeDadosException;
import br.edu.ufersa.minhaCasaClandestinaTech.model.connection.ConnectionFactory;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Cliente;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemVenda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

public class RelatorioDeVendasDAO implements BaseDAO<Venda> {

    private final Connection con = ConnectionFactory.getInstance().getConnection();
    @Override
    public Venda inserir(Venda entity) {
        throw new UnsupportedOperationException("Relatório não realiza inserções.");
    }

    @Override
    public void alterar(Venda entity) {
        throw new UnsupportedOperationException("Relatório não realiza alterações.");
    }

    @Override
    public void deletar(Venda entity) {
        throw new UnsupportedOperationException("Relatório não realiza exclusões.");
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM venda WHERE id_venda = ?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar relatorio.", e);
        }
    }

    @Override
    public List<Venda> listar() {
        String sql = "SELECT v.id_venda, v.data_venda, v.valor_total, " +
                "c.id_cliente, c.nome, c.endereco, c.cpf " +
                "FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente";

        List<Venda> lista = new ArrayList<>();

        try (
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = montarCliente(rs, "nome");

                Venda venda = new Venda(rs.getInt("id_venda"), cliente);
                venda.setDataVenda(rs.getDate("data_venda").toLocalDate());
                venda.setValorTotal(rs.getBigDecimal("valor_total"));

                lista.add(venda);
            }

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao inserir relatório.", e);
        }
        return lista;
    }

    public List<Venda> buscarVendasPorPeriodo(String dataInicio, String dataFim) {
        String sql = "SELECT v.id_venda, v.data_venda, v.valor_total, " +
                "c.id_cliente, c.nome, c.endereco, c.cpf " +
                "FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "WHERE v.data_venda BETWEEN ? AND ?";

        List<Venda> lista = new ArrayList<>();

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(dataInicio));
            ps.setDate(2, Date.valueOf(dataFim));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = montarCliente(rs, "nome");

                    Venda venda = new Venda(rs.getInt("id_venda"), cliente);
                    venda.setDataVenda(rs.getDate("data_venda").toLocalDate());
                    venda.setValorTotal(rs.getBigDecimal("valor_total"));

                    lista.add(venda);
                }
            }

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar relatório.", e);
        }
        return lista;
    }

    public List<Venda> buscarVendasComItens(String dataInicio, String dataFim) {
        String sql = "SELECT v.id_venda, v.data_venda, v.valor_total, " +
                "c.id_cliente, c.nome AS nome_cliente, c.endereco, c.cpf, " +
                "iv.id_item_venda, iv.quantidade, iv.preco_unitario, iv.id_venda AS id_venda_item, " +
                "e.id_equipamento, e.nome AS nome_equipamento, e.numero_serie, " +
                "e.preco, e.quantidade_estoque " +
                "FROM venda v " +
                "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                "LEFT JOIN item_venda iv ON iv.id_venda = v.id_venda " +
                "LEFT JOIN equipamento e ON iv.id_equipamento = e.id_equipamento " +
                "WHERE v.data_venda BETWEEN ? AND ? " +
                "ORDER BY v.id_venda";

        List<Venda> lista = new ArrayList<>();
        Venda vendaAtual = null;

        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(dataInicio));
            ps.setDate(2, Date.valueOf(dataFim));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idVenda = rs.getInt("id_venda");

                    if (vendaAtual == null || vendaAtual.getIdVenda() != idVenda) {
                        Cliente cliente = montarCliente(rs, "nome_cliente");

                        vendaAtual = new Venda(idVenda, cliente);
                        vendaAtual.setDataVenda(rs.getDate("data_venda").toLocalDate());

                        lista.add(vendaAtual);
                    }

                    int idItemVenda = rs.getInt("id_item_venda");
                    if (!rs.wasNull()) {
                        Equipamento equipamento = new Equipamento(
                                rs.getInt("id_equipamento"),
                                rs.getString("nome_equipamento"),
                                rs.getInt("numero_serie"),
                                rs.getFloat("preco"),
                                rs.getInt("quantidade_estoque")
                        );

                        ItemVenda item = new ItemVenda(
                                rs.getInt("quantidade"),
                                rs.getBigDecimal("preco_unitario"),
                                equipamento
                        );
                        item.setIdItemVenda(idItemVenda);
                        item.setIdVenda(idVenda);

                        vendaAtual.adicionarItemDeVenda(item);
                    }
                }
            }

        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao buscar relatorio.", e);
        }
        return lista;
    }

    private Cliente montarCliente(ResultSet rs, String colunaNome) throws SQLException {
        Cliente cliente = new Cliente(
                rs.getString(colunaNome),
                rs.getString("endereco"),
                rs.getString("cpf")
        );
        cliente.setIdCliente(rs.getInt("id_cliente"));
        return cliente;
    }
}
