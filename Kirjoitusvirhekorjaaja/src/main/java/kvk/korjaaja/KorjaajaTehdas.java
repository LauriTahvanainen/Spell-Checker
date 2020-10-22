package kvk.korjaaja;

import kvk.algoritmi.EtaisyyslaskijaTehdas;
import kvk.algoritmi.IMuokkausEtaisyyslaskija;
import kvk.algoritmi.LevenshteinEtaisyys;
import kvk.enums.EtaisyysFunktio;
import kvk.enums.Korjaaja;
import kvk.enums.Sanasto;
import kvk.io.ITiedostonKasittelija;

public class KorjaajaTehdas {

    public static IKorjaaja luoKorjaaja(ITiedostonKasittelija lukija, Korjaaja korjaaja, EtaisyysFunktio etaisyysFunktio, int etaisyysToleranssi, int montaKorjausta, Sanasto sanasto) throws Exception {
        if (korjaaja.equals(Korjaaja.TRIE_BK)) {
            IMuokkausEtaisyyslaskija etaisyysLaskija = EtaisyyslaskijaTehdas.luoLaskija(etaisyysFunktio);
            return new TrieBK(lukija, etaisyysLaskija, etaisyysToleranssi, montaKorjausta, sanasto);
        }
        throw new Exception("Väärät parametrit korjaajan luonnissa!" + korjaaja + ":" + etaisyysFunktio + ":" + etaisyysToleranssi + ":" + montaKorjausta + ":" + sanasto);
    }
;
}
