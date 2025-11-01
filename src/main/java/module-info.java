module cz.vse.enga03_adventuraswi {
    requires javafx.controls;
    requires javafx.fxml;
    // javafx.web pro webview


    opens cz.vse.enga03_adventuraswi.main to javafx.fxml;
    exports cz.vse.enga03_adventuraswi.main;
}