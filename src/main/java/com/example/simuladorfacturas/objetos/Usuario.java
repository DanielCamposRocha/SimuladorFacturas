package com.example.simuladorfacturas.objetos;

import java.util.ArrayList;
import java.util.HashMap;

public class Usuario {
    private String nombre;
    private String contrasenha;
    private HashMap<String,String> CUPS;

    public Usuario(String nombre, String contrasenha) {
        this.nombre = nombre;
        this.contrasenha = contrasenha;
        this.CUPS = new HashMap<>();
    }

    public Usuario(String nombre, HashMap<String,String> CUPS) {
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

    public HashMap<String,String> getCUPS() {
        return CUPS;
    }

    public void setCUPS(HashMap<String,String> CUPS) {
        this.CUPS = CUPS;
    }
}
