package com.example.simuladorfacturas.basedatos;

import com.example.simuladorfacturas.objetos.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class BaseDatos {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FacturaLuz";
    private static final String DB_USER = "root"; // Reemplaza con tu usuario de MySQL
    private static final String DB_PASSWORD = "abc123."; // Reemplaza con tu contraseña de MySQL
    private static Connection conexion;

    public static void openConexion() {
        conexion = Database.getInstance(DB_URL, DB_USER, DB_PASSWORD);
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

    public static LocalDateTime fechaUltima() {
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

    public static boolean tablaExiste(String nombreTabla) {
        DatabaseMetaData meta = null;
        try {
            meta = conexion.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (var resultSet = meta.getTables(null, null, nombreTabla, new String[]{"TABLE"})) {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void crearTabla(String nombreTabla) {
        String sqlCreateTable = "CREATE TABLE " + nombreTabla + " (" +
                "fecha DATETIME, " +
                "consumo DOUBLE, " +
                "metodos boolean, " +
                "verano boolean," +
                "primary key (fecha,verano));";
        try (Statement statement = conexion.createStatement()) {
            statement.execute(sqlCreateTable);
            System.out.println("Tabla " + nombreTabla + " creada.");
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Coste> calcularCostes(String tabla, LocalDateTime fecha_inicio, LocalDateTime fecha_final) {
        System.out.println(fecha_inicio + " " + fecha_final);
        ArrayList<Coste> listacoste = new ArrayList<>();
        String sql = "SELECT p.precio, l.consumo, (p.precio * l.consumo) AS coste, p.fecha,p.verano\n" +
                "FROM precios p\n" +
                "JOIN " + tabla + " l ON p.fecha = l.fecha AND p.verano = l.verano\n" +
                "WHERE p.fecha BETWEEN ? AND ?;";
        try (
                PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(fecha_inicio));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(fecha_final));
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                double precio = rs.getDouble("precio");
                double consumo = rs.getDouble("consumo");
                double coste = rs.getDouble("coste");
                LocalDateTime fecha = (LocalDateTime) rs.getObject("fecha");
                boolean verano = rs.getBoolean("verano");
                listacoste.add(new Coste(precio, consumo, coste / 1000, fecha, verano));
            }
            return listacoste;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Potencia anoPotencia(int ano) {
        String sql = "SELECT * FROM potencia where ano=?;";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, ano);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int ano2 = resultSet.getInt("ano");
                double p1 = resultSet.getDouble("periodo1");
                double p2 = resultSet.getDouble("periodo2");
                double margenp1=resultSet.getDouble("margenp1");
                double margenp2=resultSet.getDouble("margenp2");
                double costefijo=resultSet.getDouble("costefijo");
                return new Potencia(ano2, p1, p2,margenp1,margenp2,costefijo);

            } else {
                System.out.println("No se encontraron registros.");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int insertarUsuario(Usuario usuario) {
        String sqlInsert = "INSERT INTO usuarios (nombre, password_hash) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getContrasenha());
            preparedStatement.executeUpdate();
            System.out.println("Nuevo usuario creado");
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Usuario obtenerUsuario(String nombre) {
        String sql = "Select * from usuarios where nombre=?";
        String sql2="SELECT * FROM usuariotablas WHERE nombre_usuario = ?";
        HashMap<String,String> listaCUPS=new HashMap<>();
        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql);
             PreparedStatement preparedStatement2 = conexion.prepareStatement(sql2)) {
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                preparedStatement2.setString(1, nombre);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                if (resultSet2.next()) {
                    String CUPS=resultSet2.getString("CUPS");
                    String clave=resultSet2.getString("clave");
                    listaCUPS.put(clave,CUPS);
                }else{
                    System.out.println("No se encontraron CUPS para ese Usuario.");
                }
            } else {
                System.out.println("No se encontraron Usuarios con ese nombre.");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Usuario(nombre,listaCUPS);
    }

    public static String recuperarHash(String nombre) {
        String hash;
        String sql = "Select * from usuarios where nombre=?";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)){
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                hash=resultSet.getString("password_hash");
                System.out.println(hash);
            } else {
                System.out.println("No se encontraron Usuarios con ese nombre.");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hash;
    }

    public static boolean cambiarContrasenha(Usuario usuarioLogueado) {
        String sql="UPDATE Usuarios SET password_hash = ? WHERE nombre = ?";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)){
            preparedStatement.setString(1, usuarioLogueado.getContrasenha());
            preparedStatement.setString(2, usuarioLogueado.getNombre());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void insertPreciosMayorista(ArrayList<Precio> precios) {
        String insertSQL = "INSERT INTO preciomayorista (fecha, precio, verano) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conexion.prepareStatement(insertSQL)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Precio precio : precios) {
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

    public static ArrayList<Double> mediaMayorista() {
        ArrayList<Double> mediasMensuales=new ArrayList<>();
        String sql = "SELECT MONTH(fecha) AS month, AVG(precio) AS avg_price " +
                "FROM precioMayorista GROUP BY MONTH(fecha) ";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Procesa los resultados
            while (rs.next()) {
                double mediaMensual = rs.getDouble("avg_price");
                mediasMensuales.add(mediaMensual);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mediasMensuales;

    }

    public static LocalDateTime ultimaMayorista() {
        String query = "SELECT fecha FROM preciomayorista ORDER BY fecha DESC LIMIT 1";

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

    public static ArrayList<LocalDate> listafestivos(int anho) {
        ArrayList<LocalDate> listafestivos=new ArrayList<>();//todo sql y montar todo aqui
        String sql = "Select * from festivos where año=?";
        return listafestivos;
    }
}
