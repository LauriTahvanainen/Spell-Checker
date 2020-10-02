package kvk.tietorakenne;

/**
 * Pino, jonka alkioden tyyppi on valittavissa. Kaikkien alkioiden pitää tätä valittua tyyppiä.
 * @param <T>
 */
public class Pino<T> {

    private T[] lista;
    private int osoittaja;
    private int varattuKoko;

    public Pino() {
        this.varattuKoko = 18;
        this.lista = (T[]) new Object[this.varattuKoko];
        this.osoittaja = -1;
    }

    /**
     * Onko jono tyhjä.
     * @return True jos jono on tyhjä, muuten false.
     */
    public boolean onTyhja() {
        return this.osoittaja == -1;
    }

    /**
     * Lisää jonoon päällimäiseksi alkion.
     * @param alkio joka lisätään.
     */
    public void lisaa(T alkio) {
        if (this.osoittaja == this.varattuKoko - 1) {
            kasvataPinoa();
        }
        this.osoittaja += 1;
        this.lista[this.osoittaja] = alkio;
    }

    /**
     * Poistaa ja palauttaa pinon päällimmäisen, eli viimeeksi lisätyn, alkion. 
     * @return poistettu päällimmäinen alkio.
     */
    public T poista() {
        if (onTyhja()) {
            return null;
        }
        return this.lista[this.osoittaja--];
    }

    private void kasvataPinoa() {
        this.varattuKoko = this.varattuKoko * 2;
        T[] uusiLista = (T[]) new Object[this.varattuKoko];
        int i = 0;
        for (T alkio : this.lista) {
            uusiLista[i] = alkio;
            i += 1;
        }
        this.lista = uusiLista;
    }

}
