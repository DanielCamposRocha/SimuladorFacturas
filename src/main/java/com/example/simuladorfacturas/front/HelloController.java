package com.example.simuladorfacturas.front;

import com.example.simuladorfacturas.AplicacionUsuarios;
import com.example.simuladorfacturas.Scripts;
import com.example.simuladorfacturas.contratos.PVPC;
import com.example.simuladorfacturas.controlador.Controlador;
import com.example.simuladorfacturas.validadores.Validaciones;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HelloController {
    @FXML
    public Button cargaCSV;
    @FXML
    public DatePicker fechaFinal;
    @FXML
    public DatePicker fechaInicial;
    public TextField p1;
    public TextField p2;
    private LocalDate fechafinalSeleccionada;
    private LocalDate fechaInicialSeleccionada;
    private File selectedFile;




    @FXML
    protected void onHelloButtonClick() {
        //if(selectedFile!=null) Parseos.leerCsvConsumos(selectedFile.getAbsolutePath());
        if (Validaciones.validarFechas(fechaInicialSeleccionada,fechafinalSeleccionada)){

            LocalDateTime fechaI=LocalDateTime.of(fechaInicialSeleccionada,LocalTime.of(0,0,0));
            LocalDateTime fechaf=LocalDateTime.of(fechafinalSeleccionada, LocalTime.of(23,0,0));
            double P1= Validaciones.validarDouble(p1.getText());
            double P2=Validaciones.validarDouble(p2.getText());
            if(P1!=-1 & P2!=-1) PVPC.calcularFactura(fechaI,fechaf,P1,P2);

        }
    }


    public void setCargaCSV(ActionEvent actionEvent) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Selecciona consumos .CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo csv","*.csv"));
        // Mostrar el diálogo y obtener el archivo seleccionado
        selectedFile = fileChooser.showOpenDialog((Stage)((Node)actionEvent.getSource()).getScene().getWindow());

        // Verificar si se ha seleccionado un archivo
        if (selectedFile != null) {
            System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());
            // Aquí puedes realizar acciones con el archivo seleccionado
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }

    public void loguear(ActionEvent actionEvent) {
        AplicacionUsuarios app=new AplicacionUsuarios();
        app.ejecutar();
    }

    public void setFechaFinal(ActionEvent actionEvent) {
        fechafinalSeleccionada= fechaFinal.getValue();
    }

    public void setFechaInicial(ActionEvent actionEvent) {
        fechaInicialSeleccionada= fechaInicial.getValue();
    }
}