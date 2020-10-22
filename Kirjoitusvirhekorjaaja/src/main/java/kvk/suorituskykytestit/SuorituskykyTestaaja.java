package kvk.suorituskykytestit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import javafx.util.Pair;
import kvk.algoritmi.EtaisyyslaskijaTehdas;
import kvk.algoritmi.IMuokkausEtaisyyslaskija;
import kvk.enums.EtaisyysFunktio;
import kvk.enums.Korjaaja;
import kvk.enums.Sanasto;
import kvk.io.TiedostonKasittelija;
import kvk.korjaaja.IKorjaaja;
import kvk.korjaaja.KorjaajaTehdas;

/**
 * Suorituskykytestaamiseen. Virheellisten sanojen otos generoidaan ottamalla
 * sanastosta satunnainen otos, sitten suorittamalla jokaiseen otoksen sanaa
 * neljää sanaa muuttavaa operaatiota halutun monta kertaa. Operaatior ovat
 * yhden merkin lisäys, poisto, vaihto, transpositio. Vaihdon ja lisäyksen
 * kanssa käytetään heuristiikkaa, jossa otetaan huomioon merkin viereiset
 * merkit näppäimistössä.
 */
public class SuorituskykyTestaaja {

    private static final Random RANDOM = new Random();
    private static final String ALPHABET = " -.,<'+0123456789abcdefghijklmnopqrstuvwxyzåäö";
    private static final TiedostonKasittelija IO = new TiedostonKasittelija();
    private static final HashMap<Character, char[]> MERKIN_VIEREISET_MERKIT = new HashMap<Character, char[]>() {
        {
            put('q', new char[]{'1', '2', 'w', 's', 'a'});
            put('w', new char[]{'q', '2', '3', 'e', 's', 'a',});
            put('e', new char[]{'w', '3', '4', 'r', 'd', 's'});
            put('r', new char[]{'e', '4', '5', 't', 'f', 'd'});
            put('t', new char[]{'r', '5', '6', 'y', 'g', 'f'});
            put('y', new char[]{'t', '6', '7', 'u', 'h', 'g'});
            put('u', new char[]{'y', '7', '8', 'i', 'j', 'h'});
            put('i', new char[]{'u', '8', '9', 'o', 'k', 'j'});
            put('o', new char[]{'i', '9', '0', 'p', 'l', 'k'});
            put('p', new char[]{'o', '0', '+', 'å', 'ö', 'l'});
            put('å', new char[]{'p', '+', 'ä', 'ö', "'".charAt(0)});
            put('a', new char[]{'q', 'w', 's', 'z', '<'});
            put('s', new char[]{'a', 'w', 'e', 'd', 'x', 'z'});
            put('d', new char[]{'s', 'e', 'r', 'f', 'c', 'x'});
            put('f', new char[]{'d', 'r', 't', 'g', 'v', 'c'});
            put('g', new char[]{'f', 't', 'y', 'h', 'b', 'v'});
            put('h', new char[]{'g', 'y', 'u', 'j', 'n', 'b'});
            put('j', new char[]{'h', 'u', 'i', 'k', 'm', 'n'});
            put('k', new char[]{'j', 'i', 'o', 'l', ',', 'm'});
            put('l', new char[]{'k', 'o', 'p', 'ö', '.', ','});
            put('ö', new char[]{'l', 'p', 'å', 'ä', '-', '.'});
            put('ä', new char[]{'ö', 'å', "'".charAt(0), '-'});
            put('z', new char[]{'<', 'a', 's', 'x'});
            put('x', new char[]{'z', 's', 'd', 'c'});
            put('c', new char[]{'x', 'd', 'f', 'v', ' '});
            put('v', new char[]{'c', 'f', 'g', 'b', ' '});
            put('b', new char[]{'v', 'g', 'h', 'n', ' '});
            put('n', new char[]{'b', 'h', 'j', 'm', ' '});
            put('m', new char[]{'n', 'j', 'k', ',', ' '});
            put('-', new char[]{'.', 'ö', 'ä'});
            put('1', new char[]{'q', '2'});
            put('2', new char[]{'1', 'q', 'w', '3'});
            put('3', new char[]{'2', 'w', 'e', '4'});
            put('4', new char[]{'3', 'e', 'r', '5'});
            put('5', new char[]{'4', 'r', 't', '6'});
            put('6', new char[]{'5', 't', 'y', '7'});
            put('7', new char[]{'6', 'y', 'u', '8'});
            put('8', new char[]{'7', 'u', 'i', '9'});
            put('9', new char[]{'8', 'i', 'o', '0'});
            put('0', new char[]{'9', 'o', 'p', '+'});
            put("'".charAt(0), new char[]{'ä', 'å'});
            put(' ', new char[]{'c', 'v', 'b', 'n', 'm'});
        }

    };

