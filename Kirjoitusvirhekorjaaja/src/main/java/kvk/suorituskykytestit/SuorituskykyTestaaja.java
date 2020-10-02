package kvk.suorituskykytestit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import kvk.io.TekstitiedostonKasittelija;
import kvk.korjaaja.IKorjaaja;

public class SuorituskykyTestaaja {

    private static Random RANDOM = new Random();
    private static final String ALPHABET = " -.,<'+0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzÄÖÅäö";

    public static double[] korjausOnnistumisProsentti(IKorjaaja korjaaja, int maksVirheitaSanassa) throws IOException {
        TekstitiedostonKasittelija lukija = new TekstitiedostonKasittelija();
        double[] tulokset = new double[300];
        int iteraatioita = 10;
        int otosKoko = 500;
        for (int i = 0; i < iteraatioita; i++) {
            String[] otos = lukija.lataaSatunnainenOtosSanoja(otosKoko);
            double oikeellisia = 0;
            for (String sana : otos) {
                String muokattuSana = muokkaaSanaa(sana, maksVirheitaSanassa);
                ArrayList<String> ehdotukset = new ArrayList<>(Arrays.asList(korjaaja.ehdotaKorjauksia(muokattuSana)));
                if (ehdotukset.contains(sana)) {
                    oikeellisia += 1;
                }
            }
            tulokset[i] = oikeellisia / 10000;
            System.out.println(oikeellisia / 10000);
            System.out.println(i + "/" + iteraatioita);
        }
        return tulokset;
    }

    private static String muokkaaSanaa(String sana, int maksVirheitaSanassa) {
        for (int i = 0; i < maksVirheitaSanassa; i++) {
            int operaatio = RANDOM.nextInt(maksVirheitaSanassa);
            switch (operaatio) {
                case 0:
                    sana = poisto(sana);
                    break;
                case 1:
                    sana = lisays(sana);
                    break;
                case 2:
                    sana = vaihto(sana);
                    break;
                case 3:
                    sana = transpositio(sana);
                    break;
                default:
                    break;
            }
        }
        return sana;
    }

    private static String poisto(String sana) {
        int indeksi = RANDOM.nextInt(sana.length());
        return sana.substring(0, indeksi) + sana.substring(indeksi + 1, sana.length());
    }

    private static String lisays(String sana) {
        int indeksi = RANDOM.nextInt(sana.length() + 1);
        return sana.substring(0, indeksi) + satunnainenKirjain() + sana.substring(indeksi, sana.length());
    }

    private static String vaihto(String sana) {
        int indeksi = RANDOM.nextInt(sana.length());
        return sana.substring(0, indeksi) + satunnainenKirjain() + sana.substring(indeksi + 1, sana.length());
    }

    private static String transpositio(String sana) {
        int indeksi1 = RANDOM.nextInt(sana.length());
        int indeksi2 = RANDOM.nextInt(sana.length());
        while (Math.abs(indeksi1 - indeksi2) != 1) {
            indeksi2 = RANDOM.nextInt(sana.length());
        }
        char indeksi1Merkki = sana.charAt(indeksi1);
        char indeksi2Merkki = sana.charAt(indeksi2);
        if (indeksi1 > indeksi2) {
            return sana.substring(0, indeksi2) + indeksi1Merkki + indeksi2Merkki + sana.substring(indeksi1 + 1, sana.length());
        } else {
            return sana.substring(0, indeksi1) + indeksi2Merkki + indeksi1Merkki + sana.substring(indeksi2 + 1, sana.length());
        }
    }

    private static char satunnainenKirjain() {
        return ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()));
    }
}
