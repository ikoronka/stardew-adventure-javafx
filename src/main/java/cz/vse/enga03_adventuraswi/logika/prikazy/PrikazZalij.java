package cz.vse.enga03_adventuraswi.logika.prikazy;

import cz.vse.enga03_adventuraswi.logika.HerniPlan;
import cz.vse.enga03_adventuraswi.logika.IPrikaz;

/**
 * Zalit rostlinku
 */
public class PrikazZalij implements IPrikaz {
    private static final String NAZEV = "zalij";
    private final HerniPlan plan;

    public PrikazZalij(HerniPlan plan) {
        this.plan = plan;
    }

    @Override
    public String provedPrikaz(String... parametry) {
        if (!plan.getAktualniProstor().getNazev().equals("farma")) {
            return "Zalévat můžeš jen na farmě.";
        }
        if (!plan.getBatoh().obsahujeVec("konev")) {
            return "Nemáš konev.";
        }
        if (!plan.isZasadeno()) {
            return "Nic tu neroste.";
        }
        plan.zalij();
        if (plan.jeDozrano()) {
            return "Plodina je připravena ke sklizni.";
        }
        return "Zalito.";
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}