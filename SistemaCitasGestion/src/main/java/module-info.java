module com.tuempresa.sistemacitasgestion {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tuempresa.sistemacitasgestion to javafx.fxml;
    exports com.tuempresa.sistemacitasgestion;
}