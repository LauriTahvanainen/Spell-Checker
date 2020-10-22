package kvk.ui;

import java.io.File;
import java.text.BreakIterator;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.IndexRange;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import kvk.enums.EtaisyysFunktio;
import kvk.enums.Korjaaja;
import kvk.enums.Sanasto;
import kvk.io.TiedostonKasittelija;
import kvk.korjaaja.IKorjaaja;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.model.Paragraph;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;
import org.reactfx.collection.LiveList;
import kvk.io.ITiedostonKasittelija;
import kvk.korjaaja.KorjaajaTehdas;
import static kvk.utils.UiUtils.luoProsessiKeskenIlmoitus;

public class Main extends Application {

    private final int windowWidth = 1200;
    private final int windowHeight = 800;
    private Scene tausta;
    private Stage ikkuna;
    private IKorjaaja virheenKorjaaja;
    private String initVirheViesti;
    private ITiedostonKasittelija tiedostonKasittelija;
    private StyleClassedTextArea kirjoitusAlue;
    private LiveList kappaleLista;
    private BreakIterator sanaIteroija;
    private int viimeisinTarkistettuKappale;
    private KorjausEhdotusPonnahdus nykyinenPonnahdus;
    private ObservableSet<KeyCode> painetutNappaimet;
    private VBox nappiPaneeli;
    private VBox juuriKomponentti;
    private File nykyinenTiedosto;
    private boolean muutoksiaTekstissa;
    private boolean tekstiJuuriLadattuTiedostosta;
    private Properties asetukset;
    private KorjaajaValitsin korjaajaValitsin;

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
    public void stop() {
        virheenKorjaaja.tallennaSanastoMuutokset();
        tiedostonKasittelija.tallennaAsetukset(asetukset);
    }

    @Override
    public void init() throws Exception {
        this.tiedostonKasittelija = new TiedostonKasittelija();
        this.asetukset = this.tiedostonKasittelija.lataaAsetukset();
        alustaKorjaaja();
        this.painetutNappaimet = FXCollections.observableSet(new HashSet<KeyCode>());
        this.viimeisinTarkistettuKappale = 0;
        this.muutoksiaTekstissa = false;
        this.tekstiJuuriLadattuTiedostosta = false;
        this.kirjoitusAlue = new StyleClassedTextArea();
        this.kirjoitusAlue.setPrefHeight(this.windowHeight);
        this.kirjoitusAlue.setWrapText(true);
        this.kappaleLista = this.kirjoitusAlue.getParagraphs();
        this.sanaIteroija = BreakIterator.getWordInstance(new Locale("fi", "FI"));

        alustaNapit();

        this.juuriKomponentti = new VBox();
        this.juuriKomponentti.getChildren().addAll(nappiPaneeli, kirjoitusAlue);

        this.tausta = new Scene(this.juuriKomponentti, this.windowWidth, this.windowHeight);
        tausta.getStylesheets().add(getClass().getResource("/tekstityylit.css").toExternalForm());
    }

