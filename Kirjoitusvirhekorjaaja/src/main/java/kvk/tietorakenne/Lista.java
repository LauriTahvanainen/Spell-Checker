package kvk.tietorakenne;

public class Lista<T> {

    private T[] lista;
    private int maksimiPituus;
    private int pituus;

    public Lista(int alustavaPituus) {
        this.maksimiPituus = alustavaPituus;
        this.pituus = 0;
        this.lista = (T[]) new Object[this.maksimiPituus];
    }

    public int pituus() {
        return this.pituus;
    }

    public void lisaaListaan(T alkio) {
        if (this.pituus == this.maksimiPituus - 1) {
            kasvataListaa();
        }
        this.lista[this.pituus] = alkio;
        this.pituus += 1;
    }

    public void asetaIndeksiin(T alkio, int indeksi) {
        while (indeksi >= this.maksimiPituus) {
            kasvataListaa();
        }
        if (indeksi > this.pituus) {
            this.pituus = indeksi + 1;
        }
        this.lista[indeksi] = alkio;
    }

    public T hae(int indeksi) {
        if (indeksi < 0 || indeksi >= this.maksimiPituus) {
            return null;
        }
        return this.lista[indeksi];
    }

    public boolean onTyhja() {
        return this.pituus == 0;
    }
    
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
