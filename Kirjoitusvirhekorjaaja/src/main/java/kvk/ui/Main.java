package kvk.ui;

import java.text.BreakIterator;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kvk.algoritmi.LevenshteinEtaisyys;
import kvk.io.ITekstitiedostonKasittelija;
import kvk.io.TekstitiedostonKasittelija;
import kvk.korjaaja.IKorjaaja;
import kvk.korjaaja.Korjaaja;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.model.Paragraph;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;
import org.reactfx.collection.LiveList;

public class Main extends Application {

    private final int windowWidth = 800;
    private final int windowHeight = 500;
    private Scene tausta;
    private Stage ikkuna;
    private IKorjaaja virheenKorjaaja;
    private String initVirheViesti;
    private ITekstitiedostonKasittelija tiedostonKasittelija;
    private StyleClassedTextArea kirjoitusAlue;
    private LiveList kappaleLista;
    private BreakIterator sanaIteroija;
    private int viimeisinTarkistettuKappale;

    @Override
    public void start(Stage paaIkkuna) {
        ikkuna = paaIkkuna;
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
        this.viimeisinTarkistettuKappale = 0;
        this.tiedostonKasittelija = new TekstitiedostonKasittelija();
        this.virheenKorjaaja = new Korjaaja(this.tiedostonKasittelija, new LevenshteinEtaisyys(), 2);

//        double[] korjausProsentit = SuorituskykyTestaaja.korjausOnnistumisProsentti(virheenKorjaaja, 2);
//        long[] korjausAjat = SuorituskykyTestaaja.keskimaarainenKorjausAika(virheenKorjaaja, 2, 10, 500);
//        System.out.println(SuorituskykyTestaaja.etaisyysLaskijoidenSuoritusAjat()[0]);
//        Arrays.sort(korjausProsentit);
//        System.out.println(korjausProsentit[korjausProsentit.length / 2]);
        this.kirjoitusAlue = new StyleClassedTextArea();
        this.kappaleLista = this.kirjoitusAlue.getParagraphs();
        this.kirjoitusAlue.setPrefHeight(450);
        this.kirjoitusAlue.setWrapText(true);
        this.sanaIteroija = BreakIterator.getWordInstance(new Locale("fi", "FI"));

        Button tallennaTiedostoonNappi = new Button("Tallenna");
        Button lataaTiedostostaNappi = new Button("Lataa teksti tiedostosta");
        Button tarkistaVirhe = new Button("Tarkista kirjoitusvirhe");
        tarkistaVirhe.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
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
        tausta.getStylesheets().add(getClass().getResource("/tekstityylit.css").toExternalForm());

        Subscription tekstiKuuntelija = this.kirjoitusAlue.multiPlainChanges()
                .successionEnds(Duration.ofMillis(600))
                .subscribe(change -> {
                    tarkistaVirheetViimeisimmistaKappaleista(this.kirjoitusAlue.getCurrentParagraph());
                });

        Subscription klikkausKuuntelija = EventStreams.eventsOf(this.kirjoitusAlue, MouseEvent.MOUSE_CLICKED)
                .subscribe(click -> {
                    kasitteleKlikkaus();
                });
    }

    private void tarkistaVirheetKaikistaKappaleista() {
        for (int kappaleNumero = 0; kappaleNumero < kappaleLista.size(); kappaleNumero++) {
            tarkistaVirheetKappaleesta(kappaleNumero);
        }
    }

    private void tarkistaVirheetViimeisimmistaKappaleista(int kappaleNumero) {
        if (this.viimeisinTarkistettuKappale > kappaleNumero) {
            this.viimeisinTarkistettuKappale = kappaleNumero;
        }
        for (int i = this.viimeisinTarkistettuKappale; i <= kappaleNumero; i++) {
            tarkistaVirheetKappaleesta(i);
        }
    }

    private void tarkistaVirheetKappaleesta(int kappaleNumero) {
        Paragraph kappale = (Paragraph) kappaleLista.get(kappaleNumero);
        String kappaleMerkkijonona = kappale.getText();
        sanaIteroija.setText(kappale.getText());
        int alkuIndeksi = sanaIteroija.first();
        int loppuIndeksi = sanaIteroija.next();

        while (loppuIndeksi != BreakIterator.DONE) {
            String sana = kappaleMerkkijonona.substring(alkuIndeksi, loppuIndeksi);
            if (sana.equals("-")) {
                loppuIndeksi = sanaIteroija.next();
                sana = kappaleMerkkijonona.substring(alkuIndeksi, loppuIndeksi);
            }
            if (!Character.isLetterOrDigit(sana.charAt(0)) || sana.matches("[0-9]+")) {
                kirjoitusAlue.setStyle(kappaleNumero, alkuIndeksi, loppuIndeksi, Collections.EMPTY_LIST);
            } else {
                try {
                    if (virheenKorjaaja.onkoSanaVirheellinen(sana)) {
                        kirjoitusAlue.setStyle(kappaleNumero, alkuIndeksi, loppuIndeksi, Collections.singleton("underlined"));
                    } else {
                        kirjoitusAlue.setStyle(kappaleNumero, alkuIndeksi, loppuIndeksi, Collections.EMPTY_LIST);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            alkuIndeksi = loppuIndeksi;
            loppuIndeksi = sanaIteroija.next();
        }
        this.viimeisinTarkistettuKappale = kappaleNumero;
    }

    private void kasitteleKlikkaus() {
        int kappaleNumero = kirjoitusAlue.getCurrentParagraph();
        int kursoriKappaleessa = kirjoitusAlue.getCaretColumn();
        Paragraph kappale = (Paragraph) this.kappaleLista.get(kappaleNumero);
        Collection tyyliKursorinKohdalla = (Collection) kappale.getStyleAtPosition(kursoriKappaleessa);
        if (tyyliKursorinKohdalla.contains("underlined")) {
            IndexRange sananIndeksit = kappale.getStyleRangeAtPosition(kursoriKappaleessa);
            String[] korjausEhdotukset = this.virheenKorjaaja.ehdotaKorjauksia(kappale.substring(sananIndeksit.getStart(), sananIndeksit.getEnd()));
            KorjausEhdotusPonnahdus ponnahdus = new KorjausEhdotusPonnahdus(korjausEhdotukset);
            ponnahdus.show(ikkuna);
        }
    }

    private class KorjausEhdotusPonnahdus extends Popup {
        
        private final VBox tausta;
        
        KorjausEhdotusPonnahdus(String[] korjausEhdotukset) {
            super();
            this.tausta = new VBox();
            for (String ehdotus: korjausEhdotukset) {
                this.tausta.getChildren().add(new Button(ehdotus));
            }
            getContent().add(this.tausta);
        }

    }

}