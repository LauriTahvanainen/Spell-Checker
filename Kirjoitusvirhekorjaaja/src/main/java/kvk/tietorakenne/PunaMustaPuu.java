package kvk.tietorakenne;

import java.util.TreeSet;

class PunaMustaPuu {
    
    private PunaMustaSolmu juuri;
    private TreeSet<SanaEtaisyysPari> puu;
    
    public PunaMustaPuu(PunaMustaSolmu juuri) {
        juuri.asetaMustaksi();
        this.juuri = juuri;
        this.puu = new TreeSet<>();
    }

    public void lisaa(SanaEtaisyysPari sanaEtaisyysPari) {
        this.puu.add(sanaEtaisyysPari);
    }
    
    public String[] haeXPieninta(int montaHaetaan) {
        String[] lista = new String[10];
        for (int i = 0; i < 10; i++) {
            SanaEtaisyysPari sana = this.puu.pollFirst();
            if (sana == null) {
                break;
            }
            lista[i] = sana.toString();
        }
        return lista;
    }
    
}
