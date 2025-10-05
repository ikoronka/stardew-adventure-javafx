module cz.vse.enga03_adventuraswi {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.vse.enga03_adventuraswi to javafx.fxml;
    exports cz.vse.enga03_adventuraswi;
}