package kvk.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.stage.Stage;
import kvk.korjaaja.IKorjaaja;
import kvk.tietorakenne.Lista;

/**
 * Määrittelee tiedostonkäsittelijän.
 */
public interface ITekstitiedostonKasittelija {

    File valitseTallennusTiedosto(Stage ikkuna);

    File valitseTekstiTiedosto(Stage ikkuna) throws IOException;

    boolean tallennaTeksti(String teksti, File tiedostoPolku);

    String lataaTeksti(File tiedostoPolku) throws IOException;

    boolean taytaSanastoTiedostosta(IKorjaaja korjaaja) throws IOException;

}
