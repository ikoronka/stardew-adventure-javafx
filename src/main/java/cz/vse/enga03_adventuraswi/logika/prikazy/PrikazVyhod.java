package cz.vse.enga03_adventuraswi.logika.prikazy;

import cz.vse.enga03_adventuraswi.logika.HerniPlan;
import cz.vse.enga03_adventuraswi.logika.Hra;
import cz.vse.enga03_adventuraswi.logika.IHra;
import cz.vse.enga03_adventuraswi.logika.IPrikaz;

/**
 * Třída implementující příkaz pro vyhození věci z batohu.
 */
public class PrikazVyhod implements IPrikaz {
    private static final String NAZEV = "vyhod";
    private HerniPlan plan;
    private Hra hra;

    /**
     * Konstruktor třídy.
     *
     * @param plan herní plán, ve kterém se bude příkaz provádět
     * @param hra  reference na hru pro možnost ukončení hry
     */
    public PrikazVyhod(HerniPlan plan, Hra hra) { // <-- UPRAVENO
        this.plan = plan;
        this.hra = hra;
    }

    /**
     * Provádí příkaz "vyhod".
     * Pokud hráč vyhodí "seminko", hra končí prohrou.
     *
     * @param parametry jako parametr se očekává název věci, kterou chce hráč vyhodit.
     * @return zpráva, která se vypíše hráči.
     */
    @Override
    public String provedPrikaz(String... parametry) {
        if (parametry.length == 0) {
            return "Co mám vyhodit? Musíš napsat název věci.";
        }

        String nazevVeci = parametry[0];

        // Speciální logika pro prohru
        if (nazevVeci.equals("seminko")) {
            String vysledek = plan.vyhodVec(nazevVeci); // Nejdřív věc vyhodíme
            if (vysledek.startsWith("Vyhodil jsi")) { // Jen pokud se to povedlo
                hra.setKonecHry(true); // Ukončíme hru
                return "Vyhodil jsi semínko... Tvoje jediná naděje na záchranu farmy je pryč. Prohrál jsi.";
            }
            return vysledek; // Vrátí chybovou hlášku (např. "nemáš v batohu")
        }

        // Běžné vyhození pro ostatní věci
        return plan.vyhodVec(nazevVeci);
    }

    /**
     * Metoda vrací název příkazu (slovo, které hráč zadá pro jeho spuštění).
     *
     * @return název příkazu
     */
    @Override
    public String getNazev() {
        return NAZEV;
    }
}