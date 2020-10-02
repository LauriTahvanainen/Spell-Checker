
package kvk.tietorakenne;

public class PunaMustaSolmu {
    
    private SanaEtaisyysPari data;
    private Boolean onMusta;
    private PunaMustaSolmu vanhempi;
    private PunaMustaSolmu oikeaLapsi;
    private PunaMustaSolmu vasenLapsi;
    
    
    
    public PunaMustaSolmu() {
        this.data = null;
        this.onMusta = true;
        this.vasenLapsi = null;
        this.oikeaLapsi = null;
        this.vanhempi = null;
    }
    
    public void asetaData(SanaEtaisyysPari sanaEtaisyysPari) {
        this.data = sanaEtaisyysPari;
    }
    
    public void asetaVanhempi(PunaMustaSolmu vanhempi) {
        this.vanhempi = vanhempi;
    }
    
    public void asetaOikeaLapsi(PunaMustaSolmu lapsi) {
        this.oikeaLapsi = lapsi;
    }
    
    public void asetaVasenLapsi(PunaMustaSolmu lapsi) {
        this.vasenLapsi = lapsi;
    }
    
    public void asetaMustaksi() {
        this.onMusta = true;
    }
    
    public void asetaPunaiseksi() {
        this.onMusta = false;
    }
}
