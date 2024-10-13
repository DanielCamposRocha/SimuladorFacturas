package com.example.simuladorfacturas.front;

import com.example.simuladorfacturas.AplicacionUsuarios;
import com.example.simuladorfacturas.Scripts;
import com.example.simuladorfacturas.contratos.PVPC;
import com.example.simuladorfacturas.controlador.Controlador;
import com.example.simuladorfacturas.objetos.Usuario;
import com.example.simuladorfacturas.parseos.Parseos;
import com.example.simuladorfacturas.validadores.Validaciones;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

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
    private ArrayList<String> lista;

    @FXML
    public Label usuario;
    @FXML
    public ComboBox<String> comboox;
    private String nombreUsuario;

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
            Parseos.leerCsvConsumos(selectedFile.getAbsolutePath());
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }


    public void loguear(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        AplicacionUsuarios app=new AplicacionUsuarios(stage,this);
        app.ejecutar();
        stage.setOpacity(0);

    }

    public void setFechaFinal(ActionEvent actionEvent) {
        fechafinalSeleccionada= fechaFinal.getValue();
    }

    public void setFechaInicial(ActionEvent actionEvent) {
        fechaInicialSeleccionada= fechaInicial.getValue();
    }
    public void usuarioLogueado(String nombreUsuario, String cup, JList<String> jlista){
        this.nombreUsuario=nombreUsuario;
        usuario.setText("Usuario: " + nombreUsuario+" Cup seleccionado: "+cup);
        lista=new ArrayList<>();
        cargaCSV.setVisible(false);
        String cups= Controlador.unicaCup(nombreUsuario,cup);
        PVPC.setIdentificador(cups);
        for (int i = 0; i < jlista.getModel().getSize(); i++) {
            lista.add(jlista.getModel().getElementAt(i));
        }
        comboox.setDisable(false);
        if (comboox != null) {
            comboox.setItems(FXCollections.observableArrayList(lista));
        } else {
            // Si comboox no está creado, lo inicializamos con la lista y lo hacemos visible
            comboox = new ComboBox<>(FXCollections.observableArrayList(lista));
         }
        comboox.setVisible(true);
    }


    public void despliegaCombo(ActionEvent actionEvent) {
        String seleccion = comboox.getSelectionModel().getSelectedItem();
        String textolabel=usuario.getText().split("ado: ")[0];
        usuario.setText(textolabel+"ado: "+seleccion);
        String cups= Controlador.unicaCup(nombreUsuario,seleccion);
        PVPC.setIdentificador(cups);
    }
}