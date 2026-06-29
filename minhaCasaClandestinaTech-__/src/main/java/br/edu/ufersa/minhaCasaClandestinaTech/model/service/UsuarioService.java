package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.UsuarioDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Usuario;

import java.util.List;

public class UsuarioService{

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void cadastrar(Usuario usuario) {
        if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
            throw new RuntimeException("Email inválido!");
        }
        if (usuario.getSenha() == null || usuario.getSenha().length() < 6) {
            throw new RuntimeException("Senha deve ter pelo menos 6 caracteres!");
        }
        usuarioDAO.inserir(usuario);
    }

    public boolean logar(String email, String senha) {
        if (email == null || senha == null) {
            throw new RuntimeException("Email e senha obrigatórios!");
        }
        // Busca o usuário pelo email
        java.sql.ResultSet rs = usuarioDAO.buscar(email);
        try {
            if (rs != null && rs.next()) {
                String senhaBanco = rs.getString("senha");
                if (senhaBanco.equals(senha)) {
                    System.out.println("Login realizado com sucesso!");
                    return true;
                }
            }
            System.out.println("Email ou senha inválidos!");
            return false;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void alterar(Usuario usuario) {
        if (usuario.getIdUsuario() <= 0) {
            throw new RuntimeException("Id do usuário inválido!");
        }
        usuarioDAO.alterar(usuario);
    }

    public void deletar(Usuario usuario) {
        if (usuario.getIdUsuario() <= 0) {
            throw new RuntimeException("Id do usuário inválido!");
        }
        usuarioDAO.deletar(usuario);
    }

    public List<Usuario> listar()  {
        return usuarioDAO.listar();
    }
}
