package kvk.tietorakenne;

public class Lista<T> {

    private T[] lista;
    private int length;
    private Class<T> type;

    public Lista(Class<T> type) {
        this.type = type;
        this.length = 10;
        this.lista = (T[]) java.lang.reflect.Array.newInstance(type, this.length);
    }

    public int length() {
        return this.length;
    }

    private void kasvataListaa() {
        T[] uusiLista = (T[]) java.lang.reflect.Array.newInstance(type, this.length * 2);
        int i = 0;
        for (T arvo : this.lista) {
            uusiLista[i] = arvo;
            i += 1;
        }
    }

    public T getAt(int i) {
        return this.lista[i];
    }
}
