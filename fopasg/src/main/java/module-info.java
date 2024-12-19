module com.mycompany {
    //javafx
    requires javafx.controls;
    requires javafx.fxml;

    //gson
    requires com.google.gson;

    opens com.mycompany.frontend to javafx.fxml;
    opens com.mycompany.backend to com.google.gson, com.google.guava;

    exports com.mycompany.frontend;
    exports com.mycompany.backend;
}
