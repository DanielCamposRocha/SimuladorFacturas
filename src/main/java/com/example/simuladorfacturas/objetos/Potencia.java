package com.example.simuladorfacturas.objetos;

public class Potencia {
    private int ano;
    private double p1;
    private double p2;
    private double margenp1;
    private double margenp2;
    private double costefijo;

    public Potencia(int ano, double p1, double p2, double margenp1, double margenp2, double costefijo) {
        this.ano = ano;
        this.p1 = p1;
        this.p2 = p2;
        this.margenp1 = margenp1;
        this.margenp2 = margenp2;
        this.costefijo = costefijo;
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

    public double getMargenp1() {
        return margenp1;
    }

    public void setMargenp1(double margenp1) {
        this.margenp1 = margenp1;
    }

    public double getMargenp2() {
        return margenp2;
    }

    public void setMargenp2(double margenp2) {
        this.margenp2 = margenp2;
    }

    public double getCostefijo() {
        return costefijo;
    }

    public void setCostefijo(double costefijo) {
        this.costefijo = costefijo;
    }
}
