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
        assertEquals(0, this.sut.laskeEtaisyys("Kissa", "Kissa"));
    }

    @Test
    public void LevenshteinEtaisyys_HelpollaTapauksella_PalauttaaOikeanEtaisyyden() {
        assertEquals(3, this.sut.laskeEtaisyys("Kissa", "Koira"));
    }

    @Test
    public void LevenshteinEtaisyys_SanoillaJoissaIsojaJaPieniaKirjaimia_PalauttaaSamanEtaisyydenRiippumattaKirjainKoosta() {
        assertEquals(this.sut.laskeEtaisyys("KiSsA", "KOirA"), this.sut.laskeEtaisyys("kissa", "koira"));
    }

    @Test
    public void LevenshteinEtaisyys_EriSanoilla_PalauttaaOikeanEtaisyyden() {
        assertEquals(8, this.sut.laskeEtaisyys("Kissa", "VainuKoira"));
        assertEquals(8, this.sut.laskeEtaisyys("SisäKissa", "Tie"));
    }
    
    @Test
    public void LevenshteinEtaisyys_EriSanoilla_PalauttaaOikeanEtaisyydenRiippumattaParametrienJarjestyksesta() {
        assertEquals(this.sut.laskeEtaisyys("Kissa", "VainuKoira"), this.sut.laskeEtaisyys("VainuKoira", "Kissa"));
    }

    @Test
    public void LevenshteinEtaisyys_TyhjäänSanaan_PalauttaaToisenMerkkijononPituuden() {
        assertEquals(5, this.sut.laskeEtaisyys("Kissa", ""));
    }

    @Test
    public void LevenshteinEtaisyys_PalauttaaOikeanEtaisyyden() {
        assertEquals(5, this.sut.laskeEtaisyys("jalkapallo", "uppopallo"));
        assertEquals(9, this.sut.laskeEtaisyys("Hevonen", "Antilooppi"));
        assertEquals(2, this.sut.laskeEtaisyys("Juosta", "Juoda"));
        assertEquals(7, this.sut.laskeEtaisyys("Kalastaa", "Onkia"));
        assertEquals(4, this.sut.laskeEtaisyys("1234", "4321"));
        assertEquals(8, this.sut.laskeEtaisyys("veturikortti", "etuilla"));
    }

}
