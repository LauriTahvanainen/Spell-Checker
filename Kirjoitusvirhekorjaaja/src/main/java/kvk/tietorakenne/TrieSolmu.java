package kvk.tietorakenne;

/**
 * Trie tietorakenteen yksi solmu. Solmussa solmua kuvaava merkki, hajautuslista
 * toisista solmuista joihin solmusta pääsee, sekä boolean arvo sille, onko
 * solmuun päättyvä merkkijono sana.
 */
public class TrieSolmu {

    private final MerkkiSolmuTaulu solmuLista;
    private final char solmunMerkki;
    private boolean onSana;

    public TrieSolmu(char merkki) {
        this.solmuLista = new MerkkiSolmuTaulu();
        this.solmunMerkki = merkki;
        this.onSana = false;
    }

    /**
     * Hakee parametrina annettavalla merkillä solmun solmulistasta mahdollista
     * lapsisolmua.
     *
     * @param merkki
     * @return lapsisolmu, jos se on haetulla merkillä olemassa. Muuten null.
     * @throws java.lang.Exception
     */
    public TrieSolmu haeSolmuListasta(char merkki) throws Exception {
        return this.solmuLista.hae(Character.toLowerCase(merkki));
    }

    /**
     * Asettaa, että solmuun päättyvä merkkijono on sana.
     */
    public void onSana() {
        this.onSana = true;
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
        final TrieSolmu other = (TrieSolmu) obj;
        if (this.solmunMerkki != other.solmunMerkki) {
            return false;
        }
        return this.onSana == other.onSana;
    }

    /**
     * Lisää solmulle uuden lapsisolmun.
     *
     * @param lapsiMerkki
     * @param lapsiSolmu
     * @throws java.lang.Exception
     */
    public void lisaaLapsi(Character lapsiMerkki, TrieSolmu lapsiSolmu) throws Exception {
        this.solmuLista.lisaa(Character.toLowerCase(lapsiMerkki), lapsiSolmu);
    }

    /**
     * Kertoo onko solmuun päättyvä merkkijono sana.
     *
     * @return true jos solmuun päättyvä merkkijono on sana. Muuten false.
     */
    public boolean onkoSana() {
        return this.onSana;
    }
}
