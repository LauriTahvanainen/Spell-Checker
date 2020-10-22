package kvk.enums;

public enum Sanasto {
    PIENI("/sanasto1k.txt"),
    KESKIPIENI("/sanasto10k.txt"),
    KESKIKOKO("/sanasto100k.txt"),
    KESKILAAJA("/sanasto350k.txt"),
    LAAJA("/sanasto500k.txt"),
    KESKIMASSIIVINEN("/sanasto1m.txt"),
    MASSIIVINEN("/sanasto2m.txt"),
    LIIANSUURI("/sanasto4m.txt");

    public final String tiedostoNimi;

    Sanasto(String tiedostoNimi) {
        this.tiedostoNimi = tiedostoNimi;
    }

}
