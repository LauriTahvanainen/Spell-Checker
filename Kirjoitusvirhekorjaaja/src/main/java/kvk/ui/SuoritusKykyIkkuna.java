package kvk.ui;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import kvk.enums.EtaisyysFunktio;
import kvk.enums.Korjaaja;
import kvk.enums.Sanasto;
import kvk.io.ITiedostonKasittelija;
import kvk.korjaaja.IKorjaaja;
import kvk.korjaaja.KorjaajaTehdas;
import kvk.suorituskykytestit.SuorituskykyTestaaja;
import static kvk.utils.UiUtils.*;

/**
 * Luokka suorituskykyikkunalle. Sisältää eri testien ui:n
 */
public class SuoritusKykyIkkuna {

    private Scene tausta;
    private int windowWidth;
    private int windowHeight;
    private Stage ikkuna;
    private VBox testiValikko;
    private BorderPane korjaajaSuoritusTestiTausta;
    private BorderPane alustusTausta;
    private BorderPane etaisyyslaskijaTausta;
    private final ITiedostonKasittelija IO;

    SuoritusKykyIkkuna(Stage ikkuna, ITiedostonKasittelija tiedostonKasittelija, int windowWidth, int windowHeight) {
        this.IO = tiedostonKasittelija;
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        alustaTestiNakymat();
        alustaValikko();
        this.tausta = new Scene(this.testiValikko, windowWidth, windowHeight + 200);
        this.tausta.getStylesheets().add(getClass().getResource("/tekstityylit.css").toExternalForm());
        this.ikkuna = new Stage();
        this.ikkuna.setTitle("Suorituskykytestit");
        this.ikkuna.setScene(this.tausta);
        this.ikkuna.initModality(Modality.APPLICATION_MODAL);
        this.ikkuna.initOwner(ikkuna);
        this.ikkuna.setX(this.ikkuna.getX() + 50);
        this.ikkuna.setY(this.ikkuna.getY() + 50);
    }

    void nayta() {
        this.ikkuna.show();
    }

    private void alustaTestiNakymat() {
        alustaAikaJaProsenttiTestit();
        alustaAikaTesti();
        alustaEtaisyysFunktioTesti();
    }

