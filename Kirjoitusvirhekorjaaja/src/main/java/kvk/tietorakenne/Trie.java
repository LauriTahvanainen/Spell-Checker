package kvk.tietorakenne;

/**
 * Toteuttaa Trie tietorakenteen suomen kielen sanastoa varten. Trien avulla
 * yksittäisestä sanasta saadaan nopeasti selville kuuluuko se suomen kielen
 * sanastoon vai ei.
 *
 * Aikavaativuudet: Lisäys O(m) Joukkoon kuuluminen O(m) Alustaminen O(nm) ,
 * missä m on merkkijonon pituus ja n on lisättävien merkkijonojen määrä.
 *
 *
 * Tilavaatimus noin: O(m*A*N), missä m on keskimääräinen merkkijonon pituus, A
 * on aakkoston koko ja N on merkkijonojen määrä.
 *
 * Lyhyesti: https://en.wikipedia.org/wiki/Trie
 *
 * Ei erityisen optimoitu versio.
 */
public class Trie {

    private final TrieSolmu juuri;

    public Trie(TrieSolmu juuriSolmu) {
        juuri = juuriSolmu;
    }

    /**
     * Lisää Trie-puuhun uuden merkkijonon. Jos merkkijono on jo puussa, ei puu
     * muutu. Aikavaatimus O(m), missä m on lisättävän sanan pituus.
     *
     * @param sana
     */
    public void lisaaSana(String sana) {
        TrieSolmu nykyinen = juuri;
        for (char kirjain : sana.toLowerCase().toCharArray()) {
            TrieSolmu lapsi = nykyinen.haeSolmuListasta(kirjain);
            if (lapsi == null) {
                lapsi = new TrieSolmu(kirjain);
                nykyinen.lisaaLapsi(kirjain, lapsi);
            }
            nykyinen = lapsi;
        }
        nykyinen.onSana();
    }

    /**
     * Tarkastaa onko Trie-puussa parametrina annettava merkkijono. Aikavaatimus
     * O(m), missä m on tarkistettavan merkkijonon pituus.
     *
     * @param sana
     * @return true jos merkkijono löytyy Trie-puusta, muuten false
     */
    public Boolean onkoSana(String sana) {
        TrieSolmu nykyinen = juuri;
        for (char kirjain : sana.toLowerCase().toCharArray()) {
            TrieSolmu lapsi = nykyinen.haeSolmuListasta(kirjain);
            if (lapsi == null) {
                return false;
            }
            nykyinen = lapsi;
        }
        return nykyinen.onkoSana();
    }
}
