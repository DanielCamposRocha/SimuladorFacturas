package com.example.simuladorfacturas.objetos;

import com.example.simuladorfacturas.controlador.Controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CosteImpuestos extends Coste{
    private double iva;
    private double impEl;
    public CosteImpuestos(double precio, double consumo, double coste, LocalDateTime fecha, boolean verano) {
        super(precio, consumo, coste, fecha, verano);
    }

    public CosteImpuestos(double precio, double consumo, double coste, LocalDateTime fecha, boolean verano, double iva, double impEl) {
        super(precio, consumo, coste, fecha, verano);
        this.iva = iva;
        this.impEl = impEl;
    }

    public CosteImpuestos(Coste coste, double iva, double impEl) {
        super(coste.getPrecio(),coste.getConsumo(),coste.getCoste(),coste.getFecha(),coste.isVerano());
        this.iva = iva;
        this.impEl = impEl;
    }

    public static double calculoImp(LocalDateTime fecha) {
        return 0;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getImpEl() {
        return impEl;
    }

    public void setImpEl(double impEl) {
        this.impEl = impEl;
    }
    public static double calculoIva(LocalDateTime fecha){
        if (fecha.isBefore(LocalDateTime.of(2022,06,01,0,0))| fecha.isAfter(LocalDateTime.of(2025,01,01,0,0)))return 0.21;
        else if (fecha.isBefore(LocalDateTime.of(2022,9,01,0,0))) return 0.1;
        else if (fecha.isBefore(LocalDateTime.of(2024,01,01,0,0)))return 0.05;
        ArrayList<Double> listaMayorista= Controlador.mediaMayorista();
        double precioMedio=0.0;
        switch (fecha.getMonth()){
            case JANUARY ->precioMedio=listaMayorista.get(0);
            case FEBRUARY -> precioMedio=listaMayorista.get(1);
            case MARCH -> precioMedio=listaMayorista.get(2);
            case APRIL -> precioMedio=listaMayorista.get(3);
            case MAY -> precioMedio=listaMayorista.get(4);
            case JUNE -> precioMedio=listaMayorista.get(5);
            case JULY -> precioMedio=listaMayorista.get(6);
            case AUGUST -> precioMedio=listaMayorista.get(7);
            case SEPTEMBER -> precioMedio=listaMayorista.get(8);
            case OCTOBER -> precioMedio=listaMayorista.get(9);
            case NOVEMBER -> precioMedio=listaMayorista.get(10);
            case DECEMBER -> precioMedio=listaMayorista.get(11);
        }
        if(precioMedio>45)return 0.1;
        else return 0.21;

    }
}
