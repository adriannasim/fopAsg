module com.mycompany.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.frontend to javafx.fxml;
    exports com.mycompany.frontend;
}
