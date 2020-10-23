package kvk.poikkeukset;

/**
 * Poikkeus tilannetta varten, jossa Trie puuhun yritetään lisätä ei tuettu
 * merkki.
 */
public class VirheellinenKirjainPoikkeus extends Exception {

    public VirheellinenKirjainPoikkeus(String viesti) {
        super(viesti);
    }
}
