package tietorakenne;

import kvk.tietorakenne.Jarjestys;
import kvk.tietorakenne.JarjestyvaTaulukko;
import kvk.tietorakenne.SanaEtaisyysPari;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JarjestyvaTaulukkoTest {

    private JarjestyvaTaulukko sut;

    @Before
    public void setUp() {
        this.sut = new JarjestyvaTaulukko(10, Jarjestys.NOUSEVA);
    }

    @Test
    public void UusiLista_ListanKoolla_LuoOikeanKokoisenListan() {
        JarjestyvaTaulukko taulukko10 = new JarjestyvaTaulukko(10, Jarjestys.LASKEVA);
        JarjestyvaTaulukko taulukko5 = new JarjestyvaTaulukko(5, Jarjestys.NOUSEVA);
        JarjestyvaTaulukko taulukko15 = new JarjestyvaTaulukko(15, Jarjestys.NOUSEVA);
        assertEquals(10, taulukko10.haeMerkkijonoTaulukkona().length);
        assertEquals(5, taulukko5.haeMerkkijonoTaulukkona().length);
        assertEquals(15, taulukko15.haeMerkkijonoTaulukkona().length);
    }

    @Test
    public void UusiLista_Jarjestyksella_LuoListanJokaTayttyyOikeallaJarjestyksella() {
        JarjestyvaTaulukko taulukkoNouseva = new JarjestyvaTaulukko(10, Jarjestys.NOUSEVA);
        JarjestyvaTaulukko taulukkoLaskeva = new JarjestyvaTaulukko(5, Jarjestys.LASKEVA);
        taulukkoNouseva.lisaa(new SanaEtaisyysPari("koira", 1));
        taulukkoNouseva.lisaa(new SanaEtaisyysPari("kissa", 2));
        taulukkoNouseva.lisaa(new SanaEtaisyysPari("Norsu", 5));

        taulukkoLaskeva.lisaa(new SanaEtaisyysPari("koira", 1));
        taulukkoLaskeva.lisaa(new SanaEtaisyysPari("kissa", 2));
        taulukkoLaskeva.lisaa(new SanaEtaisyysPari("Norsu", 5));

        String[] mJonoListaNouseva = taulukkoNouseva.haeMerkkijonoTaulukkona();
        assertEquals("koira", mJonoListaNouseva[0]);
        assertEquals("kissa", mJonoListaNouseva[1]);
        assertEquals("Norsu", mJonoListaNouseva[2]);

        String[] mJonoListaLaskeva = taulukkoLaskeva.haeMerkkijonoTaulukkona();
        assertEquals("koira", mJonoListaLaskeva[2]);
        assertEquals("kissa", mJonoListaLaskeva[1]);
        assertEquals("Norsu", mJonoListaLaskeva[0]);
    }

    @Test
    public void Lisaaminen_UusiaAlkioitaJarjestyksessaTaulukkoonJossaTyhjaa_LisaaAinaViimeiseenTyhjaanKohtaan() {
        this.sut.lisaa(new SanaEtaisyysPari("koira", 1));
        tarkistaLista(new String[]{"koira", null, null, null, null, null, null, null, null, null});
        this.sut.lisaa(new SanaEtaisyysPari("koir", 2));
        tarkistaLista(new String[]{"koira", "koir", null, null, null, null, null, null, null, null});
        this.sut.lisaa(new SanaEtaisyysPari("kissa", 3));
        tarkistaLista(new String[]{"koira", "koir", "kissa", null, null, null, null, null, null, null});
        this.sut.lisaa(new SanaEtaisyysPari("norsu", 4));
        tarkistaLista(new String[]{"koira", "koir", "kissa", "norsu", null, null, null, null, null, null});
        this.sut.lisaa(new SanaEtaisyysPari("apina", 7));
        tarkistaLista(new String[]{"koira", "koir", "kissa", "norsu", "apina", null, null, null, null, null});
    }

    @Test
    public void Lisaaminen_UusiAlkioVanhojenValissaNousevaanListaan_LisaaOikeinTyontaaVanhojaEteen() {
        this.sut.lisaa(new SanaEtaisyysPari("koira", 1));
        tarkistaLista(new String[]{"koira", null, null, null, null, null, null, null, null, null});
        this.sut.lisaa(new SanaEtaisyysPari("koir", 2));
        tarkistaLista(new String[]{"koira", "koir", null, null, null, null, null, null, null, null});
        this.sut.lisaa(new SanaEtaisyysPari("kissa", 4));
        tarkistaLista(new String[]{"koira", "koir", "kissa", null, null, null, null, null, null, null});
        this.sut.lisaa(new SanaEtaisyysPari("kaira", 3));
        tarkistaLista(new String[]{"koira", "koir", "kaira", "kissa", null, null, null, null, null, null});
    }

    @Test
    public void Lisaaminen_UudetAlkiotSamallaEtaisyydella_LyhyempiEnsinSittenNousevaAakkosJarjestys() {
        this.sut.lisaa(new SanaEtaisyysPari("koira", 2));
        this.sut.lisaa(new SanaEtaisyysPari("coira", 2));
        this.sut.lisaa(new SanaEtaisyysPari("zira", 2));
        tarkistaLista(new String[]{"zira", "koira", "coira", null, null, null, null, null, null, null});
    }

    @Test
    public void Lisaaminen_UusiaPienempiaAlkioitaNousevaanListaan_LisaaOikeinTyontaaVanhojaPois() {
        lisaaSanojaSuuremmastaPienimpaan(this.sut);
        tarkistaLista(new String[]{"sana1", "sana2", "sana3", "sana4", "sana5", "sana6", "sana7", "sana8", "sana9", "sana10"});
    }

    @Test
    public void Lisaaminen_UusiaSuurempiaAlkiotaLaskevaanListaan_LisaaOikeinTyontaaVanhojaPois() {
        this.sut = new JarjestyvaTaulukko(10, Jarjestys.LASKEVA);
        lisaaSanojaPienemmastaSuurimpaan(this.sut);
        tarkistaLista(new String[]{"sana99", "sana98", "sana97", "sana96", "sana95", "sana94", "sana93", "sana92", "sana91", "sana90"});
    }

    private void tarkistaLista(String[] oikeatSanat) {
        assertArrayEquals(oikeatSanat, this.sut.haeMerkkijonoTaulukkona());
    }

    private void lisaaSanojaSuuremmastaPienimpaan(JarjestyvaTaulukko taulukko) {
        for (int i = 100; i > 0; i--) {
            taulukko.lisaa(new SanaEtaisyysPari("sana" + i, i));
        }
    }

    private void lisaaSanojaPienemmastaSuurimpaan(JarjestyvaTaulukko taulukko) {
        for (int i = 0; i < 100; i++) {
            taulukko.lisaa(new SanaEtaisyysPari("sana" + i, i));
        }
    }
}