    /**
     * Testaa parametrina annetun korjaajan korjausehdotusten pitävyyttä.
     * Mittarina toimii onnistuneiden korjausten prosenttiosuus kaikista
     * korjausyrityksistä.
     *
     * @param korjaaja jonka suoritusta testataan.
     * @param maksVirheitaSanassa maksimimäärä virheitä, joita aineston sanoihin
     * generoidaan.
     * @param iteraatioita
     * @param otoskoko
     * @return taulukko jossa jokaista iteraatiota kohti yksi
     * onnistumisprosentti.
     * @throws IOException
     */
    public static double korjausOnnistumisProsenttiTesti(IKorjaaja korjaaja, int maksVirheitaSanassa, int iteraatioita, int otoskoko) throws IOException {
        double tulokset = 0;
        for (int i = 0; i < iteraatioita; i++) {
            String[] otos = IO.lataaSatunnainenOtosSanoja(otoskoko, korjaaja.sanasto().tiedostoNimi);
            double oikeellisia = 0;
            for (String sana : otos) {
                String muokattuSana = muokkaaSanaa(sana, maksVirheitaSanassa);
                ArrayList<String> ehdotukset = new ArrayList<>(Arrays.asList(korjaaja.ehdotaKorjauksia(muokattuSana)));
                if (ehdotukset.contains(sana.toLowerCase())) {
                    oikeellisia += 1;
                }
            }
            tulokset += oikeellisia / otoskoko;
        }
        return (tulokset / iteraatioita) * 100;
    }

    /**
     * Laskee listan keksimääräisiä aikoja, joiden verran parametrina annetulla
     * korjaajalla menee korjata otoskoon määrittämä määrä virheellisiä sanoja.
     *
     * @param korjaaja jota testataan
     * @param maksVirheitaSanassa maksimimäärä monta virhettä testiaineiston
     * sanoihin generoidaan.
     * @param iteraatioita monta iteraatiota suoritetaan. Määrittää
     * palautettavan taulukon pituuden.
     * @param otosKoko yhden testin korjattavien sanojen määrä.
     * @return Korjaajan keskimäärisen korjausajan millisekunteina.
     * @throws IOException
     */
    public static double keskimaarainenKorjausAikaTesti(IKorjaaja korjaaja, int maksVirheitaSanassa, int iteraatioita, int otosKoko) throws IOException {
        long kumuloituvaTulos = 0;
        double kulunutAika;
        long aika;
        long kumuloituvaAika;

        for (int i = 0; i < iteraatioita; i++) {
            kumuloituvaAika = 0;
            String[] otos = IO.lataaSatunnainenOtosSanoja(otosKoko, korjaaja.sanasto().tiedostoNimi);
            for (String sana : otos) {
                String muokattuSana = muokkaaSanaa(sana, maksVirheitaSanassa);
                aika = System.nanoTime();
                korjaaja.ehdotaKorjauksia(muokattuSana);
                kumuloituvaAika += System.nanoTime() - aika;
            }
            kumuloituvaTulos += kumuloituvaAika / otosKoko;
        }
        return (kumuloituvaTulos / iteraatioita) / 1000000.0;
    }

