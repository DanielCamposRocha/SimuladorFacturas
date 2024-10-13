package com.example.simuladorfacturas.contratos;

import com.example.simuladorfacturas.AplicacionUsuarios;
import com.example.simuladorfacturas.controlador.Controlador;
import com.example.simuladorfacturas.estadisticas.Medias;
import com.example.simuladorfacturas.objetos.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class PVPC {
    private static ArrayList<CosteImpuestos> listacostes=new ArrayList<CosteImpuestos>();
    ArrayList<Potencia>listadoPotencias=new ArrayList<>();
    private static ArrayList<Precio> listadoPrecios = new ArrayList<>();
    private static ArrayList<Precio> listadoMayorista=new ArrayList<>();
    private static HashMap<LocalDateTime , Lectura> listadoLecturas= new HashMap<>();
    private static String identificador="es0022000004433403rw1p";
    private static BigDecimal total;
    private static BigDecimal clavada=new BigDecimal(0);




    public static void calcularFactura(LocalDateTime localDateTimeI, LocalDateTime localDateTimeF, double pot1, double pot2) {
        Usuario usuarioLogueado= AplicacionUsuarios.getUsuarioLogueado();
        double consumos = 0;
        double autoconsumos=0;
        int dias=0;
        int punta=0;
        int valle=0;
        int llano=0;
        double pc=0;
        double vc=0;
        double llc=0;

        Potencia preciosPotencia = null;
        Potencia preciosPotencia2=null;
        BigDecimal margen=new BigDecimal(0);
        BigDecimal costep1=new BigDecimal(0);
        BigDecimal costep2=new BigDecimal(0);
        BigDecimal pp=new BigDecimal(0);
        BigDecimal impuestoEl=new BigDecimal(0);
        BigDecimal iva=new BigDecimal(0);
        BigDecimal bono=new BigDecimal(0);
        BigDecimal alquiler=new BigDecimal(0);

        total=new BigDecimal(0.00);

        double bonosocial=2.299047*5.394483/366;

        if(usuarioLogueado==null){listacostes=Controlador.calcularCostes(localDateTimeI,localDateTimeF);}
        else listacostes= Controlador.calcularCostes(identificador, localDateTimeI,localDateTimeF);
        double IVA=CosteImpuestos.ivaAplicable(listacostes);
        double ImpE=CosteImpuestos.impuestoElectricoAplicable(listacostes);
        for (CosteImpuestos coste:listacostes ) {
            total=total.add(new BigDecimal(coste.getCoste()));
            switch (tramo(coste.getFecha())){
                case "punta" ->{
                    punta++;
                    pc+=coste.getConsumo();
                }
                case "valle" ->{
                    valle++;
                    vc+=coste.getConsumo();
                }
                case "llano" ->{
                    llano++;
                    llc+=coste.getConsumo();
                }
            }
            if(coste.getConsumo()>=0)consumos+=coste.getConsumo();
            if(coste.getConsumo()<0)autoconsumos+=coste.getConsumo();
        }
            //Caso de ser en el mismo año
        if(localDateTimeI.getYear()==localDateTimeF.getYear()){
            preciosPotencia= Controlador.anoPotencia(localDateTimeI.getYear());
            double peaje= preciosPotencia.getMargenp1();
            double peaje2= preciosPotencia.getMargenp2();
            double margenC=preciosPotencia.getCostefijo();
            double precioPo1=preciosPotencia.getP1()+peaje;//coste de la potencia en P1
            double precioPo2=preciosPotencia.getP2()+peaje2;//coste de la potencia en P2
            Duration duracion=Duration.between(localDateTimeI,localDateTimeF);
            dias=1+(int)duracion.toDays();
            double proporcionDias=dias/366.0;
            bono=new BigDecimal(dias*bonosocial).setScale(2, RoundingMode.HALF_DOWN);//coste bono social
            costep1=new BigDecimal(pot1).multiply(new BigDecimal(precioPo1)).multiply(new BigDecimal(proporcionDias)).
                    setScale(2, RoundingMode.HALF_DOWN);//total coste P1
            costep2=new BigDecimal(pot2).multiply(new BigDecimal(precioPo2)).multiply(new BigDecimal(proporcionDias)).
                    setScale(2, RoundingMode.HALF_DOWN);//total coste P2
            margen=new BigDecimal(pot1*margenC*dias/366).setScale(2, RoundingMode.HALF_DOWN);//margen potencia distribuidora
            pp=costep1.add(costep2).add(margen).setScale(2, RoundingMode.HALF_DOWN);//coste total de la potencia contratada
            alquiler=new BigDecimal(dias*0.026557).setScale(2, RoundingMode.HALF_DOWN);//alquiler contador
            impuestoEl=total.add(bono).add(pp).multiply(new BigDecimal(ImpE)).
                    setScale(2, RoundingMode.HALF_DOWN);//impuesto electrico
            iva=total.add(pp).add(bono).add(impuestoEl).add(alquiler).multiply(new BigDecimal(IVA)).
                    setScale(2, RoundingMode.HALF_DOWN);//iva, ojo con los cambios de valor
            clavada=total.add(pp).add(bono).add(impuestoEl).add(alquiler).add(iva).
                    setScale(2, RoundingMode.HALF_DOWN);//total de la factura

        }else{
            //distintos años
            ArrayList<Potencia> listadoPotencias=new ArrayList<>();
            int anhos=localDateTimeF.getYear()-localDateTimeI.getYear();
            double anhosPerdidos=0;
            //listado precios años intermedios
            for(int i=localDateTimeI.getYear()+1;i<localDateTimeI.getYear();i++) listadoPotencias.add(Controlador.anoPotencia(i));
            //costes de potencia en los años intermedios
            for (Potencia pot:listadoPotencias)  anhosPerdidos+=pot.getP1()+pot.getMargenp1()+pot.getP2()+pot.getMargenp2()+pot.getCostefijo();
            BigDecimal costep1F=new BigDecimal(0);
            BigDecimal costep2F=new BigDecimal(0);
            preciosPotencia=Controlador.anoPotencia(localDateTimeI.getYear());
            preciosPotencia2=Controlador.anoPotencia(localDateTimeF.getYear());
            Duration duracion=Duration.between(localDateTimeI,LocalDateTime.of(localDateTimeI.getYear(),12,31,23,59));
            Duration duracion2=Duration.between(LocalDateTime.of(localDateTimeF.getYear(),1,1,0,0),localDateTimeF);
            int diasI=(int)duracion.toDays();//desde la fecha inicial a fin de año
            int diasF=(int)duracion2.toDays();//desde inicio de año a la fecha final
            dias= (int) Duration.between(localDateTimeI,localDateTimeF).toDays();
            double proporcionDiasI=diasI/366.0;
            double proporcionDiasF=diasF/366.0;
            double peaje= preciosPotencia.getMargenp1();
            double peaje2= preciosPotencia.getMargenp2();
            double margenC=preciosPotencia.getCostefijo();
            double precioPo1=preciosPotencia.getP1()+peaje;
            double precioPo2=preciosPotencia.getP2()+peaje2;
            double precioPo1F=preciosPotencia2.getP1()+preciosPotencia2.getMargenp1();
            double precioPo2F=preciosPotencia2.getP2()+preciosPotencia2.getMargenp1();
            BigDecimal margenF=new BigDecimal(0);
            bono=new BigDecimal(dias*bonosocial).setScale(2, RoundingMode.HALF_DOWN);
            costep1=new BigDecimal(pot1).multiply(new BigDecimal(precioPo1)).multiply(new BigDecimal(proporcionDiasI)).setScale(2, RoundingMode.HALF_DOWN);//coste P1 año inicial
            costep2=new BigDecimal(pot2).multiply(new BigDecimal(precioPo2)).multiply(new BigDecimal(proporcionDiasI)).setScale(2, RoundingMode.HALF_DOWN);//coste P2 año inicial
            margen=new BigDecimal(pot1*margenC*diasI/366).setScale(2, RoundingMode.HALF_DOWN);
            costep1F=new BigDecimal(pot1).multiply(new BigDecimal(precioPo1F)).multiply(new BigDecimal(proporcionDiasF)).setScale(2, RoundingMode.HALF_DOWN);//coste P1 año final
            costep2F=new BigDecimal(pot2).multiply(new BigDecimal(precioPo2F)).multiply(new BigDecimal(proporcionDiasF)).setScale(2, RoundingMode.HALF_DOWN);//coste P2 año final
            margenF=new BigDecimal(pot1*preciosPotencia2.getCostefijo()*diasF/366).setScale(2, RoundingMode.HALF_DOWN);
            pp=costep1.add(costep2).add(margen).add(costep1F).add(costep2F).add(margenF).add(new BigDecimal(anhosPerdidos)).setScale(2, RoundingMode.HALF_DOWN);
            alquiler=new BigDecimal(dias*0.026557).setScale(2, RoundingMode.HALF_DOWN);
            impuestoEl=total.add(bono).add(pp).multiply(new BigDecimal("0.0511269632")).setScale(2, RoundingMode.HALF_DOWN);// TODO: 09/09/2024  
            iva=total.add(pp).add(bono).add(impuestoEl).add(alquiler).multiply(new BigDecimal(0.1)).setScale(2, RoundingMode.HALF_DOWN);// TODO: 09/09/2024  
            clavada=total.add(pp).add(bono).add(impuestoEl).add(alquiler).add(iva).setScale(2, RoundingMode.HALF_DOWN);
        }
        BigDecimal totalredondeado=total.setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal asd=new BigDecimal(0);
        if(consumos!=0.0)asd=totalredondeado.divide(new BigDecimal(consumos),4,RoundingMode.HALF_DOWN);

        System.out.println("Consumo de energia "+consumos+" KWH"+" Coste de la energia "+totalredondeado+" € precio promedio "+(asd));
        System.out.println( "coste de la potencia "+pp+" €");
        System.out.println("impuesto electrico "+impuestoEl);
        System.out.println("Vertido autoconsumo "+autoconsumos+" KWH");
        System.out.println("Bono Social "+bono);
        System.out.println("Alquiler del contador "+alquiler);
        System.out.println("El Iva aplicado es "+IVA+" lo que da un valor de "+iva);
        System.out.println("Final "+clavada+" €");
        System.out.println("El iva aplicado es "+IVA+" los dias son "+dias);
        System.out.println("las horas valle son "+valle+" consumen "+vc);
        System.out.println("las horas punta son "+punta+" consumen "+pc);
        System.out.println("las horas llano son "+llano+" consumen "+llc);
        Medias.promedioHoras(consumos,dias);
        Medias.promedioSemanales(consumos,dias);
    }

    private static String tramo(LocalDateTime fecha) {
        if (fecha.getDayOfWeek()== DayOfWeek.SATURDAY | fecha.getDayOfWeek()== DayOfWeek.SUNDAY |
                fecha.getHour()<8 | isFestivo(fecha)) return "valle";
        else if (fecha.getHour()>=8 & fecha.getHour()<10 |fecha.getHour()>=14 & fecha.getHour()<18 |
                fecha.getHour()>=22) return "llano";
        return "punta";

    }

    private static boolean isFestivo(LocalDateTime fecha) {
        boolean festivo=false;
        if(Controlador.festivos(fecha.getYear()).contains(fecha))festivo=true;
        return festivo;
    }

    public static ArrayList<CosteImpuestos> getListacostes() {
        return listacostes;
    }

    public static void setListadoPrecios(ArrayList<Precio> listadoPrecios) {
        PVPC.listadoPrecios = listadoPrecios;
    }

    public static HashMap<LocalDateTime, Lectura> getListadoLecturas() {
        return listadoLecturas;
    }

    public static void setListadoLecturas(HashMap<LocalDateTime, Lectura> listadoLecturas) {
        PVPC.listadoLecturas = listadoLecturas;
    }

    public static String getIdentificador() {
        return identificador;
    }

    public static void setIdentificador(String identificador) {
        PVPC.identificador = identificador;
    }

    public static BigDecimal getClavada() {
        return clavada;
    }
    public static ArrayList<Precio> getListadoPrecios() {
        return listadoPrecios;
    }
    public static void setListadoMayorista(ArrayList<Precio> listadoMayorista) {
        PVPC.listadoMayorista = listadoMayorista;
    }

    public static ArrayList<Precio> getListadoMayorista() {
        return listadoMayorista;
    }
}
