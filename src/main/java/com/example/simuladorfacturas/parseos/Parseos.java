package com.example.simuladorfacturas.parseos;

import com.example.simuladorfacturas.contratos.PVPC;
import com.example.simuladorfacturas.objetos.Lectura;
import com.example.simuladorfacturas.objetos.Precio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Parseos {
    public static void parseaREE(String recibido) {
        String[] primer=recibido.split("values");
        String[] segund=primer[1].split("]");
        String[] tercer=segund[0].split("}");
        ArrayList<String> listado=new ArrayList<>();
        listado.add(tercer[0].substring(7));
        for(int i=1;i< tercer.length;i++) listado.add(tercer[i].substring(4));
        ArrayList<Precio>listadoP= PVPC.getListadoPrecios();

        for (String objeto:listado) {
            boolean verano;
            double precioHora;
            String[] param=objeto.split(",");
            String[] precios=param[0].split(":");
            precioHora= Double.parseDouble((precios[1].substring(1)));
            String[] fechaHora=param[2].split(":");
            String[] fechai=fechaHora[1].substring(2).split("T");
            String fecha=fechai[0];
            String[] pasar=fecha.split("-");
            String hora=fechai[1];
            LocalDateTime ahora=LocalDateTime.of(Integer.parseInt(pasar[0]),Integer.parseInt(pasar[1]),Integer.parseInt(pasar[2]),Integer.parseInt(hora),0);
            if(Integer.parseInt(fechaHora[3].substring(8))==1){ verano=false; }else verano=true;
            System.out.println(ahora+" tiene un precio de "+ precioHora+" "+fecha+" "+hora+" "+verano);
            listadoP.add(new Precio(ahora,precioHora,verano));
        }
        PVPC.setListadoPrecios(listadoP);
    }

    public static void leerExcellPrecios(String archivo){
        ArrayList<Precio>listadoPrecios=PVPC.getListadoPrecios();
        try (BufferedReader br= new BufferedReader(new FileReader(archivo))){
            boolean verano= false;
            String linea;
            int contadorlineas=0;
            while ((linea = br.readLine()) != null) {
                String[] valores=linea.split(";");
                if (contadorlineas !=0){
                    if(Integer.parseInt(valores[2])==8741){//asi solo procesa el sistema peninsular 8741 es su codigo
                        double precio = Double.parseDouble(valores[4]);
                        String fecha = valores[5];
                        LocalDateTime fechL = pasarLectura(fecha);
                        if (Integer.parseInt(fecha.substring(21,22)) == 1) {verano = false; } else verano = true;
                        listadoPrecios.add(new Precio(fechL, precio, verano));
                    }
                }
                contadorlineas++;
            }
            PVPC.setListadoPrecios(listadoPrecios);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
            e.printStackTrace();
        }
        System.out.println("Archivo leido correctamente");
    }

    public static void leerCsvConsumos(String archivo){
        try (BufferedReader br= new BufferedReader(new FileReader(archivo))){
            PVPC.setListadoLecturas(new HashMap<>());
            boolean real= true;
            String linea;
            int contadorlineas=0;
            while ((linea = br.readLine()) != null) {
                String[] valores=linea.split(";");
                if (contadorlineas !=0){
                    if(contadorlineas==1) PVPC.setIdentificador(valores[0]);
                    String CUPS=valores[0];
                    double consumo = Double.parseDouble(valores[3].replace(",","."));
                    if(!valores[4].equals("R"))real=false;
                    String[] partesfecha=valores[1].split("/");
                    LocalDateTime fecha = LocalDateTime.of(Integer.parseInt(partesfecha[2]),Integer.parseInt(partesfecha[1]),
                            Integer.parseInt(partesfecha[0]),Integer.parseInt(valores[2])-1,0);
                    agregarlectura(new Lectura(CUPS, fecha, consumo, real));
                }
                contadorlineas++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
            e.printStackTrace();
        }
        System.out.println("Archivo leido correctamente");
    }

    public static ArrayList<Precio> leerPreciosMayorista(String archivo) {
        ArrayList<Precio> listadoPreciosMayorista=new ArrayList<>();
        try (BufferedReader br= new BufferedReader(new FileReader(archivo))){
            boolean verano= false;
            String linea;
            int contadorlineas=0;
            while ((linea = br.readLine()) != null) {
                String[] valores=linea.split(";");
                if (contadorlineas !=0){
                    if(Integer.parseInt(valores[0])==805){//asi solo procesa el sistema peninsular 8741 es su codigo
                        double precio = Double.parseDouble(valores[4]);
                        String fecha = valores[5];
                        LocalDateTime fechL = pasarLectura(fecha);
                        if (Integer.parseInt(fecha.substring(21,22)) == 1) {verano = false; } else verano = true;
                        listadoPreciosMayorista.add(new Precio(fechL, precio, verano));
                    }
                }
                contadorlineas++;
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
            e.printStackTrace();
        }
        System.out.println("Archivo leido correctamente");
        return listadoPreciosMayorista;
    }
    private static void agregarlectura(Lectura lectura) {
         HashMap<LocalDateTime , Lectura> listadoLecturas= PVPC.getListadoLecturas();
        if (listadoLecturas.containsKey(lectura.getFecha())) {
            lectura.setVerano(false);
        }
        listadoLecturas.put(lectura.getFecha(),lectura);
        PVPC.setListadoLecturas(listadoLecturas);
    }

    public static LocalDateTime pasarLectura(String fecha){
        String[] pasar=fecha.split("-");
        String[] dia=pasar[2].split("T");
        String[] horas=dia[1].split(":");
        LocalDateTime ahora=LocalDateTime.of(Integer.parseInt(pasar[0]),Integer.parseInt(pasar[1]),Integer.parseInt(dia[0]),Integer.parseInt(horas[0]),0);
        return ahora;
    }
}
