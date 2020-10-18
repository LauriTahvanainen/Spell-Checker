package kvk.ui;

import java.io.File;
import java.text.BreakIterator;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.IndexRange;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
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
    private KorjausEhdotusPonnahdus nykyinenPonnahdus;
    private ObservableSet<KeyCode> painetutNappaimet;
    private TilePane nappiPaneeli;
    private VBox juuriKomponentti;
    private File nykyinenTiedosto;
    private boolean muutoksiaTekstissa;
    private boolean tekstiJuuriLadattuTiedostosta;

    @Override
    public void start(Stage paaIkkuna) {
        ikkuna = paaIkkuna;
        ikkuna.setResizable(false);
        ikkuna.centerOnScreen();
        ikkuna.initStyle(StageStyle.UNIFIED);
        ikkuna.setTitle("Kirjoitusvirhekorjaaja");
        ikkuna.setScene(this.tausta);
        alustaKuuntelijat();

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
        this.painetutNappaimet = FXCollections.observableSet(new HashSet<KeyCode>());
        this.viimeisinTarkistettuKappale = 0;
        this.muutoksiaTekstissa = false;
        this.tekstiJuuriLadattuTiedostosta = false;
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

        lataaTiedostostaNappi.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    nykyinenTiedosto = tiedostonKasittelija.valitseTekstiTiedosto(ikkuna);
                    if (nykyinenTiedosto != null) {
                        muutaIkkunaTeksti(nykyinenTiedosto.getName());
                        kirjoitusAlue.replaceText(tiedostonKasittelija.lataaTeksti(nykyinenTiedosto));
                        tekstiJuuriLadattuTiedostosta = true;
                    }
                } catch (Exception ex) {
                    muutaIkkunaTeksti("");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        tallennaTiedostoonNappi.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                valitseTallennettavaTiedosto();
                tallennaValittuunTiedostoon();
            }

        });

        this.nappiPaneeli = new TilePane();
        nappiPaneeli.getChildren().addAll(tallennaTiedostoonNappi, lataaTiedostostaNappi);

        this.juuriKomponentti = new VBox();
        this.juuriKomponentti.getChildren().addAll(nappiPaneeli, kirjoitusAlue);

        this.tausta = new Scene(this.juuriKomponentti, this.windowWidth, this.windowHeight);
        tausta.getStylesheets().add(getClass().getResource("/tekstityylit.css").toExternalForm());
    }

    private void valitseTallennettavaTiedosto() {
        this.nykyinenTiedosto = this.tiedostonKasittelija.valitseTallennusTiedosto(this.ikkuna);
    }

    private void tallennaValittuunTiedostoon() {
        boolean tallennusOnnistui = this.tiedostonKasittelija.tallennaTeksti(this.kirjoitusAlue.getText(), this.nykyinenTiedosto);
        if (tallennusOnnistui) {
            muutaIkkunaTeksti(nykyinenTiedosto.getName() + " (Tallennettu)");
            this.muutoksiaTekstissa = false;
        } else {
            muutaIkkunaTeksti(nykyinenTiedosto.getName() + " (Tallennus epäonnistui)");
        }
    }

    private void alustaKuuntelijat() {

        Subscription tekstiKuuntelija = this.kirjoitusAlue.multiPlainChanges()
                .successionEnds(Duration.ofMillis(600))
                .subscribe(change -> {
                    tarkistaVirheetViimeisimmistaKappaleista(this.kirjoitusAlue.getCurrentParagraph());
                    if (!this.muutoksiaTekstissa && this.nykyinenTiedosto != null && !this.tekstiJuuriLadattuTiedostosta) {
                        this.muutoksiaTekstissa = true;
                        muutaIkkunaTeksti(nykyinenTiedosto.getName() + " (Tallentamattomia muutoksia)");
                    }
                    this.tekstiJuuriLadattuTiedostosta = false;
                });

        Subscription klikkausKuuntelija = EventStreams.eventsOf(this.kirjoitusAlue, MouseEvent.MOUSE_CLICKED)
                .subscribe(click -> {
                    kasitteleTapahtuma();
                });

        Subscription painetutNappaimetKuuntelija = EventStreams.changesOf(this.painetutNappaimet)
                .subscribe(change -> {
                    kasittelePainallukset();
                });

        lisaaNappainKuuntelijatKomponenttiin(this.juuriKomponentti);

        this.ikkuna.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                if (nykyinenTiedosto != null && muutoksiaTekstissa) {
                    Alert varmistusIlmoitus = new Alert(AlertType.CONFIRMATION, "Tallentamattomat muutokset menetetään!");
                    varmistusIlmoitus.setHeaderText("Tallentamattomia muutoksia! Oletko varma, että haluat sulkea sovelluksen?");
                    varmistusIlmoitus.setTitle("");
                    Optional<ButtonType> vastaus = varmistusIlmoitus.showAndWait();
                    if (!ButtonType.OK.equals(vastaus.get())) {
                        event.consume();
                    }
                }
            }

        });

    }

    private void lisaaNappainKuuntelijatKomponenttiin(Node osa) {

        Subscription nappainPainallusKuuntelija = EventStreams.eventsOf(osa, KeyEvent.KEY_PRESSED)
                .subscribe(keyEvent -> {
                    this.painetutNappaimet.add(keyEvent.getCode());
                });

        Subscription nappainNostoKuuntelija = EventStreams.eventsOf(osa, KeyEvent.KEY_RELEASED)
                .subscribe(keyEvent -> {
                    this.painetutNappaimet.remove(keyEvent.getCode());
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

    private void muutaIkkunaTeksti(String muutettava) {
        this.ikkuna.setTitle("Kirjoitusvirhekorjaaja: " + muutettava);
    }

    private void kasitteleTapahtuma() {
        if (nykyinenPonnahdus != null) {
            this.nykyinenPonnahdus.hide();
        }
        int kappaleNumero = kirjoitusAlue.getCurrentParagraph();
        int kursoriKappaleessa = kirjoitusAlue.getCaretColumn();
        Paragraph kappale = (Paragraph) this.kappaleLista.get(kappaleNumero);
        Collection tyyliKursorinKohdalla = (Collection) kappale.getStyleAtPosition(kursoriKappaleessa);
        if (tyyliKursorinKohdalla.contains("underlined") && !kappale.getText().isEmpty()) {
            IndexRange sananIndeksit = kappale.getStyleRangeAtPosition(kursoriKappaleessa);
            String[] korjausEhdotukset = this.virheenKorjaaja.ehdotaKorjauksia(kappale.substring(sananIndeksit.getStart(), sananIndeksit.getEnd()));
            this.nykyinenPonnahdus = new KorjausEhdotusPonnahdus(korjausEhdotukset, kappaleNumero, sananIndeksit);
            this.nykyinenPonnahdus.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    nykyinenPonnahdus.hide();
                }

            });
            Bounds hiirenKoordinaatit = this.kirjoitusAlue.caretBoundsProperty().getValue().get();
            this.nykyinenPonnahdus.show(this.kirjoitusAlue, hiirenKoordinaatit.getMaxX(), hiirenKoordinaatit.getMaxY());
            this.nappiPaneeli.requestFocus();
            lisaaNappainKuuntelijatKomponenttiin(this.nykyinenPonnahdus.tausta);
        }
    }

    private void korvaaSana(String korvaava, int kappaleNumero, IndexRange korjattavanIndeksitKappaleessa) {
        this.kirjoitusAlue.replace(kappaleNumero, korjattavanIndeksitKappaleessa.getStart(), kappaleNumero, korjattavanIndeksitKappaleessa.getEnd(), korvaava, Collections.EMPTY_LIST);
        this.kirjoitusAlue.requestFocus();
    }

    private void kasittelePainallukset() {
        if (this.nykyinenPonnahdus != null && this.nykyinenPonnahdus.isShowing() && this.painetutNappaimet.contains(KeyCode.ESCAPE)) {
            this.kirjoitusAlue.requestFocus();
        }
        if (this.painetutNappaimet.contains(KeyCode.CONTROL)) {
            if (this.painetutNappaimet.contains(KeyCode.ENTER)) {
                kasitteleTapahtuma();
            } else if (this.painetutNappaimet.contains(KeyCode.S)) {
                if (this.nykyinenTiedosto != null) {
                    if (this.muutoksiaTekstissa) {
                        tallennaValittuunTiedostoon();
                    }
                } else {
                    valitseTallennettavaTiedosto();
                    tallennaValittuunTiedostoon();
                }
            }
        }
    }

    private class KorjausEhdotusPonnahdus extends Popup {

        private final VBox tausta;

        KorjausEhdotusPonnahdus(String[] korjausEhdotukset, int kappaleNumero, IndexRange korjattavanIndeksit) {
            super();
            this.tausta = new VBox();
            int ehdotuksia = 0;
            for (String ehdotus : korjausEhdotukset) {
                if (ehdotus != null) {
                    ehdotuksia += 1;
                    Button nappi = new Button(ehdotus);
                    nappi.setDefaultButton(true);
                    this.tausta.getChildren().add(nappi);
                    nappi.getStyleClass().add("ehdotusnappi");
                    nappi.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            korvaaSana(ehdotus, kappaleNumero, korjattavanIndeksit);
                        }

                    });
                }
            }
            this.tausta.setAlignment(Pos.CENTER);
            if (ehdotuksia == 0) {
                Text eiEhdotuksiaTeksti = new Text();
                eiEhdotuksiaTeksti.getStyleClass().add("eiehdotuksia");
                this.tausta.getChildren().add(new Text("Ei ehdotuksia!"));
            }
            getContent().add(this.tausta);
        }

    }

}
