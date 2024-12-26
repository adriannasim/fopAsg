module com.mycompany {
    //javafx
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
    requires transitive javafx.graphics;


    //gson
    requires com.google.gson;

    opens com.mycompany.frontend to javafx.fxml;
    opens com.mycompany.frontend.helper to javafx.fxml;
    opens com.mycompany.frontend.exportOptions to javafx.fxml;
    opens com.mycompany.backend to com.google.gson, com.google.guava;

    exports com.mycompany.frontend;
    exports com.mycompany.backend;
}
