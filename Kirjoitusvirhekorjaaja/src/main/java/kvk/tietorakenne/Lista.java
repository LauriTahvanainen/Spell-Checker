package kvk.tietorakenne;

/**
 * Dynaamisesti kasvava lista.
 *
 * @param <T> listan alkioiden tyyppi.
 */
public class Lista<T> {

    private T[] lista;
    private int maksimiPituus;
    private int pituus;

    /**
     * Luo uuden Lista-instanssin.
     *
     * @param alustavaPituus Kuinka pitkä taulukko listalle alustavasti
     * varataan.
     */
    public Lista(int alustavaPituus) {
        this.maksimiPituus = alustavaPituus;
        this.pituus = 0;
        this.lista = (T[]) new Object[this.maksimiPituus];
    }

    /**
     *
     * @return Listan pituus.
     */
    public int pituus() {
        return this.pituus;
    }

    /**
     * Lisää listan perään uuden alkion.
     *
     * @param alkio
     */
    public void lisaaListaan(T alkio) {
        if (this.pituus == this.maksimiPituus - 1) {
            kasvataListaa();
        }
        this.lista[this.pituus] = alkio;
        this.pituus += 1;
    }

    /**
     * Asettaa alkion parametrina annettuun indeksiin. Jos indeksi on suurempi
     * kuin listan pituus, tulee listan uudeksi pituudeksi indeksi + 1.
     *
     * @param alkio
     * @param indeksi
     */
    public void asetaIndeksiin(T alkio, int indeksi) {
        while (indeksi >= this.maksimiPituus) {
            kasvataListaa();
        }
        if (indeksi > this.pituus) {
            this.pituus = indeksi + 1;
        }
        this.lista[indeksi] = alkio;
    }

    /**
     * Hakee parametrina annetusta indeksistä sen osoittaman alkion.
     *
     * @param indeksi
     * @return indeksin osoittama alkio
     * @throws IndexOutOfBoundsException jos indeksi on virheellinen.
     */
    public T hae(int indeksi) throws IndexOutOfBoundsException {
        if (indeksi >= this.pituus) {
            throw new IndexOutOfBoundsException();
        }
        return this.lista[indeksi];
    }

    /**
     *
     * @return True jos lista on tyhjä, muuten false.
     */
    public boolean onTyhja() {
        return this.pituus == 0;
    }

    /**
     *
     * @return tämän hetkinen sisäisen listan koko
     */
    public int varattuListanKoko() {
        return this.lista.length;
    }

    private void kasvataListaa() {
        this.maksimiPituus = this.maksimiPituus * 2;
        this.lista = kopioiLista();
    }

    private T[] kopioiLista() {
        T[] uusiLista = (T[]) new Object[this.maksimiPituus];
        int i = 0;
        for (T arvo : this.lista) {
            uusiLista[i] = arvo;
            i += 1;
        }
        return uusiLista;
    }

}
