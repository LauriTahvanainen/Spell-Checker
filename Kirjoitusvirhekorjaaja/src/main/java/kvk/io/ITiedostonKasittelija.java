package kvk.io;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javafx.stage.Stage;
import kvk.korjaaja.IKorjaaja;
import kvk.korjaaja.TrieBK;

/**
 * Määrittelee tiedostonkäsittelijän.
 */
public interface ITiedostonKasittelija {

    File valitseTallennusTiedosto(Stage ikkuna);

    File valitseTekstiTiedosto(Stage ikkuna) throws IOException;

    boolean tallennaTeksti(String teksti, File tiedostoPolku);

    String lataaTeksti(File tiedostoPolku) throws IOException;

    boolean taytaSanastoTiedostosta(IKorjaaja korjaaja, String tiedostoNimi) throws IOException;

    Properties lataaAsetukset();

    void tallennaAsetukset(Properties asetukset);
    
    boolean lisaaSanatTiedostoon(String[] sanat, String tiedostoNimi);

}
