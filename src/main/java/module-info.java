module com.mycompany.undertale_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;

    opens com.mycompany.undertale_3.controller to javafx.fxml;
    exports com.mycompany.undertale_3;
    exports com.mycompany.undertale_3.model;
}
