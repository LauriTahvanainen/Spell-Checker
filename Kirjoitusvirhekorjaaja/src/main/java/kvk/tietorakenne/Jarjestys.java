package kvk.tietorakenne;

public enum Jarjestys {
    LASKEVA(1), NOUSEVA(-1);

    public final int arvo;

    private Jarjestys(int arvo) {
        this.arvo = arvo;
    }
}
