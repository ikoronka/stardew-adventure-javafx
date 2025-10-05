package cz.vse.enga03_adventuraswi.logika.prikazy;

import cz.vse.enga03_adventuraswi.logika.HerniPlan;
import cz.vse.enga03_adventuraswi.logika.Hra;
import cz.vse.enga03_adventuraswi.logika.IPrikaz;
import cz.vse.enga03_adventuraswi.logika.Npc;
import cz.vse.enga03_adventuraswi.logika.Vec;

/**
 * Command for talking to characters.
 */
public class PrikazMluv implements IPrikaz {
    private static final String NAZEV = "mluv";
    private final HerniPlan plan;
    private final Hra hra;

    public PrikazMluv(HerniPlan plan, Hra hra) {
        this.plan = plan;
        this.hra = hra;
    }

    @Override
    public String provedPrikaz(String... parametry) {
        if (parametry.length == 0) {
            return "S kym mam mluvit?";
        }
        String jmeno = parametry[0];
        Npc npc = plan.getAktualniProstor().najdiNpc(jmeno);
        if (npc == null) {
            return "Takova osoba tu neni.";
        }
        switch (jmeno) {
            case "lewis":
                if (!npc.isGaveMoney()) {
                    plan.pridejPenize(50);
                    npc.setGaveMoney(true);
                    return "Lewis ti da 50g na rozjezd a mrkne na tebe spiklenecky.";
                }
                return "Lewis ti preje hodne stesti a doufa, ze Joja nevyhraje.";
            case "pierre":
                if (plan.getPenize() < 50) {
                    return "Potrebujes 50g.";
                }
                if (!plan.odeberPenize(50)) {
                    return "Potrebujes 50g.";
                }
                Vec semeno = new Vec("semeno", true);
                if (!plan.getBatoh().vlozVec(semeno)) {
                    plan.getAktualniProstor().vlozVec(semeno);
                    return "Batoh je plny, semeno je na zemi.";
                }
                return "Pierre ti prodal semeno.";
            case "kouzelnik":
                String msg = "";
                msg = msg + "Kouzelnik si prohlizi tvou karmu a ceka na pastinak.";
                msg = msg + " Pry to jde do lektvaru na lepsi naladu.";
                return msg;
            case "morriss":
                return "Morriss se ti pochlubi svym zlym planem promenit mestecko v obrovsky nakupni mall.";
            default:
                return "Nemate si co rict.";
        }
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}