    private void alustaNapit() {
        this.nappiPaneeli = new VBox();
        this.nappiPaneeli.setAlignment(Pos.CENTER);
        this.nappiPaneeli.setPadding(new Insets(10, 0, 10, 0));
        HBox toimintoNappiPaneeli = new HBox();
        toimintoNappiPaneeli.setAlignment(Pos.CENTER);

        Button tallennaTiedostoonNappi = new Button("Tallenna");
        Button lataaTiedostostaNappi = new Button("Lataa teksti tiedostosta");
        Button suoritusKykytestitNappi = new Button("Suorituskykytestit");
        Button naytaKorjaajaValitsin = new Button("Muuta korjaajan asetuksia");
        Button muutaKorjaajanAsetuksia = new Button("Tallenna");
        Button suljeKorjaajanAsetukset = new Button("Sulje");

        this.korjaajaValitsin.asetaLapsi(muutaKorjaajanAsetuksia, 4, 1);
        this.korjaajaValitsin.asetaLapsi(suljeKorjaajanAsetukset, 4, 2);

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
                File tiedosto = valitseTallennettavaTiedosto();
                if (tiedosto != null) {
                    nykyinenTiedosto = tiedosto;
                    tallennaValittuunTiedostoon();
                }
            }

        });

        suoritusKykytestitNappi.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                avaaSuorituskykyIkkuna();
            }

        });

        muutaKorjaajanAsetuksia.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                tarkistaJaTallennaKorjaajanAsetustenMuutokset();
            }

        });

        naytaKorjaajaValitsin.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                GridPane valitsimenJuuri = korjaajaValitsin.haeJuuri();
                if (!nappiPaneeli.getChildren().contains(valitsimenJuuri)) {
                    nappiPaneeli.getChildren().add(valitsimenJuuri);
                }
            }

        });

        suljeKorjaajanAsetukset.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                nappiPaneeli.getChildren().remove(korjaajaValitsin.haeJuuri());
            }

        });

        toimintoNappiPaneeli.getChildren().addAll(tallennaTiedostoonNappi, lataaTiedostostaNappi, suoritusKykytestitNappi, naytaKorjaajaValitsin);
        nappiPaneeli.getChildren().add(toimintoNappiPaneeli);
    }

    private void avaaSuorituskykyIkkuna() {
        SuoritusKykyIkkuna suorituskykyIkkuna = new SuoritusKykyIkkuna(this.ikkuna, this.tiedostonKasittelija, this.windowWidth, this.windowHeight);
        suorituskykyIkkuna.nayta();
    }

    private void tarkistaJaTallennaKorjaajanAsetustenMuutokset() {
        Sanasto nykyinenSanasto = Sanasto.valueOf(this.asetukset.getProperty("sanasto").toUpperCase());
        Korjaaja nykyinenKorjaaja = Korjaaja.valueOf(this.asetukset.getProperty("korjaaja"));
        EtaisyysFunktio nykyinenEtaisyysFunktio = EtaisyysFunktio.valueOf(this.asetukset.getProperty("etaisyysFunktio"));
        int nykyinenEtaisyys = Integer.valueOf(this.asetukset.getProperty("etaisyys"));
        int nykyinenMontaHaetaan = Integer.valueOf(this.asetukset.getProperty("montaHaetaan"));

        EtaisyysFunktio valittuEtaisyysFunktio = this.korjaajaValitsin.etaisyysFunktio();
        Korjaaja valittuKorjaaja = this.korjaajaValitsin.korjaaja();
        Sanasto valittuSanasto = this.korjaajaValitsin.sanasto().get(0);
        int valittuMontaHaetaan = this.korjaajaValitsin.montaHaetaan();
        int valittuToleranssi = this.korjaajaValitsin.toleranssi();

        if (valittuSanasto == null) {
            Alert ilmoitus = new Alert(Alert.AlertType.ERROR);
            ilmoitus.setTitle("Virhe");
            ilmoitus.setContentText("Sanastoa ei ole valittu!\nValintoja ei voida tallentaa");
            ilmoitus.setHeaderText("Virhe!");
            ilmoitus.showAndWait();
            return;
        }

        boolean uudenAlustuksenVaativaMuutos = valittuEtaisyysFunktio != nykyinenEtaisyysFunktio || valittuKorjaaja != nykyinenKorjaaja || valittuSanasto != nykyinenSanasto;
        if (uudenAlustuksenVaativaMuutos) {
            alustaKorjaajaUudelleen(valittuKorjaaja, valittuEtaisyysFunktio, valittuToleranssi, valittuMontaHaetaan, valittuSanasto);
            this.asetukset.setProperty("etaisyys", String.valueOf(valittuToleranssi));
            this.asetukset.setProperty("montaHaetaan", String.valueOf(valittuMontaHaetaan));
            this.asetukset.setProperty("korjaaja", valittuKorjaaja.toString());
            this.asetukset.setProperty("etaisyysFunktio", valittuEtaisyysFunktio.toString());
            this.asetukset.setProperty("sanasto", valittuSanasto.toString());
        } else {
            this.virheenKorjaaja.asetaMontaEhdotustaHaetaan(valittuMontaHaetaan);
            this.virheenKorjaaja.asetaEtaisyysToleranssi(valittuToleranssi);
            this.asetukset.setProperty("etaisyys", String.valueOf(valittuToleranssi));
            this.asetukset.setProperty("montaHaetaan", String.valueOf(valittuMontaHaetaan));
        }
        nappiPaneeli.getChildren().remove(korjaajaValitsin.haeJuuri());
    }

    private void alustaKorjaaja() throws Exception {
        this.korjaajaValitsin = new KorjaajaValitsin(this.windowWidth, SelectionMode.SINGLE);

        Sanasto sanasto = Sanasto.valueOf(this.asetukset.getProperty("sanasto").toUpperCase());
        Korjaaja korjaaja = Korjaaja.valueOf(this.asetukset.getProperty("korjaaja"));
        EtaisyysFunktio etaisyysFunktio = EtaisyysFunktio.valueOf(this.asetukset.getProperty("etaisyysFunktio"));
        int etaisyys = Integer.valueOf(this.asetukset.getProperty("etaisyys"));
        int montaHaetaan = Integer.valueOf(this.asetukset.getProperty("montaHaetaan"));

        this.korjaajaValitsin.asetaArvot(korjaaja, etaisyysFunktio, etaisyys, montaHaetaan, sanasto);

        this.virheenKorjaaja = KorjaajaTehdas.luoKorjaaja(tiedostonKasittelija, korjaaja, etaisyysFunktio, etaisyys, montaHaetaan, sanasto);
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
                    kasitteleKlikkaus();
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

    private File valitseTallennettavaTiedosto() {
        return this.tiedostonKasittelija.valitseTallennusTiedosto(this.ikkuna);
    }

    private void tallennaValittuunTiedostoon() {
        boolean tallennusOnnistui = this.tiedostonKasittelija.tallennaTeksti(this.kirjoitusAlue.getText(), this.nykyinenTiedosto);
        if (tallennusOnnistui) {
            muutaIkkunaTeksti(nykyinenTiedosto.getName() + " (Tallennettu!)");
            this.muutoksiaTekstissa = false;
        } else {
            muutaIkkunaTeksti(nykyinenTiedosto.getName() + " (Tallentaminen epäonnistui!)");
        }
    }
//
//    private void tarkistaVirheetKaikistaKappaleista() {
//        for (int kappaleNumero = 0; kappaleNumero < kappaleLista.size(); kappaleNumero++) {
//            tarkistaVirheetKappaleesta(kappaleNumero);
//        }
//    }

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
            if (!sana.matches("(^[a-zA-Z-öäå]*$)") || sana.matches("[0-9]+")) {
                kirjoitusAlue.setStyle(kappaleNumero, alkuIndeksi, loppuIndeksi, Collections.EMPTY_LIST);
            } else {
                try {
                    if (virheenKorjaaja.onkoSanaVirheellinen(sana)) {
                        kirjoitusAlue.setStyle(kappaleNumero, alkuIndeksi, loppuIndeksi, Collections.singleton("alleviivattu"));
                    } else {
                        kirjoitusAlue.setStyle(kappaleNumero, alkuIndeksi, loppuIndeksi, Collections.singleton("sanastonsana"));
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

    private void kasitteleKlikkaus() {
        if (nykyinenPonnahdus != null) {
            this.nykyinenPonnahdus.hide();
        }
        int kappaleNumero = kirjoitusAlue.getCurrentParagraph();
        int kursoriKappaleessa = kirjoitusAlue.getCaretColumn();
        Paragraph kappale = (Paragraph) this.kappaleLista.get(kappaleNumero);
        Collection tyyliKursorinKohdalla = (Collection) kappale.getStyleAtPosition(kursoriKappaleessa);
        if (tyyliKursorinKohdalla.contains("alleviivattu") && !kappale.getText().isEmpty()) {
            IndexRange sananIndeksit = kappale.getStyleRangeAtPosition(kursoriKappaleessa);
            String korvattavaSana = kappale.substring(sananIndeksit.getStart(), sananIndeksit.getEnd());
            String[] korjausEhdotukset = this.virheenKorjaaja.ehdotaKorjauksia(korvattavaSana);
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
        String korvattava = this.kirjoitusAlue.getText(kappaleNumero, korjattavanIndeksitKappaleessa.getStart(), kappaleNumero, korjattavanIndeksitKappaleessa.getEnd());
        if (Character.isUpperCase(korvattava.charAt(0))) {
            korvaava = Character.toUpperCase(korvaava.charAt(0)) + korvaava.substring(1);
        }
        this.kirjoitusAlue.replace(kappaleNumero, korjattavanIndeksitKappaleessa.getStart(), kappaleNumero, korjattavanIndeksitKappaleessa.getEnd(), korvaava, Collections.singleton("sanastonsana"));
        this.kirjoitusAlue.requestFocus();
    }

    private void kasittelePainallukset() {
        if (this.nykyinenPonnahdus != null && this.nykyinenPonnahdus.isShowing() && this.painetutNappaimet.contains(KeyCode.ESCAPE)) {
            this.kirjoitusAlue.requestFocus();
        }
        if (this.painetutNappaimet.contains(KeyCode.CONTROL)) {
            if (this.painetutNappaimet.contains(KeyCode.ENTER)) {
                kasitteleKlikkaus();
            } else if (this.painetutNappaimet.contains(KeyCode.S)) {
                if (this.nykyinenTiedosto != null) {
                    if (this.muutoksiaTekstissa) {
                        tallennaValittuunTiedostoon();
                    }
                } else {
                    File tiedosto = valitseTallennettavaTiedosto();
                    if (tiedosto != null) {
                        this.nykyinenTiedosto = tiedosto;
                        tallennaValittuunTiedostoon();
                    }
                }
            } else if (this.painetutNappaimet.contains(KeyCode.L)) {
                lisaaSanaSanastoon();
            } else if (this.painetutNappaimet.contains(KeyCode.P)) {
                poistaSanaSanastosta();
            }
        }
    }

    private void lisaaSanaSanastoon() {
        int kappaleNumero = kirjoitusAlue.getCurrentParagraph();
        int kursoriKappaleessa = kirjoitusAlue.getCaretColumn();
        Paragraph kappale = (Paragraph) this.kappaleLista.get(kappaleNumero);
        Collection tyyliKursorinKohdalla = (Collection) kappale.getStyleAtPosition(kursoriKappaleessa);
        if (tyyliKursorinKohdalla.contains("alleviivattu") && !kappale.getText().isEmpty()) {
            IndexRange sananIndeksit = kappale.getStyleRangeAtPosition(kursoriKappaleessa);
            String lisattavaSana = kappale.substring(sananIndeksit.getStart(), sananIndeksit.getEnd());
            this.virheenKorjaaja.lisaaSanastoonSana(lisattavaSana);
            kirjoitusAlue.setStyle(kappaleNumero, sananIndeksit.getStart(), sananIndeksit.getEnd(), Collections.singleton("sanastonsana"));
        }
    }

    private void poistaSanaSanastosta() {
        int kappaleNumero = kirjoitusAlue.getCurrentParagraph();
        int kursoriKappaleessa = kirjoitusAlue.getCaretColumn();
        Paragraph kappale = (Paragraph) this.kappaleLista.get(kappaleNumero);
        Collection tyyliKursorinKohdalla = (Collection) kappale.getStyleAtPosition(kursoriKappaleessa);
        if (tyyliKursorinKohdalla.contains("sanastonsana") && !kappale.getText().isEmpty()) {
            IndexRange sananIndeksit = kappale.getStyleRangeAtPosition(kursoriKappaleessa);
            String poistettavaSana = kappale.substring(sananIndeksit.getStart(), sananIndeksit.getEnd());
            this.virheenKorjaaja.poistaSanastostaSana(poistettavaSana);
            kirjoitusAlue.setStyle(kappaleNumero, sananIndeksit.getStart(), sananIndeksit.getEnd(), Collections.singleton("alleviivattu"));
        }
    }

    private void alustaKorjaajaUudelleen(Korjaaja valittuKorjaaja, EtaisyysFunktio valittuEtaisyysFunktio, int valittuToleranssi, int valittuMontaHaetaan, Sanasto valittuSanasto) {
        Alert alustusKaynnissaIlmoitus = luoProsessiKeskenIlmoitus("Korjaajan alustus", "Korjaajaa alustetaan uudelleen!", "");

        Task testiTehtava = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                virheenKorjaaja.tallennaSanastoMuutokset();
                virheenKorjaaja = KorjaajaTehdas.luoKorjaaja(tiedostonKasittelija, valittuKorjaaja, valittuEtaisyysFunktio, valittuToleranssi, valittuMontaHaetaan, valittuSanasto);
                return null;
            }
        };
        testiTehtava.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                alustusKaynnissaIlmoitus.close();
            }

        });
        alustusKaynnissaIlmoitus.show();
        new Thread(testiTehtava).start();
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
