package cz.vse.enga03_adventuraswi.logika.prikazy;

import cz.vse.enga03_adventuraswi.logika.HerniPlan;
import cz.vse.enga03_adventuraswi.logika.Hra;
import cz.vse.enga03_adventuraswi.logika.Npc;
import cz.vse.enga03_adventuraswi.logika.Vec;
import cz.vse.enga03_adventuraswi.logika.IPrikaz;

/**
 * Příkaz na darování přenositelné věci NPCčku
 */
public class PrikazDaruj implements IPrikaz {
    private static final String NAZEV = "daruj";
    private final HerniPlan plan;
    private final Hra hra;

    public PrikazDaruj(HerniPlan plan, Hra hra) {
        this.plan = plan;
        this.hra = hra;
    }

    @Override
    public String provedPrikaz(String... parametry) {
        if (parametry.length < 2) {
            return "Komu a co mam darovat?";
        }
        String osoba = parametry[0];
        String nazev = parametry[1];

        Npc npc = plan.getAktualniProstor().najdiNpc(osoba);
        if (npc == null) {
            return "Takova osoba tu neni.";
        }

        if (!plan.getBatoh().obsahujeVec(nazev)) {
            return "Takovou vec nemas.";
        }

        if ("kouzelnik".equals(osoba)) {
            if (!"pastinak".equals(nazev)) {
                hra.setKonecHry(true);
                return "Kouzelnik je urazen a uz ti nepomuze. Prohral jsi.";
            }
            plan.getBatoh().odeberVec(nazev);
            plan.upozorniNaZmenuInventare();
            hra.setKonecHry(true);
            return "Kouzelnik dekuje za pastinak a zachrani mestecko. Vyhral jsi!";
        }

        // kapitalizace, proč ne
        return npc.getJmeno().substring(0,1).toUpperCase() + npc.getJmeno().substring(1)
                + " nevi, proc mu to davas.";
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}
