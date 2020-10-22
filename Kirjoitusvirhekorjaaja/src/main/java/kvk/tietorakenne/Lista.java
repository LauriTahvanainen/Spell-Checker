package kvk.tietorakenne;

import java.io.Serializable;

/**
 * Dynaamisesti kasvava lista.
 *
 * @param <T> listan alkioiden tyyppi.
 */
public class Lista<T>  implements Serializable{

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
     * @return indeksin osoittama alkio, null jos indeksiin ei ole liitetty
     * alkiota tai jos indeksi on suurempi kuin taulukon pituus;
     */
    public T hae(int indeksi) {
        if (indeksi >= this.maksimiPituus) {
            return null;
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
     * @return tämän hetkinen sisäisen listan koko.
     */
    public int varattuListanKoko() {
        return this.lista.length;
    }
    
    public String[] haeListaMerkkijonoTaulukkona() {
        String[] palautettava = new String[this.pituus];
        int i = 0;
        for (T arvo : this.lista) {
            if (i == this.pituus) {
                break;
            }
            palautettava[i] = arvo.toString();
            i += 1;
        }
        return palautettava;
    }

    private void kasvataListaa() {
        this.maksimiPituus = this.maksimiPituus * 2;
        this.lista = kopioiLista(this.maksimiPituus);
    }

    private T[] kopioiLista(int uusiPituus) {
        T[] uusiLista = (T[]) new Object[uusiPituus];
        int i = 0;
        for (T arvo : this.lista) {
            if (i == uusiPituus) {
                break;
            }
            uusiLista[i] = arvo;
            i += 1;
        }
        return uusiLista;
    }

}
