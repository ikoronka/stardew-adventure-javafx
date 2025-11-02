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
import javafx.scene.layout.VBox;
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


    // seznam souradnic jednotlivych prostoru
    private Map<String, Point2D> souradniceProstoru = new HashMap<>();
    
    @FXML
    private VBox akcePanel;
    
    @FXML
    private ImageView pierre, lewis, caroline, kouzelnik, robin, morris;
    
    @FXML
    private ImageView klacky, kamen, motyka, konev;

    private ImageView selectedObject = null;

    @FXML
    private void initialize() {
        vystup.appendText(hra.vratUvitani() + "\n\n");
        Platform.runLater(() -> vstup.requestFocus());
        hra.getHerniPlan().registruj(ZmenaHry.ZMENA_MISTNOSTI, this::aktualizujPolohuHrace);
        hra.registruj(ZmenaHry.KONEC_HRY, this::aktualizujKonecHry);

        vlozSouradnice();
        nastavKlikProstoru();
        
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
        
        // Nastavení kliknutí pro NPC
        pierre.setOnMouseClicked(event -> zobrazAkceNpc("pierre"));
        lewis.setOnMouseClicked(event -> zobrazAkceNpc("lewis"));
        caroline.setOnMouseClicked(event -> zobrazAkceNpc("caroline"));
        kouzelnik.setOnMouseClicked(event -> zobrazAkceNpc("kouzelnik"));
        robin.setOnMouseClicked(event -> zobrazAkceNpc("robin"));
        morris.setOnMouseClicked(event -> zobrazAkceNpc("morris"));
        
        // Nastavení kliknutí pro předměty
        klacky.setOnMouseClicked(event -> zobrazAkcePredmet("klacky"));
        kamen.setOnMouseClicked(event -> zobrazAkcePredmet("kamen"));
        motyka.setOnMouseClicked(event -> zobrazAkcePredmet("motyka"));
        konev.setOnMouseClicked(event -> zobrazAkcePredmet("konev"));
    }
    
    private void zobrazAkceNpc(String jmenoNpc) {
        // Zvýraznění vybraného NPC
        zrusitVyberObjektu();
        zvyraznitObjekt(getNpcImageView(jmenoNpc));

        akcePanel.getChildren().clear();

        if (!hra.getHerniPlan().getAktualniProstor().obsahujeNpc(jmenoNpc)) {
            Label upozorneni = new Label("Nejsi ve stejném prostoru!");
            upozorneni.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            akcePanel.getChildren().add(upozorneni);
            return;
        }

        Button mluvButton = new Button("Mluvit");
        mluvButton.setMaxWidth(Double.MAX_VALUE);
        mluvButton.setOnAction(event -> {
            zpracujPrikaz("mluv " + jmenoNpc);
            zrusitVyberObjektu();
        });
        
        Button urazButton = new Button("Urazit");
        urazButton.setMaxWidth(Double.MAX_VALUE);
        urazButton.setOnAction(event -> {
            zpracujPrikaz("uraz " + jmenoNpc);
            zrusitVyberObjektu();
        });
        
        akcePanel.getChildren().addAll(mluvButton, urazButton);
    }
    
    private void zobrazAkcePredmet(String nazevPredmetu) {
        // Zvýraznění vybraného předmětu
        zrusitVyberObjektu();
        zvyraznitObjekt(getPredmetImageView(nazevPredmetu));

        akcePanel.getChildren().clear();

        if (!hra.getHerniPlan().getAktualniProstor().obsahujePredmet(nazevPredmetu)) {
            Label upozorneni = new Label("Nejsi ve stejném prostoru!");
            upozorneni.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            akcePanel.getChildren().add(upozorneni);
            return;
        }

        Button vezmiButton = new Button("Vzít");
        vezmiButton.setMaxWidth(Double.MAX_VALUE);
        vezmiButton.setOnAction(event -> {
            zpracujPrikaz("vezmi " + nazevPredmetu);
            zrusitVyberObjektu();
        });

        akcePanel.getChildren().add(vezmiButton);
    }
    
    private void zvyraznitObjekt(ImageView imageView) {
        if (imageView != null) {
            imageView.setOpacity(0.7);
            selectedObject = imageView;
        }
    }
    
    private void zrusitVyberObjektu() {
        if (selectedObject != null) {
            selectedObject.setOpacity(1.0);
            selectedObject = null;
        }
        akcePanel.getChildren().clear();
    }
    
    private ImageView getNpcImageView(String jmenoNpc) {
        return switch (jmenoNpc) {
            case "pierre" -> pierre;
            case "lewis" -> lewis;
            case "caroline" -> caroline;
            case "kouzelnik" -> kouzelnik;
            case "robin" -> robin;
            case "morris" -> morris;
            default -> null;
        };
    }
    
    private ImageView getPredmetImageView(String nazevPredmetu) {
        return switch (nazevPredmetu) {
            case "klacky" -> klacky;
            case "kamen" -> kamen;
            case "motyka" -> motyka;
            case "konev" -> konev;
            default -> null;
        };
    }

    private void klikProstor(String nazevProstoru) {
        String prikaz = PrikazJdi.NAZEV + " " + nazevProstoru;
        zpracujPrikaz(prikaz);
    }

    private void vlozSouradnice() {
        souradniceProstoru.put("farma", new Point2D(126, 100));
        souradniceProstoru.put("louka", new Point2D(148, 236));
        souradniceProstoru.put("namesti", new Point2D(358, 267));
        souradniceProstoru.put("obchod", new Point2D(358, 132));
        souradniceProstoru.put("joja", new Point2D(575, 300));
        souradniceProstoru.put("vez", new Point2D(39, 236));
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


    private void aktualizujKonecHry() {

        if(hra.konecHry()) {
            vystup.appendText(hra.vratEpilog());
            vstup.setDisable(true);
        tlacitkoOdesli.setDisable(true);
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
