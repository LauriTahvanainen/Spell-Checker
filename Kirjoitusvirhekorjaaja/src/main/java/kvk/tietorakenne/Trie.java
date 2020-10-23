package kvk.tietorakenne;

import kvk.poikkeukset.VirheellinenKirjainPoikkeus;

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
 * Solmulistassa käytetty hienoista optimointia.
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
     * @throws kvk.poikkeukset.VirheellinenKirjainPoikkeus
     * @throws java.lang.Exception
     */
    public void lisaaSana(String sana) throws VirheellinenKirjainPoikkeus, Exception {
        TrieSolmu nykyinen = juuri;
        for (char kirjain : sana.toLowerCase().toCharArray()) {
            TrieSolmu lapsi = nykyinen.haeSolmuListasta(kirjain);
            if (lapsi == null) {
                lapsi = new TrieSolmu(kirjain);
                nykyinen.lisaaLapsi(kirjain, lapsi);
            }
            nykyinen = lapsi;
        }
        nykyinen.asetaOnSana(true);
    }

    /**
     * Poistaa parametrina annetun sanan puusta. Poistaa samalla kaikki puun
     * solmut jotka jäävät ilman lapsia.
     *
     * @param sana
     * @throws VirheellinenKirjainPoikkeus
     * @throws Exception
     */
    public void poistaSana(String sana) throws VirheellinenKirjainPoikkeus, Exception {
        TrieSolmu nykyinen = juuri;
        TrieSolmu[] kirjaimiaVastaavatSolmut = new TrieSolmu[sana.length()];
        int i = 0;
        for (char kirjain : sana.toLowerCase().toCharArray()) {
            TrieSolmu lapsi = nykyinen.haeSolmuListasta(kirjain);
            if (lapsi == null) {
                return;
            }
            kirjaimiaVastaavatSolmut[i] = lapsi;
            nykyinen = lapsi;
            i++;
        }
        nykyinen.asetaOnSana(false);
        i = sana.length() - 1;
        while (nykyinen.solmuListaOnTyhja() && !nykyinen.onSana()) {
            char lapsenMerkki = nykyinen.solmunMerkki;
            if (i != 0) {
                nykyinen = kirjaimiaVastaavatSolmut[i - 1];
            } else {
                nykyinen = this.juuri;
            }
            nykyinen.asetaLapsiTyhjaksi(lapsenMerkki);
            i--;
        }

    }

    /**
     * Tarkastaa onko Trie-puussa parametrina annettava merkkijono. Aikavaatimus
     * O(m), missä m on tarkistettavan merkkijonon pituus.
     *
     * @param sana
     * @return true jos merkkijono löytyy Trie-puusta, muuten false
     * @throws kvk.poikkeukset.VirheellinenKirjainPoikkeus
     * @throws java.lang.Exception
     */
    public Boolean onkoSana(String sana) throws VirheellinenKirjainPoikkeus, Exception {
        TrieSolmu nykyinen = juuri;
        for (char kirjain : sana.toLowerCase().toCharArray()) {
            TrieSolmu lapsi = nykyinen.haeSolmuListasta(kirjain);
            if (lapsi == null) {
                return false;
            }
            nykyinen = lapsi;
        }
        return nykyinen.onSana();
    }
}
