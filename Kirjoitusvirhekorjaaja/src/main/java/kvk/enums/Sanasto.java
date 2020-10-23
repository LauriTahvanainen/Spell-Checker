package kvk.enums;

/**
 * Enum sanastoille. Sanastojen tiedostonimet kovakoodattuna enumin avulla.
 */
public enum Sanasto {
    PIENI("./sanastot/sanastoPieni.txt"),
    KESKIPIENI("./sanastot/sanastoKeskiPieni.txt"),
    KESKIKOKO("./sanastot/sanastoKeskikoko.txt"),
    KESKILAAJA("./sanastot/sanastoKeskilaaja.txt"),
    LAAJA("./sanastot/sanastoLaaja.txt"),
    KESKIMASSIIVINEN("./sanastot/sanastoKeskiMassiivinen.txt"),
    MASSIIVINEN("./sanastot/sanastoMassiivinen.txt"),
    LIIANSUURI("./sanastot/sanastoLiianSuuri.txt");

    public final String tiedostoNimi;

    Sanasto(String tiedostoNimi) {
        this.tiedostoNimi = tiedostoNimi;
    }

}
