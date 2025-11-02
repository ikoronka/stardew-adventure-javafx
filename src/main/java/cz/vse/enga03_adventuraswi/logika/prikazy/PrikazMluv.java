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
        String msg = "";
        switch (jmeno) {
            case "lewis":
                if (!npc.isGaveMoney()) {
                    plan.pridejPenize(50);
                    npc.setGaveMoney(true);
                    msg = "Á, nový farmář! Jakpak ti to na dědečkově farmě šlape? \n";
                    msg += "Víš, s tvým dědečkem jsem se dobře znal, byl to dobrý muž. \n";
                    msg += "Na, tady máš 50g, ať se ti to na farmě rozjede!";
                    return msg;
                }
                return "Lewis ti preje hodne stesti a doufa, ze Joja nevyhraje.";
            case "pierre":
                if (plan.getPenize() < 50) {
                    msg = "Dobré poledne, máme v nabídce širokou škálu semínek.\n";
                    msg += "borůvky  ...  80g\n";
                    msg += "meloun   ... 100g\n";
                    msg += "pastiňák ...  50g\n";
                    return msg;
                }
                if (!plan.odeberPenize(50)) {
                    return "Potrebujes 50g.";
                }
                Vec seminko = new Vec("seminko", true);
                if (!plan.getBatoh().vlozVec(seminko)) {
                    plan.getAktualniProstor().vlozVec(seminko);
                    return "Batoh je plny, zkus ho vyprázdnit!";
                }
                plan.upozorniNaZmenuInventare();
                return "Pierre ti prodal seminko.";
            case "kouzelnik":
                msg = msg + "Kouzelnik si prohlizi tvou karmu a ceka na pastinak.";
                msg = msg + " Pry to jde do lektvaru na lepsi naladu.";
                return msg;
            case "morris":
                msg = "Víte, lidé jako starosta Lewis nebo Pierre, ti vidí jen to, co je.\n";
                msg += "Zastaralé budovy, neefektivní malé krámky, sentiment... Ztracený potenciál.";
                return msg;
            case "robin":
                msg = "Perfektní den na trocha práce, nemyslíš si?\n";
                msg += "Zrovna tady hledám nějaké kvalitní duby pro nový projekt.\n";
                msg += "Pokud budeš potřebovat vylepšit dům nebo postavit kurník, dej vedět.";
                return msg;
            default:
                return "Nemate si co rict.";
        }
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}
