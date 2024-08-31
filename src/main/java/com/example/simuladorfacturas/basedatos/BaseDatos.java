package com.example.simuladorfacturas.basedatos;

import com.example.simuladorfacturas.contratos.PVPC;
import com.example.simuladorfacturas.objetos.Coste;
import com.example.simuladorfacturas.objetos.Lectura;
import com.example.simuladorfacturas.objetos.Potencia;
import com.example.simuladorfacturas.objetos.Precio;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseDatos {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FacturaLuz";
    private static final String DB_USER = "root"; // Reemplaza con tu usuario de MySQL
    private static final String DB_PASSWORD = "abc123."; // Reemplaza con tu contraseña de MySQL

    public static void main(String[] args) {


    }

    public static void insertPrecio(Precio precio) {
        String insertSQL = "INSERT INTO precios (fecha, precio, verano) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = precio.getFecha().format(formatter);

            pstmt.setString(1, formattedDate);
            pstmt.setDouble(2, precio.getPrecio());
            pstmt.setBoolean(3, precio.isVerano());


            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Filas insertadas: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertPrecios(ArrayList<Precio> listado) {
        String insertSQL = "INSERT INTO precios (fecha, precio, verano) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Precio precio : listado) {
                String formattedDate = precio.getFecha().format(formatter);

                pstmt.setString(1, formattedDate);
                pstmt.setDouble(2, precio.getPrecio());
                pstmt.setBoolean(3, precio.isVerano());

                pstmt.addBatch();  // Agrega la instrucción al lote
            }

            int[] rowsAffected = pstmt.executeBatch();  // Ejecuta el lote de instrucciones
            System.out.println("Filas insertadas: " + rowsAffected.length);

        } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public  static LocalDateTime fechaUltima(){
        String query = "SELECT fecha FROM precios ORDER BY fecha DESC LIMIT 1";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Fecha más reciente: " + resultSet.getTimestamp("fecha"));
                Timestamp timestamp = resultSet.getTimestamp("fecha");
                return timestamp.toLocalDateTime();  // Convertir a LocalDateTime
            } else {
                System.out.println("No se encontraron registros.");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void crearTablaLecturas(HashMap<LocalDateTime, Lectura> listadoLecturas){
        String cups = PVPC.getIdentificador();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Comprobar si la tabla existe
            if (!tablaExiste(connection, cups)) {
                crearTabla(connection, cups);
            }
            // Insertar los datos
            for (Map.Entry<LocalDateTime, Lectura> lectura : listadoLecturas.entrySet()) {
                insertarDatos(connection, cups, lectura.getValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean tablaExiste(Connection connection, String nombreTabla) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        try (var resultSet = meta.getTables(null, null, nombreTabla, new String[]{"TABLE"})) {
            return resultSet.next();
        }
    }
    private static void crearTabla(Connection connection, String nombreTabla) throws SQLException {
        String sqlCreateTable = "CREATE TABLE " + nombreTabla + " (" +
                "fecha DATETIME, " +
                "consumo DOUBLE, " +
                "metodos boolean, " +
                "verano boolean," +
                "primary key (fecha,verano));";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlCreateTable);
            System.out.println("Tabla " + nombreTabla + " creada.");
        }
    }
    private static void insertarDatos(Connection connection, String nombreTabla, Lectura lectura) throws SQLException {
        String sqlInsert = "INSERT INTO " + nombreTabla + " (fecha, consumo, metodos, verano) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setObject(1, lectura.getFecha());
            preparedStatement.setDouble(2, lectura.getConsumo());
            preparedStatement.setBoolean(3, lectura.isMetodos());
            preparedStatement.setBoolean(4, lectura.isVerano());
            preparedStatement.executeUpdate();
            System.out.println("Datos insertados en la tabla " + nombreTabla);
        }
    }

    public static ArrayList<Coste> calcularCostes(String tabla, LocalDateTime fecha_inicio, LocalDateTime fecha_final){
        System.out.println(fecha_inicio+" "+ fecha_final);
        ArrayList<Coste> listacoste=new ArrayList<>();
        String sql = "SELECT p.precio, l.consumo, (p.precio * l.consumo) AS coste, p.fecha,p.verano\n" +
                "FROM precios p\n" +
                "JOIN "+tabla+" l ON p.fecha = l.fecha AND p.verano = l.verano\n" +
                "WHERE p.fecha BETWEEN ? AND ?;";
        try (
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1,Timestamp.valueOf(fecha_inicio));
            preparedStatement.setTimestamp(2,Timestamp.valueOf(fecha_final));
            ResultSet rs= preparedStatement.executeQuery();

            while(rs.next()){
                double precio= rs.getDouble("precio");
                double consumo=rs.getDouble("consumo");
                double coste= rs.getDouble("coste");
                LocalDateTime fecha= (LocalDateTime) rs.getObject("fecha");
                boolean verano= rs.getBoolean("verano");
                listacoste.add(new Coste(precio,consumo,coste/1000,fecha,verano));
                }
            return listacoste;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  static Potencia anoPotencia(int ano){
        String sql = "SELECT * FROM potencia where ano=?;";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,ano);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int ano2= resultSet.getInt("ano");
                double p1=resultSet.getDouble("periodo1");
                double p2=resultSet.getDouble("periodo2");
                return new Potencia(ano2, p1,p2);

            } else {
                System.out.println("No se encontraron registros.");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
