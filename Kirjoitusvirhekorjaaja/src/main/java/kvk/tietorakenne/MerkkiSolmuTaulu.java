package kvk.tietorakenne;

public class MerkkiSolmuTaulu {

    private TrieSolmu[] lista;
    private int pituus;
    private static final int MAKSIMI_INDEKSI = 53;

    public MerkkiSolmuTaulu() {
        this.pituus = 0;
        this.lista = new TrieSolmu[this.pituus];
    }

    public TrieSolmu hae(char merkki) throws Exception {
        int indeksi = merkkiKokonaisluvuksi(merkki);
        if (indeksi >= this.pituus && indeksi < MAKSIMI_INDEKSI) {
            return null;
        }
        return this.lista[indeksi];
    }

    public void lisaa(char lapsiMerkki, TrieSolmu lapsiSolmu) throws Exception {
        lapsiMerkki = Character.toLowerCase(lapsiMerkki);
        int indeksi = merkkiKokonaisluvuksi(lapsiMerkki);
        if (indeksi >= this.pituus) {
            kasvataListaaIndeksinPituiseksi(indeksi);
        }
        if (this.lista[indeksi] != null) {
            throw new Exception("Trien solmulistassa yhtä merkkiä voi vastata vain yksi lapsi!");
        }
        this.lista[indeksi] = lapsiSolmu;
    }

    public int listanPituus() {
        return this.lista.length;
    }

    private void kasvataListaaIndeksinPituiseksi(int uusiPituus) {
        this.pituus = uusiPituus + 1;
        TrieSolmu[] uusiLista = new TrieSolmu[this.pituus];
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