    /**
     * Alustaa korjaajan annetulla sanastolla ja palauttaa alustamiseen kuluneen
     * ajan sekunteina. Käyttää korjaajaa, (korjaaja:levenshtein:2:10). 5
     * alustusiteraatiota.
     *
     * @param sanasto
     * @return korjaajan keskimääräinen alustusaika
     * @throws java.lang.Exception
     */
    public static Pair<Integer, Double> sanastonLatausAikaTesti(Sanasto sanasto) throws Exception {
        int iteraatioita = 3;
        long aika;
        long kumuloituvaAika = 0;
        int sanastonKoko = 0;
        for (int i = 0; i < iteraatioita; i++) {
            aika = System.nanoTime();
            IKorjaaja korjaaja = KorjaajaTehdas.luoKorjaaja(IO, Korjaaja.TRIE_BK, EtaisyysFunktio.LEVENSHTEIN, 2, 10, sanasto);
            kumuloituvaAika += System.nanoTime() - aika;
            sanastonKoko = korjaaja.sanastonKoko();
        }
        return new Pair(sanastonKoko,(kumuloituvaAika / iteraatioita) / 1000000000.0);
    }

    /**
     * Testaa annetun etäisyyslaskijan suoritusaikaa syötteen kasvaessa.
     *
     * @return lista pareja, joissa sanojen pituus, ja etäisyyden laskemisen suoritusaika tämän pituisilla sanoilla.
     */
    public static ArrayList<Pair> etaisyysLaskijanSuoritusAika(EtaisyysFunktio etaisyysFunktio, int maksSanojenPituus) throws Exception {
        IMuokkausEtaisyyslaskija laskija = EtaisyyslaskijaTehdas.luoLaskija(etaisyysFunktio);
        ArrayList<Pair> tulokset = new ArrayList<>();
        long t;
        int i = 1;
        while (i <= 100) {
            tulokset.add(new Pair(i, laskeEtaisyyslaskuKeskimSuoritusAika(laskija, i)));
            i++;
        }
        while (i <= maksSanojenPituus) {
            tulokset.add(new Pair(i, laskeEtaisyyslaskuKeskimSuoritusAika(laskija, i)));
            i += 200;
        }
        return tulokset;
    }
    
    private static double laskeEtaisyyslaskuKeskimSuoritusAika(IMuokkausEtaisyyslaskija laskija, int merkkiJonojenPituus) {
        int iteraatioita = 5;
        long kumuloituvaArvo = 0;
        for (int i = 0; i < iteraatioita; i++) {
            String verrattava1 = satunnainenMerkkijono(merkkiJonojenPituus);
            String verrattava2 = satunnainenMerkkijono(merkkiJonojenPituus);
            long t = System.nanoTime();
            laskija.laskeEtaisyys(verrattava1, verrattava2);
            kumuloituvaArvo += System.nanoTime() - t;
        }
        return (kumuloituvaArvo / iteraatioita) / 1000000.0;
    }

    private static String satunnainenMerkkijono(int pituus) {
        String palautettava = "";
        for (int i = 0; i < pituus; i++) {
            palautettava = palautettava + ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()));
        }
        return palautettava;
    }

    private static String muokkaaSanaa(String sana, int maksVirheitaSanassa) {
        for (int i = 0; i < maksVirheitaSanassa; i++) {
            int operaatio = RANDOM.nextInt(4);
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
        return sana.toLowerCase();
    }

    private static String poisto(String sana) {
        int indeksi = RANDOM.nextInt(sana.length());
        return sana.substring(0, indeksi) + sana.substring(indeksi + 1, sana.length());
    }

    private static String lisays(String sana) {
        try {
            int indeksi = RANDOM.nextInt(sana.length() + 1);
            if (indeksi == 0) {
                return sana.substring(0, indeksi) + satunnainenKirjain(sana.charAt(indeksi)) + sana.substring(indeksi, sana.length());
            }
            return sana.substring(0, indeksi) + satunnainenKirjain(sana.charAt(indeksi - 1)) + sana.substring(indeksi, sana.length());
        } catch (Exception e) {
            System.out.println("");
        }
        return "";
    }

    private static String vaihto(String sana) {
        try {
            int indeksi = RANDOM.nextInt(sana.length());
            return sana.substring(0, indeksi) + satunnainenKirjain(sana.charAt(indeksi)) + sana.substring(indeksi + 1, sana.length());
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
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

    private static char satunnainenKirjain(char kirjain) {
        char[] korvaavatKirjaimet = MERKIN_VIEREISET_MERKIT.get(kirjain);
        if (korvaavatKirjaimet == null) {
            return ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()));
        }
        return MERKIN_VIEREISET_MERKIT.get(kirjain)[RANDOM.nextInt(MERKIN_VIEREISET_MERKIT.get(kirjain).length)];
    }
}
