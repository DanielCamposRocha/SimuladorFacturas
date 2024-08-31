package com.example.simuladorfacturas.objetos;
import java.time.LocalDateTime;

public class Precio {
    private LocalDateTime fecha;
    private double precio;
    private boolean verano;

    public Precio(LocalDateTime fecha, double precio, boolean verano) {
        this.fecha = fecha;
        this.precio = precio;
        this.verano = verano;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean isVerano() {
        return verano;
    }
}

