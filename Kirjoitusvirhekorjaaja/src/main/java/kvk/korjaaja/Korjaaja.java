package kvk.korjaaja;

import java.io.IOException;
import java.util.List;
import kvk.io.ITekstitiedostonKasittelija;
import kvk.tietorakenne.Trie;
import kvk.tietorakenne.TrieSolmu;

/**
 * Perustoteutus korjaajasta, käyttää Trie-puuta virheellisten sanojen
 * tunnistamiseen.
 */
public class Korjaaja implements IKorjaaja {

    private Trie sanasto;

    public Korjaaja(ITekstitiedostonKasittelija lukija) throws IOException {
        alustaKorjaaja(lukija);
    }

    @Override
    public boolean onkoSanaVirheellinen(String sana) {
        return !this.sanasto.onkoSana(sana);
    }

    @Override
    public String[] ehdotaKorjauksia(String sana) {
        return null;
    }

    private void alustaKorjaaja(ITekstitiedostonKasittelija lukija) throws IOException {
        this.sanasto = new Trie(new TrieSolmu(Character.MIN_VALUE));
        List<String> sanalista = lukija.lataaSanastoListana();
        for (String sana : sanalista) {
            this.sanasto.lisaaSana(sana);
        }
    }

}
