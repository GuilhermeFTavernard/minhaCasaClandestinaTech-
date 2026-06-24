package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.ClienteDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Cliente;

import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public void cadastrar(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new RuntimeException("Nome do cliente obrigatório!");
        }
        if (cliente.getEndereco() == null || cliente.getEndereco().isBlank()) {
            throw new RuntimeException("Endereço do cliente obrigatório!");
        }
        if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
            throw new RuntimeException("CPF do cliente obrigatório!");
        }

        clienteDAO.inserir(cliente);
    }

    public void alterar(Cliente cliente) {
        if (cliente.getIdCliente() <= 0) {
            throw new RuntimeException("Id do cliente inválido!");
        }

        clienteDAO.alterar(cliente);
    }

    public void deletar(Cliente cliente) {
        if (cliente.getIdCliente() <= 0) {
            throw new RuntimeException("Id do cliente inválido!");
        }

        clienteDAO.deletar(cliente);
    }

    public List<Cliente> listar() {
        return clienteDAO.listar();
    }

    public Cliente buscar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("Nome obrigatório para busca!");
        }

        return clienteDAO.buscarCliente(nome);
    }
}
