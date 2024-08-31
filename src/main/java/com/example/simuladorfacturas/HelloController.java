package com.example.simuladorfacturas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HelloController {
    @FXML
    public Button cargaCSV;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    public void setCargaCSV(ActionEvent actionEvent) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Selecciona consumos .CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo csv","*.csv"));
        // Mostrar el diálogo y obtener el archivo seleccionado
        File selectedFile = fileChooser.showOpenDialog((Stage)((Node)actionEvent.getSource()).getScene().getWindow());

        // Verificar si se ha seleccionado un archivo
        if (selectedFile != null) {
            System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());
            // Aquí puedes realizar acciones con el archivo seleccionado
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }
}