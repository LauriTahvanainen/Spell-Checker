package algoritmi;

import kvk.algoritmi.LevenshteinEtaisyys;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

/**
 *
 */
public class LevenshteinEtaisyysTest {

    private LevenshteinEtaisyys sut;
    
    @Before
    public void setUp() {
        this.sut = new LevenshteinEtaisyys();
    }

    @Test
    public void LevenshteinEtaisyys_SamallaSanalla_Palauttaa0() {
        assertEquals(this.sut.laskeEtaisyys("Kissa", "Kissa"), 0);
    }
    
    @Test
    public void LevenshteinEtaisyys_HelpollaTapauksella_PalauttaaOikeanEtaisyyden() {
        assertEquals(this.sut.laskeEtaisyys("Kissa", "Koira"), 3);
    }
    
    @Test
    public void LevenshteinEtaisyys_SanoillaJoissaIsojaJaPieniaKirjaimia_PalauttaaSamanEtaisyydenRiippumattaKirjainKoosta() {
        assertEquals(this.sut.laskeEtaisyys("KiSsA", "KOirA"), this.sut.laskeEtaisyys("kissa", "koira"));
    }
    
    @Test
    public void LevenshteinEtaisyys_EriPituisillaSanoilla_PalauttaaOikeanEtaisyyden() {
        assertEquals(this.sut.laskeEtaisyys("Kissa", "VainuKoira"), 8);
        assertEquals(this.sut.laskeEtaisyys("SisäKissa", "Tie"), 8);
    }
    
    @Test
    public void LevenshteinEtaisyys_TyhjäänSanaan_PalauttaaToisenMerkkijononPituude() {
        assertEquals(this.sut.laskeEtaisyys("Kissa", ""), 5);
        assertEquals(this.sut.laskeEtaisyys("", "vainukoira"), 10);
    }
    
    @Test
    public void LevenshteinEtaisyys_PalauttaaOikeanEtaisyyden() {
        assertEquals(this.sut.laskeEtaisyys("jalkapallo", "uppopallo"), 5);
        assertEquals(this.sut.laskeEtaisyys("Hevonen", "Antilooppi"), 9);
        assertEquals(this.sut.laskeEtaisyys("Juosta", "Juoda"), 2);
        assertEquals(this.sut.laskeEtaisyys("Kalastaa", "Onkia"), 7);
        assertEquals(this.sut.laskeEtaisyys("1234", "4321"), 4);
        assertEquals(this.sut.laskeEtaisyys("veturikortti", "etuilla"), 8);
    }

}
