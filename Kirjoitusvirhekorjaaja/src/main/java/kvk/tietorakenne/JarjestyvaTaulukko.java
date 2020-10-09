package kvk.tietorakenne;

/**
 * Järjestyvä kiinteän kokoinen taulukko BK-puun hausta saamia tuloksia varten.
 * Toimii ikään kuin järjestettynä jonoja, johon mahtuu aina kerralla aina
 * arvokkaimmat jonottajat. Lisäys aina O(k), missä k on listan määritelty koko.
 * Mahdollistaa BK-puun hausta parhaiden (eli pienimpien) tulosten saamisen
 * tehokkaasti järjestettynä. Puskee aina järjestyksen mukaan arvottomimmat
 * alkiot pois taulukosta. Esim jos olemassaolevaan nousevaan taulukkoon
 * {("koira", 1), ("kissa", 2), ("krokotiili", 7)} lisättäisiin alkio
 * ("koiruus", 4), niin taulukko tulisi muotoon {("koira", 1), ("kissa", 2),
 * ("koiruus", 4)}
 */
public class JarjestyvaTaulukko {

    private final SanaEtaisyysPari[] lista;
    private final Jarjestys jarjestys;

    /**
     *
     * @param koko taulukon koko, ei muutettavissa.
     * @param jarjestys järjestetäänkö lisättävät alkiot nousevaan vai laskevaan
     * järjestykseen.
     */
    public JarjestyvaTaulukko(int koko, Jarjestys jarjestys) {
        this.lista = new SanaEtaisyysPari[koko];
        this.jarjestys = jarjestys;
    }

    /**
     * Jos lisättävä alkio on määritellyn järjestys säännön (Suurempi/Pienempi)
     * mukaan arvokkaampi, kuin jokin taulukon nykyinen alkio, lisää alkion
     * seuraavaksi tärkeysjärjestyksessä. Työntää viimeisen alkion pois listalta
     * jos sellainen on.
     *
     * @param lisattava
     */
    public void lisaa(SanaEtaisyysPari lisattava) {
        SanaEtaisyysPari nykyinen;
        for (int i = 0; i < this.lista.length; i++) {
            nykyinen = this.lista[i];
            if (nykyinen == null) {
                this.lista[i] = lisattava;
                break;
            }
            if (lisattava.compareTo(nykyinen) == this.jarjestys.arvo) {
                lisaaValiin(lisattava, i);
                break;
            }
        }
    }

    private void lisaaValiin(SanaEtaisyysPari lisattava, int indeksi) {
        SanaEtaisyysPari nykyinen = this.lista[indeksi];
        this.lista[indeksi] = lisattava;
        for (int i = indeksi + 1; i < this.lista.length; i++) {
            SanaEtaisyysPari seuraava = this.lista[i];
            this.lista[i] = nykyinen;
            nykyinen = seuraava;
        }

    }

    /**
     * Palauttaa sisältönsä merkkijonotaulukkona. Tyhjät taulukon indeksit null
     * muodossa.
     *
     * @return Taulukon alustuksessa määritellyn taulukon koon kokoinen
     * merkkijonotaulukko.
     */
    public String[] haeMerkkijonoTaulukkona() {
        String[] palautettava = new String[this.lista.length];
        int i = 0;
        for (SanaEtaisyysPari sanaEtaisyysPari : this.lista) {
            if (sanaEtaisyysPari == null) {
                break;
            }
            palautettava[i] = sanaEtaisyysPari.sana;
            i += 1;
        }
        return palautettava;
    }

}
