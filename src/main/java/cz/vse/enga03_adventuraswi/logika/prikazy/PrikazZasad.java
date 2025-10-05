package cz.vse.enga03_adventuraswi.logika.prikazy;

import cz.vse.enga03_adventuraswi.logika.HerniPlan;
import cz.vse.enga03_adventuraswi.logika.IPrikaz;

/**
 * Zasadit seminko
 */
public class PrikazZasad implements IPrikaz {
    private static final String NAZEV = "zasad";
    private final HerniPlan plan;

    public PrikazZasad(HerniPlan plan) {
        this.plan = plan;
    }

    @Override
    public String provedPrikaz(String... parametry) {
        if (!plan.getAktualniProstor().getNazev().equals("farma")) {
            return "Zasadit můžeš jen na farmě.";
        }
        if (parametry.length == 0) {
            return "Co mám zasadit?";
        }
        String nazev = parametry[0];
        if (!plan.getBatoh().obsahujeVec(nazev)) {
            return "Takové semínko nemáš.";
        }
        if (!plan.getBatoh().obsahujeVec("motyka")) {
            return "Nemáš motyku.";
        }
        if (plan.isZasadeno()) {
            return "Na poli už roste plodina.";
        }
        plan.getBatoh().odeberVec(nazev);
        plan.zasad();
        return "Zasadil jsi " + nazev + ".";
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}