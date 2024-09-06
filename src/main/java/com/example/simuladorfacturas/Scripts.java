package com.example.simuladorfacturas;

import com.example.simuladorfacturas.contratos.PVPC;
import com.example.simuladorfacturas.controlador.Controlador;
import com.example.simuladorfacturas.parseos.Parseos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Scripts {


    public static void main(String[] args) {
//        Parseos.leerExcellPrecios("export_TerminoDeFacturacionDeEnergiaActivaDelPVPC20TD_2024-08-11_16_20.csv");
//        Parseos.leerCsvConsumos("consumptions (6).csv");
//        System.out.println(PVPC.getListadoLecturas());
//        Controlador.crearTablaLecturas( PVPC.getListadoLecturas());
//        lanzarScript(Controlador.ultimaActualizacionREE());
//        Controlador.insertPrecios(PVPC.getListadoPrecios());
//        PVPC.calcularFactura();
 //       Scripts.lanzarMayorista(Controlador.Inertarmayorista());

//        Controlador.Insertarmayorista(Parseos.leerPreciosMayorista("D:\\Proyectos Intellij\\SimuladorFacturas\\src\\main\\resources\\export_PrecioMedioHorarioFinalSumaDeComponentes_2024-09-04_10_20.csv"));
        System.out.println(Controlador.mediaMayorista());
        }

public static void lanzarMayorista(){

}


    public static void lanzarScript(LocalDateTime fechaInicial) {
        if(fechaInicial.isBefore(LocalDateTime.now())){
            try {
                //el ultimo dato guardado es de un dia a las 23 por eso se suma una hora para que salte al dia siguiente
                String inic = fechaInicial.plusHours(1).toString();
                LocalDateTime hoy= LocalDateTime.now().withHour(23).withMinute(59);//completamos el dia de hoy
                String fin = hoy.toString();
                String token = "2777dc229f57e51aa9cf5eeb98db7d43cccdfd625322ce31bc5ee54049a25bc8";

                // Crear el comando para ejecutar el script de Python
                List<String> command = new ArrayList<>();
                command.add("python"); // o "python3" igual hay que cambiarlo en otro equipo
                command.add("src/main/resources/consultaree.py");
                command.add(inic);
                command.add(fin);
                command.add(token);

                // Construir el proceso
                ProcessBuilder pb = new ProcessBuilder(command);
                pb.redirectErrorStream(true); // Redirigir el error estándar a la salida estándar
                Process process = pb.start();

                // Leer la salida del script de Python
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                // Esperar a que el proceso termine
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    //parsear la salida JSON aquí
                    String json=output.toString();
                    Parseos.parseaREE(json);
                } else {
                    System.err.println("Error al ejecutar el script de Python. Código de salida: " + exitCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("base de precios ya actualizada");
        }
    }




}

