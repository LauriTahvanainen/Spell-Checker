package kvk.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Määrittelee tiedostonkäsittelijän.
 */
public interface ITekstitiedostonKasittelija {

    boolean tallennaTeksti(String teksti, File tiedostoPolku);

    String lataaTeksti(File tiedostoPolku) throws IOException;

    List<String> lataaSanastoListana() throws IOException;

}
