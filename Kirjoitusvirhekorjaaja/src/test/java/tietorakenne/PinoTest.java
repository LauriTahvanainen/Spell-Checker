package tietorakenne;

import kvk.tietorakenne.BKSolmu;
import kvk.tietorakenne.Pino;
import kvk.tietorakenne.TrieSolmu;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PinoTest {

    private Pino<String> sut;

    @Before
    public void setUp() {
        this.sut = new Pino();
    }

    @Test
    public void LisaaPoista_HelppoTapaus_Onnistuu() {
        this.sut.lisaa("Testi");
        assertEquals("Testi", this.sut.poista());
    }

    @Test
    public void LisaaPoista_MontaAlkiota_HakeeViimeisenaLisatyn() {
        lisaaAlkioita(5);
        assertEquals("Alkio5", this.sut.poista());
    }
    
    @Test
    public void LisaaPoista_MontaAlkiota_PoistaaOikeassaJarjestyksessa() {
        lisaaAlkioita(5);
        assertEquals("Alkio5", this.sut.poista());
        assertEquals("Alkio4", this.sut.poista());
        assertEquals("Alkio3", this.sut.poista());
        assertEquals("Alkio2", this.sut.poista());
        assertEquals("Alkio1", this.sut.poista());
        assertEquals("Alkio0", this.sut.poista());
    }

    @Test
    public void Lisaa_MontaAlkiota_EiOleTyhja() {
        lisaaAlkioita(5);
        assertFalse(this.sut.onTyhja());
    }

    @Test
    public void LisaaPoista_MontaAlkiotaPoistoTyhjaksiAsti_OnTyhja() {
        lisaaAlkioita(5);
        poistaAlkioita(5);
        assertTrue(this.sut.onTyhja());
    }

    @Test
    public void Lisaa_EnemmanAlkioitaKuinMuistiaVarattu_LaajentaaMuistiaLisaysToimii() {
        lisaaAlkioita(5000);
    }

    @Test
    public void Poista_TyhjallaPinolla_PalauttaaNULL() {
        assertEquals(null, this.sut.poista());
    }

    private void lisaaAlkioita(int monta) {
        for (int i = 0; i <= monta; i++) {
            this.sut.lisaa("Alkio" + i);
        }
    }

    private void poistaAlkioita(int monta) {
        for (int i = 0; i <= monta; i++) {
            this.sut.poista();
        }
    }
}
