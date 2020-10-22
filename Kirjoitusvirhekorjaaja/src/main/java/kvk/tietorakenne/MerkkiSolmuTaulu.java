package kvk.tietorakenne;

/**
 * Taulukkoon pohjautuva rakenne Trie puun solmulistojen toteuttamiseksi.
 * Yrittää optimoida käytettyä muistia. Määrittää myös sallitut merkit.
 *
 * Sallitut merkit muunnetaan sopivalle kokonaislukuvälille, ja näitä
 * kokonaislukuja käytetään avaimina tallettamaan TrieSolmu-instanssi
 * rakenteeseen. Jokaista merkkiä vastaa siis yksi alkio.
 *
 * Merkki-kokonaisluku muunnokset on tehty niin, että pienimmästä suurimpaan
 * ensin on aakkoset, sitten erikoismerkit. Tämä yhdistettynä siihen, että
 * taulukon koko valitaan dynaamisesti vähentää tarvittavaa muistia. Esim jos
 * Trie-solmu tallentaa solmulistaansa vain merkille 'b' solmun, on tämän
 * rakenteen taulukon koko 2. Toisaalta jos Trie-Solmuun tallennetaan vain
 * '-merkki, niin taulukon kooksi tulee 54, ja tallennetaan turhaan 53
 * null-arvoa. Kuitenkin '-merkkisiä sanoja on todella vähän, joten tällaisia
 * taulukoita ei tule olemaan kovin montaa. Suurin osa taulukoista on kooltaan
 * maksimissaan aakkosten lukumäärä, todennäköisesti vielä pienempiä.
 *
 */
public class MerkkiSolmuTaulu {

    private TrieSolmu[] lista;
    private int varattuPituus;
    private int alkioita;
    private static final int MAKSIMI_PITUUS = 53;

    public MerkkiSolmuTaulu() {
        this.varattuPituus = 0;
        this.alkioita = 0;
        this.lista = new TrieSolmu[this.varattuPituus];
    }

    /**
     * Hakee taulukosta merkkiä vastaavan TrieSolmu-alkion.
     *
     * @param merkki
     * @return Merkkiä vastaava TrieSolmu-instanssi, tai null, jos merkkiä
     * vastaavaa alkiota ei löydy.
     * @throws Exception
     */
    public TrieSolmu hae(char merkki) throws Exception {
        int indeksi = merkkiKokonaisluvuksi(merkki);
        if (indeksi >= this.varattuPituus && indeksi < MAKSIMI_PITUUS) {
            return null;
        }
        return this.lista[indeksi];
    }

    /**
     * Lisää rakenteeseen TrieSolmu-instanssin avaimena annettu merkki.
     *
     * @param lapsiMerkki Avain
     * @param lapsiSolmu TrieSolmu-instanssi
     * @throws Exception jos yritetään ylikirjoittaa jonkun merkin päälle.
     */
    public void lisaa(char lapsiMerkki, TrieSolmu lapsiSolmu) throws Exception {
        lapsiMerkki = Character.toLowerCase(lapsiMerkki);
        int indeksi = merkkiKokonaisluvuksi(lapsiMerkki);
        if (indeksi >= this.varattuPituus) {
            kasvataListaaIndeksinPituiseksi(indeksi);
        }
        if (this.lista[indeksi] != null) {
            throw new Exception("Trien solmulistassa yhtä merkkiä voi vastata vain yksi lapsi!");
        }
        this.lista[indeksi] = lapsiSolmu;
        this.alkioita++;
    }
    
    public void asetaTyhjaksi(char lapsiMerkki) throws Exception {
        lapsiMerkki = Character.toLowerCase(lapsiMerkki);
        int indeksi = merkkiKokonaisluvuksi(lapsiMerkki);
        if (indeksi >= this.varattuPituus) {
            kasvataListaaIndeksinPituiseksi(indeksi);
        }
        this.lista[indeksi] = null;
        this.alkioita--;
    }

    public int listalleVarattuPituus() {
        return this.lista.length;
    }
    
    public int alkioitaListassa() {
        return this.alkioita;
    }

    private void kasvataListaaIndeksinPituiseksi(int uusiPituus) {
        this.varattuPituus = uusiPituus + 1;
        TrieSolmu[] uusiLista = new TrieSolmu[this.varattuPituus];
        int i = 0;
        for (TrieSolmu arvo : this.lista) {
            uusiLista[i] = arvo;
            i += 1;
        }
        this.lista = uusiLista;
    }

    private int merkkiKokonaisluvuksi(char merkki) throws Exception {
        int luku = (int) merkki - 97;
        if (luku >= 0 && luku < 26) {
            return luku;
        }
        if (merkki == 'å') {
            return 26;
        }
        if (merkki == 'ä') {
            return 27;
        }
        if (merkki == 'ö') {
            return 28;
        }
        if (merkki == '0' || merkki == '1' || merkki == '2' || merkki == '3' || merkki == '4' || merkki == '5' || merkki == '6' || merkki == '7' || merkki == '8' || merkki == '9') {
            return (int) merkki - 19;
        }
        if (merkki == '-') {
            return 39;
        }
        if (merkki == ' ') {
            return 40;
        }
        if (merkki == 'à') {
            return 41;
        }
        if (merkki == 'ž') {
            return 42;
        }
        if (merkki == 'é') {
            return 43;
        }
        if (merkki == 'š') {
            return 44;
        }
        if (merkki == 'â') {
            return 45;
        }
        if (merkki == 'è') {
            return 46;
        }
        if (merkki == 'î') {
            return 47;
        }
        if (merkki == 'ê') {
            return 48;
        }
        if (merkki == 'á') {
            return 49;
        }
        if (merkki == 'ô') {
            return 50;
        }
        if (merkki == 'û') {
            return 51;
        }

        if (merkki == "'".charAt(0)) {
            return 52;
        }

        throw new Exception("Virheellinen merkki annettu: " + "'" + +merkki + "'");
    }
}
