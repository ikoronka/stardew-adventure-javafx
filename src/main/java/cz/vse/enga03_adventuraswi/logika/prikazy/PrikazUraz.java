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
            return "Koho mám urazit? Musíš zadat jméno.";
        }

        String jmeno = parametry[0].toLowerCase(); // Převod na malá písmena pro spolehlivost
        Npc npc = plan.getAktualniProstor().najdiNpc(jmeno);

        if (npc == null) {
            return "Osoba jménem '" + jmeno + "' tu nikde není. Možná se schovává před tvými urážkami?";
        }

        // Zde začíná switch pro různé reakce
        switch (jmeno) {
            case "pierre":
                hra.setKonecHry(true);
                return "Řekl jsi Pierrovi, že je jen chamtivý lokaj, horší než Morris, a že jeho semínka jsou předražený odpad.\n" +
                        "Pierre se rozzuřil, vykopl tě z obchodu a už ti nikdy nic neprodá. Prohrál jsi.";

            case "lewis":
                return "Řekl jsi Lewisovi, že jeho 'zlatá' socha je nevkusná a že by měl trávit méně času s Marnie a více času opravou chodníků.\n" +
                        "Starosta vypadá hluboce uraženě a zamumlal něco o 'nevděčných farmářích'.";

            case "robin":
                return "Prohodil jsi jízlivou poznámku o tom, že její stavby jsou předražené a trvají věčnost.\n" +
                        "Robin vztekle sevřela své kladivo a křikla na tebe, ať si to příště zkusíš postavit sám!";

            case "caroline":
                return "Urazil jsi její rodinu tím, že jsi jejího manžela nazval ubrečeným zkrachovalcem a její dceru divnou.\n" +
                        "Caroline ti ledově oznámila, že už nejsi v jejím domě vítán.";

            case "kouzelnik":
                return "Vysmál jsi se jeho 'energetickým proudům' a nazval ho pouhým podvodníkem v hábitu.\n" +
                        "Kouzelník jen pozvedl obočí, zamumlal něco o 'ignorantských myslích' a obklopila tě fialová mlha. Cítíš se... divně.";

            case "morris":
                return "Řekl jsi Morrisovi, že je jen bezduchá korporátní krysa a že jeho plány na 'JoJa Megaplex' jsou nechutné.\n" +
                        "Morris se jen samolibě usmál a řekl: 'Pokrok je nevyhnutelný, příteli. Na rozdíl od tvé farmy.'";

            default:
                return "Urazil jsi " + npc.getJmeno() + ". Nevypadá to, že by ho/ji to nějak zvlášť zasáhlo, ale reputace ti klesla.";
        }
    }

    @Override
    public String getNazev() {
        return NAZEV;
    }
}
