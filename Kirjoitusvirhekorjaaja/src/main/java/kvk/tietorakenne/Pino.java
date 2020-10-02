package kvk.tietorakenne;

public class Pino<T> {

    private T[] lista;
    private int osoittaja;
    private int varattuKoko;

    public Pino() {
        this.varattuKoko = 18;
        this.lista = (T[]) new Object[this.varattuKoko];
        this.osoittaja = -1;
    }

    public boolean onTyhja() {
        return this.osoittaja == -1;
    }

    public void lisaa(T alkio) {
        if (this.osoittaja == this.varattuKoko - 1) {
            kasvataPinoa();
        }
        this.osoittaja += 1;
        this.lista[this.osoittaja] = alkio;
    }

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
