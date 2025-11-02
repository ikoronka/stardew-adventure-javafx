package cz.vse.enga03_adventuraswi.main;

import cz.vse.enga03_adventuraswi.logika.Prostor;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class ListCellProstor extends ListCell<Prostor> {

    private BorderPane borderPane = new BorderPane();
    private ImageView imageView = new ImageView();
    private Label label = new Label();

    public ListCellProstor() {
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        BorderPane.setAlignment(imageView, Pos.CENTER_LEFT);

        BorderPane.setAlignment(label, Pos.CENTER_RIGHT);
        label.setStyle("-fx-padding: 0 10 0 10;");

        borderPane.setLeft(imageView);
        borderPane.setRight(label);
    }

    @Override
    protected void updateItem(Prostor prostor, boolean empty) {
        super.updateItem(prostor, empty);

        if (empty || prostor == null) {
            setText(null);
            setGraphic(null);
        } else {
            label.setText(prostor.getNazev());

            try {
                String cesta = getClass().getResource("prostory/" + prostor.getNazev() + ".png").toExternalForm();
                Image img = new Image(cesta);
                imageView.setImage(img);
            } catch (Exception e) {
                imageView.setImage(null);
                System.err.println("Could not load image for: " + prostor.getNazev());
            }

            setGraphic(borderPane);
            setText(null);
        }
    }
}