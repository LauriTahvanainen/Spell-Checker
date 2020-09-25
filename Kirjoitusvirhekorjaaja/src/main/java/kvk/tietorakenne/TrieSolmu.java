package kvk.tietorakenne;

import java.util.HashMap;

/**
 * Trie tietorakenteen yksi solmu. Solmussa solmua kuvaava merkki, hajautuslista
 * toisista solmuista joihin solmusta pääsee, sekä boolean arvo sille, onko
 * solmuun päättyvä merkkijono sana.
 */
public class TrieSolmu {

    private final HashMap<Character, TrieSolmu> solmuLista;
    private final char solmunMerkki;
    private boolean onSana;

    public TrieSolmu(char merkki) {
        this.solmuLista = new HashMap<>();
        this.solmunMerkki = merkki;
        this.onSana = false;
    }

    /**
     * Hakee parametrina annettavalla merkillä solmun solmulistasta mahdollista
     * lapsisolmua.
     *
     * @param merkki
     * @return lapsisolmu, jos se on haetulla merkillä olemassa. Muuten null.
     */
    public TrieSolmu haeSolmuListasta(char merkki) {
        return this.solmuLista.get(Character.toLowerCase(merkki));
    }

    /**
     * Asettaa, että solmuun päättyvä merkkijono on sana.
     */
    public void onSana() {
        this.onSana = true;
    }

    /**
     * Lisää solmulle uuden lapsisolmun.
     *
     * @param lapsiMerkki
     * @param lapsiSolmu
     */
    public void lisaaLapsi(Character lapsiMerkki, TrieSolmu lapsiSolmu) {
        this.solmuLista.put(lapsiMerkki, lapsiSolmu);
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
