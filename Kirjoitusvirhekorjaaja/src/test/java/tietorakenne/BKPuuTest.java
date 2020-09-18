package tietorakenne;

import kvk.algoritmi.LevenshteinEtaisyys;
import kvk.tietorakenne.BKPuu;
import kvk.tietorakenne.BKSolmu;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author coronatus
 */
public class BKPuuTest {

    private BKPuu sut;
    private BKSolmu juuri;

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.juuri = new BKSolmu("ja", null, 0);
        this.sut = new BKPuu(new LevenshteinEtaisyys(), 2, this.juuri);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void lisaaSana_OlemassaOlevallaSanalla_EiLisaaSanaa() {
        this.sut.lisaaSana("ja");
        assertTrue(this.juuri.solmuLista.isEmpty());           
    }
    
    @Test
    public void lisaaSana_PerusTapaus_LisaaSanan() {
        this.sut.lisaaSana("Kilpikonna");
        assertFalse(this.juuri.solmuLista.isEmpty());
    }
    
    @Test
    public void lisaaSana_SanaSamallaEtaisyydella_LisataanLapsenPeraan() {
        this.sut.lisaaSana("työ");
        this.sut.lisaaSana("vyö");
        assertFalse(this.juuri.lapsiEtaisyydella(3).solmuLista.isEmpty());
    }
    
    @Test
    public void lisaaSana_SanaSamallaEtaisyydella_LisataanLapsenPeraanOikeallaEtaisyydella() {
        this.sut.lisaaSana("työ");
        this.sut.lisaaSana("vyö");
        assertTrue(this.juuri.lapsiEtaisyydella(3).lapsiEtaisyydella(1) != null);
    }

}
