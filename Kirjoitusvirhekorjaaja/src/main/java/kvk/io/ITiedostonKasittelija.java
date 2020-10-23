package kvk.io;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javafx.stage.Stage;
import kvk.korjaaja.IKorjaaja;

/**
 * Määrittelee tiedostonkäsittelijän.
 */
public interface ITiedostonKasittelija {

    File valitseTallennusTiedosto(Stage ikkuna);

    File valitseTekstiTiedosto(Stage ikkuna) throws IOException;

    void tallennaTeksti(String teksti, File tiedostoPolku) throws IOException;

    String lataaTeksti(File tiedostoPolku) throws IOException;

    void taytaSanastoTiedostosta(IKorjaaja korjaaja, String tiedostoNimi) throws IOException, Exception;

    Properties lataaAsetukset();

    void tallennaAsetukset(Properties asetukset) throws Exception;
    
    void lisaaSanatTiedostoon(String[] sanat, String tiedostoNimi) throws Exception;

}
