package cz.vse.enga03_adventuraswi.logika;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Batoh uchovává věci, které hráč nese.
 */
public class Batoh {
    private final int kapacita;
    private final Set<Vec> veci;

    /**
     * Vytvoří batoh s danou kapacitou.
     */
    public Batoh(int kapacita) {
        this.kapacita = kapacita;
        this.veci = new HashSet<>();
    }

    /**
     * Pokusí se vložit věc do batohu.
     * @param vec vkládaná věc
     * @return true pokud se vložení podařilo
     */
    public boolean vlozVec(Vec vec) {
        if (veci.size() >= kapacita) {
            return false;
        }
        return veci.add(vec);
    }

    /**
     * Vyjme věc z batohu podle názvu.
     * @param nazev název věci
     * @return vyjmutá věc nebo null
     */
    public Vec odeberVec(String nazev) {
        for (Vec v : veci) {
            if (v.getNazev().equals(nazev)) {
                veci.remove(v);
                return v;
            }
        }
        return null;
    }

    /**
     * Zjistí zda je v batohu daná věc.
     */
    public boolean obsahujeVec(String nazev) {
        for (Vec v : veci) {
            if (v.getNazev().equals(nazev)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vypíše obsah batohu.
     */
    public String seznamVeci() {
        if (veci.isEmpty()) {
            return "Batoh je prázdný.";
        }
        return "Batoh má kapacitu " + kapacita + " věcí \n" +
                "V batohu máš: " +
                veci.stream().map(Vec::getNazev)
                        .collect(Collectors.joining(", "));
    }
}