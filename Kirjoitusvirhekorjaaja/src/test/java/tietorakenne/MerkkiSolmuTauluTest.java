package tietorakenne;

import kvk.tietorakenne.MerkkiSolmuTaulu;
import kvk.tietorakenne.TrieSolmu;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MerkkiSolmuTauluTest {

    private MerkkiSolmuTaulu sut;

    @Before
    public void setUp() {
        this.sut = new MerkkiSolmuTaulu();
    }

    @Test
    public void LisaaHae_PerusTapaus_LisattuHaettavissaOikein() throws Exception {
        this.sut.lisaa('a', new TrieSolmu('a'));
        assertEquals(new TrieSolmu('a'), this.sut.hae('a'));
    }

    @Test
    public void LisaaHae_MontaLisatty_LisattuHaettavissaOikein() throws Exception {
        this.sut.lisaa('a', new TrieSolmu('a'));
        this.sut.lisaa('b', new TrieSolmu('b'));
        this.sut.lisaa('c', new TrieSolmu('c'));
        assertEquals(new TrieSolmu('a'), this.sut.hae('a'));
        assertEquals(new TrieSolmu('b'), this.sut.hae('b'));
        assertEquals(new TrieSolmu('c'), this.sut.hae('c'));
    }

    @Test(expected = Exception.class)
    public void Lisaa_SamanLisaaminenKaksiKertaa_Kaatuu() throws Exception {
        this.sut.lisaa('a', new TrieSolmu('a'));
        this.sut.lisaa('a', new TrieSolmu('b'));
    }

    @Test
    public void Lisaa_ASCIIAakkosia_KasvattaaListaa() throws Exception {
        assertEquals(0, this.sut.listanPituus());
        lisaaASCIIAakkoset();
        assertEquals(26, this.sut.listanPituus());
    }

    @Test
    public void Lisaa_numeroita_KasvattaaListaa() throws Exception {
        this.sut.lisaa('0', new TrieSolmu('0'));
        assertEquals(30, this.sut.listanPituus());
        this.sut.lisaa('3', new TrieSolmu('3'));
        assertEquals(33, this.sut.listanPituus());
        this.sut.lisaa('9', new TrieSolmu('9'));
        assertEquals(39, this.sut.listanPituus());
    }

    @Test
    public void Lisaa_aeaekkosia_KasvattaaListaa() throws Exception {
        this.sut.lisaa('å', new TrieSolmu('å'));
        assertEquals(27, this.sut.listanPituus());
        this.sut.lisaa('ä', new TrieSolmu('ä'));
        assertEquals(28, this.sut.listanPituus());
        this.sut.lisaa('ö', new TrieSolmu('ö'));
        assertEquals(29, this.sut.listanPituus());
    }

    @Test
    public void Lisaa_Erikoismerkkeja_KasvattaaListaa() throws Exception {
        this.sut.lisaa('-', new TrieSolmu('-'));
        this.sut.lisaa(' ', new TrieSolmu(' '));
        this.sut.lisaa('è', new TrieSolmu('è'));
        this.sut.lisaa("'".charAt(0), new TrieSolmu("'".charAt(0)));
        assertTrue(this.sut.listanPituus() > 26 && this.sut.listanPituus() < 54);
    }

    @Test
    public void Hae_EiLisattyja_HakuPalauttaaNull() throws Exception {
        assertEquals(null, this.sut.hae('a'));
    }

    @Test
    public void Hae_HyvaksyttyaMerkkiaJotaEiOllaLisattyEikaTilaaVarattu_PalauttaaNULL() throws Exception {
        assertNull(this.sut.hae('b'));
        assertNull(this.sut.hae('k'));
        assertNull(this.sut.hae('9'));
        assertNull(this.sut.hae('-'));
    }

    private void lisaaASCIIAakkoset() throws Exception {
        for (char kirjain = 'a'; kirjain <= 'z'; kirjain++) {
            this.sut.lisaa(kirjain, new TrieSolmu(kirjain));
        }
    }

}
