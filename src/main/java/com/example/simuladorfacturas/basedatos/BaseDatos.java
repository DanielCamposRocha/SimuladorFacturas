package com.example.simuladorfacturas.basedatos;

import com.example.simuladorfacturas.objetos.Coste;
import com.example.simuladorfacturas.objetos.Lectura;
import com.example.simuladorfacturas.objetos.Potencia;
import com.example.simuladorfacturas.objetos.Precio;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BaseDatos {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FacturaLuz";
    private static final String DB_USER = "root"; // Reemplaza con tu usuario de MySQL
    private static final String DB_PASSWORD = "abc123."; // Reemplaza con tu contraseña de MySQL
    private static Connection conexion;

    public static void openConexion(){
        conexion= Database.getInstance(DB_URL,DB_USER,DB_PASSWORD);
    }
    public static void closeConexion() {
        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión");
        }
    }

    public static void insertPrecio(Precio precio) {
        String insertSQL = "INSERT INTO precios (fecha, precio, verano) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conexion.prepareStatement(insertSQL)) {

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

        try (PreparedStatement pstmt = conexion.prepareStatement(insertSQL)) {

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

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {

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

    public static boolean tablaExiste( String nombreTabla){
        DatabaseMetaData meta = null;
        try {
            meta = conexion.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (var resultSet = meta.getTables(null, null, nombreTabla, new String[]{"TABLE"})) {
            return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static void crearTabla(String nombreTabla)  {
        String sqlCreateTable = "CREATE TABLE " + nombreTabla + " (" +
                "fecha DATETIME, " +
                "consumo DOUBLE, " +
                "metodos boolean, " +
                "verano boolean," +
                "primary key (fecha,verano));";
        try (Statement statement = conexion.createStatement()) {
            statement.execute(sqlCreateTable);
            System.out.println("Tabla " + nombreTabla + " creada.");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void insertarDatos(String nombreTabla, Lectura lectura) {
        String sqlInsert = "INSERT INTO " + nombreTabla + " (fecha, consumo, metodos, verano) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(sqlInsert)) {
            preparedStatement.setObject(1, lectura.getFecha());
            preparedStatement.setDouble(2, lectura.getConsumo());
            preparedStatement.setBoolean(3, lectura.isMetodos());
            preparedStatement.setBoolean(4, lectura.isVerano());
            preparedStatement.executeUpdate();
            System.out.println("Datos insertados en la tabla " + nombreTabla);
        }catch (SQLException e){
            e.printStackTrace();
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
            PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
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

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
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
