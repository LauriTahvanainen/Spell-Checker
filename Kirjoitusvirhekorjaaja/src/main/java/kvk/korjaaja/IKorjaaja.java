package kvk.korjaaja;

import java.io.Serializable;
import kvk.enums.Sanasto;
import kvk.poikkeukset.VirheellinenKirjainPoikkeus;

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
     * @throws kvk.poikkeukset.VirheellinenKirjainPoikkeus
     * @throws java.lang.Exception
     */
    boolean onkoSanaVirheellinen(String sana) throws VirheellinenKirjainPoikkeus, Exception;

    /**
     * Palauttaa korvaavia ehdotuksia parametrina annettuun sanaan.
     *
     * @param sana
     * @return listan sanoista ehdotuksena parametrina annetun sanan tilalle.
     */
    String[] ehdotaKorjauksia(String sana);

    void lisaaSanastoonSana(String lisattava) throws Exception;

    void poistaSanastostaSana(String poistettava) throws VirheellinenKirjainPoikkeus, Exception;

    void asetaMontaEhdotustaHaetaan(int montaHaetaan);
    
    void asetaEtaisyysToleranssi(int toleranssi);

    void tallennaSanastoMuutokset() throws Exception;
    
    Sanasto sanasto();
    
    int sanastonKoko();
    
    /**
     * Lippu sille, onko alustuksessa yritetty lisätä virheellisellä merkillä sanoja. Näin voidaan ilmoittaa käyttäjälle, että hänen sanastotiedostonsa kaikkia sanoja ei oteta sanastoon.
     * @return
     */
    boolean yritettiinLisataSanaVirheellisellaMerkilla();

}
