package kvk.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
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

    private final Scene tausta;
    private final int windowWidth;
    private final int windowHeight;
    private final Stage ikkuna;
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
        alustaAikaJaTilavaativuusTesti();
        alustaEtaisyysFunktioTesti();
    }

    private void alustaValikko() {
        this.testiValikko = new VBox();
        this.testiValikko.getStyleClass().add("testivalikko");
        this.testiValikko.setAlignment(Pos.CENTER);

        Button onnistumisProsenttiTestit = new Button("Testaa korjaajien korjauskykyä ja aikavaativuutta");
        Button sanastoAlustusTestit = new Button("Testaa korjaajien alustusaikaa ja tilavaativuutta");
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
                    naytaVirheIlmoitus("Yhtään sanaston kokoa ei valittu!\nTestiä ei voida suorittaa!");
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

                Alert testiKaynnissaIlmoitus = luoProsessiKeskenIlmoitus("Testi", "Testaus käynnissä!", "Tässä voi kestää hetki!", true);
                Task testiTehtava = new Task<ArrayList<Pair>>() {

                    @Override
                    protected ArrayList<Pair> call() throws Exception {
                        ArrayList<Pair> tulokset = new ArrayList<>();
                        IKorjaaja testattava;
                        for (Sanasto sanasto : sanastoValinnat) {
                            if (this.isCancelled()) {
                                return tulokset;
                            }
                            testattava = KorjaajaTehdas.luoKorjaaja(IO, korjaaja, etaisyysFunktio, toleranssi, montaIteraatiota, sanasto);
                            if (prosenttiTesti) {
                                tulokset.add(new Pair(testattava.sanastonKoko(), SuorituskykyTestaaja.korjausOnnistumisProsenttiTesti(testattava, maksVirheitaSanasssa, montaIteraatiota, otosKoko, this)));
                            } else {
                                tulokset.add(new Pair(testattava.sanastonKoko(), SuorituskykyTestaaja.keskimaarainenKorjausAikaTesti(testattava, maksVirheitaSanasssa, montaIteraatiota, otosKoko, this)));
                            }
                        }
                        return tulokset;
                    }
                };

                testiTehtava.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        testiKaynnissaIlmoitus.close();
                        naytaVirheIlmoitus(testiTehtava.getException().getMessage());
                    }

                });
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
                new Thread(testiTehtava).start();
                Optional<ButtonType> tulos = testiKaynnissaIlmoitus.showAndWait();
                if (tulos.isPresent() && tulos.get() == ButtonType.CANCEL) {
                    testiTehtava.cancel();
                }
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
                    naytaVirheIlmoitus("Etäisyysfunktiota ei valittu!\nTestiä ei voida suorittaa!");
                    return;
                }

                XYChart.Series arvoSarja = new XYChart.Series();
                arvoSarja.setName(etaisyysFunktio.toString());

                Alert testiKaynnissaIlmoitus = luoProsessiKeskenIlmoitus("Testi", "Testaus käynnissä!", "Tässä voi kestää hetki!", true);

                Task testiTehtava = new Task<ArrayList<Pair>>() {

                    @Override
                    protected ArrayList<Pair> call() throws Exception {
                        return SuorituskykyTestaaja.etaisyysLaskijanSuoritusAika(etaisyysFunktio, maksSanojenPituus, this);
                    }
                };
                testiTehtava.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        testiKaynnissaIlmoitus.close();
                        naytaVirheIlmoitus("Tapahtui odottamaton virhe!\nKokeile uudestaan.");
                    }

                });

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
                new Thread(testiTehtava).start();
                Optional<ButtonType> tulos = testiKaynnissaIlmoitus.showAndWait();
                if (tulos.isPresent() && tulos.get() == ButtonType.CANCEL) {
                    testiTehtava.cancel();
                }
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

    private void alustaAikaJaTilavaativuusTesti() {
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
        LineChart<Number, Number> aikaViivaKaavio = luoViivaKaavio("Alustusaika (s)", "Sanaston koko", 1000, 4000000, 50000);
        LineChart<Number, Number> tilaViivaKaavio = luoViivaKaavio("Käytetty muisti (mb)", "Sanaston koko", 1000, 4000000, 50000);

        // ToimintaNapit
        Button palaaValikkoon = luoPalaaValikkoonNappi();
        Button tyhjennaKaavio = new Button("Tyhjenna kaaviot");
        tyhjennaKaavio.setAlignment(Pos.CENTER);
        tyhjennaKaavio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aikaViivaKaavio.getData().clear();
                tilaViivaKaavio.getData().clear();
            }

        });
        Button suoritaTesti = new Button("Suorita testi");
        Button naytaTilavaativuudet = new Button("Näytä tilavaativuudet");

        suoritaTesti.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<Sanasto> sanastoValinnat = valitseSanastonKoot.getSelectionModel().getSelectedItems();
                if (sanastoValinnat.isEmpty()) {
                    naytaVirheIlmoitus("Yhtään sanaston kokoa ei valittu!\nTestiä ei voida suorittaa!");
                    return;
                }

                XYChart.Series aikaArvoSarja = new XYChart.Series();
                aikaArvoSarja.setName("(korjaaja:levenshtein:2:10)" + "Iter:5, Sanastokoot: " + Arrays.toString(sanastoValinnat.toArray()));
                XYChart.Series tilaArvoSarja = new XYChart.Series();
                tilaArvoSarja.setName("(korjaaja:levenshtein:2:10)" + "Iter:5, Sanastokoot: " + Arrays.toString(sanastoValinnat.toArray()));

                int mb = 1024 * 1024;

                Alert testiKaynnissaIlmoitus = luoProsessiKeskenIlmoitus("Testi", "Testaus käynnissä!", "Tässä voi kestää hetki!", false);

                Task testiTehtava = new Task<ArrayList<Pair<Integer, Pair<Double, Long>>>>() {

                    @Override
                    protected ArrayList<Pair<Integer, Pair<Double, Long>>> call() throws Exception {
                        ArrayList<Pair<Integer, Pair<Double, Long>>> tulokset = new ArrayList<>();
                        for (Sanasto sanasto : sanastoValinnat) {
                            if (this.isCancelled()) {
                                return tulokset;
                            }
                            Runtime instanssi = Runtime.getRuntime();
                            long joKaytettyMuisti = (instanssi.totalMemory() - instanssi.freeMemory()) / mb;
                            tulokset.add(SuorituskykyTestaaja.sanastonLatausAikaTesti(sanasto, joKaytettyMuisti));
                        }

                        return tulokset;
                    }
                };
                testiTehtava.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        testiKaynnissaIlmoitus.close();
                        naytaVirheIlmoitus(testiTehtava.getException().getMessage());
                    }

                });
                testiTehtava.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        ArrayList<Pair<Integer, Pair<Double, Long>>> tulokset = (ArrayList<Pair<Integer, Pair<Double, Long>>>) testiTehtava.getValue();
                        tulokset.forEach((tulos) -> {
                            aikaArvoSarja.getData().add(new XYChart.Data(tulos.getKey(), tulos.getValue().getKey()));
                            tilaArvoSarja.getData().add(new XYChart.Data(tulos.getKey(), tulos.getValue().getValue()));
                        });
                        aikaViivaKaavio.getData().add(aikaArvoSarja);
                        tilaViivaKaavio.getData().add(tilaArvoSarja);
                        testiKaynnissaIlmoitus.close();
                    }

                });
                new Thread(testiTehtava).start();
                testiKaynnissaIlmoitus.show();
            }

        });

        naytaTilavaativuudet.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                kaavioToimintaNapitTausta.getChildren().clear();
                if (naytaTilavaativuudet.getText().equals("Näytä tilavaativuudet")) {
                    kaavioToimintaNapitTausta.getChildren().addAll(tilaViivaKaavio, toimintaNappiTausta);
                    naytaTilavaativuudet.setText("Näytä aikavaativuudet");
                } else {
                    kaavioToimintaNapitTausta.getChildren().addAll(aikaViivaKaavio, toimintaNappiTausta);
                    naytaTilavaativuudet.setText("Näytä tilavaativuudet");
                }
            }

        });

        //Kasaaminen 
        sanastoValitsinTausta.getChildren().addAll(valitseSanastonKootTeksti, valitseSanastonKoot);
        this.alustusTausta.setTop(sanastoValitsinTausta);

        toimintaNappiTausta.addColumn(0, suoritaTesti, naytaTilavaativuudet, tyhjennaKaavio, palaaValikkoon);
        kaavioToimintaNapitTausta.getChildren().addAll(aikaViivaKaavio, toimintaNappiTausta);
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
