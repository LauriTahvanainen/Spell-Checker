package kvk.algoritmi;

import kvk.enums.EtaisyysFunktio;

public class EtaisyyslaskijaTehdas {
    
    public static IMuokkausEtaisyyslaskija luoLaskija(EtaisyysFunktio funktio) throws Exception {
        if (funktio.equals(EtaisyysFunktio.LEVENSHTEIN)) {
            return new LevenshteinEtaisyys();
        }
        throw new Exception("Tuntematon etäisyysfunktio");
    }
}
