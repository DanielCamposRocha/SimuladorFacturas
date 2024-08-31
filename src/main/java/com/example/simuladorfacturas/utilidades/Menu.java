package com.example.simuladorfacturas.utilidades;
import java.util.Scanner;

public class Menu {
    public enum Direccion { HORIZONTAL, VERTICAL }

    private final String title;
    private final String[] options;
    private final String permitidos;
    private final Direccion direccion;

    public Menu(String title, String[] options, String permitidos, Direccion direccion) {
        this.title=title;
        this.options=options;
        this.permitidos=permitidos;
        this.direccion=direccion;
    }

    public char getOption() {
        String opt;
        Scanner scn=new Scanner(System.in);
        do {
            System.out.print(title);
            next();
            for(int idx=0;idx<options.length;idx++) {
                System.out.print(options[idx]);
                next();
            }
            opt=scn.nextLine();
        } while (permitidos.indexOf(opt)<0);
        return opt.charAt(0);
    }

    private void next() {
        if (direccion==Direccion.HORIZONTAL) System.out.print(" ");
        else System.out.println();
    }


}
