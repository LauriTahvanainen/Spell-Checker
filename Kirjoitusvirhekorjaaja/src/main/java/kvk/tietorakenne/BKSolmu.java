package kvk.tietorakenne;

import java.util.HashMap;

public class BKSolmu {

    public final String sana;
    public final HashMap<Integer, BKSolmu> solmuLista;
    public final BKSolmu vanhempi;
    public final int etaisyysVanhempaan;

    public BKSolmu(String sana, BKSolmu vanhempi, int etaisyysVanhempaan) {
        this.sana = sana;
        this.vanhempi = vanhempi;
        this.solmuLista = new HashMap<>();
        this.etaisyysVanhempaan = etaisyysVanhempaan;
    }

    public void lisaaLapsi(BKSolmu lapsi, int etaisyys) {
        this.solmuLista.put(etaisyys, lapsi);
    }

    public BKSolmu lapsiEtaisyydella(int etaisyys) {
        return this.solmuLista.get(etaisyys);
    }
}
