package kvk.korjaaja;

/**
 * Määrittää kirjoitusvirheen korjaajan. 
 */
public interface IKorjaaja {
    
    /**
     * Selvittää jotain tapaa käyttäen, onko parametrina annettu sana virheellinen
     * @param sana
     * @return true jos sana on virheellinen
     */
    boolean onkoSanaVirheellinen(String sana);
    
    /**
     * Palauttaa korvaavia ehdotuksia parametrina annettuun sanaan.
     * @param sana
     * @return listan sanoista ehdotuksena parametrina annetun sanan tilalle.
     */
    String[] ehdotaKorjauksia(String sana);
}
