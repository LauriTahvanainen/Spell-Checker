package kvk.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import kvk.enums.EtaisyysFunktio;
import kvk.enums.Korjaaja;
import kvk.enums.Sanasto;
import static kvk.utils.UiUtils.luoEtaisyysFunktionValintaLaatikko;
import static kvk.utils.UiUtils.luoKorjaajanValintaLaatikko;
import static kvk.utils.UiUtils.luoSanastoValitsin;
import static kvk.utils.UiUtils.luoSlideri;

/**
 * UI komponentti korjaajan asetusten valitsemiseen.
 */
public class KorjaajaValitsin {

    private final GridPane juuri;
    private final ChoiceBox<Korjaaja> korjaajaValitsin;
    private final ChoiceBox<EtaisyysFunktio> etaisyysFunktioValitsin;
    private final Slider toleranssiValitsin;
    private final Slider montaHaetaanValitsin;
    private final ListView<Sanasto> sanastoValitsin;

    /**
     * Alustaa valitsijakomponentin.
     *
     * @param windowWidth
     * @param sanastoValinta, voiko valita vain yhden vai monta sanastoa.
     */
    public KorjaajaValitsin(int windowWidth, SelectionMode sanastoValinta) {

        this.juuri = new GridPane();
        this.juuri.setStyle("-fx-background-color: bisque");
        this.juuri.setAlignment(Pos.CENTER);
        this.juuri.setHgap(20);
        this.juuri.setPrefWidth(windowWidth);
        this.juuri.setPadding(new Insets(10, 10, 10, 10));

        // valitsimet
        Label valitseKorjaajaTeksti = new Label("Korjaaja");
        this.korjaajaValitsin = luoKorjaajanValintaLaatikko();

        Label valitseEtaisyysFunktioTeksti = new Label("Etäisyysfunktio");
        this.etaisyysFunktioValitsin = luoEtaisyysFunktionValintaLaatikko();

        Label valitseToleranssiTeksti = new Label("Etäisyystoleranssi");
        this.toleranssiValitsin = luoSlideri(1, 10, 200, 1, 2, 1);

        Label montaHaetaanTeksti = new Label("Monta korjausta haetaan");
        this.montaHaetaanValitsin = luoSlideri(1, 50, 550, 1, 10, 1);

        Label valitseSanastonKokoTeksti = new Label("Sanasto");
        valitseSanastonKokoTeksti.setTextAlignment(TextAlignment.RIGHT);

        ObservableList<Sanasto> sanastoLista = FXCollections.observableArrayList();
        sanastoLista.addAll(Sanasto.values());
        this.sanastoValitsin = luoSanastoValitsin(sanastoLista, sanastoValinta);

        // Kasaaminen
        this.juuri.addRow(0, valitseKorjaajaTeksti, valitseEtaisyysFunktioTeksti, valitseToleranssiTeksti, montaHaetaanTeksti);
        this.juuri.addRow(1, this.korjaajaValitsin, this.etaisyysFunktioValitsin, this.toleranssiValitsin, this.montaHaetaanValitsin);
        this.juuri.addRow(2, valitseSanastonKokoTeksti);
        this.juuri.add(this.sanastoValitsin, 1, 2, 3, 1);

    }

    public void asetaArvot(Korjaaja korjaaja, EtaisyysFunktio etaisyysFunktio, int toleranssi, int montaHaetaan, Sanasto sanasto) {
        this.korjaajaValitsin.setValue(korjaaja);
        this.etaisyysFunktioValitsin.setValue(etaisyysFunktio);
        this.toleranssiValitsin.setValue(toleranssi);
        this.montaHaetaanValitsin.setValue(montaHaetaan);
        this.sanastoValitsin.getSelectionModel().select(sanasto);
    }

    public GridPane haeJuuri() {
        return this.juuri;
    }

    /**
     * Asettaa uuden noden valintapaneeliin halutulle paikalle.
     *
     * @param node
     * @param sarake
     * @param rivi
     */
    public void asetaLapsi(Node node, int sarake, int rivi) {
        this.juuri.add(node, sarake, rivi);
    }

    public int montaHaetaan() {
        return (int) this.montaHaetaanValitsin.getValue();
    }

    public int toleranssi() {
        return (int) this.toleranssiValitsin.getValue();
    }

    public Korjaaja korjaaja() {
        return this.korjaajaValitsin.getValue();
    }

    public EtaisyysFunktio etaisyysFunktio() {
        return this.etaisyysFunktioValitsin.getValue();
    }

    public ObservableList<Sanasto> sanasto() {
        return this.sanastoValitsin.getSelectionModel().getSelectedItems();
    }
}
