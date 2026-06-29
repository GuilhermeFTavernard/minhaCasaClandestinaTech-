package br.edu.ufersa.minhaCasaClandestinaTech.exception;

public class BancoDeDadosException extends AppException {

    public BancoDeDadosException(String mensagem) {
        super(mensagem);
    }

    public BancoDeDadosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
