module com.pp.s.printingpos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.pp.s.printingpos to javafx.fxml;
    exports com.pp.s.printingpos;
}