package cz.vse.enga03_adventuraswi.main;

// importy
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

// hlavni ovladac (controller)
public class HomeController {
    @FXML
    private ImageView hrac; // fxml: hrac
    @FXML
    private ImageView farma; // fxml: farma
    @FXML
    private ImageView louka; // fxml: louka
    @FXML
    private ImageView namesti; // fxml: namesti
    @FXML
    private ImageView obchod; // fxml: obchod
    @FXML
    private ImageView joja; // fxml: joja
    @FXML
    private ImageView vez; // fxml: vez
    @FXML
    private ImageView batohImage; // fxml: batoh
    @FXML
    private VBox batohContentPane; // fxml: obsah batohu

    @FXML
    private ScrollPane vystupScrollPane; // fxml: rolovaci vystup
    @FXML
    private VBox vystupPanel; // fxml: panel vystupu

    @FXML
    private RadioMenuItem lightModeMenuItem; // fxml: svetly mod
    @FXML
    private RadioMenuItem darkModeMenuItem; // fxml: tmavy mod
    @FXML
    private ImageView mapImageView; // fxml: mapa

    private IHra hra = new Hra(); // instance hry

    // zamek proti akcim po konci hry
    private boolean jeHraUkoncena = false;
    // ------------------------------------


    // pozice hrace na mape (souradnice)
    private Map<String, Point2D> souradniceProstoru = new HashMap<>();    @FXML
    private VBox akcePanel; // fxml: panel akci

    @FXML
    private ImageView pierre, lewis, caroline, kouzelnik, robin, morris; // fxml: npc postavy

    @FXML
    private ImageView klacky, kamen, motyka, konev, seminko, rustStages; // fxml: predmety a rust

    private ImageView selectedObject = null; // aktualne vybrany objekt

    @FXML
    // metoda po spusteni (inicializace)
    private void initialize() {
        vlozSouradnice(); // nacteni souradnic
        nastavKlikProstoru(); // nastaveni klikani

        // automaticke rolovani vystupu
        vystupPanel.heightProperty().addListener((obs, oldVal, newVal) -> {
            vystupScrollPane.setVvalue(1.0);
        });

        // spusteni nove hry
        restartGame();
    }

    @FXML
    // tlacitko nova hra
    private void novaHra(ActionEvent actionEvent) {
        // dialog pro potvrzeni
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Skutečně si přejete začít novou hru?");
        alert.setTitle("Nová hra");
        alert.setHeaderText("Potvrzení nové hry");
        Optional<ButtonType> result = alert.showAndWait(); // cekani na odpoved
        // pokud potvrdi, restart
        if (result.isPresent() && result.get() == ButtonType.OK) {
            restartGame();
        }
    }

    // restartuje hru
    private void restartGame() {
        // vytvoreni nove instance hry
        this.hra = new Hra(); // nova hra

        // reset zamku
        this.jeHraUkoncena = false;
        // ----------------------------------------

        // sledovani zmen (mistnost, konec, inventar)
        hra.getHerniPlan().registruj(ZmenaHry.ZMENA_MISTNOSTI, this::aktualizujPolohuHrace);
        hra.registruj(ZmenaHry.KONEC_HRY, this::aktualizujKonecHry);
        hra.getHerniPlan().registruj(ZmenaHry.ZMENA_INVENTARE, this::aktualizujPredmety);

        // reset ui
        akcePanel.getChildren().clear(); // cisteni akci
        zrusitVyberObjektu(); // zruseni vyberu

        vystupPanel.getChildren().clear(); // cisteni vystupu
        pridejVystup(hra.vratUvitani()); // uvitaci zprava

        // inicializace pozice hrace
        vlozSouradnice();
        aktualizujPolohuHrace(); // hrac na start

        // nastaveni aktualniho tematu
        aplikovatTema(lightModeMenuItem != null ? lightModeMenuItem.isSelected() : true); // nastaveni vzhledu

        // aktualizace viditelnosti predmetu
        aktualizujPredmety(); // zobrazeni predmetu

    }

