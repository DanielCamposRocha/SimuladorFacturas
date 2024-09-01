module com.example.simuladorfacturas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jbcrypt;


    opens com.example.simuladorfacturas to javafx.fxml;
    exports com.example.simuladorfacturas;
    exports com.example.simuladorfacturas.front;
    opens com.example.simuladorfacturas.front to javafx.fxml;
    exports com.example.simuladorfacturas.estadisticas;
    opens com.example.simuladorfacturas.estadisticas to javafx.fxml;
}