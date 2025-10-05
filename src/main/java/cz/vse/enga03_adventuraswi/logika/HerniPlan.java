package cz.vse.enga03_adventuraswi.logika;

import cz.vse.enga03_adventuraswi.logika.Vec;
import cz.vse.enga03_adventuraswi.logika.Npc;

/**
 *  Class HerniPlan - třída představující mapu a stav adventury.
 *
 *  Tato třída inicializuje prvky ze kterých se hra skládá:
 *  vytváří všechny prostory,
 *  propojuje je vzájemně pomocí východů
 *  a pamatuje si aktuální prostor, ve kterém se hráč právě nachází.
 *
 *@author     Amelie Engelmaierová
 */
public class HerniPlan {

    private Prostor aktualniProstor;
    private Batoh batoh;
    private Prostor farma;
    private boolean zasazeno;
    private int zalivani;
    private int penize;

    /**
     *  Konstruktor který vytváří jednotlivé prostory a propojuje je pomocí východů.
     *  Jako výchozí aktuální prostor nastaví halu.
     */
    public HerniPlan() {
        batoh = new Batoh(5);
        zalozProstoryHry();

    }
    /**
     *  Vytváří jednotlivé prostory a propojuje je pomocí východů.
     *  Jako výchozí aktuální prostor nastaví domeček.
     */
    private void zalozProstoryHry() {
        farma = new Prostor("farma", "zarostla farma tveho dedecka");
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

        farma.vlozVec(new Vec("motyka", true));
        farma.vlozVec(new Vec("konev", true));
        farma.vlozVec(new Vec("klacky", false));
        farma.vlozVec(new Vec("kamen", false));


        namesti.vlozNpc(new Npc("lewis"));
        louka.vlozNpc(new Npc("robin"));
        obchod.vlozNpc(new Npc("pierre"));
        obchod.vlozNpc(new Npc("caroline"));
        vez.vlozNpc(new Npc("kouzelnik"));
        joja.vlozNpc(new Npc("morriss"));

        aktualniProstor = farma;
    }

    /**
     *  Metoda vrací odkaz na aktuální prostor, ve ktetém se hráč právě nachází.
     *
     *@return     aktuální prostor
     */

    public Prostor getAktualniProstor() {
        return aktualniProstor;
    }

    /**
     *  Metoda nastaví aktuální prostor, používá se nejčastěji při přechodu mezi prostory
     *
     *@param  prostor nový aktuální prostor
     */
    public void setAktualniProstor(Prostor prostor) {
        aktualniProstor = prostor;
    }

    public Batoh getBatoh() {
        return batoh;
    }

    public boolean isZasadeno() {
        return zasazeno;
    }

    public void zasad() {
        zasazeno = true;
        zalivani = 0;
    }

    public void zalij() {
        if (zasazeno) {
            zalivani++;
            if (zalivani >= 4 && farma.najdiVec("pastinak") == null) {
                farma.vlozVec(new Vec("pastinak", true));
            }
        }
    }

    public boolean jeDozrano() {
        return zalivani >= 4;
    }

    public void sklid() {
        zasazeno = false;
        zalivani = 0;
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

}