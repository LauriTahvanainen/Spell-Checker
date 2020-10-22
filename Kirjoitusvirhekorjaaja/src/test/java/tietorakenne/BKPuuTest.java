package tietorakenne;

import kvk.algoritmi.IMuokkausEtaisyyslaskija;
import kvk.algoritmi.LevenshteinEtaisyys;
import kvk.tietorakenne.BKPuu;
import kvk.tietorakenne.BKSolmu;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BKPuuTest {

    private BKPuu sut;
    private BKSolmu juuri;

    @Before
    public void setUp() {
        this.juuri = new BKSolmu("ja");
        this.sut = new BKPuu(new LevenshteinEtaisyys(), this.juuri);
    }

    private void taytaPuuKasin() {
        BKSolmu solmu1 = new BKSolmu("yö");
        this.juuri.lisaaLapsi(solmu1, 2);

        BKSolmu solmu2 = new BKSolmu("työ");
        this.juuri.lisaaLapsi(solmu2, 3);
        BKSolmu solmu2_1 = new BKSolmu("syö");
        BKSolmu solmu2_2 = new BKSolmu("yöt");
        solmu2.lisaaLapsi(solmu2_1, 1);
        solmu2.lisaaLapsi(solmu2_2, 2);

        BKSolmu solmu3 = new BKSolmu("työt");
        this.juuri.lisaaLapsi(solmu3, 4);

        BKSolmu solmu4 = new BKSolmu("syödä");
        this.juuri.lisaaLapsi(solmu4, 5);
        BKSolmu solmu4_1 = new BKSolmu("lyödä");
        solmu4.lisaaLapsi(solmu4_1, 1);

        BKSolmu solmu5 = new BKSolmu("työntää");
        this.juuri.lisaaLapsi(solmu5, 7);
        BKSolmu solmu5_1 = new BKSolmu("työntäjä");
        BKSolmu solmu5_2 = new BKSolmu("syötävä");
        solmu5.lisaaLapsi(solmu5_1, 1);
        solmu5.lisaaLapsi(solmu5_2, 3);
        BKSolmu solmu5_2_1 = new BKSolmu("lyötävä");
        solmu5_2.lisaaLapsi(solmu5_2_1, 1);

        BKSolmu solmu6 = new BKSolmu("työntävä");
        this.juuri.lisaaLapsi(solmu6, 8);

        BKSolmu solmu7 = new BKSolmu("kuulantyöntäjä");
        this.juuri.lisaaLapsi(solmu7, 13);
        BKSolmu solmu7_1 = new BKSolmu("kuulanmyöntäjä");
        BKSolmu solmu7_2 = new BKSolmu("pallonsyöttäjä");
        solmu7.lisaaLapsi(solmu7_1, 1);
        solmu7.lisaaLapsi(solmu7_2, 6);
    }

    @Test
    public void lisaaSana_OlemassaOlevallaSanalla_EiLisaaSanaa() {
        this.sut.lisaaSana("ja");
        assertTrue(this.juuri.solmuLista.onTyhja());
    }

    @Test
    public void lisaaSana_PerusTapaus_LisaaSanan() {
        this.sut.lisaaSana("Kilpikonna");
        assertFalse(this.juuri.solmuLista.onTyhja());
    }

    @Test
    public void lisaaSana_PerusTapaus_LisaaSananOikein() {
        this.sut.lisaaSana("Kilpikonna");
        assertTrue(this.juuri.solmuLista.hae(9).equals(new BKSolmu("kilpikonna")));
    }

    @Test
    public void lisaaSana_SanaSamallaEtaisyydella_LisataanLapsenPeraan() {
        this.sut.lisaaSana("työ");
        this.sut.lisaaSana("vyö");
        assertFalse(this.juuri.lapsiEtaisyydella(3).solmuLista.onTyhja());
    }

    @Test
    public void lisaaSana_SanaSamallaEtaisyydella_LisataanLapsenPeraanOikeallaEtaisyydella() {
        this.sut.lisaaSana("työ");
        this.sut.lisaaSana("vyö");

        BKSolmu lisattyLapsi = this.juuri.lapsiEtaisyydella(3).lapsiEtaisyydella(1);
        assertEquals(new BKSolmu("vyö"), lisattyLapsi);
    }

    @Test
    public void haeLahimmatSanat_VirheellisellaSanallaLahimpiaSamoillaEtaisyyksilla_HakeeOikeatLahimmatSanatJarjestaaSamanEtaisyydenNousevaanAakkosJarjestykseen() {
        taytaPuuKasin();

        String[] lahimmatSanat = this.sut.haeLahimmatSanat("kyötävä", 2, 10);
        int alkioita = montaEhdotustaPalautettiin(lahimmatSanat);

        assertTrue(alkioita == 3);
        assertEquals("syötävä", lahimmatSanat[0]);
        assertEquals("lyötävä", lahimmatSanat[1]);
        assertEquals("työntävä", lahimmatSanat[2]);
    }

    @Test
    public void haeLahimmatSanat_PerusTapaus10Lahinta_Palauttaa10AlkioisenTaulukon() {
        taytaPuuKasin();

        String[] lahimmatSanat = this.sut.haeLahimmatSanat("kyötävä", 2, 10);

        assertTrue(lahimmatSanat.length == 10);
    }

    @Test
    public void haeLahimmatSanat_SanallaEiLoydyTuloksia10Lahinta_Palauttaa10AlkioisenTaulukon() {
        taytaPuuKasin();

        String[] lahimmatSanat = this.sut.haeLahimmatSanat("sdlkajglaewiungikavlkjnaiurew", 2, 10);

        assertTrue(lahimmatSanat.length == 10);
    }

    @Test
    public void haeLahimmatSanat_OlemassaOlevallaSanalla_HakeeKaikkiToleranssinSisallaOlevatSanatJaHaetunSanan() {
        taytaPuuKasin();

        String[] lahimmatSanat = this.sut.haeLahimmatSanat("lyötävä", 2, 10);
        int alkioita = montaEhdotustaPalautettiin(lahimmatSanat);

        assertTrue(alkioita == 3);
        assertTrue(lahimmatSanat[0].equals("lyötävä"));
        assertTrue(lahimmatSanat[1].equals("syötävä"));
        assertTrue(lahimmatSanat[2].equals("työntävä"));
    }

    @Test
    public void haeLahimmatSanat_OlemassaOlevallaSanallaNollaToleranssilla_HakeeYhdenOikeanSanan() {
        taytaPuuKasin();

        String[] lahimmatSanat = this.sut.haeLahimmatSanat("lyötävä", 0, 10);
        int alkioita = montaEhdotustaPalautettiin(lahimmatSanat);

        assertTrue(alkioita == 1);
        assertTrue(lahimmatSanat[0].equals("lyötävä"));
    }

    @Test
    public void haeLahimmatSanat_OlemattomallaSanallaNollaToleranssilla_EiHaeYhtaanSanaa() {
        taytaPuuKasin();

        String[] lahimmatSanat = this.sut.haeLahimmatSanat("lyötävyys", 0, 10);

        assertTrue(montaEhdotustaPalautettiin(lahimmatSanat) == 0);
    }

    @Test
    public void poistaSana_HelppoTapaus_PoistaaSanan() {
        this.sut.lisaaSana("ja");
        this.sut.lisaaSana("syötävä");
        this.sut.lisaaSana("lyötävä");
        this.sut.lisaaSana("työntävä");
        this.sut.poistaSana("lyötävä");
        String[] lahimmatSanat = this.sut.haeLahimmatSanat("lyötävä", 2, 10);

        assertTrue(montaEhdotustaPalautettiin(lahimmatSanat) == 2);
        assertEquals("syötävä", lahimmatSanat[0]);
        assertEquals("työntävä", lahimmatSanat[1]);
    }

    @Test
    public void poistaSana_PoistoJaLisaysUudestaan_PoistaaSananJaLisaaUudestaanHaettavaksi() {
        this.sut.lisaaSana("ja");
        this.sut.lisaaSana("syötävä");
        this.sut.lisaaSana("lyötävä");
        this.sut.lisaaSana("työntävä");
        this.sut.poistaSana("lyötävä");
        String[] lahimmatSanat = this.sut.haeLahimmatSanat("lyötävä", 2, 10);

        assertTrue(montaEhdotustaPalautettiin(lahimmatSanat) == 2);
        assertEquals("syötävä", lahimmatSanat[0]);
        assertEquals("työntävä", lahimmatSanat[1]);

        this.sut.lisaaSana("lyötävä");
        lahimmatSanat = this.sut.haeLahimmatSanat("lyötävä", 2, 10);

        assertTrue(montaEhdotustaPalautettiin(lahimmatSanat) == 3);
        assertEquals("lyötävä", lahimmatSanat[0]);
        assertEquals("syötävä", lahimmatSanat[1]);
        assertEquals("työntävä", lahimmatSanat[2]);
    }

    private int montaEhdotustaPalautettiin(String[] ehdotukset) {
        int alkioita = 0;
        for (String sana : ehdotukset) {
            if (sana != null) {
                alkioita += 1;
            }
        }
        return alkioita;
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
        this.juuri = new BKSolmu("ja");
        this.sut = new BKPuu(etaisyydenLaskija, this.juuri);
        taytaPuuKasin();

        String[] lahimmatSanat = this.sut.haeLahimmatSanat("pyötävä", 2, 10);
        verify(etaisyydenLaskija, times(7)).laskeEtaisyys(anyString(), anyString());
    }

}
