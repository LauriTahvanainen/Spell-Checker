package kvk.korjaaja;

import java.io.IOException;
import kvk.algoritmi.EtaisyyslaskijaTehdas;
import kvk.algoritmi.IMuokkausEtaisyyslaskija;
import kvk.enums.EtaisyysFunktio;
import kvk.enums.Korjaaja;
import kvk.enums.Sanasto;
import kvk.io.ITiedostonKasittelija;

/**
 * Luo erilaisia korjaajia.
 */
public class KorjaajaTehdas {

    /**
     * Luo uuden parametrien mukaisen korjaajan.
     * @param lukija
     * @param korjaaja
     * @param etaisyysFunktio
     * @param etaisyysToleranssi
     * @param montaKorjausta
     * @param sanasto
     * @return uusi korjaaja
     * @throws java.io.IOException
     * @throws Exception
     */
    public static IKorjaaja luoKorjaaja(ITiedostonKasittelija lukija, Korjaaja korjaaja, EtaisyysFunktio etaisyysFunktio, int etaisyysToleranssi, int montaKorjausta, Sanasto sanasto) throws IOException, Exception {
        if (korjaaja.equals(Korjaaja.TRIE_BK)) {
            IMuokkausEtaisyyslaskija etaisyysLaskija = EtaisyyslaskijaTehdas.luoLaskija(etaisyysFunktio);
            return new TrieBKKorjaaja(lukija, etaisyysLaskija, etaisyysToleranssi, montaKorjausta, sanasto);
        }
        throw new Exception("Väärät parametrit korjaajan luonnissa!" + korjaaja + ":" + etaisyysFunktio + ":" + etaisyysToleranssi + ":" + montaKorjausta + ":" + sanasto);
    }
;
}
