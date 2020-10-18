package kvk.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Random;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kvk.korjaaja.IKorjaaja;

/**
 * Hoitaa I/O toimenpiteet.
 */
public class TekstitiedostonKasittelija implements ITekstitiedostonKasittelija {

    private final String sanastoTiedostonNimi = "/sanalista.txt";

    private final FileChooser tiedostonValitsija;

    public TekstitiedostonKasittelija() {
        this.tiedostonValitsija = new FileChooser();
    }

    @Override
    public File valitseTekstiTiedosto(Stage ikkuna) throws IOException {
        this.tiedostonValitsija.setTitle("Valitse tekstitiedosto");
        return this.tiedostonValitsija.showOpenDialog(ikkuna);
    }

    @Override
    public File valitseTallennusTiedosto(Stage ikkuna) {
        this.tiedostonValitsija.setTitle("Tallenna teksti tiedostoon");
        return this.tiedostonValitsija.showSaveDialog(ikkuna);
    }

    /**
     * Tallentaa String objectissa annetun muuttujan annettuun tiedostoon.
     *
     * @param teksti
     * @param tiedostoPolku
     * @return true jos tekstin tallentaminen tiedostoon onnistui.
     */
    @Override
    public boolean tallennaTeksti(String teksti, File tiedostoPolku) {
        try (BufferedWriter puskuroituKirjoittaja = Files.newBufferedWriter(tiedostoPolku.toPath());) {
            puskuroituKirjoittaja.write(teksti);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Lataa parametrina annetusta tiedostosta tekstin ja palauttaa sen String
     * objektissa.
     *
     * @param tiedostoPolku
     * @return tiedostosta ladattu teksti.
     * @throws IOException
     */
    @Override
    public String lataaTeksti(File tiedostoPolku) throws IOException {
        try (BufferedReader puskuroituLukija = Files.newBufferedReader(tiedostoPolku.toPath())) {

            String teksti = "";
            String rivi;

            while ((rivi = puskuroituLukija.readLine()) != null) {
                teksti = teksti + rivi + "\n";
            }
            return teksti;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Lataa resursseista korjaajan käyttämän sanaston listana String objekteja.
     *
     * @return Resursseissa sijaitseva sanasto string listana.
     * @throws IOException
     */
    @Override
    public boolean taytaSanastoTiedostosta(IKorjaaja korjaaja) throws IOException {
        try (BufferedReader puskuroituLukija = new BufferedReader(new InputStreamReader(TekstitiedostonKasittelija.class.getResourceAsStream(this.sanastoTiedostonNimi)))) {

            String rivi;

            while ((rivi = puskuroituLukija.readLine()) != null) {
                korjaaja.lisaaSanastoonSana(rivi);
            }
            return true;
        } catch (IOException e) {
            System.out.println("VIRHE LADATESSA SANASTOA");
            throw e;
        }
    }

    /**
     * Satunnaisotanta kaikista sanaston sanoista käyttäen reservoir sampling
     * algoritmia.
     *
     * @param otosKoko
     * @return otos listana sanoja
     * @throws IOException
     */
    public String[] lataaSatunnainenOtosSanoja(int otosKoko) throws IOException {
        String[] otos = new String[otosKoko];

        try (BufferedReader puskuroituLukija = new BufferedReader(new InputStreamReader(TekstitiedostonKasittelija.class.getResourceAsStream(this.sanastoTiedostonNimi)))) {

            String rivi;
            int indeksi = 0;
            Random satunnaisGeneraattori = new Random(System.nanoTime());

            while ((rivi = puskuroituLukija.readLine()) != null) {
                if (indeksi < otosKoko) {
                    otos[indeksi] = rivi;
                } else {
                    int randomIndex = satunnaisGeneraattori.nextInt(indeksi);
                    if (randomIndex < otosKoko) {
                        otos[randomIndex] = rivi;
                    }
                }
                indeksi += 1;
            }

        } catch (IOException e) {
            throw e;
        }
        return otos;
    }

}
