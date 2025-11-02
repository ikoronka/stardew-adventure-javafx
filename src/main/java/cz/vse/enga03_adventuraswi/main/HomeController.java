package cz.vse.enga03_adventuraswi.main;

import cz.vse.enga03_adventuraswi.logika.Hra;
import cz.vse.enga03_adventuraswi.logika.IHra;
import cz.vse.enga03_adventuraswi.logika.Prostor;
import cz.vse.enga03_adventuraswi.logika.prikazy.PrikazJdi;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HomeController {
    @FXML
    private ImageView hrac;
    @FXML
    private ImageView farma;
    @FXML
    private ImageView louka;
    @FXML
    private ImageView namesti;
    @FXML
    private ImageView obchod;
    @FXML
    private ImageView joja;
    @FXML
    private ImageView vez;
    @FXML
    private ListView<Prostor> panelVychodu;
    @FXML
    private Button tlacitkoOdesli;
    @FXML
    private TextArea vystup;
    @FXML
    private TextField vstup;
    @FXML
    private RadioMenuItem lightModeMenuItem;
    @FXML
    private RadioMenuItem darkModeMenuItem;
    @FXML
    private ImageView mapImageView;

    private IHra hra = new Hra();

    private ObservableList<Prostor> seznamVychodu = FXCollections.observableArrayList();

    // seznam souradnic jednotlivych prostoru
    private Map<String, Point2D> souradniceProstoru = new HashMap<>();

    @FXML
    private void initialize() {
        vystup.appendText(hra.vratUvitani() + "\n\n");
        Platform.runLater(() -> vstup.requestFocus());
        panelVychodu.setItems(seznamVychodu);
        hra.getHerniPlan().registruj(ZmenaHry.ZMENA_MISTNOSTI, () -> {
            aktualizujSeznamVychodu();
            aktualizujPolohuHrace();
        });
        hra.registruj(ZmenaHry.KONEC_HRY, () -> aktualizujKonecHry());

        aktualizujSeznamVychodu();
        vlozSouradnice();
        nastavKlikProstoru();

        panelVychodu.setCellFactory(param -> new ListCellProstor());
        
        // Initialize theme
        aplikovatTema(true);
    }

    @FXML
    private void zmenitTema(ActionEvent event) {
        boolean isLightMode = lightModeMenuItem.isSelected();
        aplikovatTema(isLightMode);
    }

    private void aplikovatTema(boolean isLightMode) {
        Scene scene = hrac.getScene();
        if (scene != null) {
            // Clear existing stylesheets
            scene.getStylesheets().clear();
            
            // Apply theme stylesheet
            String themePath = isLightMode ? 
                "/cz/vse/enga03_adventuraswi/main/light-theme.css" :
                "/cz/vse/enga03_adventuraswi/main/dark-theme.css";
            scene.getStylesheets().add(getClass().getResource(themePath).toExternalForm());

            // Update map image
            String mapPath = isLightMode ?
                "/cz/vse/enga03_adventuraswi/main/mapa-light.png" :
                "/cz/vse/enga03_adventuraswi/main/mapa-dark.png";
            Image newMapImage = new Image(getClass().getResourceAsStream(mapPath));
            mapImageView.setImage(newMapImage);
        }
    }

    private void nastavKlikProstoru() {
        farma.setOnMouseClicked(event -> klikProstor("farma"));
        louka.setOnMouseClicked(event -> klikProstor("louka"));
        namesti.setOnMouseClicked(event -> klikProstor("namesti"));
        obchod.setOnMouseClicked(event -> klikProstor("obchod"));
        joja.setOnMouseClicked(event -> klikProstor("joja"));
        vez.setOnMouseClicked(event -> klikProstor("vez"));
    }

    private void klikProstor(String nazevProstoru) {
        String prikaz = PrikazJdi.NAZEV + " " + nazevProstoru;
        zpracujPrikaz(prikaz);
    }

    private void vlozSouradnice() {
        souradniceProstoru.put("farma", new Point2D(127, 69));
        souradniceProstoru.put("louka", new Point2D(179, 165));
        souradniceProstoru.put("namesti", new Point2D(392, 222));
        souradniceProstoru.put("obchod", new Point2D(598, 99));
        souradniceProstoru.put("joja", new Point2D(569, 300));
        souradniceProstoru.put("vez", new Point2D(14, 236));
    }

    private void aktualizujPolohuHrace() {
        // ziskej nove souradnice
        String prostor = hra.getHerniPlan().getAktualniProstor().getNazev();
        Point2D noveSouradnice = souradniceProstoru.get(prostor);

        // definuj cilove hodnoty animace
        KeyValue kvX = new KeyValue(hrac.layoutXProperty(), noveSouradnice.getX());
        KeyValue kvY = new KeyValue(hrac.layoutYProperty(), noveSouradnice.getY());

        // jak dlouho animace potrva
        KeyFrame kf = new KeyFrame(Duration.millis(700), kvX, kvY);

        // vytvor a spust animaci
        Timeline timeline = new Timeline(kf);
        timeline.play();
    }

    @FXML
    private void aktualizujSeznamVychodu() {
        seznamVychodu.clear();
        seznamVychodu.addAll(hra.getHerniPlan().getAktualniProstor().getVychody());
    }

    private void aktualizujKonecHry() {

        if(hra.konecHry()) {
            vystup.appendText(hra.vratEpilog());
            vstup.setDisable(true);
            tlacitkoOdesli.setDisable(true);
            panelVychodu.setDisable(true);
        }
    }

    @FXML
    private void odesliVstup(ActionEvent actionEvent) {
        String prikaz = vstup.getText();
        vstup.clear();

        zpracujPrikaz(prikaz);
    }

    private void zpracujPrikaz(String prikaz) {
        vystup.appendText("> " + prikaz +"\n");
        String vysledek = hra.zpracujPrikaz(prikaz);
        vystup.appendText(vysledek+"\n");
    }

    public void ukoncitHru(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Skutečně si přejete ukončit hru?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    @FXML
    private void klikPanelVychodu(MouseEvent mouseEvent) {
        Prostor cil = panelVychodu.getSelectionModel().getSelectedItem();
        if(cil == null) return;
        String prikaz = PrikazJdi.NAZEV +  " " + cil.getNazev();
        zpracujPrikaz(prikaz);
    }

    @FXML
    private void napovedaKlik(ActionEvent actionEvent) {

        URL resourceUrl = getClass().getResource("/cz/vse/enga03_adventuraswi/main/napoveda.html");

        if (resourceUrl == null) {
            System.err.println("Chyba: Soubor 'napoveda.html' nebyl nalezen. Ujistěte se, že je v 'src/main/resources'.");
            // Můžete zde také zobrazit Alert uživateli
            return;
        }

        Stage napovedaStage = new Stage();
        WebView wv = new WebView();

        wv.getEngine().load(resourceUrl.toExternalForm());

        Scene napovedaScena = new Scene(wv, 800, 600);
        napovedaStage.setScene(napovedaScena);
        napovedaStage.setTitle("Nápověda");
        napovedaStage.show();
    }
}
