package cz.vse.enga03_adventuraswi.logika.prikazy;

import cz.vse.enga03_adventuraswi.logika.IPrikaz;
import cz.vse.enga03_adventuraswi.logika.SeznamPrikazu;

/**
 *  Command implementing help for the game.
 *  Tato třída je součástí jednoduché textové hry.
 *  
 *@author     Amelie Engelmaierová
 *  
 */
public class PrikazNapoveda implements IPrikaz {

    private static final String NAZEV = "napoveda";
    private SeznamPrikazu platnePrikazy;


    /**
     *  Konstruktor třídy
     *
     *  @param platnePrikazy seznam příkazů,
     *                       které je možné ve hře použít,
     *                       aby je nápověda mohla zobrazit uživateli.
     */
    public PrikazNapoveda(SeznamPrikazu platnePrikazy) {
        this.platnePrikazy = platnePrikazy;
    }

    /**
     *  Vrací základní nápovědu po zadání příkazu "napoveda". Nyní se vypisuje
     *  vcelku primitivní zpráva a seznam dostupných příkazů.
     *
     *  @return napoveda ke hre
     */
    @Override
    public String provedPrikaz(String... parametry) {
        return "Tvym cilem je vypestovat pastinak a darovat ho kouzelnikovi.\n"
                + "\n"
                + "Muzes zadat tyto prikazy:\n"
                + platnePrikazy.vratNazvyPrikazu();
    }

    /**
     *  Metoda vrací název příkazu (slovo které používá hráč pro jeho vyvolání)
     *
     *  @ return nazev prikazu
     */
    @Override
    public String getNazev() {
        return NAZEV;
    }

}