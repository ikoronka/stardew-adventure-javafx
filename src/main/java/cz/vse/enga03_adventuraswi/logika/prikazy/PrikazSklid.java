package cz.vse.enga03_adventuraswi.logika.prikazy;

import cz.vse.enga03_adventuraswi.logika.HerniPlan;
import cz.vse.enga03_adventuraswi.logika.IPrikaz;
import cz.vse.enga03_adventuraswi.logika.Vec;

/**
 * Command harvests the grown crop.
 */
public class PrikazSklid implements IPrikaz {
    private static final String NAZEV = "sklid";
    private final HerniPlan plan;

    public PrikazSklid(HerniPlan plan) {
        this.plan = plan;
    }

    @Override
    public String provedPrikaz(String... parametry) {
        if (!plan.getAktualniProstor().getNazev().equals("farma")) {
            return "Sklidit můžeš jen na farmě.";
        }

        // Kontrola motyky
        if (!plan.getBatoh().obsahujeVec("motyka")) {
            return "Nemáš motyku, nemůžeš sklízet.";
        }

        // Zkusíme z místnosti odebrat předmět "sklizen"
        Vec sklizen = plan.getAktualniProstor().odeberVec("sklizen");

        // Pokud v místnosti "sklizen" není, není co sklízet
        if (sklizen == null) {
            return "Není co sklízet. Ujisti se, že je plodina plně dorostlá.";
        }

        // Vytvoříme novou věc "pastinak", kterou dáme hráči
        Vec pastinak = new Vec("pastinak", true); // true = přenositelný

        // Pokusíme se vložit pastinak do batohu
        if (!plan.getBatoh().vlozVec(pastinak)) {
            // Pokud se to nepovedlo (plný batoh), vrátíme "sklizen" zpět do místnosti
            plan.getAktualniProstor().vlozVec(sklizen);
            return "Batoh je plný. Nemůžeš sklidit pastinak.";
        }

        // Pokud se vše povedlo:
        // 1. Pastinak je v batohu.
        // 2. "sklizen" je pryč z místnosti.
        // 3. Teď musíme resetovat stav farmy v HerniPlanu.
        plan.sklid(); // Tato metoda teď bude jen resetovat stav

        return "Sklidil jsi pastinak.";
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}