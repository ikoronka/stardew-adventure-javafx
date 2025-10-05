// InventarTest.java
package cz.vse.enga03_adventuraswi.logika;

import cz.vse.enga03_adventuraswi.logika.prikazy.PrikazInventar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření chování inventáře (batohu).
 * Testuje se zejména kapacita inventáře a chování při pokusu
 * o vložení předmětu, když je inventář plný.
 */
public class InventarTest {

    private HerniPlan plan;
    private PrikazInventar prikazInventar;
    
    // předpokládáme kapacitu 5
    private static final int MAX_KAPACITA = 5;

    @BeforeEach
    public void setUp() {
        Hra hra = new Hra();
        plan = hra.getHerniPlan();
        prikazInventar = new PrikazInventar(plan);
    }

    @Test
    public void testPridejVecDoInventare() {
        for (int i = 1; i <= MAX_KAPACITA; i++) {
            Vec vec = new Vec("vec" + i, true);
            assertTrue(plan.getBatoh().vlozVec(vec), "Vložení vec" + i + " by mělo být úspěšné.");
        }
        String seznam = plan.getBatoh().seznamVeci();
        for (int i = 1; i <= MAX_KAPACITA; i++) {
            assertTrue(seznam.contains("vec" + i), "Seznam inventáře by měl obsahovat vec" + i);
        }
    }

    @Test
    public void testKapacitaInventare() {
        for (int i = 1; i <= MAX_KAPACITA; i++) {
            Vec vec = new Vec("predmet" + i, true);
            assertTrue(plan.getBatoh().vlozVec(vec), "Vložení predmet" + i + " by mělo proběhnout úspěšně.");
        }
        Vec extraVec = new Vec("extra", true);
        assertFalse(plan.getBatoh().vlozVec(extraVec), "Vložení extra předmětu by mělo selhat, protože inventář je plný.");
    }

    @Test
    public void testInventarPomociPrikazuInventar() {
        Vec vecA = new Vec("klic", true);
        Vec vecB = new Vec("mapa", true);
        plan.getBatoh().vlozVec(vecA);
        plan.getBatoh().vlozVec(vecB);

        String vystup = prikazInventar.provedPrikaz();
        assertTrue(vystup.contains("klic"), "Inventář by měl obsahovat položku 'klic'.");
        assertTrue(vystup.contains("mapa"), "Inventář by měl obsahovat položku 'mapa'.");
    }
}