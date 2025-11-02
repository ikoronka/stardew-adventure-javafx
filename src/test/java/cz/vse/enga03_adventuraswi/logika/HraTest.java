package cz.vse.enga03_adventuraswi.logika;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*******************************************************************************
 * Testovací třída HraTest slouží ke komplexnímu otestování
 * třídy Hra
 *
 * @author    Amelie Engelmaierová
 */
public class HraTest {
    private Hra hra1;

    //== Datové atributy (statické i instancí)======================================

    //== Konstruktory a tovární metody =============================================
    //-- Testovací třída vystačí s prázdným implicitním konstruktorem ----------

    //== Příprava a úklid přípravku ================================================

    /***************************************************************************
     * Metoda se provede před spuštěním každé testovací metody. Používá se
     * k vytvoření tzv. přípravku (fixture), což jsou datové atributy (objekty),
     * s nimiž budou testovací metody pracovat.
     */
    @BeforeEach
    public void setUp() {
        hra1 = new Hra();
    }


    //== Soukromé metody používané v testovacích metodách ==========================

    //== Vlastní testovací metody ==================================================

    /***************************************************************************
     * Testuje průběh hry, po zavolání každěho příkazu testuje, zda hra končí
     * a v jaké aktuální místnosti se hráč nachází.
     * Při dalším rozšiřování hry doporučujeme testovat i jaké věci nebo osoby
     * jsou v místnosti a jaké věci jsou v batohu hráče.
     * 
     */
    @Test
    public void testPrubehHry() {
        assertEquals("farma", hra1.getHerniPlan().getAktualniProstor().getNazev());
        hra1.zpracujPrikaz("vezmi motyka");
        hra1.zpracujPrikaz("vezmi konev");
        hra1.zpracujPrikaz("jdi louka");
        hra1.zpracujPrikaz("jdi namesti");
        hra1.zpracujPrikaz("mluv lewis");
        hra1.zpracujPrikaz("jdi obchod");
        hra1.zpracujPrikaz("mluv pierre");
        hra1.zpracujPrikaz("jdi namesti");
        hra1.zpracujPrikaz("jdi louka");
        hra1.zpracujPrikaz("jdi farma");
        hra1.zpracujPrikaz("zasad seminko");
        hra1.zpracujPrikaz("zalij");
        hra1.zpracujPrikaz("zalij");
        hra1.zpracujPrikaz("zalij");
        hra1.zpracujPrikaz("zalij");
        hra1.zpracujPrikaz("sklid");
        hra1.zpracujPrikaz("jdi louka");
        hra1.zpracujPrikaz("jdi vez");
        hra1.zpracujPrikaz("daruj kouzelnik pastinak");
        assertEquals(true, hra1.konecHry());
    }
}