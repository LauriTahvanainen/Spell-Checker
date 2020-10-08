package kvk.tietorakenne;

/**
 * Pari-rakenne BK-puuta varten tallentamaan Sana ja sen etäisyys toiseen
 * sanaan.
 */
public class SanaEtaisyysPari implements Comparable<SanaEtaisyysPari> {

    public String sana;
    public int etaisyys;

    public SanaEtaisyysPari(String sana, int etaisyys) {
        this.sana = sana;
        this.etaisyys = etaisyys;
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
        final SanaEtaisyysPari other = (SanaEtaisyysPari) obj;
        if (this.etaisyys != other.etaisyys) {
            return false;
        }
        return this.sana.equals(other.sana);
    }

    @Override
    public String toString() {
        return sana;
    }

    @Override
    public int compareTo(SanaEtaisyysPari o) {
        if (this.etaisyys > o.etaisyys) {
            return 1;
        } else if (this.etaisyys < o.etaisyys) {
            return -1;
        }
        return this.sana.compareTo(o.sana);
    }

}
