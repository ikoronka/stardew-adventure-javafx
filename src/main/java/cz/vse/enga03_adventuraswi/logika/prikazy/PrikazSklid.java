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
        Vec pastinak = plan.getAktualniProstor().odeberVec("pastinak");
        if (pastinak == null) {
            return "Není co sklízet.";
        }
        if (!plan.getBatoh().vlozVec(pastinak)) {
            plan.getAktualniProstor().vlozVec(pastinak);
            return "Batoh je plný.";
        }
        plan.sklid();
        return "Sklidil jsi pastinak.";
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}