    @FXML
    // prepnuti tematu (dark/light)
    private void zmenitTema(ActionEvent event) {
        boolean isLightMode = lightModeMenuItem.isSelected(); // zjisteni modu
        aplikovatTema(isLightMode);
    }

    // aplikace css stylu
    private void aplikovatTema(boolean isLightMode) {
        Scene scene = hrac.getScene(); // ziskani sceny
        if (scene != null) {
            // smazani starych stylu
            scene.getStylesheets().clear();

            // vyber cesty k css
            String themePath = isLightMode ?
                    "/cz/vse/enga03_adventuraswi/main/light-theme.css" :
                    "/cz/vse/enga03_adventuraswi/main/dark-theme.css";
            scene.getStylesheets().add(getClass().getResource(themePath).toExternalForm()); // nacteni novych stylu

            // vyber obrazku mapy
            String mapPath = isLightMode ?
                    "/cz/vse/enga03_adventuraswi/main/mapa-light.png" :
                    "/cz/vse/enga03_adventuraswi/main/mapa-dark.png";
            Image newMapImage = new Image(getClass().getResourceAsStream(mapPath)); // nacteni mapy
            mapImageView.setImage(newMapImage); // nastaveni mapy
        }
    }

    // nastaveni klikacich oblasti
    private void nastavKlikProstoru() {
        // klikani na mistnosti
        farma.setOnMouseClicked(event -> klikProstor("farma"));
        louka.setOnMouseClicked(event -> klikProstor("louka"));
        namesti.setOnMouseClicked(event -> klikProstor("namesti"));
        obchod.setOnMouseClicked(event -> klikProstor("obchod"));
        joja.setOnMouseClicked(event -> klikProstor("joja"));
        vez.setOnMouseClicked(event -> klikProstor("vez"));

        // klikani na npc
        pierre.setOnMouseClicked(event -> zobrazAkceNpc("pierre"));
        lewis.setOnMouseClicked(event -> zobrazAkceNpc("lewis"));
        caroline.setOnMouseClicked(event -> zobrazAkceNpc("caroline"));
        kouzelnik.setOnMouseClicked(event -> zobrazAkceNpc("kouzelnik"));
        robin.setOnMouseClicked(event -> zobrazAkceNpc("robin"));
        morris.setOnMouseClicked(event -> zobrazAkceNpc("morris"));

        // klikani na predmety
        klacky.setOnMouseClicked(event -> zobrazAkcePredmet("klacky"));
        kamen.setOnMouseClicked(event -> zobrazAkcePredmet("kamen"));

        motyka.setOnMouseClicked(event -> zobrazAkcePredmet("motyka"));
        konev.setOnMouseClicked(event -> zobrazAkcePredmet("konev"));
        seminko.setOnMouseClicked(event -> zobrazAkcePredmet("seminko"));

        // kliknuti na rostlinu
        if (rustStages != null) {
            rustStages.setOnMouseClicked(event -> zobrazAkceRostliny());
        }
    }

