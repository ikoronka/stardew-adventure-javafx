package cz.vse.enga03_adventuraswi.logika;

import cz.vse.enga03_adventuraswi.logika.Vec;
import cz.vse.enga03_adventuraswi.logika.Npc;
import cz.vse.enga03_adventuraswi.main.Pozorovatel;
import cz.vse.enga03_adventuraswi.main.PredmetPozorovani;
import cz.vse.enga03_adventuraswi.main.ZmenaHry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class HerniPlan - třída představující mapu a stav adventury.
 *
 * Tato třída inicializuje prvky ze kterých se hra skládá:
 * vytváří všechny prostory,
 * propojuje je vzájemně pomocí východů
 * a pamatuje si aktuální prostor, ve kterém se hráč právě nachází.
 *
 *@author     Amelie Engelmaierová
 */
public class HerniPlan implements PredmetPozorovani {

    private Prostor aktualniProstor;
    private Batoh batoh;
    private Prostor farma;
    private boolean zasazeno;
    private int zalivani;
    private int penize;
    private int rustStage;  // 0: not planted, 1-4: growth stages
    private Map<ZmenaHry, Set<Pozorovatel>> seznamPozorovatelu = new HashMap<>();

    // Mapa pro ukládání, odkud věci původně jsou
    private Map<String, Prostor> puvodniLokaceVeci = new HashMap<>();

    /**
     * Konstruktor který vytváří jednotlivé prostory a propojuje je pomocí východů.
     * Jako výchozí aktuální prostor nastaví halu.
     */
    public HerniPlan() {
        batoh = new Batoh(5);
        zalozProstoryHry();
        for(ZmenaHry zmenaHry : ZmenaHry.values()) {
            seznamPozorovatelu.put(zmenaHry, new HashSet<>());
        }
    }
    /**
     * Vytváří jednotlivé prostory a propojuje je pomocí východů.
     * Jako výchozí aktuální prostor nastaví domeček.
     */
    private void zalozProstoryHry() {
        // --- Ukládáme reference na prostory ---
        Prostor farma = new Prostor("farma", "zarostla farma tveho dedecka");
        Prostor louka = new Prostor("louka", "cesta z farmy ke vezi a do mesta");
        Prostor namesti = new Prostor("namesti", "centrum mestecka");
        Prostor obchod = new Prostor("obchod", "obchod u Pierra");
        Prostor vez = new Prostor("vez", "vez mistniho kouzelnika");
        Prostor joja = new Prostor("joja", "korporatni pobocka Joji");

        farma.setVychod(louka);
        louka.setVychod(farma);
        louka.setVychod(namesti);
        louka.setVychod(vez);
        namesti.setVychod(louka);
        namesti.setVychod(obchod);
        namesti.setVychod(joja);
        obchod.setVychod(namesti);
        joja.setVychod(namesti);
        vez.setVychod(louka);

        // --- Věci na farmě ---
        Vec motyka = new Vec("motyka", true);
        farma.vlozVec(motyka);
        puvodniLokaceVeci.put("motyka", farma);

        Vec konev = new Vec("konev", true);
        farma.vlozVec(konev);
        puvodniLokaceVeci.put("konev", farma);

        Vec seminko = new Vec("seminko", true);
        obchod.vlozVec(seminko);
        puvodniLokaceVeci.put("seminko", obchod);

        // --- Ostatní věci ---
        Vec klacky = new Vec("klacky", true);
        louka.vlozVec(klacky);
        puvodniLokaceVeci.put("klacky", louka);

        Vec kamen = new Vec("kamen", false);
        namesti.vlozVec(kamen);
        puvodniLokaceVeci.put("kamen", namesti);

        namesti.vlozNpc(new Npc("lewis"));
        louka.vlozNpc(new Npc("robin"));
        obchod.vlozNpc(new Npc("pierre"));
        obchod.vlozNpc(new Npc("caroline"));
        vez.vlozNpc(new Npc("kouzelnik"));
        joja.vlozNpc(new Npc("morris"));

        aktualniProstor = farma;
        this.farma = farma;  // Save reference to farma for later use
    }

    /**
     * Metoda vrací odkaz na aktuální prostor, ve ktetém se hráč právě nachází.
     *
     *@return     aktuální prostor
     */

    public Prostor getAktualniProstor() {
        return aktualniProstor;
    }

