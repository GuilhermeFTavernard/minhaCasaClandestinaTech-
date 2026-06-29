package br.edu.ufersa.minhaCasaClandestinaTech.model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface BaseDAO <E>{
    /*final static String URL = "jdbc:mysql://localhost:3306/minha_casa_clandestina_tech";
    final static String USER = "root";
    final static String PASS = " ";
    static Connection con = getConnection();

    public static Connection getConnection(){
            try{
                return DriverManager.getConnection(URL,USER,PASS);
            }catch (SQLException e){e.printStackTrace();}
        return null;
    }

    public static void closeConnection(){
        if(con != null){
            try{
                con.close();
            }catch (SQLException e){e.printStackTrace();}
        }
    }*/

    public E inserir (E entity);

    public void deletar(E entity);
    public ResultSet buscar(String param);
    public void alterar(E entity);
    List<E> listar();
}
