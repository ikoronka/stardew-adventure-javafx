package cz.vse.enga03_adventuraswi.logika.prikazy;

import cz.vse.enga03_adventuraswi.logika.HerniPlan;
import cz.vse.enga03_adventuraswi.logika.IPrikaz;

/**
 * Vytiskne inventar a jeho kapacitu
 */
public class PrikazInventar implements IPrikaz {
    private static final String NAZEV = "inventar";
    private final HerniPlan plan;

    public PrikazInventar(HerniPlan plan) {
        this.plan = plan;
    }

    @Override
    public String provedPrikaz(String... parametry) {
        return plan.getBatoh().seznamVeci();
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}