    // zobrazi akce pro rostlinu
    private void zobrazAkceRostliny() {
        // kontrola zamku
        if (jeHraUkoncena) return;
        // -----------------------------

        zrusitVyberObjektu(); // zrusi predchozi vyber
        ImageView plantView = rustStages;
        zvyraznitObjekt(plantView); // zvyrazni rostlinu

        akcePanel.getChildren().clear(); // vycisti panel akci

        Prostor aktualniProstor = hra.getHerniPlan().getAktualniProstor();

        // kontrola, zda je hrac na farme
        if (!"farma".equals(aktualniProstor.getNazev())) {
            Label upozorneni = new Label("Nejsi ve stejném prostoru!");
            upozorneni.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            akcePanel.getChildren().add(upozorneni);
            return;
        }

        // zjisteni faze rustu
        String currentStage = null;
        for (String s : new String[]{"rust1", "rust2", "rust3", "rust4", "sklizen"}) {
            if (aktualniProstor.obsahujePredmet(s)) {
                currentStage = s;
                break;
            }
        }

        // pokud nic neroste, ale je zasazeno
        if (currentStage == null && hra.getHerniPlan().isZasadeno()) {
            currentStage = "seminko";
        }

        if (currentStage == null) {
            Label upozorneni = new Label("Nejsi ve stejném prostoru!");
            upozorneni.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            akcePanel.getChildren().add(upozorneni);
            return;
        }

        // tlacitko pro zaliti
        Button zalijButton = new Button("Zalít"); // tlacitko zalit
        boolean hasKonev = hra.getHerniPlan().getBatoh().obsahujeVec("konev"); // kontrola konve
        zalijButton.setDisable(!hasKonev); // blokovani bez konve
        if (!hasKonev) {
            zalijButton.setTooltip(new Tooltip("Potřebuješ konev v batohu"));
        }
        // akce po kliknuti na zalit
        zalijButton.setOnAction(event -> {
            zpracujPrikaz("zalij");
            aktualizujPredmety();
            zrusitVyberObjektu();
        });
        akcePanel.getChildren().add(zalijButton);

        // tlacitko pro sklizen
        // pokud je rostlina zrala
        if ("sklizen".equals(currentStage)) {
            Button sklidButton = new Button("Sklidit"); // tlacitko sklidit
            boolean hasMotyka = hra.getHerniPlan().getBatoh().obsahujeVec("motyka"); // kontrola motyky
            sklidButton.setDisable(!hasMotyka);
            if (!hasMotyka) {
                sklidButton.setTooltip(new Tooltip("Potřebuješ motyku v batohu"));
            }
            // akce po kliknuti na sklidit
            sklidButton.setOnAction(event -> {
                zpracujPrikaz("sklid");
                aktualizujPredmety();
                zrusitVyberObjektu();
            });
            akcePanel.getChildren().add(sklidButton);
        }
    }

    // zobrazi akce pro npc
    private void zobrazAkceNpc(String jmenoNpc) {
        // kontrola zamku
        if (jeHraUkoncena) return;
        // -----------------------------

        zrusitVyberObjektu();
        zvyraznitObjekt(getNpcImageView(jmenoNpc)); // zvyrazneni npc

        akcePanel.getChildren().clear();

        // kontrola, zda je npc ve stejne mistnosti
        if (!hra.getHerniPlan().getAktualniProstor().obsahujeNpc(jmenoNpc)) {
            Label upozorneni = new Label("Nejsi ve stejném prostoru!");
            upozorneni.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            akcePanel.getChildren().add(upozorneni);
            return;
        }

        Button mluvButton = new Button("Mluvit"); // tlacitko mluvit
        mluvButton.setOnAction(event -> {
            zpracujPrikaz("mluv " + jmenoNpc);
            zrusitVyberObjektu();
        });

        Button urazButton = new Button("Urazit"); // tlacitko urazit
        urazButton.setOnAction(event -> {
            zpracujPrikaz("uraz " + jmenoNpc);
            zrusitVyberObjektu();
        });

        // pokud ma hrac pastinak, zobrazi darovani
        if (hra.getHerniPlan().getBatoh().obsahujeVec("pastinak")) {
            Button darujButton = new Button("Darovat pastinák"); // tlacitko darovat
            darujButton.setOnAction(event -> {
                zpracujPrikaz("daruj " + jmenoNpc + " pastinak");
                zrusitVyberObjektu();
            });
            akcePanel.getChildren().addAll(mluvButton, urazButton, darujButton);
        } else {
            akcePanel.getChildren().addAll(mluvButton, urazButton);
        }
    }

