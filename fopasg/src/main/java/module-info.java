module com.mycompany.fopasg {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.fopasg to javafx.fxml;
    exports com.mycompany.fopasg;
}
