package com.example.simuladorfacturas.utilidades;

import java.util.Scanner;

public class Utilidades {
    public static String pedirString(String textoimpreso){
        Scanner scn=new Scanner(System.in);
        System.out.println(textoimpreso);
        return scn.nextLine();
    }

    public static int pedirInt(String textoimpreso) {

        int comprobacion;
        int lectura=0;

        do{
            comprobacion=0;
            System.out.println(textoimpreso);
            try{
                Scanner scn=new Scanner(System.in);
                lectura=scn.nextInt();
            }catch (RuntimeException e){
                System.out.println("Recuerde por favor, debe introducir un numero entero");
                comprobacion=1;
            }
        }while(comprobacion==1);
        return lectura;
    }

    public static double pedirDouble(String textoimpreso) {
        int comprobacion;
        double lectura=0.0;

        do{
            comprobacion=0;
            System.out.println(textoimpreso);
            try{
                Scanner scn=new Scanner(System.in);
                lectura=scn.nextDouble();
            }catch (RuntimeException e){
                System.out.println("Recuerde por favor, debe introducir un numero decimal");
                comprobacion=1;
            }
        }while(comprobacion==1);
        return lectura;
    }
}
