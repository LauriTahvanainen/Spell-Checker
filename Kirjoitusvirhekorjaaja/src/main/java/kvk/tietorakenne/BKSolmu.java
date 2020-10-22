package kvk.tietorakenne;

import java.util.Objects;

/**
 * BK-puun yksittäinen solmu.
 */
public class BKSolmu {

    public final String sana;
    public final Lista<BKSolmu> solmuLista;
    public boolean onPoistettu;

    public BKSolmu(String sana) {
        this.sana = sana;
        this.solmuLista = new Lista<>(2);
        this.onPoistettu = false;
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
        if (this.onPoistettu != other.onPoistettu) {
            return false;
        }
        if (!Objects.equals(this.sana, other.sana)) {
            return false;
        }
        return true;
    }

    public void lisaaLapsi(BKSolmu lapsi, int etaisyys) {
        this.solmuLista.asetaIndeksiin(lapsi, etaisyys);
    }

    public BKSolmu lapsiEtaisyydella(int etaisyys) {
        return this.solmuLista.hae(etaisyys);
    }
}