    private void alustaValikko() {
        this.testiValikko = new VBox();
        this.testiValikko.getStyleClass().add("testivalikko");
        this.testiValikko.setAlignment(Pos.CENTER);

        Button onnistumisProsenttiTestit = new Button("Testaa korjaajien korjauskykyä ja aikavaativuutta");
        Button sanastoAlustusTestit = new Button("Testaa korjaajien alustusaikaa");
        Button etaisyysLaskijoidenTestit = new Button("Testaa etäisyyslaskijoiden suorituskykyä");
        onnistumisProsenttiTestit.getStyleClass().add("testivalikkonappi");
        sanastoAlustusTestit.getStyleClass().add("testivalikkonappi");
        etaisyysLaskijoidenTestit.getStyleClass().add("testivalikkonappi");

        onnistumisProsenttiTestit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tausta.setRoot(korjaajaSuoritusTestiTausta);
            }

        });

        sanastoAlustusTestit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tausta.setRoot(alustusTausta);
            }

        });

        etaisyysLaskijoidenTestit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tausta.setRoot(etaisyyslaskijaTausta);
            }

        });

        this.testiValikko.getChildren().addAll(onnistumisProsenttiTestit, sanastoAlustusTestit, etaisyysLaskijoidenTestit);
        for (Node node : this.testiValikko.getChildren()) {
            VBox.setVgrow(node, Priority.ALWAYS);
        }
    }

    private void alustaAikaJaProsenttiTestit() {
        // layouts
        this.korjaajaSuoritusTestiTausta = new BorderPane();
        GridPane toimintaNappiTausta = luoToimintaNappiTausta();
        StackPane kaavioNappiTausta = new StackPane();

        //Valitsimet
        KorjaajaValitsin korjaajaValitsin = new KorjaajaValitsin(this.windowWidth, SelectionMode.MULTIPLE);

        Label valitseOtosKokoTeksti = new Label("Yksittäisen testin otoskoko");
        Slider valitseOtosKoko = luoSlideri(10, 1000, 550, 10, 300, 10);

        Label valitseMontaIteraatioitaTeksti = new Label("Testi-iteraatioiden määrä");
        Slider valitseMontaIteraatiota = luoSlideri(1, 10, 200, 1, 5, 1);

        Label valitseMaksVirheitaSanassaTeksti = new Label("Maks. määrä virheitä sanassa");
        Slider valitseMaksVirheitaSanassa = luoSlideri(1, 5, 200, 1, 2, 1);

        korjaajaValitsin.asetaLapsi(valitseMaksVirheitaSanassaTeksti, 1, 3);
        korjaajaValitsin.asetaLapsi(valitseMaksVirheitaSanassa, 1, 4);
        korjaajaValitsin.asetaLapsi(valitseMontaIteraatioitaTeksti, 2, 3);
        korjaajaValitsin.asetaLapsi(valitseMontaIteraatiota, 2, 4);
        korjaajaValitsin.asetaLapsi(valitseOtosKokoTeksti, 3, 3);
        korjaajaValitsin.asetaLapsi(valitseOtosKoko, 3, 4);

        // kaavioit
        LineChart<Number, Number> korjausProsenttiViivaKaavio = luoViivaKaavio("% otoksen sanoista korjattu oikein", "Sanaston koko", 1000, 4000000, 50000);
        LineChart<Number, Number> korjausAikaViivaKaavio = luoViivaKaavio("Korjausaika (ms)", "Sanaston koko", 1000, 4000000, 50000);

        //napit
        Button palaaTestiValikkoon = luoPalaaValikkoonNappi();
        Button vaihdaTestia = new Button("Vaihda korjausaikatestiin");
        vaihdaTestia.setAlignment(Pos.CENTER);
        vaihdaTestia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (vaihdaTestia.getText().equals("Vaihda korjausaikatestiin")) {
                    vaihdaTestia.setText("Vaihda korjauskykytestiin");
                    kaavioNappiTausta.getChildren().clear();
                    kaavioNappiTausta.getChildren().addAll(korjausAikaViivaKaavio, toimintaNappiTausta);
                } else {
                    vaihdaTestia.setText("Vaihda korjausaikatestiin");
                    kaavioNappiTausta.getChildren().clear();
                    kaavioNappiTausta.getChildren().addAll(korjausProsenttiViivaKaavio, toimintaNappiTausta);
                }
            }

        });

        Button suoritaTesti = new Button("Suorita testi");
        suoritaTesti.setAlignment(Pos.CENTER);
        suoritaTesti.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Korjaaja korjaaja = korjaajaValitsin.korjaaja();
                EtaisyysFunktio etaisyysFunktio = korjaajaValitsin.etaisyysFunktio();
                int toleranssi = korjaajaValitsin.toleranssi();
                int montaHaetaan = korjaajaValitsin.montaHaetaan();
                int montaIteraatiota = (int) valitseMontaIteraatiota.getValue();
                int otosKoko = (int) valitseOtosKoko.getValue();
                int maksVirheitaSanasssa = (int) valitseMaksVirheitaSanassa.getValue();
                ObservableList<Sanasto> sanastoValinnat = korjaajaValitsin.sanasto();
                if (sanastoValinnat.isEmpty()) {
                    Alert ilmoitus = new Alert(Alert.AlertType.ERROR);
                    ilmoitus.setTitle("Virhe");
                    ilmoitus.setContentText("Yhtään sanaston kokoa ei valittu!\nTestiä ei voida suorittaa!");
                    ilmoitus.setHeaderText("Virhe!");
                    ilmoitus.showAndWait();
                    return;
                }

                boolean prosenttiTesti;
                if (vaihdaTestia.getText().equals("Vaihda korjausaikatestiin")) {
                    prosenttiTesti = true;
                } else {
                    prosenttiTesti = false;
                }

                XYChart.Series arvoSarja = new XYChart.Series();
                arvoSarja.setName("(" + korjaaja.toString() + ":" + etaisyysFunktio.toString() + ":" + toleranssi + ":" + montaHaetaan + ")" + "Iter: " + montaIteraatiota + ", Otoskoko: " + otosKoko);

                Alert testiKaynnissaIlmoitus = luoProsessiKeskenIlmoitus("Testi", "Testaus käynnissä!", "Tässä voi kestää hetki!");
                Task testiTehtava = new Task<ArrayList<Pair>>() {

                    @Override
                    protected ArrayList<Pair> call() throws Exception {
                        ArrayList<Pair> tulokset = new ArrayList<>();
                        try {
                            IKorjaaja testattava;
                            for (Sanasto sanasto : sanastoValinnat) {
                                testattava = KorjaajaTehdas.luoKorjaaja(IO, korjaaja, etaisyysFunktio, toleranssi, montaIteraatiota, sanasto);
                                if (prosenttiTesti) {
                                    tulokset.add(new Pair(testattava.sanastonKoko(), SuorituskykyTestaaja.korjausOnnistumisProsenttiTesti(testattava, maksVirheitaSanasssa, montaIteraatiota, otosKoko)));
                                } else {
                                    tulokset.add(new Pair(testattava.sanastonKoko(), SuorituskykyTestaaja.keskimaarainenKorjausAikaTesti(testattava, maksVirheitaSanasssa, montaIteraatiota, otosKoko)));
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        return tulokset;
                    }
                };
                testiTehtava.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        ArrayList<Pair> tulokset = (ArrayList<Pair>) testiTehtava.getValue();
                        for (Pair tulos : tulokset) {
                            arvoSarja.getData().add(new XYChart.Data((int) tulos.getKey(), (double) tulos.getValue()));
                        }
                        if (prosenttiTesti) {
                            korjausProsenttiViivaKaavio.getData().add(arvoSarja);
                        } else {
                            korjausAikaViivaKaavio.getData().add(arvoSarja);
                        }
                        testiKaynnissaIlmoitus.close();
                    }

                });
                testiKaynnissaIlmoitus.show();
                new Thread(testiTehtava).start();
            }

        });

        Button tyhjennaKaavio = new Button("Tyhjenna kaavio");
        tyhjennaKaavio.setAlignment(Pos.CENTER);
        tyhjennaKaavio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (vaihdaTestia.getText().equals("Vaihda korjausaikatestiin")) {
                    korjausProsenttiViivaKaavio.getData().clear();
                } else {
                    korjausAikaViivaKaavio.getData().clear();
                }
            }

        });

        // Kasaaminen
        this.korjaajaSuoritusTestiTausta.setTop(korjaajaValitsin.haeJuuri());

        toimintaNappiTausta.addColumn(0, suoritaTesti, vaihdaTestia, tyhjennaKaavio, palaaTestiValikkoon);
        kaavioNappiTausta.setAlignment(Pos.CENTER);
        kaavioNappiTausta.getChildren().addAll(korjausProsenttiViivaKaavio, toimintaNappiTausta);
        this.korjaajaSuoritusTestiTausta.setCenter(kaavioNappiTausta);
    }

    private void alustaEtaisyysFunktioTesti() {
        // layouts
        this.etaisyyslaskijaTausta = new BorderPane();

        GridPane etaisyysFunktioValintaTausta = new GridPane();
        etaisyysFunktioValintaTausta.setAlignment(Pos.CENTER);
        etaisyysFunktioValintaTausta.setVgap(10);
        etaisyysFunktioValintaTausta.setHgap(10);
        etaisyysFunktioValintaTausta.setPadding(new Insets(10, 10, 10, 10));

        StackPane kaavioToimintaNapitTausta = new StackPane();
        kaavioToimintaNapitTausta.setAlignment(Pos.CENTER);

        GridPane toimintaNappiTausta = luoToimintaNappiTausta();

        // valitsimet
        Label valitseEtaisyysFunktioTeksti = new Label("Etäisyysfunktio");
        ChoiceBox<EtaisyysFunktio> valitseEtaisyysFunktio = luoEtaisyysFunktionValintaLaatikko();

        Label valitseMaksSanojenPituusTeksti = new Label("Maksimi sanan pituus testissä");
        Slider valitseMaksSanojenPituus = luoSlideri(100, 20000, 600, 50, 300, 10);

        // kaavio
        LineChart<Number, Number> viivaKaavio = luoViivaKaavio("Suoritusaika (ms)", "Merkkijonojen pituus", 0, 20000, 500);

        // ToimintaNapit
        Button palaaValikkoon = luoPalaaValikkoonNappi();
        Button tyhjennaKaavio = luoTyhjennaKaavioNappi(viivaKaavio);
        Button suoritaTesti = new Button("Suorita testi");
        suoritaTesti.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EtaisyysFunktio etaisyysFunktio = valitseEtaisyysFunktio.getValue();
                int maksSanojenPituus = (int) valitseMaksSanojenPituus.getValue();
                if (etaisyysFunktio == null) {
                    Alert ilmoitus = new Alert(Alert.AlertType.ERROR);
                    ilmoitus.setTitle("Virhe");
                    ilmoitus.setContentText("Etäisyysfunktiota ei valittu!\nTestiä ei voida suorittaa!");
                    ilmoitus.setHeaderText("Virhe!");
                    ilmoitus.showAndWait();
                    return;
                }

                XYChart.Series arvoSarja = new XYChart.Series();
                arvoSarja.setName(etaisyysFunktio.toString());

                Alert testiKaynnissaIlmoitus = luoProsessiKeskenIlmoitus("Testi", "Testaus käynnissä!", "Tässä voi kestää hetki!");

                Task testiTehtava = new Task<ArrayList<Pair>>() {

                    @Override
                    protected ArrayList<Pair> call() throws Exception {
                        try {
                            return SuorituskykyTestaaja.etaisyysLaskijanSuoritusAika(etaisyysFunktio, maksSanojenPituus);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        return null;
                    }
                };
                testiTehtava.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        ArrayList<Pair> tulokset = (ArrayList<Pair>) testiTehtava.getValue();
                        for (Pair tulos : tulokset) {
                            arvoSarja.getData().add(new XYChart.Data((int) tulos.getKey(), (double) tulos.getValue()));
                        }
                        viivaKaavio.getData().add(arvoSarja);
                        testiKaynnissaIlmoitus.close();
                    }

                });
                testiKaynnissaIlmoitus.show();
                new Thread(testiTehtava).start();
            }

        });

        //Kasaaminen 
        etaisyysFunktioValintaTausta.addRow(0, valitseEtaisyysFunktioTeksti, valitseMaksSanojenPituusTeksti);
        etaisyysFunktioValintaTausta.addRow(1, valitseEtaisyysFunktio, valitseMaksSanojenPituus);
        this.etaisyyslaskijaTausta.setTop(etaisyysFunktioValintaTausta);

        toimintaNappiTausta.addColumn(0, suoritaTesti, tyhjennaKaavio, palaaValikkoon);
        kaavioToimintaNapitTausta.getChildren().addAll(viivaKaavio, toimintaNappiTausta);
        this.etaisyyslaskijaTausta.setCenter(kaavioToimintaNapitTausta);
    }

    private void alustaAikaTesti() {
        // layouts
        this.alustusTausta = new BorderPane();

        VBox sanastoValitsinTausta = new VBox();
        sanastoValitsinTausta.setStyle("-fx-background-color: bisque");
        sanastoValitsinTausta.setAlignment(Pos.CENTER);
        sanastoValitsinTausta.setSpacing(20);
        sanastoValitsinTausta.setPrefWidth(this.windowWidth);
        sanastoValitsinTausta.setPadding(new Insets(10, 10, 10, 10));

        StackPane kaavioToimintaNapitTausta = new StackPane();
        kaavioToimintaNapitTausta.setAlignment(Pos.CENTER);

        GridPane toimintaNappiTausta = luoToimintaNappiTausta();

        // valitsimet
        Label valitseSanastonKootTeksti = new Label("Sanaston koot");
        ObservableList<Sanasto> sanastoLista = FXCollections.observableArrayList();
        sanastoLista.addAll(Sanasto.values());
        ListView<Sanasto> valitseSanastonKoot = luoSanastoValitsin(sanastoLista, SelectionMode.MULTIPLE);

        // kaavio
        LineChart<Number, Number> viivaKaavio = luoViivaKaavio("Alustusaika (s)", "Sanaston koko", 1000, 4000000, 50000);

        // ToimintaNapit
        Button palaaValikkoon = luoPalaaValikkoonNappi();
        Button tyhjennaKaavio = luoTyhjennaKaavioNappi(viivaKaavio);
        Button suoritaTesti = new Button("Suorita testi");
        suoritaTesti.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<Sanasto> sanastoValinnat = valitseSanastonKoot.getSelectionModel().getSelectedItems();
                if (sanastoValinnat.isEmpty()) {
                    Alert ilmoitus = new Alert(Alert.AlertType.ERROR);
                    ilmoitus.setTitle("Virhe");
                    ilmoitus.setContentText("Yhtään sanaston kokoa ei valittu!\nTestiä ei voida suorittaa!");
                    ilmoitus.setHeaderText("Virhe!");
                    ilmoitus.showAndWait();
                    return;
                }

                XYChart.Series arvoSarja = new XYChart.Series();
                arvoSarja.setName("(korjaaja:levenshtein:2:10)" + "Iter:5, Sanastokoot: " + Arrays.toString(sanastoValinnat.toArray()));

                Alert testiKaynnissaIlmoitus = luoProsessiKeskenIlmoitus("Testi", "Testaus käynnissä!", "Tässä voi kestää hetki!");

                Task testiTehtava = new Task<ArrayList<Pair>>() {

                    @Override
                    protected ArrayList<Pair> call() throws Exception {
                        ArrayList<Pair> tulokset = new ArrayList<>();
                        try {
                            for (Sanasto sanasto : sanastoValinnat) {
                                tulokset.add(SuorituskykyTestaaja.sanastonLatausAikaTesti(sanasto));
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        return tulokset;
                    }
                };
                testiTehtava.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        ArrayList<Pair> tulokset = (ArrayList<Pair>) testiTehtava.getValue();
                        for (Pair tulos : tulokset) {
                            arvoSarja.getData().add(new XYChart.Data((int) tulos.getKey(), (double) tulos.getValue()));
                        }
                        viivaKaavio.getData().add(arvoSarja);
                        testiKaynnissaIlmoitus.close();
                    }

                });
                testiKaynnissaIlmoitus.show();
                new Thread(testiTehtava).start();
            }

        });

        //Kasaaminen 
        sanastoValitsinTausta.getChildren().addAll(valitseSanastonKootTeksti, valitseSanastonKoot);
        this.alustusTausta.setTop(sanastoValitsinTausta);

        toimintaNappiTausta.addColumn(0, suoritaTesti, tyhjennaKaavio, palaaValikkoon);
        kaavioToimintaNapitTausta.getChildren().addAll(viivaKaavio, toimintaNappiTausta);
        this.alustusTausta.setCenter(kaavioToimintaNapitTausta);

    }

    private static GridPane luoToimintaNappiTausta() {
        GridPane tausta = new GridPane();
        tausta.setAlignment(Pos.TOP_RIGHT);
        tausta.setVgap(10);
        tausta.setPadding(new Insets(10, 10, 0, 0));
        return tausta;
    }

    private Button luoPalaaValikkoonNappi() {
        Button nappi = new Button("Palaa testivalikkoon");
        nappi.setAlignment(Pos.CENTER);
        nappi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tausta.setRoot(testiValikko);
            }

        });
        return nappi;
    }

    private static Button luoTyhjennaKaavioNappi(LineChart<Number, Number> kaavio) {
        Button nappi = new Button("Tyhjenna kaavio");
        nappi.setAlignment(Pos.CENTER);
        nappi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                kaavio.getData().clear();
            }

        });
        return nappi;
    }

    private static LineChart<Number, Number> luoViivaKaavio(String yAkseliNimi, String xAkseliNimi, int xAkseliMin, int xAkseliMax, int xAkseliVali) {
        NumberAxis yAkseli = new NumberAxis(yAkseliNimi, 0, 100, 1);
        NumberAxis xAkseli = new NumberAxis(xAkseliNimi, xAkseliMin, xAkseliMax, xAkseliVali);
        yAkseli.setAutoRanging(true);
        xAkseli.setAutoRanging(true);
        xAkseli.setMinorTickCount(0);
        LineChart<Number, Number> kaavio = new LineChart<Number, Number>(xAkseli, yAkseli);

        return kaavio;
    }

}
