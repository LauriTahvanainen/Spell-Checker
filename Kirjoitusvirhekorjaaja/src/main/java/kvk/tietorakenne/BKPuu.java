package kvk.tietorakenne;

import java.util.ArrayList;
import kvk.algoritmi.IMuokkausEtaisyyslaskija;

/**
 * BK-puu tietorakenne, jonka avulla sanastosta voi etsiä nopeasti merkkijonoja, jotka ovat tietyn muokkausetäisyyden sisällä yksittäisestä merkkijonosta.
 */
public class BKPuu {

    private BKSolmu juuri;
    private final IMuokkausEtaisyyslaskija etaisyysLaskija;
    private final int toleranssi;

    public BKPuu(IMuokkausEtaisyyslaskija etaisyysLaskija, int toleranssi, BKSolmu juuriSolmu) {
        this.etaisyysLaskija = etaisyysLaskija;
        this.toleranssi = toleranssi;
        this.juuri = juuriSolmu;
    }

    public void lisaaSana(String sana) {
        sana = sana.trim().toLowerCase();
        BKSolmu nykyinenSolmu = this.juuri;
        while (true) {
            int etaisyysNykyiseenSolmuun = this.etaisyysLaskija.laskeEtaisyys(nykyinenSolmu.sana, sana);
            if (etaisyysNykyiseenSolmuun == 0) {
                break;
            }
            BKSolmu nykyisenLapsiLisattavanSananEtaisyydella = nykyinenSolmu.lapsiEtaisyydella(etaisyysNykyiseenSolmuun);
            if (nykyisenLapsiLisattavanSananEtaisyydella == null) {
                nykyinenSolmu.lisaaLapsi(new BKSolmu(sana, nykyinenSolmu, etaisyysNykyiseenSolmuun), etaisyysNykyiseenSolmuun);
                break;
            } else {
                nykyinenSolmu = nykyisenLapsiLisattavanSananEtaisyydella;
            }
        }
    }
    
    public ArrayList<String> haeLahimmatSanat(String sana) {
        ArrayList<String> lahimmatSanat = new ArrayList<>();
        
        return null;
    }
    
}
