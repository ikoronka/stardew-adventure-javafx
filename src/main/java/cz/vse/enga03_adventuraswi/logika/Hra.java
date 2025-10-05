package cz.vse.enga03_adventuraswi.logika;

import cz.vse.enga03_adventuraswi.logika.prikazy.*;
/**
 *  Třída Hra - třída představující logiku adventury.
 *
 *
 *@author     Amelie Engelmaierová
 */

public class Hra implements IHra {
    private SeznamPrikazu platnePrikazy;    // obsahuje seznam přípustných příkazů
    private HerniPlan herniPlan;
    private boolean konecHry = false;

    /**
     *  Vytváří hru a inicializuje místnosti (prostřednictvím třídy HerniPlan) a seznam platných příkazů.
     */
    public Hra() {
        herniPlan = new HerniPlan();
        platnePrikazy = new SeznamPrikazu();
        platnePrikazy.vlozPrikaz(new PrikazNapoveda(platnePrikazy));
        platnePrikazy.vlozPrikaz(new PrikazJdi(herniPlan));
        platnePrikazy.vlozPrikaz(new PrikazKonec(this));
        platnePrikazy.vlozPrikaz(new PrikazVezmi(herniPlan));
        platnePrikazy.vlozPrikaz(new PrikazInventar(herniPlan));
        platnePrikazy.vlozPrikaz(new PrikazZasad(herniPlan));
        platnePrikazy.vlozPrikaz(new PrikazZalij(herniPlan));
        platnePrikazy.vlozPrikaz(new PrikazSklid(herniPlan));
        platnePrikazy.vlozPrikaz(new PrikazDaruj(herniPlan, this));
        platnePrikazy.vlozPrikaz(new PrikazMluv(herniPlan, this));
        platnePrikazy.vlozPrikaz(new PrikazUraz(herniPlan, this));
    }

    /**
     *  Vrátí úvodní zprávu pro hráče.
     */
    public String vratUvitani() {
        String uvod = "Vitejte na dedeckove farme!\n" +
                "Po dlouhých letech v korporátu Joja se rozhodnete navrátit se k rodinnému způsobu života\n" +
                "odstěhováním na farmu zesnulého dědečka. Farma je avšak zarostlá a zchátralá a zlý\n" +
                "korporát Joja chce skoupit všechny pozemky a místo městečka postavit nákupní centrum.\n" +
                "Tvým úkolem je obnovit farmu a najít špínu na Joju, která zajistí, že městečko zůstane v\n" +
                "původním stavu \n\n" +
                "=== CÍL HRY ===\n" +
                "Obnovte hospodarstvi a darujte kouzelnikovi pastinak.\n" +
                "Napis 'napoveda', pokud si nevis rady, jak hrat dal.\n";
        return uvod + herniPlan.getAktualniProstor().dlouhyPopis();
    }

    /**
     *  Vrátí závěrečnou zprávu pro hráče.
     */
    public String vratEpilog() {
        return "Dík, že jste si zahráli. Doufám, že farmu nezapálíte hned zítra. Ahoj.";
    }

    /**
     * Vrací true, pokud hra skončila.
     */
    public boolean konecHry() {
        return konecHry;
    }

    /**
     *  Metoda zpracuje řetězec uvedený jako parametr, rozdělí ho na slovo příkazu a další parametry.
     *  Pak otestuje zda příkaz je klíčovým slovem  např. jdi.
     *  Pokud ano spustí samotné provádění příkazu.
     *
     *@param  radek  text, který zadal uživatel jako příkaz do hry.
     *@return          vrací se řetězec, který se má vypsat na obrazovku
     */
    public String zpracujPrikaz(String radek) {
        String [] slova = radek.split("[ \t]+");
        String slovoPrikazu = slova[0];
        String []parametry = new String[slova.length-1];
        for(int i=0 ;i<parametry.length;i++){
            parametry[i]= slova[i+1];
        }
        String textKVypsani=" .... ";
        if (platnePrikazy.jePlatnyPrikaz(slovoPrikazu)) {
            IPrikaz prikaz = platnePrikazy.vratPrikaz(slovoPrikazu);
            textKVypsani = prikaz.provedPrikaz(parametry);
        }
        else {
            textKVypsani="Nevím co tím myslíš? Tento příkaz neznám. ";
        }
        return textKVypsani;
    }


    /**
     *  Nastaví, že je konec hry, metodu využívá třída PrikazKonec,
     *  mohou ji použít i další implementace rozhraní Prikaz.
     *
     *  @param  konecHry  hodnota false= konec hry, true = hra pokračuje
     */
    public void setKonecHry(boolean konecHry) {
        this.konecHry = konecHry;
    }

    /**
     *  Metoda vrátí odkaz na herní plán, je využita hlavně v testech,
     *  kde se jejím prostřednictvím získává aktualní místnost hry.
     *
     *  @return     odkaz na herní plán
     */
    public HerniPlan getHerniPlan(){
        return herniPlan;
    }

}