package kvk.ui;

import java.text.BreakIterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kvk.algoritmi.LevenshteinEtaisyys;
import kvk.io.ITekstitiedostonKasittelija;
import kvk.io.TekstitiedostonKasittelija;
import kvk.korjaaja.IKorjaaja;
import kvk.korjaaja.Korjaaja;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.model.EditableStyledDocument;
import org.fxmisc.richtext.model.Paragraph;
import org.reactfx.collection.LiveList;

/**
 * Starts the application
 *
 */
public class Main extends Application {

    private final int windowWidth = 800;
    private final int windowHeight = 500;
    private Scene tausta;
    private IKorjaaja virheenKorjaaja;
    private String initVirheViesti;
    private ITekstitiedostonKasittelija tiedostonKasittelija;
    private StyleClassedTextArea kirjoitusAlue;
    private BreakIterator sanaIteroija;

    @Override
    public void start(Stage ikkuna) {
        ikkuna.setResizable(false);
        ikkuna.centerOnScreen();
        ikkuna.initStyle(StageStyle.UNIFIED);
        ikkuna.setTitle("Kirjoitusvirhekorjaaja");
        ikkuna.setScene(this.tausta);
        if (initVirheViesti != null) {
            Text virhe = new Text(initVirheViesti);
            VBox pane = new VBox();
            pane.setAlignment(Pos.CENTER);
            pane.getChildren().add(virhe);
            this.tausta.setRoot(pane);
        }

        ikkuna.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        this.tiedostonKasittelija = new TekstitiedostonKasittelija();
        this.virheenKorjaaja = new Korjaaja(this.tiedostonKasittelija, new LevenshteinEtaisyys(), 2);

        this.kirjoitusAlue = new StyleClassedTextArea();
        this.kirjoitusAlue.setPrefHeight(450);
        this.kirjoitusAlue.setWrapText(true);
        this.sanaIteroija = BreakIterator.getWordInstance(new Locale("fi", "FI"));

        Button tallennaTiedostoonNappi = new Button("Tallenna");
        Button lataaTiedostostaNappi = new Button("Lataa teksti tiedostosta");
        Button tarkistaVirhe = new Button("Tarkista kirjoitusvirhe");
        tarkistaVirhe.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                for (Paragraph lause : kirjoitusAlue.getParagraphs()) {
                    String kappale = lause.getText();
                    sanaIteroija.setText(lause.getText());
                    int alkuIndeksi = sanaIteroija.first();
                    int loppuIndeksi = sanaIteroija.next();
                    while (loppuIndeksi != BreakIterator.DONE) {
                        String sana = kappale.substring(alkuIndeksi, loppuIndeksi);
                        if (Character.isLetterOrDigit(sana.charAt(0))) {
                            if (virheenKorjaaja.onkoSanaVirheellinen(sana)) {
                                System.out.println("sana: " + sana + ", on virheellinen. Tässä 10 korjausehdotusta: " + Arrays.toString(virheenKorjaaja.ehdotaKorjauksia(sana)));
                            }
                        }
                        alkuIndeksi = loppuIndeksi;
                        loppuIndeksi = sanaIteroija.next();
                    }
                }
            }

        });
        lataaTiedostostaNappi.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

            }
        });

        TilePane nappiPaneeli = new TilePane();
        nappiPaneeli.getChildren().addAll(tallennaTiedostoonNappi, lataaTiedostostaNappi, tarkistaVirhe);

        VBox juuri = new VBox();
        juuri.getChildren().addAll(nappiPaneeli, kirjoitusAlue);

        this.tausta = new Scene(juuri, windowWidth, windowHeight);
    }

}
