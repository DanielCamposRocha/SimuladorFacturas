package com.example.simuladorfacturas.validadores;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Validaciones {
    public static double validarDouble(String text)  {
        double valor;
        if(text.equals("")) return 0;
        try{
            if(text.contains(","))text=corregirDouble(text);
            valor=Double.parseDouble(text);
            if(valor<=0)throw new CantidadException("La cantidad introducida no es válida, debe ser un número decimal positivo");
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Cantidad no válida: recuerde debe ser valores numéricos");
            return -1;
        }
        catch (CantidadException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            return -1;}
        return valor;
    }

    private static String corregirDouble(String text) {
        String[] numeros=text.split(",");
        String corregido=numeros[0]+"."+numeros[1];
        return corregido;
    }

    public static boolean validarFechas(LocalDate fechaInicial, LocalDate fechaFinal){
        if(fechaInicial.isAfter(fechaFinal)){
            JOptionPane.showMessageDialog(null, "La fecha final debe ser posterior o igual a la inicial");
            return false;
        }
        return true;
    }
    private static class CantidadException extends Throwable {
        public CantidadException(String s) {super(s);}
    }

}
