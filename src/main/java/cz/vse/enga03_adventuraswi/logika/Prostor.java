package cz.vse.enga03_adventuraswi.logika;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import cz.vse.enga03_adventuraswi.logika.Vec;
import cz.vse.enga03_adventuraswi.logika.Npc;

/**
 * Trida Prostor - popisuje jednotlivé prostory (místnosti) hry
 *
 * Tato třída je součástí jednoduché textové hry.
 *
 * "Prostor" reprezentuje jedno místo (místnost, prostor, ..) ve scénáři hry.
 * Prostor může mít sousední prostory připojené přes východy. Pro každý východ
 * si prostor ukládá odkaz na sousedící prostor.
 *
 * @author Amelie Engelmaierová
 */

public class Prostor {

    private String nazev;
    private String popis;
    private Set<Prostor> vychody;   // obsahuje sousední místnosti
    private Set<Vec> veci;
    private Set<Npc> npcs;

    /**
     * Vytvoření prostoru se zadaným popisem, např. "kuchyň", "hala", "trávník
     * před domem"
     *
     * @param nazev nazev prostoru, jednoznačný identifikátor, jedno slovo nebo
     * víceslovný název bez mezer.
     * @param popis Popis prostoru.
     */
    public Prostor(String nazev, String popis) {
        this.nazev = nazev;
        this.popis = popis;
        vychody = new HashSet<>();
        veci = new HashSet<>();
        npcs = new HashSet<>();
    }

    /**
     * Definuje východ z prostoru
     *
     * @param vedlejsi prostor, který sousedi s aktualnim prostorem.
     *
     */
    public void setVychod(Prostor vedlejsi) {
        vychody.add(vedlejsi);
    }

    /**
     * Metoda equals pro porovnání dvou prostorů.
     *
     * @param o object, který se má porovnávat s aktuálním
     * @return hodnotu true, pokud má zadaný prostor stejný název, jinak false
     */  
      @Override
    public boolean equals(Object o) {
        // porovnáváme zda se nejedná o dva odkazy na stejnou instanci
        if (this == o) {
            return true;
        }

        if (!(o instanceof Prostor)) {
            return false;
        }
        // přetypujeme parametr na typ Prostor 
        Prostor druhy = (Prostor) o;

       return (java.util.Objects.equals(this.nazev, druhy.nazev));       
    }

    @Override
    public int hashCode() {
        int vysledek = 3;
        int hashNazvu = java.util.Objects.hashCode(this.nazev);
        vysledek = 37 * vysledek + hashNazvu;
        return vysledek;
    }
      

    /**
     * Vrací název prostoru (byl zadán při vytváření prostoru jako parametr
     * konstruktoru)
     *
     * @return název prostoru
     */
    public String getNazev() {
        return nazev;       
    }

    /**
     * Vrací "dlouhý" popis prostoru, který může vypadat následovně: Jsi v
     * mistnosti/prostoru vstupni hala budovy VSE na Jiznim meste. vychody:
     * chodba bufet ucebna
     *
     * @return Dlouhý popis prostoru
     */
    public String dlouhyPopis() {
        return "Jsi v mistnosti/prostoru " + popis + ".\n"
                + popisVychodu() + seznamVeci() + seznamNpc();
    }

    /**
     * Vrací textový řetězec, který popisuje sousední východy, například:
     * "vychody: hala ".
     *
     * @return Popis východů - názvů sousedních prostorů
     */
    private String popisVychodu() {
        String vracenyText = "východy:";
        for (Prostor sousedni : vychody) {
            vracenyText += " " + sousedni.getNazev();
        }
        return vracenyText;
    }

    /**
     * Vrací prostor, který sousedí s aktuálním prostorem a jehož název je zadán
     * jako parametr. Pokud prostor s udaným jménem nesousedí s aktuálním
     * prostorem, vrací se hodnota null.
     *
     * @param nazevSouseda Jméno sousedního prostoru (východu)
     * @return Prostor, který se nachází za příslušným východem, nebo hodnota
     * null, pokud prostor zadaného jména není sousedem.
     */
    public Prostor vratSousedniProstor(String nazevSouseda) {
        List<Prostor>hledaneProstory = 
            vychody.stream()
                   .filter(sousedni -> sousedni.getNazev().equals(nazevSouseda))
                   .collect(Collectors.toList());
        if(hledaneProstory.isEmpty()){
            return null;
        }
        else {
            return hledaneProstory.get(0);
        }
    }

    /**
     * Vrací kolekci obsahující prostory, se kterými tento prostor sousedí.
     * Takto získaný seznam sousedních prostor nelze upravovat (přidávat,
     * odebírat východy) protože z hlediska správného návrhu je to plně
     * záležitostí třídy Prostor.
     *
     * @return Nemodifikovatelná kolekce prostorů (východů), se kterými tento
     * prostor sousedí.
     */
    public Collection<Prostor> getVychody() {
        return Collections.unmodifiableCollection(vychody);
    }

    /**
     * Vloží věc do prostoru.
     */
    public void vlozVec(Vec vec) {
        veci.add(vec);
    }

    /**
     * Odebere věc z prostoru podle názvu.
     * @return odebraná věc nebo null
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
     * Vrací textový popis věcí v prostoru.
     */
    public String seznamVeci() {
        if (veci.isEmpty()) {
            return "";
        }
        return " Jsou zde: " + veci.stream()
                .map(Vec::getNazev)
                .collect(Collectors.joining(", "));
    }

    /**
     * Vlozi postavu do prostoru.
     */
    public void vlozNpc(Npc npc) {
        npcs.add(npc);
    }

    /**
     * Najde postavu podle jmena.
     */
    public Npc najdiNpc(String jmeno) {
        for (Npc n : npcs) {
            if (n.getJmeno().equals(jmeno)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Vraci textovy popis postav v prostoru.
     */
    public String seznamNpc() {
        if (npcs.isEmpty()) {
            return "";
        }
        return " Jsou tu: " + npcs.stream()
                .map(Npc::getJmeno)
                .collect(Collectors.joining(", "));
    }

    /**
     * Vrací věc podle názvu bez odebrání.
     */
    public Vec najdiVec(String nazev) {
        for (Vec v : veci) {
            if (v.getNazev().equals(nazev)) {
                return v;
            }
        }
        return null;
    }
}
