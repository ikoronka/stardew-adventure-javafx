package cz.vse.enga03_adventuraswi.logika.prikazy;

import cz.vse.enga03_adventuraswi.logika.HerniPlan;
import cz.vse.enga03_adventuraswi.logika.Hra;
import cz.vse.enga03_adventuraswi.logika.IPrikaz;
import cz.vse.enga03_adventuraswi.logika.Npc;

/**
 * Command for insulting an NPC.
 */
public class PrikazUraz implements IPrikaz {
    private static final String NAZEV = "uraz";
    private final HerniPlan plan;
    private final Hra hra;

    public PrikazUraz(HerniPlan plan, Hra hra) {
        this.plan = plan;
        this.hra = hra;
    }

    @Override
    public String provedPrikaz(String... parametry) {
        if (parametry.length == 0) {
            return "Koho mam urazit?";
        }
        String jmeno = parametry[0];
        Npc npc = plan.getAktualniProstor().najdiNpc(jmeno);
        if (npc == null) {
            return "Takova osoba tu neni.";
        }
        if ("pierre".equals(jmeno)) {
            hra.setKonecHry(true);
            return "Pierre se nastval a uz ti nic neproda. Prohral jsi.";
        }
        return "Urazil jsi " + jmeno + ".";
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}
