package com.example.simuladorfacturas.basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection conexion;
    private String url;
    private String user;
    private String pass;

    private Database(String url, String user, String pass) {
        try {
            this.conexion= DriverManager.getConnection(url,user,pass);
        } catch (SQLException e) {
            System.out.println("Error en la conexión con la base de datos");
        }
    }

    public static Connection getInstance(String url, String user, String pass){
        try {
            if (conexion ==null|| conexion.isClosed()) new Database(url,user,pass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conexion;

    }
}