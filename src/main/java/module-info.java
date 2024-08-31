module com.example.simuladorfacturas {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.simuladorfacturas to javafx.fxml;
    exports com.example.simuladorfacturas;
}