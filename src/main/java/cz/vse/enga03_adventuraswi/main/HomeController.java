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
    private ImageView batohImage;
    @FXML
    private VBox batohContentPane;
    @FXML
    private TextArea vystup;
    @FXML
    private RadioMenuItem lightModeMenuItem;
    @FXML
    private RadioMenuItem darkModeMenuItem;
    @FXML
    private ImageView mapImageView;

    private IHra hra = new Hra();


        // mapa souradnic pro pozice hrace v jednotlivych prostorech
        private Map<String, Point2D> souradniceProstoru = new HashMap<>();    @FXML
    private VBox akcePanel;
    
    @FXML
    private ImageView pierre, lewis, caroline, kouzelnik, robin, morris;
    
    @FXML
    private ImageView klacky, kamen, motyka, konev, seminko, rustStages;

    private ImageView selectedObject = null;

    @FXML
    private void initialize() {
        vlozSouradnice();
        nastavKlikProstoru();

            // spusteni nove hry od zacatku
        restartGame();
    }

    @FXML
    private void novaHra(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Skutečně si přejete začít novou hru?");
        alert.setTitle("Nová hra");
        alert.setHeaderText("Potvrzení nové hry");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            restartGame();
        }
    }

    // restart hry a inicializace hrace na vychozi pozici farmy
    private void restartGame() {
            // vytvoreni nove instance hry
        this.hra = new Hra();        // registrace observeru pro zmeny ve hre
        hra.getHerniPlan().registruj(ZmenaHry.ZMENA_MISTNOSTI, this::aktualizujPolohuHrace);
        hra.registruj(ZmenaHry.KONEC_HRY, this::aktualizujKonecHry);
        hra.getHerniPlan().registruj(ZmenaHry.ZMENA_INVENTARE, this::aktualizujPredmety);

        // reset UI komponent
        akcePanel.getChildren().clear();
        zrusitVyberObjektu();
        vystup.clear();
        vystup.appendText(hra.vratUvitani() + "\n\n");

        // inicializace pozice hrace
        vlozSouradnice();
        aktualizujPolohuHrace();

        // nastaveni aktualniho tematu
        aplikovatTema(lightModeMenuItem != null ? lightModeMenuItem.isSelected() : true);
        
        // aktualizace viditelnosti predmetu
        aktualizujPredmety();

    }

    @FXML
    private void zmenitTema(ActionEvent event) {
        boolean isLightMode = lightModeMenuItem.isSelected();
        aplikovatTema(isLightMode);
    }

    private void aplikovatTema(boolean isLightMode) {
        Scene scene = hrac.getScene();
        if (scene != null) {
            // smazeme stary barvy
            scene.getStylesheets().clear();
            
            // dame tam novy barvy
            String themePath = isLightMode ? 
                "/cz/vse/enga03_adventuraswi/main/light-theme.css" :
                "/cz/vse/enga03_adventuraswi/main/dark-theme.css";
            scene.getStylesheets().add(getClass().getResource(themePath).toExternalForm());

            // dame tam novou mapu
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
        
        // nastaveni kliknuti pro NPC
        pierre.setOnMouseClicked(event -> zobrazAkceNpc("pierre"));
        lewis.setOnMouseClicked(event -> zobrazAkceNpc("lewis"));
        caroline.setOnMouseClicked(event -> zobrazAkceNpc("caroline"));
        kouzelnik.setOnMouseClicked(event -> zobrazAkceNpc("kouzelnik"));
        robin.setOnMouseClicked(event -> zobrazAkceNpc("robin"));
        morris.setOnMouseClicked(event -> zobrazAkceNpc("morris"));
        
        // nastaveni kliknuti pro predmety v prostoru
        klacky.setOnMouseClicked(event -> zobrazAkcePredmet("klacky"));
        kamen.setOnMouseClicked(event -> zobrazAkcePredmet("kamen"));
        
        motyka.setOnMouseClicked(event -> zobrazAkcePredmet("motyka"));
        konev.setOnMouseClicked(event -> zobrazAkcePredmet("konev"));
        
        // aktivace interakce se seminkem pred zasazenim
        seminko.setOnMouseClicked(event -> zobrazAkcePredmet("seminko"));
        
        // handler pro rustStages zobrazujici faze rustu rostliny
        if (rustStages != null) {
            rustStages.setOnMouseClicked(event -> zobrazAkceRostliny());
        }
    }
    
    private void zobrazAkceRostliny() {
        // udelame rostlinku pruhlednejsi at je videt ze je vybrana
        zrusitVyberObjektu();
        // pouzivame vzdy rustStages protoze tam je rostlinka
        ImageView plantView = rustStages; 
        zvyraznitObjekt(plantView);

        akcePanel.getChildren().clear();

        Prostor aktualniProstor = hra.getHerniPlan().getAktualniProstor();

        // Check if we're in the farm room first
        if (!"farma".equals(aktualniProstor.getNazev())) {
            Label upozorneni = new Label("Nejsi ve stejném prostoru!");
            upozorneni.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            akcePanel.getChildren().add(upozorneni);
            return;
        }

        // Determine which rust stage (if any) is present in the current room
        String currentStage = null;
        for (String s : new String[]{"rust1", "rust2", "rust3", "rust4", "sklizen"}) {
            if (aktualniProstor.obsahujePredmet(s)) {
                currentStage = s;
                break;
            }
        }

        // If nothing in rust stages but a seed was planted in the farm, treat it as a seed stage
        if (currentStage == null && hra.getHerniPlan().isZasadeno()) {
            currentStage = "seminko";
        }

        if (currentStage == null) {
            Label upozorneni = new Label("Nejsi ve stejném prostoru!");
            upozorneni.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            akcePanel.getChildren().add(upozorneni);
            return;
        }

        // tlacitko pro zaliti - aktivni jen s konvi
        Button zalijButton = new Button("Zalít");
        zalijButton.setMaxWidth(Double.MAX_VALUE);
        boolean hasKonev = hra.getHerniPlan().getBatoh().obsahujeVec("konev");
        zalijButton.setDisable(!hasKonev);
        if (!hasKonev) {
            zalijButton.setTooltip(new Tooltip("Potřebuješ konev v batohu"));
        }
        zalijButton.setOnAction(event -> {
            zpracujPrikaz("zalij");
            // okamzita aktualizace zobrazeni
            aktualizujPredmety();
            zrusitVyberObjektu();
        });
        akcePanel.getChildren().add(zalijButton);

        // tlacitko pro sklizen - jen pro zralou rostlinu a s motykou
        if ("sklizen".equals(currentStage)) {
            Button sklidButton = new Button("Sklidit");
            sklidButton.setMaxWidth(Double.MAX_VALUE);
            boolean hasMotyka = hra.getHerniPlan().getBatoh().obsahujeVec("motyka");
            sklidButton.setDisable(!hasMotyka);
            if (!hasMotyka) {
                sklidButton.setTooltip(new Tooltip("Potřebuješ motyku v batohu"));
            }
            sklidButton.setOnAction(event -> {
                zpracujPrikaz("sklid");
                aktualizujPredmety();
                zrusitVyberObjektu();
            });
            akcePanel.getChildren().add(sklidButton);
        }
    }

    private void zobrazAkceNpc(String jmenoNpc) {
        // Zvýraznění vybrané NPC
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
        
        // pridani tlacitka pro darovani pastinaku
        if (hra.getHerniPlan().getBatoh().obsahujeVec("pastinak")) {
            Button darujButton = new Button("Darovat pastinák");
            darujButton.setMaxWidth(Double.MAX_VALUE);
            darujButton.setOnAction(event -> {
                zpracujPrikaz("daruj " + jmenoNpc + " pastinak");
                zrusitVyberObjektu();
            });
            akcePanel.getChildren().addAll(mluvButton, urazButton, darujButton);
        } else {
            akcePanel.getChildren().addAll(mluvButton, urazButton);
        }
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
            // Update UI: item was moved to inventory so refresh map and backpack
            aktualizujPredmety();
            aktualizujObsahBatohu();
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
            case "seminko" -> seminko;
            case "konev" -> konev;
            case "rust1", "rust2", "rust3", "rust4", "sklizen" -> rustStages;
            default -> null;
        };
    }

    private void klikProstor(String nazevProstoru) {
        String prikaz = PrikazJdi.NAZEV + " " + nazevProstoru;
        zpracujPrikaz(prikaz);
    }

    private void vlozSouradnice() {
        souradniceProstoru.put("farma", new Point2D(140, 100));
        souradniceProstoru.put("louka", new Point2D(148, 236));
        souradniceProstoru.put("namesti", new Point2D(358, 267));
        souradniceProstoru.put("obchod", new Point2D(358, 132));
        souradniceProstoru.put("joja", new Point2D(575, 300));
        souradniceProstoru.put("vez", new Point2D(39, 236));
    }

    private void aktualizujPolohuHrace() {
            // ziskani cilovych souradnic
        String prostor = hra.getHerniPlan().getAktualniProstor().getNazev();
        Point2D noveSouradnice = souradniceProstoru.get(prostor);

        // nastaveni cilovych hodnot animace
        KeyValue kvX = new KeyValue(hrac.layoutXProperty(), noveSouradnice.getX());
        KeyValue kvY = new KeyValue(hrac.layoutYProperty(), noveSouradnice.getY());

        // nastaveni doby trvani animace
        KeyFrame kf = new KeyFrame(Duration.millis(700), kvX, kvY);

        // spusteni animace pohybu
        Timeline timeline = new Timeline(kf);
        timeline.play();
    }


    private void aktualizujKonecHry() {

        if(hra.konecHry()) {
            vystup.appendText(hra.vratEpilog());
        }
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


    private void aktualizujPredmety() {
        // Projdeme všechny sbíratelné předměty na mapě
        for (String nazevPredmetu : new String[]{"motyka", "konev", "klacky", "kamen", "seminko"}) {
            ImageView imageView = getPredmetImageView(nazevPredmetu);
            if (imageView != null) {
                // Opravená logika: Obrázek je viditelný, POKUD je předmět v místnosti
                boolean jePredmetVMistnosti = hra.getHerniPlan().getAktualniProstor().obsahujePredmet(nazevPredmetu);

                imageView.setVisible(jePredmetVMistnosti);
                imageView.setManaged(jePredmetVMistnosti); // Aby zmizel i z layoutu
            }
        }

        // aktualizace vizualniho stavu rostliny
        Prostor aktualniProstor = hra.getHerniPlan().getAktualniProstor();
        ImageView plantView = rustStages;
        if (plantView != null) {
            plantView.setVisible(false); // vychozi stav neviditelny
            
            // cesta k obrazkum fazi rustu v /rust/ adresari
            if (aktualniProstor.obsahujePredmet("rust1")) {
                plantView.setVisible(true);
                plantView.setImage(new Image(getClass().getResource("/cz/vse/enga03_adventuraswi/main/rust/rust1.png").toExternalForm()));
            } else if (aktualniProstor.obsahujePredmet("rust2")) {
                plantView.setVisible(true);
                plantView.setImage(new Image(getClass().getResource("/cz/vse/enga03_adventuraswi/main/rust/rust2.png").toExternalForm()));
            } else if (aktualniProstor.obsahujePredmet("rust3")) {
                plantView.setVisible(true);
                plantView.setImage(new Image(getClass().getResource("/cz/vse/enga03_adventuraswi/main/rust/rust3.png").toExternalForm()));
            } else if (aktualniProstor.obsahujePredmet("rust4")) {
                plantView.setVisible(true);
                plantView.setImage(new Image(getClass().getResource("/cz/vse/enga03_adventuraswi/main/rust/rust4.png").toExternalForm()));
            } else if (aktualniProstor.obsahujePredmet("sklizen")) {
                plantView.setVisible(true);
                plantView.setImage(new Image(getClass().getResource("/cz/vse/enga03_adventuraswi/main/rust/sklizen.png").toExternalForm()));
            }
        }

        // Pokud je batoh otevřený, rovnou aktualizujeme i jeho obsah
        if (batohContentPane.isVisible()) {
            aktualizujObsahBatohu();
        }
    }

    private void aktualizujObsahBatohu() {
        batohContentPane.getChildren().clear();
        
        // vytvoreni obrazku pro predmety v batohu
        for (String nazevPredmetu : new String[]{"motyka", "konev", "seminko", "pastinak"}) {
            if (hra.getHerniPlan().getBatoh().obsahujeVec(nazevPredmetu)) {
                ImageView itemImage;
                if (nazevPredmetu.equals("seminko")) {
                    Image img = new Image(getClass().getResourceAsStream("/cz/vse/enga03_adventuraswi/main/veci/seminko.png"));
                    itemImage = new ImageView(img);
                } else if (nazevPredmetu.equals("pastinak")) {
                    Image img = new Image(getClass().getResourceAsStream("/cz/vse/enga03_adventuraswi/main/veci/pastinak.png"));
                    itemImage = new ImageView(img);
                } else {
                    itemImage = new ImageView(getPredmetImageView(nazevPredmetu).getImage());
                }
                itemImage.setFitHeight(50);
                itemImage.setFitWidth(50);
                
                // Add click handler
                itemImage.setOnMouseClicked(event -> zobrazAkcePredmetuInventare(nazevPredmetu));
                
                batohContentPane.getChildren().add(itemImage);
            }
        }
    }
    
    private void zobrazAkcePredmetuInventare(String nazevPredmetu) {
        akcePanel.getChildren().clear();
        String aktualniProstor = hra.getHerniPlan().getAktualniProstor().getNazev();
        
        if (nazevPredmetu.equals("seminko") && aktualniProstor.equals("farma")) {
            Button zasadButton = new Button("Zasadit");
            zasadButton.setMaxWidth(Double.MAX_VALUE);
            zasadButton.setOnAction(event -> {
                zpracujPrikaz("zasad seminko");
                // Immediately update UI so the seed disappears from inventory and plant appears
                aktualizujObsahBatohu();
                aktualizujPredmety();
                zrusitVyberObjektu();
            });
            akcePanel.getChildren().add(zasadButton);
        } else if (nazevPredmetu.equals("konev")) {
            Button zalijButton = new Button("Zalít");
            zalijButton.setMaxWidth(Double.MAX_VALUE);
            zalijButton.setOnAction(event -> {
                zpracujPrikaz("zalij");
                // Ensure UI updates immediately after watering from inventory
                aktualizujPredmety();
                aktualizujObsahBatohu();
                zrusitVyberObjektu();
            });
            akcePanel.getChildren().add(zalijButton);
            
        } else if (nazevPredmetu.equals("motyka") && aktualniProstor.equals("farma")) {
            // Opraveno: Kontrolujeme 'sklizen' (rostlinu) ne 'pastinak' (předmět)
            if (hra.getHerniPlan().getAktualniProstor().obsahujePredmet("sklizen")) {
                Button sklidButton = new Button("Sklidit");
                
                sklidButton.setMaxWidth(Double.MAX_VALUE);
                sklidButton.setOnAction(event -> {
                    zpracujPrikaz("sklid");
                    // Update UI so harvested item / plant disappears and inventory updates
                    aktualizujPredmety();
                    aktualizujObsahBatohu();
                    zrusitVyberObjektu();
                });
                akcePanel.getChildren().add(sklidButton);
            }
        }
    }

    @FXML
    private void prepnoutBatoh() {
        boolean jeBatohOtevreny = batohContentPane.isVisible();
        
        // animace zmeny velikosti batohu
        Duration duration = Duration.millis(300);
        KeyValue kvH = new KeyValue(batohImage.fitHeightProperty(), jeBatohOtevreny ? 150 : 50);
        KeyValue kvW = new KeyValue(batohImage.fitWidthProperty(), jeBatohOtevreny ? 150 : 50);
        KeyFrame kf = new KeyFrame(duration, kvH, kvW);
        
        Timeline timeline = new Timeline(kf);
        timeline.setOnFinished(e -> {
            if (!jeBatohOtevreny) {
                aktualizujObsahBatohu();
            }
            batohContentPane.setManaged(!jeBatohOtevreny);
            batohContentPane.setVisible(!jeBatohOtevreny);
        });
        timeline.play();
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
