package com.example.simuladorfacturas.controlador;

import com.example.simuladorfacturas.basedatos.BaseDatos;
import com.example.simuladorfacturas.basedatos.Database;
import com.example.simuladorfacturas.contratos.PVPC;
import com.example.simuladorfacturas.objetos.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controlador {

    public static void openConexion(){BaseDatos.openConexion();}
    public static void closeConexion(){BaseDatos.closeConexion();}
    public static LocalDateTime ultimaActualizacionREE(){ return BaseDatos.fechaUltima(); }

    public static void insertPrecio(Precio precio){ BaseDatos.insertPrecio(precio);   }

    public static void insertPrecios(ArrayList<Precio> listado) {
        BaseDatos.insertPrecios(listado);
        BaseDatos.insertPreciosMayorista(PVPC.getListadoMayorista());
    }

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
    public static ArrayList<CosteImpuestos> calcularCostes(String tabla, LocalDateTime fecha_inicio, LocalDateTime fecha_final){
        ArrayList<Coste>listacostes=BaseDatos.calcularCostes(tabla,fecha_inicio,fecha_final);
        ArrayList<CosteImpuestos> listaConImpuestos=incluirImpuestos(listacostes);
        return listaConImpuestos;
    }

    public  static Potencia anoPotencia(int ano){   return BaseDatos.anoPotencia(ano);    }

    public static int crearUsuario(Usuario usuario){
        String password_hash=BCrypt.hashpw(usuario.getContrasenha(),BCrypt.gensalt());
        usuario.setContrasenha(password_hash);
        return BaseDatos.insertarUsuario(usuario);
    }

    public static Usuario obtenerUsuario(String nombre) { return BaseDatos.obtenerUsuario(nombre); }

    public static boolean comprobarContrasenha(Usuario usuario) {

        boolean acierto=false;
        String hashed=BaseDatos.recuperarHash(usuario.getNombre());
        if(hashed!=null){
            if(BCrypt.checkpw(usuario.getContrasenha(),hashed)){
                acierto= true;
            }
        }
        return acierto;
    }

    public static boolean cambiarContrasenha(Usuario usuarioLogueado) {
        String password_hash=BCrypt.hashpw(usuarioLogueado.getContrasenha(),BCrypt.gensalt());
        usuarioLogueado.setContrasenha(password_hash);
        boolean cambio= BaseDatos.cambiarContrasenha(usuarioLogueado);
        return cambio;
    }

    public static ArrayList<Double> mediaMayorista(){
        ArrayList<Double> medias=BaseDatos.mediaMayorista();
        return medias;
    }

    public static LocalDateTime ultimaMayorista(){
        LocalDateTime ultimo=BaseDatos.ultimaMayorista();
        return ultimo;
    }

    public static ArrayList<LocalDate> festivos(int anho) {
        return BaseDatos.listafestivos(anho);
    }

    public static ArrayList<CosteImpuestos> calcularCostes(LocalDateTime localDateTimeI, LocalDateTime localDateTimeF) {
        ArrayList<Precio>listaprecios=BaseDatos.listarPrecios(localDateTimeI,localDateTimeF);
        ArrayList<Coste>listacostes=new ArrayList<>();
        for (Precio precio :listaprecios) {
            if(PVPC.getListadoLecturas().containsKey(precio.getFecha())){
                double coste=precio.getPrecio()*PVPC.getListadoLecturas().get(precio.getFecha()).getConsumo()/1000;
                listacostes.add(new Coste(precio.getPrecio(), PVPC.getListadoLecturas().get(precio.getFecha()).getConsumo()
                        ,coste,precio.getFecha(), precio.isVerano()));
            }
        }
        ArrayList<CosteImpuestos> listaConImpuestos=incluirImpuestos(listacostes);
        return listaConImpuestos;
    }

    public static ArrayList<CosteImpuestos> incluirImpuestos(ArrayList<Coste> listaCostes){
        ArrayList<CosteImpuestos> listaConImpuestos=new ArrayList<>();
        for (Coste coste:listaCostes) {
            double iva=CosteImpuestos.calculoIva(coste.getFecha());
            double impEl=CosteImpuestos.impuestoElectricoCalculo(coste.getFecha());
            listaConImpuestos.add(new CosteImpuestos(coste,iva,impEl));
        }
        return listaConImpuestos;
    }

    public static JList<String> listaPuntos(String nombreUsuario) {
        HashMap<String,String>listaCups=BaseDatos.listaCups(nombreUsuario);
        String[] nombres=listaCups.keySet().toArray(new String[0]);
        return new JList<>(nombres);
    }

    public static String unicaCup(String nombreUsuario,String cup) {
        HashMap<String,String>listaCups=BaseDatos.listaCups(nombreUsuario);
        return listaCups.get(cup);
    }
}
