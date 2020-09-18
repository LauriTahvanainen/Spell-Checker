package algoritmi;

import kvk.algoritmi.MuokkausEtaisyyslaskija;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author coronatus
 */
public class MuokkausEtaisyyslaskijaTest {
    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void LevenshteinEtaisyys_SamallaSanalla_Palauttaa0() {
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("Kissa", "Kissa"), 0);
    }
    
    @Test
    public void LevenshteinEtaisyys_HelpollaTapauksella_PalauttaaOikeanEtaisyyden() {
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("Kissa", "Koira"), 3);
    }
    
    @Test
    public void LevenshteinEtaisyys_SanoillaJoissaIsojaJaPieniaKirjaimia_PalauttaaSamanEtaisyydenRiippumattaKirjainKoosta() {
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("KiSsA", "KOirA"), MuokkausEtaisyyslaskija.LevenshteinEtaisyys("kissa", "koira"));
    }
    
    @Test
    public void LevenshteinEtaisyys_EriPituisillaSanoilla_PalauttaaOikeanEtaisyyden() {
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("Kissa", "VainuKoira"), 8);
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("SisäKissa", "Tie"), 8);
    }
    
    @Test
    public void LevenshteinEtaisyys_TyhjäänSanaan_PalauttaaToisenMerkkijononPituude() {
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("Kissa", ""), 5);
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("", "vainukoira"), 10);
    }
    
    @Test
    public void LevenshteinEtaisyys_PalauttaaOikeanEtaisyyden() {
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("jalkapallo", "uppopallo"), 5);
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("Hevonen", "Antilooppi"), 9);
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("Juosta", "Juoda"), 2);
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("Kalastaa", "Onkia"), 7);
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("1234", "4321"), 4);
        assertEquals(MuokkausEtaisyyslaskija.LevenshteinEtaisyys("veturikortti", "etuilla"), 8);
    }

}
