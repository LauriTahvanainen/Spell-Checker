package tietorakenne;

import kvk.tietorakenne.Trie;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import kvk.tietorakenne.TrieSolmu;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TrieTest {

    private Trie sut;
    private TrieSolmu juuri;

    @Before
    public void setUp() {
        this.juuri = new TrieSolmu(Character.MIN_VALUE);
        this.sut = new Trie(this.juuri);
    }

    private void lisaaSanoja() throws Exception {
        this.sut.lisaaSana("Kissa");
        this.sut.lisaaSana("Kisa");
        this.sut.lisaaSana("Katti");
        this.sut.lisaaSana("Lauma");
        this.sut.lisaaSana("Auto");
    }

    @Test
    public void LisaaSana_UusillaSanoillaIsoillaJaPienillaKirjaimilla_EiLisaaUusiaSolmuja() throws Exception {
        this.juuri = mock(TrieSolmu.class);
        this.sut.lisaaSana("Kissa");
        this.sut.lisaaSana("kissa");
        this.sut.lisaaSana("kiSsA");
        verify(this.juuri, atMost(1)).lisaaLapsi('k', new TrieSolmu('k'));
    }

    @Test
    public void LisaaSana_UudellaSanalla_LisaaUudenSananOnnistuneesti() throws Exception {
        this.sut.lisaaSana("Kissa");
        assertEquals(this.juuri.haeSolmuListasta('K').haeSolmuListasta('i').haeSolmuListasta('s').haeSolmuListasta('s').haeSolmuListasta('a').onSana(), true);
    }

    @Test
    public void Trie_MonillaUusillaSanoilla_LisaaSanatOnnistuneesti() throws Exception {
        lisaaSanoja();
        assertEquals(this.juuri.haeSolmuListasta('K').haeSolmuListasta('i').haeSolmuListasta('s').haeSolmuListasta('s').haeSolmuListasta('a').onSana(), true);
        assertEquals(this.juuri.haeSolmuListasta('K').haeSolmuListasta('i').haeSolmuListasta('s').haeSolmuListasta('a').onSana(), true);
        assertEquals(this.juuri.haeSolmuListasta('k').haeSolmuListasta('a').haeSolmuListasta('t').haeSolmuListasta('t').haeSolmuListasta('i').onSana(), true);
        assertEquals(this.juuri.haeSolmuListasta('l').haeSolmuListasta('a').haeSolmuListasta('u').haeSolmuListasta('m').haeSolmuListasta('a').onSana(), true);
        assertEquals(this.juuri.haeSolmuListasta('a').haeSolmuListasta('u').haeSolmuListasta('t').haeSolmuListasta('o').onSana(), true);
        assertEquals(this.juuri.haeSolmuListasta('a').haeSolmuListasta('u').haeSolmuListasta('t').onSana(), false);
        assertEquals(this.juuri.onSana(), false);
        assertEquals(this.juuri.haeSolmuListasta('t') == null, true);

    }

    @Test
    public void OnkoSana_OlemattomallaSanalla_PalauttaaFalse() throws Exception {
        this.sut.lisaaSana("Kissa");
        assertEquals(sut.onkoSana("kisa"), false);

    }

    @Test
    public void OnkoSana_OlemassaOlevanSananOsalla_PalauttaaFalse() throws Exception {
        this.sut.lisaaSana("Kirjain");
        assertEquals(sut.onkoSana("kirja"), false);
    }

    @Test
    public void OnkoSana_MonellaOlemassaOlevallaSanalla_PalauttaaOikein() throws Exception {
        lisaaSanoja();
        assertEquals(this.sut.onkoSana("kissa"), true);
        assertEquals(this.sut.onkoSana("kisa"), true);
        assertEquals(this.sut.onkoSana("katti"), true);
        assertEquals(this.sut.onkoSana("Lauma"), true);
        assertEquals(this.sut.onkoSana("Auto"), true);
        assertEquals(this.sut.onkoSana("Aut"), false);
        assertEquals(this.sut.onkoSana(""), false);
        assertEquals(this.sut.onkoSana("Tahiti"), false);
    }

    @Test
    public void PoistaSana_HelppoTapaus_PoistaaSanan() throws Exception {
        lisaaSanoja();
        this.sut.poistaSana("katti");
        assertEquals(false, sut.onkoSana("katti"));
    }

    @Test
    public void PoistaSana_ViimeisellaKirjaimellaEiLapsia_PoistaaSananJaKirjaimetJoillaEiLapsiKirjaimia() throws Exception {
        this.sut.lisaaSana("matti");
        this.sut.lisaaSana("mattila");
        this.sut.poistaSana("mattila");
        assertEquals(false, this.sut.onkoSana("mattila"));
        assertEquals(true, this.sut.onkoSana("matti"));
        assertEquals(new TrieSolmu('i', true), this.juuri.haeSolmuListasta('m').haeSolmuListasta('a').haeSolmuListasta('t').haeSolmuListasta('t').haeSolmuListasta('i'));
        assertEquals(null, this.juuri.haeSolmuListasta('m').haeSolmuListasta('a').haeSolmuListasta('t').haeSolmuListasta('t').haeSolmuListasta('i').haeSolmuListasta('l'));
    }

    @Test
    public void PoistaSana_SanallaEiYhteisiaKirjaimiaMuidenKanssa_PoistaaSananJaKaikkiKirjaimet() throws Exception {
        this.sut.lisaaSana("matti");
        this.sut.lisaaSana("mattila");
        this.sut.lisaaSana("Hattu");
        this.sut.lisaaSana("Hassata");
        this.sut.lisaaSana("viima");
        this.sut.poistaSana("viima");
        assertEquals(false, this.sut.onkoSana("viima"));
        assertEquals(null, this.juuri.haeSolmuListasta('v'));
    }

    @Test
    public void PoistaSana_SanallaSisaltyyOlemassaOlevaanSanaan_PoistaaVainSanan() throws Exception {
        this.sut.lisaaSana("matti");
        this.sut.lisaaSana("mattila");
        this.sut.poistaSana("matti");
        assertEquals(false, this.sut.onkoSana("matti"));
        assertEquals(true, this.sut.onkoSana("mattila"));
    }

}
