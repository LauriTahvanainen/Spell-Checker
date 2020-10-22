package kvk.korjaaja;

import java.io.Serializable;
import kvk.enums.Sanasto;

/**
 * Määrittää kirjoitusvirheen korjaajan.
 */
public interface IKorjaaja extends Serializable {

    /**
     * Selvittää jotain tapaa käyttäen, onko parametrina annettu sana
     * virheellinen
     *
     * @param sana
     * @return true jos sana on virheellinen
     * @throws java.lang.Exception
     */
    boolean onkoSanaVirheellinen(String sana) throws Exception;

    /**
     * Palauttaa korvaavia ehdotuksia parametrina annettuun sanaan.
     *
     * @param sana
     * @return listan sanoista ehdotuksena parametrina annetun sanan tilalle.
     */
    String[] ehdotaKorjauksia(String sana);

    boolean lisaaSanastoonSana(String lisattava);

    boolean poistaSanastostaSana(String poistettava);

    void asetaMontaEhdotustaHaetaan(int montaHaetaan);
    
    void asetaEtaisyysToleranssi(int toleranssi);

    void tallennaSanastoMuutokset();
    
    Sanasto sanasto();
    
    int sanastonKoko();

}
