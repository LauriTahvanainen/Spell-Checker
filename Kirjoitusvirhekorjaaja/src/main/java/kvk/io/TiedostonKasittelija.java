package kvk.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kvk.enums.EtaisyysFunktio;
import kvk.enums.Korjaaja;
import kvk.enums.Sanasto;
import kvk.korjaaja.IKorjaaja;

/**
 * Hoitaa I/O toimenpiteet.
 */
public class TiedostonKasittelija implements ITiedostonKasittelija {

    private final FileChooser tiedostonValitsija;

    public TiedostonKasittelija() {
        this.tiedostonValitsija = new FileChooser();
    }

    /**
     * Avaa tiedostohakemiston, josta käyttäjä voi valita tiedoston.
     *
     * @param ikkuna
     * @return tiedosto
     * @throws java.io.IOException
     */
    @Override
    public File valitseTekstiTiedosto(Stage ikkuna) throws IOException {
        this.tiedostonValitsija.setTitle("Valitse tekstitiedosto");
        return this.tiedostonValitsija.showOpenDialog(ikkuna);
    }

    /**
     * Avaa tiedostohakemiston, josta käyttäjä voi valita tiedoston johon
     * tallentaa.
     *
     * @param ikkuna
     * @return tiedosto
     */
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
     * @throws java.io.IOException
     */
    @Override
    public void tallennaTeksti(String teksti, File tiedostoPolku) throws IOException {
        try (BufferedWriter puskuroituKirjoittaja = Files.newBufferedWriter(tiedostoPolku.toPath())) {
            puskuroituKirjoittaja.write(teksti);
        } catch (IOException e) {
            Logger.getLogger(TiedostonKasittelija.class.getName()).log(Level.SEVERE, null, e);
            throw e;
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
            Logger.getLogger(TiedostonKasittelija.class.getName()).log(Level.SEVERE, null, e);
            throw e;
        }
    }

    /**
     * Täyttää korjaajan resursseista luettavalla sanastolistalla.
     *
     * @param korjaaja
     * @param tiedostoNimi
     * @throws IOException
     */
    @Override
    public void taytaSanastoTiedostosta(IKorjaaja korjaaja, String tiedostoNimi) throws IOException, Exception {
        try (BufferedReader puskuroituLukija = new BufferedReader(new InputStreamReader(new FileInputStream(tiedostoNimi)))) {

            String rivi;

            while ((rivi = puskuroituLukija.readLine()) != null) {
                korjaaja.lisaaSanastoonSana(rivi);
            }
        } catch (IOException e) {
            Logger.getLogger(TiedostonKasittelija.class.getName()).log(Level.SEVERE, null, e);
            throw new IOException("Sanastotiedostoa ei löydetty!");
        } catch (Exception ex) {
            Logger.getLogger(TiedostonKasittelija.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    /**
     * Satunnaisotanta kaikista sanaston sanoista käyttäen reservoir sampling
     * algoritmia.
     *
     * @param otosKoko
     * @param tiedostoNimi
     * @return otos listana sanoja
     * @throws IOException
     */
    public String[] lataaSatunnainenOtosSanoja(int otosKoko, String tiedostoNimi) throws IOException {
        String[] otos = new String[otosKoko];

        try (BufferedReader puskuroituLukija = new BufferedReader(new InputStreamReader(new FileInputStream(tiedostoNimi)))) {

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
            Logger.getLogger(TiedostonKasittelija.class.getName()).log(Level.WARNING, null, e);
            throw e;
        }
        return otos;
    }

    /**
     * Lataa käyttäjän asetukset. Jos asetuksia ei ole, niin palauttaa
     * oletusasetukset.
     *
     * @return asetukset
     */
    @Override
    public Properties lataaAsetukset() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("./userConfig.properties"));
        } catch (IOException e) {
            properties.setProperty("korjaaja", Korjaaja.TRIE_BK.toString());
            properties.setProperty("sanasto", Sanasto.LAAJA.toString());
            properties.setProperty("etaisyys", "2");
            properties.setProperty("montaHaetaan", "10");
            properties.setProperty("etaisyysFunktio", EtaisyysFunktio.LEVENSHTEIN.toString());
        }
        return properties;
    }

    /**
     * Tallentaa asetukset kansioon, jossa ohjelma suoritetaan.
     *
     * @param asetukset
     */
    @Override
    public void tallennaAsetukset(Properties asetukset) throws Exception {
        try {
            asetukset.store(new FileOutputStream("./userConfig.properties"), "");
        } catch (IOException ex) {
            Logger.getLogger(TiedostonKasittelija.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    /**
     * Lisaa parametrina annetut sanat parametrina annettuun tiedostoon.
     *
     * @param sanat
     * @param tiedostoNimi
     * @throws Exception
     */
    @Override
    public void lisaaSanatTiedostoon(String[] sanat, String tiedostoNimi) throws Exception {
        try (BufferedWriter puskuroituKirjoittaja = new BufferedWriter(new FileWriter(tiedostoNimi, true))) {
            for (String sana : sanat) {
                puskuroituKirjoittaja.write(sana);
                puskuroituKirjoittaja.newLine();
            }
        } catch (Exception e) {
            Logger.getLogger(TiedostonKasittelija.class.getName()).log(Level.SEVERE, null, e);
            throw e;
        }
    }

}
