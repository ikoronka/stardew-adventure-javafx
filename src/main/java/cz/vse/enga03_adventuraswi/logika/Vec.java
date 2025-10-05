package cz.vse.enga03_adventuraswi.logika;

/**
 * Třída Vec představuje předměty ve hře.
 */
public class Vec {
    private final String nazev;
    private final boolean prenositelna;

    /**
     * Vytvoří novou věc.
     * @param nazev název věci
     * @param prenositelna zda je věc přenosná
     */
    public Vec(String nazev, boolean prenositelna) {
        this.nazev = nazev;
        this.prenositelna = prenositelna;
    }

    /**
     * Vrací název věci.
     */
    public String getNazev() {
        return nazev;
    }

    /**
     * Vrací informaci zda je věc přenosná.
     */
    public boolean isPrenositelna() {
        return prenositelna;
    }
}