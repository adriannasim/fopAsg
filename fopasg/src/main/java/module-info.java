module com.mycompany.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.frontend to javafx.fxml;
    opens com.mycompany.frontend.helper to javafx.fxml;
    opens com.mycompany.frontend.exportOptions to javafx.fxml;
    exports com.mycompany.frontend;
}
