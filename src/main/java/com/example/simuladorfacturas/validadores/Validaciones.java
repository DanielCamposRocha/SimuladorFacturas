package com.example.simuladorfacturas.validadores;

import com.example.simuladorfacturas.excepciones.CantidadException;
import com.example.simuladorfacturas.excepciones.EdadFormatException;
import com.example.simuladorfacturas.excepciones.NombreFormatException;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static int validarEdad(String text) {
        int edad=-1;
        try {
            int idade = Integer.parseInt(text.trim());
            if(idade<0 | idade>125)throw new EdadFormatException("La edad introducida no es valida recuerde introduzca un numero entre 0 y 125") ;
            else edad=idade;
        } catch (EdadFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Edad no Válida: recuerde debe ser valores numericos");
        }
        return edad;
    }

    public static String validarNombre(String text) {
        String nombre="";
        try{
            if(!text.trim().isEmpty() && text.length()<50)nombre=text;
            else throw new NombreFormatException("El nombre no puede estar vacio ni pasar de 50 caracteres");
        }catch (NombreFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return nombre;
    }

    public static boolean emailIsValid(String text) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches())JOptionPane.showMessageDialog(null, "email no válido");
        return matcher.matches();

    }


    public static boolean contraIsValid(String contra) {
        if(contra.trim().length()==0){
            JOptionPane.showMessageDialog(null,"La contraseña no puede estar vacia o ser solo espacios");
            return false;
        }
        return true;
    }
}
