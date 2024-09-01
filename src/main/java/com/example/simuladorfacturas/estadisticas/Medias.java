package com.example.simuladorfacturas.estadisticas;



import com.example.simuladorfacturas.contratos.PVPC;
import com.example.simuladorfacturas.objetos.Coste;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Medias {
    static DecimalFormat formato= new DecimalFormat("##.##");
    public static void promedioHoras(double consumos, int dias){
        ArrayList<Coste> cortesLuz=new ArrayList<>();
        Coste costePico = new Coste(0.0,0.0,0.0, LocalDateTime.now(),true);
        Coste consumoMaximo = new Coste(0.0,0.0,0.0,LocalDateTime.now(),true);
        Coste costeMinimo = new Coste(0.0,0.0,1000.0,LocalDateTime.now(),true);
        Coste consumMinimo = new Coste(0.0,1000.0,0.0,LocalDateTime.now(),true);
        System.out.println("------------------------------");
        for (int i=0;i<24;i++){
            double consumoPromedio=0;
            double precioPromedio=0;
            double consumos1=0;
            double precio=0;
            double gasto=0;
            double gastopromedio=0;
            int contador=0;
            for (Coste coste: PVPC.getListacostes()) {

                if(i==0){
                    if(coste.getCoste()>costePico.getCoste())costePico=coste;
                    if(coste.getConsumo()>consumoMaximo.getConsumo())consumoMaximo=coste;
                    if(coste.getCoste()<costeMinimo.getCoste()& coste.getCoste()>0)costeMinimo=coste;
                    if(coste.getConsumo()<consumMinimo.getConsumo()& coste.getConsumo()>0)consumMinimo=coste;
                    if(coste.getConsumo()==0)cortesLuz.add(coste);
                }
                if(coste.getFecha().getHour()==i){
                    consumos1+=coste.getConsumo();
                    precio+=coste.getPrecio();
                    gasto+=coste.getCoste();
                    contador++;
                }
            }
            consumoPromedio=consumos1/contador;
            precioPromedio=precio/contador;
            gastopromedio=gasto/contador;
            System.out.println("para la hora "+i+" el consumo promedio es "+formato.format(consumoPromedio)+" precio promedio "+formato.format(precioPromedio/1000)+" €/KWH " +
                    "el gasto promedio es "+formato.format(gastopromedio));
        }
        if(dias!=0)System.out.println("El consumo promedio es "+formato.format(consumos/dias)+" KWH al dia o "+consumos/(dias*24)+" KWH a la hora");
        double finalt=Double.valueOf(String.valueOf(PVPC.getClavada()));
        if(finalt!=0)System.out.println("El gasto total promedio es "+formato.format(finalt/dias)+" € al dia o "+formato.format(finalt/(dias*24))+" € a la hora");
        System.out.println("El maximo coste "+costePico);
        System.out.println("El minimo coste "+costeMinimo);
        System.out.println("El maximo consumo "+consumoMaximo);
        System.out.println("El minimo consumo "+consumMinimo);
        System.out.println("Horas sin suministro "+cortesLuz.size());
    }

    public static void promedioSemanales(double consumos, int dias){
        ArrayList<Coste> lunes=new ArrayList<>();
        ArrayList<Coste> martes=new ArrayList<>();
        ArrayList<Coste> miercoles=new ArrayList<>();
        ArrayList<Coste> jueves=new ArrayList<>();
        ArrayList<Coste> viernes=new ArrayList<>();
        ArrayList<Coste> sabado=new ArrayList<>();
        ArrayList<Coste> domingo=new ArrayList<>();
        System.out.println("------------------------------");


        for (Coste coste: PVPC.getListacostes()) {
            switch (coste.getFecha().getDayOfWeek()){
                case MONDAY -> lunes.add(coste);
                case TUESDAY -> martes.add(coste);
                case WEDNESDAY -> miercoles.add(coste);
                case THURSDAY -> jueves.add(coste);
                case FRIDAY -> viernes.add(coste);
                case SATURDAY -> sabado.add(coste);
                case SUNDAY -> domingo.add(coste);
            }
        }

        diaDeLaSemana(lunes,"lunes");
        diaDeLaSemana(martes,"martes");
        diaDeLaSemana(miercoles,"miercoles");
        diaDeLaSemana(jueves,"jueves");
        diaDeLaSemana(viernes,"viernes");
        diaDeLaSemana(sabado,"sabado");
        diaDeLaSemana(domingo,"domingo");

    }

    private static void diaDeLaSemana(ArrayList<Coste> lunes,String dia) {
        double consumoPromedio=0;
        double precioPromedio=0;
        double consumos1=0;
        double precio=0;
        double gasto=0;
        double gastopromedio=0;
        for (Coste coste:lunes) {
            consumos1+=coste.getConsumo();
            precio+=coste.getPrecio();
            gasto+=coste.getCoste();
        }
        if(lunes.size()>0){
            consumoPromedio=consumos1/(lunes.size()/24);
            precioPromedio=precio/(lunes.size());
            gastopromedio=(gasto*24)/(lunes.size());
            System.out.println("Datos del "+dia);
            System.out.println("El consumo promedio es "+formato.format(consumoPromedio)+" KWH al dia o "+formato.format(consumoPromedio/24)+" KWH a la hora");
            System.out.println("El precio promedio es "+formato.format(precioPromedio/1000)+" €/KWH a la hora");
            System.out.println("El gasto promedio es "+formato.format(gastopromedio)+" € al dia");
        }
    }
}
