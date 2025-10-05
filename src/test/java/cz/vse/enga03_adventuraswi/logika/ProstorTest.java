package cz.vse.enga03_adventuraswi.logika;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro otestování funkcionality třídy Prostor.
 */
public class ProstorTest {

    private Prostor prostorKuchyn;
    private Prostor prostorObyvaci;
    private Prostor prostorLoznice;
    private Vec vecHrnce;
    private Vec vecLzice;

    @BeforeEach
    public void setUp() {
        prostorKuchyn = new Prostor("kuchyn", "Veselá kuchyně s novým vybavením.");
        prostorObyvaci = new Prostor("obyvaci", "Pohodlný obývací pokoj s krbem.");
        prostorLoznice = new Prostor("loznice", "Klidná ložnice s velkým oknem.");

        prostorKuchyn.setVychod(prostorObyvaci);
        prostorKuchyn.setVychod(prostorLoznice);

        vecHrnce = new Vec("hrnec", true);
        vecLzice = new Vec("lzice", false);
    }

    @Test
    public void testVratSousedniProstor() {
        assertEquals(prostorObyvaci, prostorKuchyn.vratSousedniProstor("obyvaci"));
        assertEquals(prostorLoznice, prostorKuchyn.vratSousedniProstor("loznice"));
        assertNull(prostorKuchyn.vratSousedniProstor("koupelna"));
    }

    @Test
    public void testVlozeniAOdebraniVec() {
        prostorKuchyn.vlozVec(vecHrnce);
        prostorKuchyn.vlozVec(vecLzice);

        Vec odebranaVec = prostorKuchyn.odeberVec("hrnec");
        assertEquals(vecHrnce, odebranaVec);

        assertNull(prostorKuchyn.odeberVec("hrnec"));
    }

    @Test
    public void testEqualsAHeshCode() {
        Prostor prostorKopie = new Prostor("kuchyn", "Alternativní popis kuchyně.");
        assertTrue(prostorKuchyn.equals(prostorKopie));
        assertEquals(prostorKuchyn.hashCode(), prostorKopie.hashCode());
    }

    @Test
    public void testSeznamVeci() {
        prostorKuchyn.vlozVec(vecHrnce);
        prostorKuchyn.vlozVec(vecLzice);

        String seznam = prostorKuchyn.seznamVeci();
        assertTrue(seznam.contains("hrnec"));
        assertTrue(seznam.contains("lzice"));
    }

    @Test
    public void testDlouhyPopis() {
        String popis = prostorKuchyn.dlouhyPopis();
        assertTrue(popis.contains("Veselá kuchyně s novým vybavením."));
        assertTrue(popis.contains("obyvaci"));
        assertTrue(popis.contains("loznice"));
    }
}