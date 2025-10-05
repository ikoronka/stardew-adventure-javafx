// VecTest.java
package cz.vse.enga03_adventuraswi.logika;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření správného vytvoření a vlastností věcí (Vec).
 */
public class VecTest {

    @Test
    public void testVytvoreniPrenositelneVec() {
        Vec vec = new Vec("klic", true);
        assertEquals("klic", vec.getNazev(), "Název věci by měl být 'klic'.");
        assertTrue(vec.isPrenositelna(), "Věc 'klic' by měla být přenositelná.");
    }

    @Test
    public void testVytvoreniNeprenositelneVec() {
        Vec vec = new Vec("obal", false);
        assertEquals("obal", vec.getNazev(), "Název věci by měl být 'obal'.");
        assertFalse(vec.isPrenositelna(), "Věc 'obal' by neměla být přenositelná.");
    }
}