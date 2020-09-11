package tietorakenne;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import kvk.tietorakenne.Trie;
import kvk.tietorakenne.TrieSolmu;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 * @author coronatus
 */
public class TrieTest {

    private Trie sut;
    private TrieSolmu juuri;

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.juuri = new TrieSolmu(Character.MIN_VALUE);
        this.sut = new Trie(this.juuri);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void LisaaSana_UusillaSanoillaIsoillaJaPienillaKirjaimilla_EiLisaaUusiaSolmuja() {
        this.juuri = mock(TrieSolmu.class);
        this.sut.lisaaSana("Kissa");
        this.sut.lisaaSana("kissa");
        this.sut.lisaaSana("kiSsA");
        verify(this.juuri, atMost(1)).lisaaLapsi('k', new TrieSolmu('k'));
    }

    @Test
    public void LisaaSana_UudellaSanalla_LisaaUudenSananOnnistuneesti() {
        this.sut.lisaaSana("Kissa");
        assertEquals(this.juuri.haeSolmuListasta('K').haeSolmuListasta('i').haeSolmuListasta('s').haeSolmuListasta('s').haeSolmuListasta('a').onkoSana(), true);
    }
    
        @Test
    public void Trie_MonillaUusillaSanoilla_LisaaSanatOnnistuneesti() {
        this.sut.lisaaSana("Kissa");
        this.sut.lisaaSana("Kisa");
        this.sut.lisaaSana("Katti");
        this.sut.lisaaSana("Lauma");
        this.sut.lisaaSana("Auto");
        assertEquals(this.juuri.haeSolmuListasta('K').haeSolmuListasta('i').haeSolmuListasta('s').haeSolmuListasta('s').haeSolmuListasta('a').onkoSana(), true);
        assertEquals(this.juuri.haeSolmuListasta('K').haeSolmuListasta('i').haeSolmuListasta('s').haeSolmuListasta('a').onkoSana(), true);
        assertEquals(this.juuri.haeSolmuListasta('k').haeSolmuListasta('a').haeSolmuListasta('t').haeSolmuListasta('t').haeSolmuListasta('i').onkoSana(), true);
        assertEquals(this.juuri.haeSolmuListasta('l').haeSolmuListasta('a').haeSolmuListasta('u').haeSolmuListasta('m').haeSolmuListasta('a').onkoSana(), true);
        assertEquals(this.juuri.haeSolmuListasta('a').haeSolmuListasta('u').haeSolmuListasta('t').haeSolmuListasta('o').onkoSana(), true);
        assertEquals(this.juuri.haeSolmuListasta('a').haeSolmuListasta('u').haeSolmuListasta('t').onkoSana(), false);
        assertEquals(this.juuri.onkoSana(), false);
        assertEquals(this.juuri.haeSolmuListasta('t') == null, true);

    }

    @Test
    public void OnkoSana_OlemattomallaSanalla_PalauttaaFalse() {
        this.sut.lisaaSana("Kissa");
        assertEquals(sut.onkoSana("kisa"), false);

    }

    @Test
    public void OnkoSana_OlemassaOlevanSananOsalla_PalauttaaFalse() {
        this.sut.lisaaSana("Kirjain");
        assertEquals(sut.onkoSana("kirja"), false);
    }
    
    @Test
    public void OnkoSana_MonellaOlemassaOlevallaSanalla_PalauttaaOikein() {
        this.sut.lisaaSana("Kissa");
        this.sut.lisaaSana("Kisa");
        this.sut.lisaaSana("Katti");
        this.sut.lisaaSana("Lauma");
        this.sut.lisaaSana("Auto");
        assertEquals(sut.onkoSana("kissa"), true);
        assertEquals(sut.onkoSana("kisa"), true);
        assertEquals(sut.onkoSana("katti"), true);
        assertEquals(sut.onkoSana("Lauma"), true);
        assertEquals(sut.onkoSana("Auto"), true);
        assertEquals(sut.onkoSana("Aut"), false);
        assertEquals(sut.onkoSana(""), false);
        assertEquals(sut.onkoSana("Tahiti"), false);
    }
}
