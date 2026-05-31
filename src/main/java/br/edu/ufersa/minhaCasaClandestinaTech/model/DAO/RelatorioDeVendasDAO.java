package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Cliente;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemVenda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDeVendasDAO implements BaseDAO<Venda> {

    // Relatório não realiza inserções
    @Override
    public Venda inserir(Venda entity) {
        throw new UnsupportedOperationException("Relatório não realiza inserções.");
    }

    // Relatório não realiza alterações
    @Override
    public void alterar(Venda entity) {
        throw new UnsupportedOperationException("Relatório não realiza alterações.");
    }

    // Relatório não realiza exclusões
    @Override
    public void deletar(Venda entity) {
        throw new UnsupportedOperationException("Relatório não realiza exclusões.");
    }

    @Override
    public ResultSet buscar(String param) {
        String sql = "SELECT * FROM venda WHERE id_venda = ?";

        try {
            Connection con = BaseDAO.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Venda> listar() {
        String sql = "SELECT v.id_venda, v.data_venda, v.valor_total, " +
                     "c.id_cliente, c.nome, c.endereco, c.cpf " +
                     "FROM venda v " +
                     "JOIN cliente c ON v.id_cliente = c.id_cliente";

        List<Venda> lista = new ArrayList<>();

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getInt("cpf")
                );
                cliente.setIdCliente(rs.getInt("id_cliente"));

                Venda venda = new Venda(rs.getInt("id_venda"), cliente);
                venda.setDataVenda(rs.getDate("data_venda").toLocalDate());
                venda.setValorTotal(rs.getBigDecimal("valor_total"));

                lista.add(venda);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Busca vendas dentro de um período — sem detalhamento dos itens
    public List<Venda> buscarVendasPorPeriodo(String dataInicio, String dataFim) {
        String sql = "SELECT v.id_venda, v.data_venda, v.valor_total, " +
                     "c.id_cliente, c.nome, c.endereco, c.cpf " +
                     "FROM venda v " +
                     "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                     "WHERE v.data_venda BETWEEN ? AND ?";

        List<Venda> lista = new ArrayList<>();

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dataInicio);
            ps.setString(2, dataFim);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getInt("cpf")
                );
                cliente.setIdCliente(rs.getInt("id_cliente"));

                Venda venda = new Venda(rs.getInt("id_venda"), cliente);
                venda.setDataVenda(rs.getDate("data_venda").toLocalDate());
                venda.setValorTotal(rs.getBigDecimal("valor_total"));

                lista.add(venda);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Busca vendas com detalhamento completo dos itens
    public List<Venda> buscarVendasComItens(String dataInicio, String dataFim) {
        String sql = "SELECT v.id_venda, v.data_venda, v.valor_total, " +
                     "c.id_cliente, c.nome AS nome_cliente, c.endereco, c.cpf, " +
                     "iv.id_item_venda, iv.quantidade, iv.preco_unitario, " +
                     "e.id_equipamento, e.nome AS nome_equipamento, e.numero_serie, " +
                     "e.preco, e.quantidade_estoque " +
                     "FROM venda v " +
                     "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                     "JOIN item_venda iv ON iv.id_venda = v.id_venda " +
                     "JOIN equipamento e ON iv.id_equipamento = e.id_equipamento " +
                     "WHERE v.data_venda BETWEEN ? AND ? " +
                     "ORDER BY v.id_venda";

        List<Venda> lista = new ArrayList<>();
        Venda vendaAtual = null;

        try (Connection con = BaseDAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dataInicio);
            ps.setString(2, dataFim);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idVenda = rs.getInt("id_venda");

                // Se é uma venda nova, cria o objeto e adiciona na lista
                if (vendaAtual == null || vendaAtual.getIdVenda() != idVenda) {
                    Cliente cliente = new Cliente(
                            rs.getString("nome_cliente"),
                            rs.getString("endereco"),
                            rs.getInt("cpf")
                    );
                    cliente.setIdCliente(rs.getInt("id_cliente"));

                    vendaAtual = new Venda(idVenda, cliente);
                    vendaAtual.setDataVenda(rs.getDate("data_venda").toLocalDate());
                    vendaAtual.setValorTotal(rs.getBigDecimal("valor_total"));

                    lista.add(vendaAtual);
                }

                // Monta o item e adiciona na venda atual
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

                vendaAtual.adicionarItemDeVenda(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
