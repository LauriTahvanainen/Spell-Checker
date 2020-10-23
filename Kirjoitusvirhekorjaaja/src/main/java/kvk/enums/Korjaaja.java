package kvk.enums;

/**
 * Enum korjaajille, käytetään muun muassa korjaajan asetusten tallentamisen ja
 * valitsemisen yhteydessä.
 */
public enum Korjaaja {
    TRIE_BK("Trie-BK");

    public final String nimi;

    Korjaaja(String nimi) {
        this.nimi = nimi;
    }
}
