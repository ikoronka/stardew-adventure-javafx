package cz.vse.enga03_adventuraswi.logika;

/**
 *  Třída implementující toto rozhraní bude ve hře zpracovávat jeden konkrétní příkaz.
 *  Toto rozhraní je součástí jednoduché textové hry.
 *  
 *@author     Amelie Engelmaierová
 *  
 */
public interface IPrikaz {
	
	/**
     *  Metoda pro provedení příkazu ve hře.
     *  
     *  @param parametry počet parametrů závisí na konkrétním příkazu.
     *  
     */
    public String provedPrikaz(String... parametry);
    
    /**
     *  Metoda vrací název příkazu (slovo které používá hráč pro jeho vyvolání)
     *  
     *  @return nazev prikazu
     */
	public String getNazev();
	
}
