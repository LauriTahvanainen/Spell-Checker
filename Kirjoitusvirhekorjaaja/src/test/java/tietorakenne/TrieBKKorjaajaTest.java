package tietorakenne;

import kvk.algoritmi.LevenshteinEtaisyys;
import kvk.enums.Sanasto;
import kvk.io.ITiedostonKasittelija;
import kvk.io.TiedostonKasittelija;
import kvk.korjaaja.TrieBKKorjaaja;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class TrieBKKorjaajaTest {
    
    private TrieBKKorjaaja sut;
    
    @Before
    public void setUp() throws Exception {
        ITiedostonKasittelija lukija = mock(ITiedostonKasittelija.class);
        this.sut = new TrieBKKorjaaja(lukija, new LevenshteinEtaisyys(), 2, 10, Sanasto.PIENI);
    }



     @Test
     public void LisaaminenJaPoistaminen_KasvattaaKokoaOikein() throws Exception {
         this.sut.lisaaSanastoonSana("sana");
         this.sut.lisaaSanastoonSana("kilo");
         assertEquals(2, this.sut.sanastonKoko());
         this.sut.poistaSanastostaSana("kilo");
         assertEquals(1, this.sut.sanastonKoko());
     }
     
          @Test
     public void Lisaaminen_SanaEiTuetullaMerkilla_NostaaLipunYlos() throws Exception {
         assertFalse(this.sut.yritettiinLisataSanaVirheellisellaMerkilla());
         this.sut.lisaaSanastoonSana("sané");
         assertTrue(this.sut.yritettiinLisataSanaVirheellisellaMerkilla());
     }
}
