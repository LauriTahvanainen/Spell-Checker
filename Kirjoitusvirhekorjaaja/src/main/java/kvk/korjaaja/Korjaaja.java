package kvk.korjaaja;

import java.io.IOException;
import java.util.List;
import kvk.algoritmi.IMuokkausEtaisyyslaskija;
import kvk.io.ITekstitiedostonKasittelija;
import kvk.tietorakenne.BKPuu;
import kvk.tietorakenne.BKSolmu;
import kvk.tietorakenne.Trie;
import kvk.tietorakenne.TrieSolmu;

/**
 * Perustoteutus korjaajasta, käyttää Trie-puuta virheellisten sanojen
 * tunnistamiseen ja BK-puuta ehdotusten generoimiseen. Muokkausetaisyystoleranssi, sekä tapa jolla muokkausetäisyys lasketaan muokattavissa.
 */
public class Korjaaja implements IKorjaaja {

    private IMuokkausEtaisyyslaskija etaisyysLaskija;
    private int etaisyysToleranssi;
    private Trie trieSanasto;
    private BKPuu BKSanasto;

    public Korjaaja(ITekstitiedostonKasittelija lukija, IMuokkausEtaisyyslaskija etaisyysLaskija, int etaisyysToleranssi) throws IOException, Exception {
        this.etaisyysLaskija = etaisyysLaskija;
        this.etaisyysToleranssi = etaisyysToleranssi;
        alustaKorjaaja(lukija);
    }

    public void setEtaisyysToleranssi(int etaisyysToleranssi) {
        this.etaisyysToleranssi = etaisyysToleranssi;
    }
    
    public void setEtaisyysLaskija(IMuokkausEtaisyyslaskija laskija) {
        this.etaisyysLaskija = laskija;
        this.BKSanasto.asetaEtaisyysLaskija(laskija);
    }

    @Override
    public boolean onkoSanaVirheellinen(String sana) throws Exception {
        return !this.trieSanasto.onkoSana(sana);
    }

    @Override
    public String[] ehdotaKorjauksia(String sana) {
        return this.BKSanasto.haeLahimmatSanat(sana, this.etaisyysToleranssi, 10);
    }

    private void alustaKorjaaja(ITekstitiedostonKasittelija lukija) throws IOException, Exception {
        this.trieSanasto = new Trie(new TrieSolmu(Character.MIN_VALUE));
        this.BKSanasto = new BKPuu(this.etaisyysLaskija, new BKSolmu("ja", null, 0));
        List<String> sanalista = lukija.lataaSanastoListana();
        for (String sana : sanalista) {
            this.trieSanasto.lisaaSana(sana);
            this.BKSanasto.lisaaSana(sana);
        }
    }

}
