package br.edu.ufersa.minhaCasaClandestinaTech.model.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL =
            "jdbc:mysql://localhost:3306/minha_casa_clandestina";

    private static final String USER = "root";
    private static final String PASS = "guilherme95";

    // única instância da classe
    private static ConnectionFactory instance;

    // única conexão
    private Connection connection;

    // construtor privado
    private ConnectionFactory() {

        try {
            connection = DriverManager.getConnection(URL, USER, PASS);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionFactory getInstance() {

        if(instance == null) {
            instance = new ConnectionFactory();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {

        try {

            if(connection != null && !connection.isClosed()) {
                connection.close();
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

    }

}