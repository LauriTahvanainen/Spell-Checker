package kvk.tietorakenne;

public class RajattuJarjestettyLista {

    private SanaEtaisyysPari[] lista;
    private Jarjestys jarjestys;

    public RajattuJarjestettyLista(int koko, Jarjestys jarjestys) {
        this.lista = new SanaEtaisyysPari[koko];
        this.jarjestys = jarjestys;
    }

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
