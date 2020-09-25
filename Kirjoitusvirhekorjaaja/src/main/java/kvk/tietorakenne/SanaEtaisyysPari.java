package kvk.tietorakenne;

import java.util.Objects;

public class SanaEtaisyysPari implements Comparable {

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
        if (!Objects.equals(this.sana, other.sana)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        SanaEtaisyysPari other = SanaEtaisyysPari.class.cast(o);
        return this.etaisyys - other.etaisyys;
    }

}
