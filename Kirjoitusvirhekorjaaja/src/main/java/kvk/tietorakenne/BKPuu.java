package kvk.tietorakenne;

import java.util.ArrayDeque;
import java.util.ArrayList;
import kvk.algoritmi.IMuokkausEtaisyyslaskija;

/**
 * BK-puu tietorakenne, jonka avulla sanastosta voi etsiä nopeasti merkkijonoja,
 * jotka ovat tietyn muokkausetäisyyden sisällä yksittäisestä merkkijonosta.
 */
public class BKPuu {

    private BKSolmu juuri;
    private final IMuokkausEtaisyyslaskija etaisyysLaskija;

    public BKPuu(IMuokkausEtaisyyslaskija etaisyysLaskija, BKSolmu juuriSolmu) {
        this.etaisyysLaskija = etaisyysLaskija;
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

    public ArrayList<SanaEtaisyysPari> haeLahimmatSanat(String sana, int etaisyysToleranssi) {
        ArrayList<SanaEtaisyysPari> lahimmatSanat = new ArrayList<>();
        ArrayDeque<BKSolmu> kandidaatit = new ArrayDeque<>();

        kandidaatit.push(this.juuri);

        while (!kandidaatit.isEmpty()) {
            BKSolmu verrattavaSolmu = kandidaatit.pop();
            int etaisyysEtsittavaanSanaan = this.etaisyysLaskija.laskeEtaisyys(sana, verrattavaSolmu.sana);
            if (etaisyysEtsittavaanSanaan <= etaisyysToleranssi) {
                lahimmatSanat.add(new SanaEtaisyysPari(verrattavaSolmu.sana, etaisyysEtsittavaanSanaan));
            }
            int rajausAlaRaja = etaisyysEtsittavaanSanaan - etaisyysToleranssi;
            int rajausYlaRaja = etaisyysEtsittavaanSanaan + etaisyysToleranssi;
            for (int i = rajausAlaRaja; i <= rajausYlaRaja; i++) {
                BKSolmu uusiKandidaatti = verrattavaSolmu.lapsiEtaisyydella(i);
                if (uusiKandidaatti != null) {
                    kandidaatit.push(uusiKandidaatti);
                }
            }
        }

        return lahimmatSanat;
    }

}
