package tietorakenne;

import kvk.korjaaja.Korjaaja;
import kvk.tietorakenne.BKSolmu;
import kvk.tietorakenne.Lista;
import kvk.tietorakenne.TrieSolmu;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListaTest {

    private Lista<String> sut;

    @Before
    public void setUp() {
        this.sut = new Lista<>(20);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void ListanKonstruktointi_OnnistuuEriTyypeilla() {
        Lista<BKSolmu> lista1 = new Lista<>(20);
        Lista<TrieSolmu> lista2 = new Lista<>(20);
        Lista<Object> lista3 = new Lista<>(20);
        Lista<Integer> lista4 = new Lista<>(20);
        Lista<Korjaaja> lista5 = new Lista<>(20);
    }

    @Test
    public void ListanKonstruktointi_LuoOikeanKokoisenSisaisenListan() {
        Lista<Object> lista1 = new Lista<>(20);
        Lista<String> lista2 = new Lista<>(2);
        Lista<String> lista3 = new Lista<>(0);

        assertTrue(lista1.varattuListanKoko() == 20);
        assertTrue(lista2.varattuListanKoko() == 2);
        assertTrue(lista3.varattuListanKoko() == 0);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void ListanKonstruktointi_MiinusPituudella_Kaatuu() {
        Lista<Object> lista1 = new Lista<>(-1);
    }

    @Test
    public void Lisaaminen_PerusTapaus_LisaaPeraan() {
        this.sut.lisaaListaan("Testi");
        assertEquals(this.sut.hae(0), "Testi");
    }

    @Test
    public void Lisaaminen_MontaAlkiota_LisaaAinaListanPeraan() {
        lisaaAlkioita(10);
        tarkistaPutkeenLisatytAlkiot(10);
    }

    @Test
    public void Lisaaminen_EnemmanAlkioitaKuinTilaaVarattu_KasvattaaTaulukkoa() {
        assertTrue(this.sut.varattuListanKoko() == 20);
        lisaaAlkioita(30);
        assertTrue(this.sut.varattuListanKoko() > 30);
    }

    @Test
    public void Lisaaminen_EnemmanAlkioitaKuinTilaaVarattu_LisaaAlkiotOikein() {
        lisaaAlkioita(5000);
        tarkistaPutkeenLisatytAlkiot(5000);
    }

    @Test
    public void Lisaaminen_YksittaiseenIndeksiin_Toimii() {
        this.sut.asetaIndeksiin("Testi", 5);
        assertTrue(this.sut.hae(5).equals("Testi"));
    }

    @Test
    public void Lisaaminen_YksittaiseenIndeksiinVaratunMuistinUlkoPuolella_KasvattaaVarattuaListaaVastaamaanIndeksia() {
        this.sut.asetaIndeksiin("Testi", 30);
        assertTrue(this.sut.hae(30).equals("Testi"));
        assertTrue(this.sut.varattuListanKoko() > 30);
    }

    @Test
    public void Lisaaminen_YksittaiseenIndeksiinVaratunMuistinUlkoPuolella_ListanPituusUudenIndeksinMukaan() {
        this.sut.asetaIndeksiin("Testi", 30);
        assertTrue(this.sut.pituus() == 31);

    }

    @Test
    public void Lisaaminen_YksittaiseenIndeksiinVaratunMuistinUlkoPuolella_MuuListaNULL() {
        this.sut.asetaIndeksiin("Testi", 30);
        for (int i = 0; i < 30; i++) {
            assertEquals(this.sut.hae(i), null);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void Haku_NegatiivisellaIndeksilla_KaatuuIndexOutOfBoundExceptioniin() {
        this.sut.hae(20);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void Haku_LiianSuurellaIndeksilla_KaatuuIndexOutOfBoundExceptioniin() {
        this.sut.hae(-1);
    }

    private void lisaaAlkioita(int maara) {
        for (int i = 0; i < maara; i++) {
            this.sut.lisaaListaan("Testi" + i);
        }
    }

    private void tarkistaPutkeenLisatytAlkiot(int maara) {
        for (int i = 0; i < 10; i++) {
            assertEquals(this.sut.hae(i), "Testi" + i);
        }
    }
}
