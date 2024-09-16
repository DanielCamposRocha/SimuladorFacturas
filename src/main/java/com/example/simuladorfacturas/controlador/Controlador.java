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



    private static void openConexion(){BaseDatos.openConexion();}
    private static void closeConexion(){BaseDatos.closeConexion();}
    public static LocalDateTime ultimaActualizacionREE(){
        openConexion();
        LocalDateTime ultima= BaseDatos.fechaUltima();
        closeConexion();
        return ultima;
    }

    public static void insertPrecio(Precio precio){
        openConexion();
        BaseDatos.insertPrecio(precio);
        closeConexion();
    }

    public static void insertPrecios(ArrayList<Precio> listado) {
        Controlador.openConexion();
        BaseDatos.insertPrecios(listado);
        BaseDatos.insertPreciosMayorista(PVPC.getListadoPrecios());
        Controlador.closeConexion();
    }

    public static void crearTablaLecturas(HashMap<LocalDateTime, Lectura> listadoLecturas){
        openConexion();
        String cups = PVPC.getIdentificador();
            // Comprobar si la tabla existe
            if (!BaseDatos.tablaExiste( cups)) {
                BaseDatos.crearTabla(cups);
            }
            // Insertar los datos
            for (Map.Entry<LocalDateTime, Lectura> lectura : listadoLecturas.entrySet()) {
                BaseDatos.insertarDatos( cups, lectura.getValue());
            }
        closeConexion();
    }
    public static ArrayList<CosteImpuestos> calcularCostes(String tabla, LocalDateTime fecha_inicio, LocalDateTime fecha_final){
        openConexion();
        ArrayList<Coste>listacostes=BaseDatos.calcularCostes(tabla,fecha_inicio,fecha_final);
        ArrayList<CosteImpuestos> listaConImpuestos=new ArrayList<>();
        for (Coste coste:listacostes) {
            double iva=CosteImpuestos.calculoIva(coste.getFecha());
            double impEl=CosteImpuestos.calculoImp(coste.getFecha());
            listaConImpuestos.add(new CosteImpuestos(coste,iva,impEl));
        }
        closeConexion();
        return listaConImpuestos;
    }

    public  static Potencia anoPotencia(int ano){
        openConexion();
        Potencia resultado= BaseDatos.anoPotencia(ano);
        closeConexion();
        return resultado;
    }

    public static int crearUsuario(Usuario usuario){
        String password_hash=BCrypt.hashpw(usuario.getContrasenha(),BCrypt.gensalt());
        usuario.setContrasenha(password_hash);
        openConexion();
        int exito= BaseDatos.insertarUsuario(usuario);
        closeConexion();
        return exito;
    }

    public static Usuario obtenerUsuario(String nombre) {
        openConexion();
        Usuario usuario= BaseDatos.obtenerUsuario(nombre);
        closeConexion();
        return usuario;
    }

    public static boolean comprobarContrasenha(Usuario usuario) {
        openConexion();
        boolean acierto=false;
        String hashed=BaseDatos.recuperarHash(usuario.getNombre());
        if(hashed!=null){
            if(BCrypt.checkpw(usuario.getContrasenha(),hashed)){
                acierto= true;
            }
        }
        closeConexion();
        return acierto;
    }

    public static boolean cambiarContrasenha(Usuario usuarioLogueado) {
        openConexion();
        String password_hash=BCrypt.hashpw(usuarioLogueado.getContrasenha(),BCrypt.gensalt());
        usuarioLogueado.setContrasenha(password_hash);
        boolean cambio= BaseDatos.cambiarContrasenha(usuarioLogueado);
        closeConexion();
        return cambio;
    }

    public static ArrayList<Double> mediaMayorista(){
        openConexion();
        ArrayList<Double> medias=BaseDatos.mediaMayorista();
        closeConexion();
        return medias;
    }

    public static LocalDateTime ultimaMayorista(){
        openConexion();
        LocalDateTime ultimo=BaseDatos.ultimaMayorista();
        closeConexion();
        return ultimo;
    }
}
