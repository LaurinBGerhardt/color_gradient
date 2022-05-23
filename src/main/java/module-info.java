module com.example.farbverlaeufe {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.farbverlaeufe to javafx.fxml;
    exports com.example.farbverlaeufe;
}