package com.example.simuladorfacturas.objetos;

import java.time.LocalDateTime;

public class Coste {
    private double precio;
    private double consumo;
    private double coste;
    private LocalDateTime fecha;
    private boolean verano;

    public Coste(double precio, double consumo, double coste, LocalDateTime fecha, boolean verano) {
        this.precio = precio;
        this.consumo = consumo;
        this.coste = coste;
        this.fecha = fecha;
        this.verano = verano;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getConsumo() {
        return consumo;
    }

    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isVerano() {
        return verano;
    }

    public void setVerano(boolean verano) {
        this.verano = verano;
    }

    @Override
    public String toString() {
        return  "precio = " + precio +", consumo = " + consumo +", coste = " + coste +", fecha = " + fecha +", verano = " + verano ;
    }
}