    /**
     * Metoda nastaví aktuální prostor, používá se nejčastěji při přechodu mezi prostory
     *
     *@param  prostor nový aktuální prostor
     */
    public void nastavAktualniProstor(Prostor prostor) {
        aktualniProstor = prostor;
        upozorniPozorovatele(ZmenaHry.ZMENA_MISTNOSTI);
    }

    /**
     * Upozorní pozorovatele na změnu inventáře
     */
    public void upozorniNaZmenuInventare() {
        upozorniPozorovatele(ZmenaHry.ZMENA_INVENTARE);
    }

    public Batoh getBatoh() {
        return batoh;
    }

    public boolean isZasadeno() {
        return zasazeno;
    }

    public void zasad() {
        // Kontrolu a odebrání semínka z batohu už provedl PrikazZasad.
        // Tato metoda pouze nastaví herní stav po zasazení.

        zasazeno = true;
        zalivani = 0;

        // Přidáme do farmy první fázi růstu, aby ji PrikazZalij našel
        farma.vlozVec(new Vec("rust1", false));

        // Upozorníme UI na změny (změnil se inventář v PrikazZasad
        // a změnila se místnost - přibyl "rust1")
        upozorniNaZmenuInventare();
        upozorniPozorovatele(ZmenaHry.ZMENA_MISTNOSTI);
    }

    public void zalij() {
        if (zasazeno && zalivani < 4) {
            zalivani++;
            String currentStage = "rust" + zalivani;
            String nextStage = (zalivani == 4) ? "sklizen" : "rust" + (zalivani + 1);

            farma.odeberVec(currentStage);
            farma.vlozVec(new Vec(nextStage, false));

            upozorniPozorovatele(ZmenaHry.ZMENA_MISTNOSTI);
        }
    }

    public int getZalivani() {
        return zalivani;
    }

    public void sklid() {
        // Tuto metodu volá PrikazSklid *poté*, co úspěšně
        // odebral "sklizen" z farmy a přidal "pastinak" do batohu.
        // Jediný úkol této metody je resetovat stav pěstování.

        zasazeno = false;
        zalivani = 0;

        // Upozorníme pozorovatele, že se změnil inventář (přibyl pastinak)
        // a místnost (zmizel "sklizen")
        upozorniNaZmenuInventare();
        upozorniPozorovatele(ZmenaHry.ZMENA_MISTNOSTI);
    }

    public boolean jeDozrano() {
        return zalivani >= 4;
    }

    public int getPenize() {
        return penize;
    }

    public void pridejPenize(int hodnota) {
        penize += hodnota;
    }

    public boolean odeberPenize(int hodnota) {
        if (penize < hodnota) {
            return false;
        }
        penize -= hodnota;
        return true;
    }

    // ------ NOVÁ METODA PRO VYHOZENÍ VĚCI ------
    /**
     * Pokusí se vyhodit věc z batohu. Věc se vrátí do své původní lokace.
     * @param nazevVeci Název věci k vyhození.
     * @return Textový výsledek akce.
     */
    public String vyhodVec(String nazevVeci) {
        if (!batoh.obsahujeVec(nazevVeci)) {
            return "Takovou věc v batohu nemáš.";
        }

        Prostor puvodniProstor = puvodniLokaceVeci.get(nazevVeci);
        if (puvodniProstor == null) {
            // Toto platí pro věci jako 'pastinak', které nemají původní lokaci
            return "Věc '" + nazevVeci + "' nemůžeš jen tak vyhodit, je příliš cenná.";
        }

        Vec vec = batoh.odeberVec(nazevVeci);
        puvodniProstor.vlozVec(vec); // Vrátí věc na původní místo

        upozorniNaZmenuInventare(); // Aktualizuje batoh
        upozorniPozorovatele(ZmenaHry.ZMENA_MISTNOSTI); // Aktualizuje mapu (věc se objevila)

        return "Vyhodil jsi " + nazevVeci + ".";
    }
    // ------------------------------------------

    @Override
    public void registruj(ZmenaHry zmenaHry, Pozorovatel pozorovatel) {
        seznamPozorovatelu.get(zmenaHry).add(pozorovatel);
    }

    private void upozorniPozorovatele(ZmenaHry zmenaHry) {
        for (Pozorovatel pozorovatel : seznamPozorovatelu.get(zmenaHry)) {
            pozorovatel.aktualizuj();
        }
    }
}