package com.example.simuladorfacturas.objetos;

import java.util.ArrayList;

public class Usuario {
    private String nombre;
    private String contrasenha;
    private ArrayList<String> CUPS;

    public Usuario(String nombre, String contrasenha) {
        this.nombre = nombre;
        this.contrasenha = contrasenha;
        this.CUPS = new ArrayList<>();
    }

    public Usuario(String nombre, ArrayList<String> CUPS) {
        this.nombre = nombre;
        this.CUPS = CUPS;
    }

    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public ArrayList<String> getCUPS() {
        return CUPS;
    }

    public void setCUPS(ArrayList<String> CUPS) {
        this.CUPS = CUPS;
    }
}
