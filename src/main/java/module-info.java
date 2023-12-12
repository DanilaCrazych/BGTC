module com.example.bgtc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;


    opens com.example.bgtc to javafx.fxml;
    exports com.example.bgtc;
}