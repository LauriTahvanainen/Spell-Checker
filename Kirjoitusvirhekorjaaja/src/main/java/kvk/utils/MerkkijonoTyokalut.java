package kvk.utils;

public class MerkkijonoTyokalut {

    public static String esikasittele(String merkkijono) {
        merkkijono = merkkijono.trim().toLowerCase();
        char[] merkkijonoTaulukkona = merkkijono.toCharArray();

        for (int i = 0; i < merkkijono.length(); i++) {
            char merkki = merkkijonoTaulukkona[i];
            if (merkki == 'à' || merkki == 'â' || merkki == 'á') {
                merkkijonoTaulukkona[i] = 'a';
            } else if (merkki == 'è' || merkki == 'ê' || merkki == 'é') {
                merkkijonoTaulukkona[i] = 'e';
            } else if (merkki == 'š') {
                merkkijonoTaulukkona[i] = 's';
            } else if (merkki == 'ô') {
                merkkijonoTaulukkona[i] = 'o';
            } else if (merkki == 'û') {
                merkkijonoTaulukkona[i] = 'u';
            } else if (merkki == 'ž') {
                merkkijonoTaulukkona[i] = 'z';
            } else if (merkki == 'î') {
                merkkijonoTaulukkona[i] = 'i';
            }
        }
        return String.valueOf(merkkijonoTaulukkona);
    }
;

}
