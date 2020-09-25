package kvk.tietorakenne;

import java.util.ArrayDeque;
import java.util.ArrayList;
import kvk.algoritmi.IMuokkausEtaisyyslaskija;

/**
 * BK-puu tietorakenne, jonka avulla sanastosta voi etsiä nopeasti merkkijonoja,
 * jotka ovat tietyn muokkausetäisyyden sisällä yksittäisestä merkkijonosta.
 * Huomattavaa on, että puulle voi antaa erilaisen etäisyyslaskijan. Pitää
 * kuitenkin muistaa, että etäisyyslaskijan pitää muodostaa sanoille metrinen
 * avaruus.
 */
public class BKPuu {

    private BKSolmu juuri;
    private final IMuokkausEtaisyyslaskija etaisyysLaskija;

    /**
     * @param etaisyysLaskija Määrittää millä tavalla sanojen välinen metrinen
     * etäisyys lasketaan.
     * @param juuriSolmu
     */
    public BKPuu(IMuokkausEtaisyyslaskija etaisyysLaskija, BKSolmu juuriSolmu) {
        this.etaisyysLaskija = etaisyysLaskija;
        this.juuri = juuriSolmu;
    }

    /**
     * Lisää parametrina annetun sanan BK-puuhun. Sana sijoitetaan juuren
     * lapseksi avaimenaan sanan etäisyys juuresta. Jos juurisolmulla on jo
     * lapsi sillä etäisyydellä, siirrytään tähän lapsisolmuun, ja yritetään
     * lisätä sana tämän lapseksi avaimena etäisyys tästä uuden kontekstin
     * solmusta. Tätä valintaa jatketaan kunnes on mahdollista lisätä sana
     * uudeksi lapseksi tietyllä etäisyydellää.
     */
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

    /**
     * Hakee BK-puusta sanat, jotka ovat muokkausetäisyydeltään lähimpänä
     * parametrina annettua sanaa. Haussa käytetään hyväksi sanojen etäisyyksien
     * välien ominaisuuksia, tarkemmin kolmioepäyhtälöä, jonka perusteella
     * haettavia haaroja voidaan karsia haun nopeuttamiseksi.
     *
     * @param sana jota lähimpänä olevat sanat haetaan.
     * @param etaisyysToleranssi verrattavan sanan maksimietäisyys haettavasta
     * sanasta.
     */
    public ArrayList<SanaEtaisyysPari> haeLahimmatSanat(String sana, int etaisyysToleranssi) {
        sana = sana.toLowerCase();
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
