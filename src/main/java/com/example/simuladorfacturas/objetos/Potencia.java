package com.example.simuladorfacturas.objetos;

public class Potencia {
    private int ano;
    private double p1;
    private double p2;

    public Potencia(int ano, double p1, double p2) {
        this.ano = ano;
        this.p1 = p1;
        this.p2 = p2;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getP1() {
        return p1;
    }

    public void setP1(double p1) {
        this.p1 = p1;
    }

    public double getP2() {
        return p2;
    }

    public void setP2(double p2) {
        this.p2 = p2;
    }
}
