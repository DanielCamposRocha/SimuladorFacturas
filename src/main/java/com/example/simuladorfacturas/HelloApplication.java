package com.example.simuladorfacturas;

import com.example.simuladorfacturas.contratos.PVPC;
import com.example.simuladorfacturas.controlador.Controlador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Controlador.openConexion();
        Scripts.lanzarScript(Controlador.ultimaActualizacionREE());
        if(PVPC.getListadoPrecios().size()>1)Controlador.insertPrecios(PVPC.getListadoPrecios());
        Scene scene = new Scene(fxmlLoader.load(), 450, 340);
        stage.setTitle("Simulador de Factura");
        stage.setScene(scene);
        stage.show();
        Controlador.closeConexion();
    }

    public static void main(String[] args) {
        launch();
    }
}