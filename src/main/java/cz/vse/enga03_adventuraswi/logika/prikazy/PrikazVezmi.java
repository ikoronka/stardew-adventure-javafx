package cz.vse.enga03_adventuraswi.logika.prikazy;

import cz.vse.enga03_adventuraswi.logika.HerniPlan;
import cz.vse.enga03_adventuraswi.logika.IPrikaz;
import cz.vse.enga03_adventuraswi.logika.Prostor;
import cz.vse.enga03_adventuraswi.logika.Vec;

/**
 * Vzit vec
 */
public class PrikazVezmi implements IPrikaz {
    private static final String NAZEV = "vezmi";
    private final HerniPlan plan;

    public PrikazVezmi(HerniPlan plan) {
        this.plan = plan;
    }

    @Override
    public String provedPrikaz(String... parametry) {
        if (parametry.length == 0) {
            return "Co mám vzít?";
        }
        String nazev = parametry[0];
        Prostor aktualni = plan.getAktualniProstor();
        Vec vec = aktualni.odeberVec(nazev);
        if (vec == null) {
            return "Taková věc tu není.";
        }
        if (!vec.isPrenositelna()) {
            aktualni.vlozVec(vec);
            return "To nemůžeš sebrat.";
        }
        if (!plan.getBatoh().vlozVec(vec)) {
            aktualni.vlozVec(vec);
            return "Batoh je plný.";
        }
        return "Sebral jsi " + nazev + ".";
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}