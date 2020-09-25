package kvk.korjaaja;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kvk.algoritmi.IMuokkausEtaisyyslaskija;
import kvk.algoritmi.LevenshteinEtaisyys;
import kvk.io.ITekstitiedostonKasittelija;
import kvk.tietorakenne.BKPuu;
import kvk.tietorakenne.BKSolmu;
import kvk.tietorakenne.SanaEtaisyysPari;
import kvk.tietorakenne.Trie;
import kvk.tietorakenne.TrieSolmu;

/**
 * Perustoteutus korjaajasta, käyttää Trie-puuta virheellisten sanojen
 * tunnistamiseen.
 */
public class Korjaaja implements IKorjaaja {

    private IMuokkausEtaisyyslaskija etaisyysLaskija;
    private int etaisyysToleranssi;
    private Trie trieSanasto;
    private BKPuu BKSanasto;

    public Korjaaja(ITekstitiedostonKasittelija lukija, IMuokkausEtaisyyslaskija etaisyysLaskija, int etaisyysToleranssi) throws IOException {
        this.etaisyysLaskija = etaisyysLaskija;
        this.etaisyysToleranssi = etaisyysToleranssi;
        alustaKorjaaja(lukija);
    }

    public void setEtaisyysToleranssi(int etaisyysToleranssi) {
        this.etaisyysToleranssi = etaisyysToleranssi;
    }

    @Override
    public boolean onkoSanaVirheellinen(String sana) {
        return !this.trieSanasto.onkoSana(sana);
    }

    @Override
    public String[] ehdotaKorjauksia(String sana) {
        ArrayList<SanaEtaisyysPari> ehdotukset = this.BKSanasto.haeLahimmatSanat(sana, this.etaisyysToleranssi);
        String[] ehdotuksetListana = new String[10];
        int i = 0;
        for (SanaEtaisyysPari ehdotus : ehdotukset) {
            if (i == 10) {
                break;
            }
            ehdotuksetListana[i] = ehdotus.sana;
            i += 1;
        }
        return ehdotuksetListana;
    }

    private void alustaKorjaaja(ITekstitiedostonKasittelija lukija) throws IOException {
        this.trieSanasto = new Trie(new TrieSolmu(Character.MIN_VALUE));
        this.BKSanasto = new BKPuu(this.etaisyysLaskija, new BKSolmu("ja", null, 0));
        List<String> sanalista = lukija.lataaSanastoListana();
        for (String sana : sanalista) {
            this.trieSanasto.lisaaSana(sana);
            this.BKSanasto.lisaaSana(sana);
        }
    }

}
