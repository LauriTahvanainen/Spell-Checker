package kvk.tietorakenne;

import java.util.Objects;

/**
 * BK-puun yksittäinen solmu.
 */
public class BKSolmu {

    public final String sana;
    public final Lista<BKSolmu> solmuLista;
    public final BKSolmu vanhempi;
    public final int etaisyysVanhempaan;

    public BKSolmu(String sana, BKSolmu vanhempi, int etaisyysVanhempaan) {
        this.sana = sana;
        this.vanhempi = vanhempi;
        this.solmuLista = new Lista<>(2);
        this.etaisyysVanhempaan = etaisyysVanhempaan;
    }

    public void lisaaLapsi(BKSolmu lapsi, int etaisyys) {
        this.solmuLista.asetaIndeksiin(lapsi, etaisyys);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BKSolmu other = (BKSolmu) obj;
        if (this.etaisyysVanhempaan != other.etaisyysVanhempaan) {
            return false;
        }
        if (!Objects.equals(this.sana, other.sana)) {
            return false;
        }
        return this.vanhempi == other.vanhempi;
    }



    public BKSolmu lapsiEtaisyydella(int etaisyys) {
        return this.solmuLista.hae(etaisyys);
    }
}
