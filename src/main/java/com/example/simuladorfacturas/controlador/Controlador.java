package com.example.simuladorfacturas.controlador;

import com.example.simuladorfacturas.basedatos.BaseDatos;
import com.example.simuladorfacturas.contratos.PVPC;
import com.example.simuladorfacturas.objetos.*;
import org.mindrot.jbcrypt.BCrypt;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controlador {

    public static void openConexion(){BaseDatos.openConexion();}
    public static void closeConexion(){BaseDatos.closeConexion();}
    public static LocalDateTime ultimaActualizacionREE(){return BaseDatos.fechaUltima();  }

    public static void insertPrecio(Precio precio){BaseDatos.insertPrecio(precio);  }

    public static void insertPrecios(ArrayList<Precio> listado) { BaseDatos.insertPrecios(listado);    }

    public static void crearTablaLecturas(HashMap<LocalDateTime, Lectura> listadoLecturas){
        String cups = PVPC.getIdentificador();
            // Comprobar si la tabla existe
            if (!BaseDatos.tablaExiste( cups)) {
                BaseDatos.crearTabla(cups);
            }
            // Insertar los datos
            for (Map.Entry<LocalDateTime, Lectura> lectura : listadoLecturas.entrySet()) {
                BaseDatos.insertarDatos( cups, lectura.getValue());
            }
    }
    public static ArrayList<Coste> calcularCostes(String tabla, LocalDateTime fecha_inicio, LocalDateTime fecha_final){
        return BaseDatos.calcularCostes(tabla,fecha_inicio,fecha_final);
    }

    public  static Potencia anoPotencia(int ano){  return BaseDatos.anoPotencia(ano);  }

    public static int crearUsuario(Usuario usuario){
        String password_hash=BCrypt.hashpw(usuario.getContrasenha(),BCrypt.gensalt());
        usuario.setContrasenha(password_hash);
        return BaseDatos.insertarUsuario(usuario);
    }

    public static Usuario obtenerUsuario(String nombre) {
        return BaseDatos.obtenerUsuario(nombre);
    }

    public static boolean comprobarContrasenha(Usuario usuario) {
        String hashed=BaseDatos.recuperarHash(usuario.getNombre());
        if(hashed!=null){
            if(BCrypt.checkpw(usuario.getContrasenha(),hashed)){
                return true;
            }
        }
        return false;
    }

    public static boolean cambiarContrasenha(Usuario usuarioLogueado) {
        String password_hash=BCrypt.hashpw(usuarioLogueado.getContrasenha(),BCrypt.gensalt());
        usuarioLogueado.setContrasenha(password_hash);
        return BaseDatos.cambiarContrasenha(usuarioLogueado);
    }
}
