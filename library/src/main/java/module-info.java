module com.library {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires transitive java.sql;
    
    opens com.library to javafx.fxml;
    exports com.library;
}
