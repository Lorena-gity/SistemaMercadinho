module SistemaMercado {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.github.librepdf.openpdf;

    opens br.edu.ufersa.sistemaMercado.view to javafx.fxml, javafx.graphics;
    exports br.edu.ufersa.sistemaMercado.view;
}