    // zobrazi akce pro predmet v mistnosti
    private void zobrazAkcePredmet(String nazevPredmetu) {
        // kontrola zamku
        if (jeHraUkoncena) return;
        // -----------------------------

        zrusitVyberObjektu();
        zvyraznitObjekt(getPredmetImageView(nazevPredmetu)); // zvyrazneni predmetu

        akcePanel.getChildren().clear();

        // kontrola, zda je predmet v mistnosti
        if (!hra.getHerniPlan().getAktualniProstor().obsahujePredmet(nazevPredmetu)) {
            Label upozorneni = new Label("Nejsi ve stejném prostoru!");
            upozorneni.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            akcePanel.getChildren().add(upozorneni);
            return;
        }

        Button vezmiButton = new Button("Vzít"); // tlacitko vzit
        // akce po kliknuti
        vezmiButton.setOnAction(event -> {
            zpracujPrikaz("vezmi " + nazevPredmetu);
            aktualizujPredmety();
            aktualizujObsahBatohu(); // po sebrani obnovi batoh
            zrusitVyberObjektu();
        });

        akcePanel.getChildren().add(vezmiButton);
    }

    // zvyrazneni objektu (pruhlednost)
    private void zvyraznitObjekt(ImageView imageView) {
        if (imageView != null) {
            imageView.setOpacity(0.7); // nastavi pruhlednost
            selectedObject = imageView; // ulozi vybrany objekt
        }
    }

    // zrusi zvyrazneni objektu
    private void zrusitVyberObjektu() {
        if (selectedObject != null) { // pokud je neco vybrano
            selectedObject.setOpacity(1.0); // vrati plnou viditelnost
            selectedObject = null; // vynuluje vyber
        }
        akcePanel.getChildren().clear(); // smaze tlacitka akci
    }

    // pomocna: vrati obrazek npc
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

    // pomocna: vrati obrazek predmetu
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

    // kliknuti na mistnost na mape
    private void klikProstor(String nazevProstoru) {
        // kontrola zamku
        if (jeHraUkoncena) return;
        // -----------------------------

        String prikaz = PrikazJdi.NAZEV + " " + nazevProstoru; // sestaveni prikazu "jdi"
        zpracujPrikaz(prikaz); // zpracovani
    }

    // souradnice mistnosti pro hrace
    private void vlozSouradnice() {
        souradniceProstoru.put("farma", new Point2D(350, 100));
        souradniceProstoru.put("louka", new Point2D(365, 250));
        souradniceProstoru.put("namesti", new Point2D(570, 290));
        souradniceProstoru.put("obchod", new Point2D(575, 130));
        souradniceProstoru.put("joja", new Point2D(800, 310));
        souradniceProstoru.put("vez", new Point2D(260, 260));
    }

    // animovany presun hrace
    private void aktualizujPolohuHrace() {
        // ziskani cilovych souradnic
        String prostor = hra.getHerniPlan().getAktualniProstor().getNazev(); // cilova mistnost
        Point2D noveSouradnice = souradniceProstoru.get(prostor); // cilove souradnice

        // nastaveni cilovych hodnot animace
        KeyValue kvX = new KeyValue(hrac.layoutXProperty(), noveSouradnice.getX()); // cilove x
        KeyValue kvY = new KeyValue(hrac.layoutYProperty(), noveSouradnice.getY()); // cilove y

        // nastaveni doby trvani animace
        KeyFrame kf = new KeyFrame(Duration.millis(700), kvX, kvY); // delka animace

        // spusteni animace pohybu
        Timeline timeline = new Timeline(kf); // vytvoreni animace
        timeline.play(); // spusteni
    }


    // co se stane pri konci hry
    private void aktualizujKonecHry() {
        if(hra.konecHry()) { // pokud hra skoncila
            // zamceni hry
            pridejVystup(hra.vratEpilog()); // zobrazeni epilogu
            this.jeHraUkoncena = true; // zamknuti hry
            zrusitVyberObjektu(); // zruseni vyberu
            // ------------------------------------
        }
    }

    /**
     * Zpracuje příkaz a vypíše výsledek do nového panelu.
     * @param prikaz text příkazu
     */
    // zpracovani textoveho prikazu
    private void zpracujPrikaz(String prikaz) {
        String vysledek = hra.zpracujPrikaz(prikaz); // poslani prikazu logice
        pridejVystup(vysledek); // zobrazeni vysledku
    }

