package com.example.simuladorfacturas.objetos;


import com.example.simuladorfacturas.CambioDeHora;
import java.time.LocalDateTime;

public class Lectura {
    private String CUPS;
    private LocalDateTime fecha;
    private  double consumo;
    private boolean metodos;
    private boolean verano;

    public Lectura(String CUPS, LocalDateTime fecha, double consumo, boolean real) {
        this.CUPS = CUPS;
        this.fecha = fecha;
        this.consumo = consumo;
        this.metodos = real;
        this.verano=horario(fecha);
    }
    public Lectura(String CUPS, LocalDateTime fecha, double consumo, boolean real,boolean verano) {
        this.CUPS = CUPS;
        this.fecha = fecha;
        this.consumo = consumo;
        this.metodos = real;
        this.verano=verano;
    }
    private boolean horario(LocalDateTime fecha) {
        if(fecha.isAfter(CambioDeHora.INVIERNO_2021.getFecha()) & fecha.isBefore(CambioDeHora.VERANO_2022.getFecha()))return false;
        if(fecha.isAfter(CambioDeHora.INVIERNO_2022.getFecha()) & fecha.isBefore(CambioDeHora.VERANO_2023.getFecha()))return false;
        if(fecha.isAfter(CambioDeHora.INVIERNO_2023.getFecha()) & fecha.isBefore(CambioDeHora.VERANO_2024.getFecha()))return false;
        if(fecha.isAfter(CambioDeHora.INVIERNO_2024.getFecha()) & fecha.isBefore(CambioDeHora.VERANO_2025.getFecha()))return false;
        if(fecha.isAfter(CambioDeHora.INVIERNO_2025.getFecha()) & fecha.isBefore(CambioDeHora.VERANO_2026.getFecha()))return false;
        else return true;
    }

    public String getCUPS() {
        return CUPS;
    }


    public LocalDateTime getFecha() {
        return fecha;
    }


    public double getConsumo() {
        return consumo;
    }


    public boolean isMetodos() {
        return metodos;
    }

    @Override
    public String toString() {
        String metodo;
        if (isMetodos())metodo= "real";
        else metodo= "estimada";
        return "CUPS " + CUPS +" en la hora " + fecha +" consume " + consumo +" KW/h, el metodo de lectura es "+metodo ;
    }

    public boolean isVerano() {return verano; }

    public void setVerano(boolean verano) {
        this.verano = verano;
    }
}
