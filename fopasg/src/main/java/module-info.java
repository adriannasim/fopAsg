module com.mycompany.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires org.fxmisc.richtext;
    requires com.gluonhq.richtextarea;
    requires com.gluonhq.emoji;
    requires org.kordamp.ikonli.lineawesome;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires java.logging;


    opens com.mycompany.frontend to javafx.fxml;
    opens com.mycompany.frontend.helper to javafx.fxml;
    opens com.mycompany.frontend.exportOptions to javafx.fxml;
    exports com.mycompany.frontend;
}
