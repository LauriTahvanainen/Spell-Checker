package kvk.algoritmi;

/**
 * Levenshtein etäisyyden toteuttava etäisyydenlaskija.
 */
public class LevenshteinEtaisyys implements IMuokkausEtaisyyslaskija{


     /**
     * Laskee kahden merkkijonon välisen (Levenshtein) muokkausetäisyyden käyttämällä dynaamisen ohjelmoinnin algoritmia. 
     * Muokkausetäisyydellä tarkoitetaan pienintä sallittujen operaation määrä merkkijonolle X, joilla X:stä saadaan merkkijono Y. 
     * Tässä tapauksessa sallittuja operaatioita ovat: Yhden merkkin vaihtaminen toiseen, yhden merkkin poistaminen ja yhden merkin lisääminen.
     * @param mjonoX 
     * @param mjonoY
     * @return Merkkijonojen välinen muokkausetäisyys kokonaislukuna.
     */
    @Override
    public int laskeEtaisyys(String mjonoX, String mjonoY) {
        mjonoX = mjonoX.trim().toLowerCase();
        mjonoY = mjonoY.trim().toLowerCase();
        int[][] etaisyysMatriisi = new int[mjonoX.length() + 1][mjonoY.length() + 1];
        int operaationHinta = 1;
        
        for (int x = 0; x <= mjonoX.length(); x++) {
            etaisyysMatriisi[x][0] = x;
        }
        for (int y = 0; y <= mjonoY.length(); y++) {
            etaisyysMatriisi[0][y] = y;
        }
        
        for (int y = 1; y <= mjonoY.length(); y++) {
            for (int x = 1; x <= mjonoX.length(); x++) {
                if (mjonoX.charAt(x-1) == mjonoY.charAt(y-1)) {
                    etaisyysMatriisi[x][y] = etaisyysMatriisi[x-1][y-1];
                } else {
                    etaisyysMatriisi[x][y] = Math.min(Math.min(etaisyysMatriisi[x-1][y], etaisyysMatriisi[x][y-1]), etaisyysMatriisi[x-1][y-1]) + operaationHinta;
                }
                
            }
        }        
        
        return etaisyysMatriisi[mjonoX.length()][mjonoY.length()];
    }
    
}