    /**
     * Pomocná metoda pro přidání textového výstupu do vystupPanel.
     * Text je formátován jako tučný a centrovaný Label.
     * @param text text, který se má zobrazit
     */
    // prida text do vystupniho panelu
    private void pridejVystup(String text) {
        if (text == null || text.trim().isEmpty()) { // pokud neni co zobrazit
            return;
        }

        // rozdeleni na radky
        String[] lines = text.split("\n");

        for (String line : lines) { // pro kazdy radek
            if (line.trim().isEmpty()) continue;

            Label label = new Label(line.trim()); // vytvoreni textu
            label.getStyleClass().add("game-output-label"); // prirazeni css stylu
            label.setWrapText(true); // zalamovani textu

            vystupPanel.getChildren().add(label); // pridani textu do panelu
        }
    }

    // ukonceni hry (z menu)
    public void ukoncitHru(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Skutečně si přejete ukončit hru?"); // dialog
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) { // pokud potvrdi, konec
            Platform.exit();
        }
    }

    // zobrazeni/skryti predmetu na mape
    private void aktualizujPredmety() {
        // projde sberatelne predmety
        for (String nazevPredmetu : new String[]{"motyka", "konev", "klacky", "kamen", "seminko"}) {
            ImageView imageView = getPredmetImageView(nazevPredmetu);
            if (imageView != null) {
                // zjisti, zda je predmet v mistnosti
                boolean jePredmetVMistnosti = hra.getHerniPlan().getAktualniProstor().obsahujePredmet(nazevPredmetu);

                imageView.setVisible(jePredmetVMistnosti); // nastavi viditelnost
                imageView.setManaged(jePredmetVMistnosti); // zmizeni z layoutu
            }
        }

        // aktualizace vizualniho stavu rostliny
        Prostor aktualniProstor = hra.getHerniPlan().getAktualniProstor();
        ImageView plantView = rustStages; // obrazek rostliny
        if (plantView != null) {
            plantView.setVisible(false); // vychozi skryti

            // pokud je v mistnosti faze rust1
            if (aktualniProstor.obsahujePredmet("rust1")) {
                plantView.setVisible(true); // zobrazi rostlinu
                // nastavi spravny obrazek
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

        // pokud je batoh otevreny, prekresli ho
        if (batohContentPane.isVisible()) {
            aktualizujObsahBatohu();
        }
    }

    // prekresleni obsahu batohu
    private void aktualizujObsahBatohu() {
        batohContentPane.getChildren().clear(); // vycisteni obsahu

        // projde predmety, ktere mohou byt v batohu
        for (String nazevPredmetu : new String[]{"motyka", "konev", "seminko", "pastinak"}) {
            // pokud je predmet v batohu
            if (hra.getHerniPlan().getBatoh().obsahujeVec(nazevPredmetu)) {
                ImageView itemImage;
                // vytvoreni a nacteni obrazku
                if (nazevPredmetu.equals("seminko")) {
                    Image img = new Image(getClass().getResourceAsStream("/cz/vse/enga03_adventuraswi/main/veci/seminko.png"));
                    itemImage = new ImageView(img);
                } else if (nazevPredmetu.equals("pastinak")) {
                    Image img = new Image(getClass().getResourceAsStream("/cz/vse/enga03_adventuraswi/main/veci/pastinak.png"));
                    itemImage = new ImageView(img);
                } else {
                    itemImage = new ImageView(getPredmetImageView(nazevPredmetu).getImage());
                }
                itemImage.setFitHeight(50); // nastaveni velikosti
                itemImage.setFitWidth(50);

                // nastaveni akce po kliknuti
                itemImage.setOnMouseClicked(event -> zobrazAkcePredmetuInventare(nazevPredmetu));

                batohContentPane.getChildren().add(itemImage); // pridani obrazku do batohu
            }
        }
    }

    // akce pro predmet v batohu
    private void zobrazAkcePredmetuInventare(String nazevPredmetu) {
        // kontrola zamku
        if (jeHraUkoncena) return;
        // -----------------------------

        akcePanel.getChildren().clear();
        String aktualniProstor = hra.getHerniPlan().getAktualniProstor().getNazev();

        // specialni akce pro seminko (zasadit)
        if (nazevPredmetu.equals("seminko") && aktualniProstor.equals("farma")) {
            Button zasadButton = new Button("Zasadit"); // tlacitko zasadit
            zasadButton.setOnAction(event -> {
                zpracujPrikaz("zasad seminko");
                aktualizujObsahBatohu();
                aktualizujPredmety();
                zrusitVyberObjektu();
            });
            akcePanel.getChildren().add(zasadButton);
            // specialni akce pro konev (zalit)
        } else if (nazevPredmetu.equals("konev")) {
            Button zalijButton = new Button("Zalít");
            zalijButton.setOnAction(event -> {
                zpracujPrikaz("zalij");
                aktualizujPredmety();
                aktualizujObsahBatohu();
                zrusitVyberObjektu();
            });
            akcePanel.getChildren().add(zalijButton);

            // specialni akce pro motyku (sklidit)
        } else if (nazevPredmetu.equals("motyka") && aktualniProstor.equals("farma")) {
            // kontrola, zda je co sklidit
            if (hra.getHerniPlan().getAktualniProstor().obsahujePredmet("sklizen")) {
                Button sklidButton = new Button("Sklidit");

                sklidButton.setOnAction(event -> {
                    zpracujPrikaz("sklid");
                    aktualizujPredmety();
                    aktualizujObsahBatohu();
                    zrusitVyberObjektu();
                });
                akcePanel.getChildren().add(sklidButton);
            }
        }
    }

    @FXML
    // otevreni/zavreni batohu
    private void prepnoutBatoh() {
        // kontrola zamku
        if (jeHraUkoncena) return;
        // -----------------------------

        boolean jeBatohOtevreny = batohContentPane.isVisible(); // zjisteni stavu batohu

        // animace zmeny velikosti batohu
        Duration duration = Duration.millis(300); // delka animace
        // nastaveni cilove vysky a sirky
        KeyValue kvH = new KeyValue(batohImage.fitHeightProperty(), jeBatohOtevreny ? 150 : 50);
        KeyValue kvW = new KeyValue(batohImage.fitWidthProperty(), jeBatohOtevreny ? 150 : 50);
        KeyFrame kf = new KeyFrame(duration, kvH, kvW);

        Timeline timeline = new Timeline(kf); // vytvoreni animace
        // co se stane po skonceni animace
        timeline.setOnFinished(e -> {
            if (!jeBatohOtevreny) {
                aktualizujObsahBatohu();
            }
            batohContentPane.setManaged(!jeBatohOtevreny); // skryti/zobrazeni (pro layout)
            batohContentPane.setVisible(!jeBatohOtevreny); // skryti/zobrazeni
        });
        timeline.play(); // spusteni animace
    }

    @FXML
    // zobrazeni napovedy
    private void napovedaKlik(ActionEvent actionEvent) {

        URL resourceUrl = getClass().getResource("/cz/vse/enga03_adventuraswi/main/napoveda.html"); // cesta k html napovede

        if (resourceUrl == null) { // kontrola souboru
            System.err.println("Chyba: Soubor 'napoveda.html' nebyl nalezen. Ujistěte se, že je v 'src/main/resources'.");
            return;
        }

        Stage napovedaStage = new Stage(); // nove okno
        WebView wv = new WebView(); // komponenta pro html

        wv.getEngine().load(resourceUrl.toExternalForm()); // nacteni html

        Scene napovedaScena = new Scene(wv, 800, 600); // vytvoreni sceny
        napovedaStage.setScene(napovedaScena); // nastaveni sceny
        napovedaStage.setTitle("Nápověda"); // titulek
        napovedaStage.show(); // zobrazeni okna
    }
}