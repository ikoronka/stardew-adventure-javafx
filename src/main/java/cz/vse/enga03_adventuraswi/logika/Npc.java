package cz.vse.enga03_adventuraswi.logika;

/**
 * Simple representation of a character in the game.
 */
public class Npc {
    private final String jmeno;
    private boolean gaveMoney;

    public Npc(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getJmeno() {
        return jmeno;
    }

    public boolean isGaveMoney() {
        return gaveMoney;
    }

    public void setGaveMoney(boolean gaveMoney) {
        this.gaveMoney = gaveMoney;
    }
}
