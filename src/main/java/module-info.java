module com.example.simuladorfacturas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.simuladorfacturas to javafx.fxml;
    exports com.example.simuladorfacturas;
}