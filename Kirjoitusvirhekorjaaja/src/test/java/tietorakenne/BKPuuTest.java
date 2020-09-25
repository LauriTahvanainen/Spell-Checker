package tietorakenne;

import java.util.ArrayList;
import kvk.algoritmi.IMuokkausEtaisyyslaskija;
import kvk.algoritmi.LevenshteinEtaisyys;
import kvk.tietorakenne.BKPuu;
import kvk.tietorakenne.BKSolmu;
import kvk.tietorakenne.SanaEtaisyysPari;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        this.sut = new BKPuu(new LevenshteinEtaisyys(), this.juuri);
    }

    @After
    public void tearDown() {
    }

    private void taytaPuuKasin() {
        BKSolmu solmu1 = new BKSolmu("yö", this.juuri, 2);
        this.juuri.lisaaLapsi(solmu1, 2);

        BKSolmu solmu2 = new BKSolmu("työ", this.juuri, 3);
        this.juuri.lisaaLapsi(solmu2, 3);
        BKSolmu solmu2_1 = new BKSolmu("syö", solmu2, 1);
        BKSolmu solmu2_2 = new BKSolmu("yöt", solmu2, 2);
        solmu2.lisaaLapsi(solmu2_1, 1);
        solmu2.lisaaLapsi(solmu2_2, 2);

        BKSolmu solmu3 = new BKSolmu("työt", this.juuri, 4);
        this.juuri.lisaaLapsi(solmu3, 4);

        BKSolmu solmu4 = new BKSolmu("syödä", this.juuri, 5);
        this.juuri.lisaaLapsi(solmu4, 5);
        BKSolmu solmu4_1 = new BKSolmu("lyödä", solmu4, 1);
        solmu4.lisaaLapsi(solmu4_1, 1);

        BKSolmu solmu5 = new BKSolmu("työntää", this.juuri, 7);
        this.juuri.lisaaLapsi(solmu5, 7);
        BKSolmu solmu5_1 = new BKSolmu("työntäjä", solmu5, 1);
        BKSolmu solmu5_2 = new BKSolmu("syötävä", solmu5, 3);
        solmu5.lisaaLapsi(solmu5_1, 1);
        solmu5.lisaaLapsi(solmu5_2, 3);
        BKSolmu solmu5_2_1 = new BKSolmu("lyötävä", solmu5_2, 1);
        solmu5_2.lisaaLapsi(solmu5_2_1, 1);

        BKSolmu solmu6 = new BKSolmu("työntävä", this.juuri, 8);
        this.juuri.lisaaLapsi(solmu6, 8);

        BKSolmu solmu7 = new BKSolmu("kuulantyöntäjä", this.juuri, 13);
        this.juuri.lisaaLapsi(solmu7, 13);
        BKSolmu solmu7_1 = new BKSolmu("kuulanmyöntäjä", solmu7, 1);
        BKSolmu solmu7_2 = new BKSolmu("pallonsyöttäjä", solmu7, 6);
        solmu7.lisaaLapsi(solmu7_1, 1);
        solmu7.lisaaLapsi(solmu7_2, 6);
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

        BKSolmu lisattyLapsi = this.juuri.lapsiEtaisyydella(3).lapsiEtaisyydella(1);
        assertTrue(lisattyLapsi != null);
        assertTrue(lisattyLapsi.etaisyysVanhempaan == 1);
    }

    @Test
    public void haeLahimmatSanat_VirheellisellaSanalla_HakeeOikeatLahimmatSanatOikeillaEtaisyyksilla() {
        taytaPuuKasin();

        ArrayList<SanaEtaisyysPari> lahimmatSanat = this.sut.haeLahimmatSanat("kyötävä", 2);
        assertTrue(lahimmatSanat.size() == 3);
        assertTrue(lahimmatSanat.contains(new SanaEtaisyysPari("syötävä", 1)));
        assertTrue(lahimmatSanat.contains(new SanaEtaisyysPari("lyötävä", 1)));
        assertTrue(lahimmatSanat.contains(new SanaEtaisyysPari("työntävä", 2)));
    }

    @Test
    public void haeLahimmatSanat_OlemassaOlemassaOevallaSanalla_HakeeKaikkiToleranssinSisallaOlevatSanatJaHaetunSanan() {
        taytaPuuKasin();

        ArrayList<SanaEtaisyysPari> lahimmatSanat = this.sut.haeLahimmatSanat("lyötävä", 2);
        assertTrue(lahimmatSanat.size() == 3);
        assertTrue(lahimmatSanat.contains(new SanaEtaisyysPari("lyötävä", 0)));
        assertTrue(lahimmatSanat.contains(new SanaEtaisyysPari("syötävä", 1)));
        assertTrue(lahimmatSanat.contains(new SanaEtaisyysPari("työntävä", 2)));
    }

    @Test
    public void haeLahimmatSanat_VirheellisellaSanalla_LaskeeEtaisyydetVainRajattujenSanojenKanssa() {
        IMuokkausEtaisyyslaskija etaisyydenLaskija = mock(LevenshteinEtaisyys.class);
        when(etaisyydenLaskija.laskeEtaisyys("pyötävä", "ja")).thenReturn(7);
        when(etaisyydenLaskija.laskeEtaisyys("pyötävä", "työntävä")).thenReturn(2);
        when(etaisyydenLaskija.laskeEtaisyys("pyötävä", "työntää")).thenReturn(3);
        when(etaisyydenLaskija.laskeEtaisyys("pyötävä", "syötävä")).thenReturn(1);
        when(etaisyydenLaskija.laskeEtaisyys("pyötävä", "lyötävä")).thenReturn(1);
        when(etaisyydenLaskija.laskeEtaisyys("pyötävä", "työntäjä")).thenReturn(3);
        when(etaisyydenLaskija.laskeEtaisyys("pyötävä", "syödä")).thenReturn(4);
        this.juuri = new BKSolmu("ja", null, 0);
        this.sut = new BKPuu(etaisyydenLaskija, this.juuri);
        taytaPuuKasin();

        ArrayList<SanaEtaisyysPari> lahimmatSanat = this.sut.haeLahimmatSanat("pyötävä", 2);
        verify(etaisyydenLaskija, times(7)).laskeEtaisyys(anyString(), anyString());
    }

}
