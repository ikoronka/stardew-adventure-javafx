package cz.vse.enga03_adventuraswi.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HomeController {
    @FXML
    private TextArea vystup;
    @FXML
    private TextField vstup;

    @FXML
    private void odesliVstup(ActionEvent actionEvent) {
        vystup.appendText(vstup.getText()+"\n");
        // delete after send
        vstup.clear();
    }
}
