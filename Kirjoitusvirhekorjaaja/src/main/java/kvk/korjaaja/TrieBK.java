package kvk.korjaaja;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kvk.algoritmi.IMuokkausEtaisyyslaskija;
import kvk.enums.Korjaaja;
import kvk.enums.Sanasto;
import kvk.tietorakenne.BKPuu;
import kvk.tietorakenne.BKSolmu;
import kvk.tietorakenne.Trie;
import kvk.tietorakenne.TrieSolmu;
import kvk.io.ITiedostonKasittelija;
import kvk.tietorakenne.Lista;

/**
 * Perustoteutus korjaajasta, käyttää Trie-puuta virheellisten sanojen
 * tunnistamiseen ja BK-puuta ehdotusten generoimiseen.
 * Muokkausetaisyystoleranssi, sekä tapa jolla muokkausetäisyys lasketaan
 * muokattavissa.
 */
public class TrieBK implements IKorjaaja {

    private IMuokkausEtaisyyslaskija etaisyysLaskija;
    private int etaisyysToleranssi;
    private int montaKorjausEhdotustaHaetaan;
    private Trie trieSanasto;
    private BKPuu BKSanasto;
    private Sanasto sanastoTiedosto;
    private boolean alustusKaynnissa;
    private final Lista<String> lisatytSanat;
    private final Lista<String> poistetutSanat;
    private ITiedostonKasittelija tiedostonKasittelija;
    private int sanastonKoko;

    /**
     * Luo uuden korjaajan.
     * @param tiedostonKasittelija
     * @param etaisyysLaskija
     * @param etaisyysToleranssi
     * @param montaKorjausEhdotusta
     * @param sanastoTiedosto
     * @throws IOException
     * @throws Exception
     */
    public TrieBK(ITiedostonKasittelija tiedostonKasittelija, IMuokkausEtaisyyslaskija etaisyysLaskija, int etaisyysToleranssi, int montaKorjausEhdotusta, Sanasto sanastoTiedosto) throws IOException, Exception {
        this.etaisyysLaskija = etaisyysLaskija;
        this.etaisyysToleranssi = etaisyysToleranssi;
        this.montaKorjausEhdotustaHaetaan = montaKorjausEhdotusta;
        this.sanastoTiedosto = sanastoTiedosto;
        this.alustusKaynnissa = true;
        this.lisatytSanat = new Lista<>(10);
        this.poistetutSanat = new Lista<>(10);
        this.tiedostonKasittelija = tiedostonKasittelija;
        this.sanastonKoko = 0;
        alustaKorjaaja();
        this.alustusKaynnissa = false;
    }

    @Override
    public void asetaEtaisyysToleranssi(int etaisyysToleranssi) {
        this.etaisyysToleranssi = etaisyysToleranssi;
    }

    public void asetaEtaisyysLaskija(IMuokkausEtaisyyslaskija laskija) {
        this.etaisyysLaskija = laskija;
        this.BKSanasto.asetaEtaisyysLaskija(laskija);
    }

    @Override
    public void asetaMontaEhdotustaHaetaan(int montaHaetaan) {
        this.montaKorjausEhdotustaHaetaan = montaHaetaan;
    }

    @Override
    public boolean onkoSanaVirheellinen(String sana) throws Exception {
        return !this.trieSanasto.onkoSana(sana);
    }

    @Override
    public String[] ehdotaKorjauksia(String sana) {
        return this.BKSanasto.haeLahimmatSanat(sana, this.etaisyysToleranssi, this.montaKorjausEhdotustaHaetaan);
    }

    @Override
    public boolean lisaaSanastoonSana(String lisattava) {
        try {
            this.BKSanasto.lisaaSana(lisattava);
            this.trieSanasto.lisaaSana(lisattava);
            if (!alustusKaynnissa) {
                this.lisatytSanat.lisaaListaan(lisattava);
            }
            this.sanastonKoko++;
            return true;
        } catch (Exception ex) {
            Logger.getLogger(TrieBK.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean poistaSanastostaSana(String sana) {
        try {
            this.trieSanasto.poistaSana(sana);
            this.BKSanasto.poistaSana(sana);
            this.sanastonKoko--;
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    private void alustaKorjaaja() throws IOException, Exception {
        this.trieSanasto = new Trie(new TrieSolmu(Character.MIN_VALUE));
        this.BKSanasto = new BKPuu(this.etaisyysLaskija, new BKSolmu("ja"));
        this.tiedostonKasittelija.taytaSanastoTiedostosta(this, this.sanastoTiedosto.tiedostoNimi);
    }

    @Override
    public String toString() {
        return Korjaaja.TRIE_BK.nimi;
    }

    @Override
    public void tallennaSanastoMuutokset() {
        this.tiedostonKasittelija.lisaaSanatTiedostoon(this.lisatytSanat.haeListaMerkkijonoTaulukkona(), this.sanastoTiedosto.tiedostoNimi);
    }

    @Override
    public Sanasto sanasto() {
        return this.sanastoTiedosto;
    }

    @Override
    public int sanastonKoko() {
        return this.sanastonKoko;
    }

}
