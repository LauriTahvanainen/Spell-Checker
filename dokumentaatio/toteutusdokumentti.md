# Ohjelman yleisrakenne
## Käyttöliittymä
Käyttöliittymänä toimii yksinkertainen javafx:llä ja richTextfx-kirjastolla toteutettu tekstieditori. Itse korjaajan toiminnasta on tärkeää se käyttöliittymän ratkaisu, että jos kirjoitetussa sanassa on muita kuin suomenkielen aakkosia, niin sitä ei tarkisteta, ja se näyttää oikeelliselta. Sanaston latauksen, sekä sanan poisto ja lisäyksen yhteydessä ei tuetuista kirjaimista kuitenkin varoitetaan. Siispä ohjelma toimii oikein vain suomen kielen aakkostolla.

## Toiminnallisuus
Ohjelman arkkitehtuuri on suunnitelu niin, että uusia virheenkorjaajan malleja on helppo lisätä, kuten myös uusia sanastoja, sekä Trie-BK-korjaajamallin käyttämiä etäisyysfunktioita.

Kuitenkin kurssin puitteissa kerettiin toteuttaamaan vain yksi korjaajan malli, joka käyttää Trie ja BK-puu tietorakenteita. Jatkossa käsitellään tätä yhtä mallia.

Tällä Trie-BK-puu korjausmallilla on muutama oleellinen komponentti, joita käyttäjä voi muuttaa. Nämä ovat: sanasto, etäisyysfunktio, toleranssi, sekä monta korjausehdotusta haetaan kerralla. Näitä muuttamalla myös korjaajan tila, sekä korjaukseen keskimäärin käytetty aika muuttuvat. Komponenteista tarkemmin myöhemmin.

Ohjelman käynnistyessä ja aina alustettaessa uusi korjaaja kielen sanasto ladataan tekstitiedostosta kahteen tietorakenteeseen, Trie-puuhun, sekä BK-puuhun. Trie puu vastaa virheellisten sanojen tunnistamisesta vakio-ajassa, ja BK-puu vastaa korjausehdotusten tekemisestä virheellisille sanoille. Tarkemmin edellä. Käyttäjä voi itse ohjelman ollessa käynnissä myös lisätä ja poistaa sanaston sanoja, näin vaikuttaen virheellisten sanojen tunnistamiseen ja korjausehdotuksiin. HUOM poistettuja sanoja ei tallenneta tällä hetkellä, siis poistetut sanat palautuvat kun korjaaja ladataan sanastosta uudellen.

Yksittäisen sanan tarkistaminen, ja korjaaminen etenee seuraavalla tavalla:
- Trie-puun avulla testataan ensin kuuluuko sana sanastoon. Jos kuuluu, lopetetaan, jos ei kuulu, niin sana alleviivataa. Korjausehdotusten haku tehdään ykisttäisestä sanasta vasta käyttäjän sitä pyytäessä. Tämä tapahtuu tekemällä BK-puusta haku määritellyllä toleranssilla. Näin haetaan korjaajan asetuksissa määritellyn monta virheellistä sanaa lähimpänä olevaa sanaa.
- Lähimmällä etäisyydellä, ja määritellyllä toleranssilla, tarkoitetaan tässä virheellisen sanan, ja sanaston sanan välistä muokkausetäisyyttä. Esim sanojen "kissa" ja "kisso" muokkausetäisyys on 1, sillä kisso saadaan kissasta vaihtamalla viimeinen kirjain a kirjaimeen o.
  - BK-puu on puu, joka on rakennettu sanaston sanojen välisten etäisyyksien perusteella. Toteutuksessa muokkausetäisyys lasketaan oletuksena Levenshtein-etäisyytenä, mutta ohjelmaan voisi helposti lisätä myös muita etäisyysfunktioita. BK-puun käyttämä etäisyysfunktio on vaihdettavissa mihin tahansa metriseen etäisyysfunktioon.
  - Riippuen valitusta toleranssista, on mahdollista, että tietylle virheelliselle sanalle ei löydetä lainkaan korjausehdotuksia. Esim "kissa" ja "virtahepo", ovat selvästi yli 2 muokkausoperaation etäisyydellä toisistaan.
  - Toisaalta, mitä suurempi toleranssi valitaan, sitä enemmän vertailuja, eli etäisyyden laskemisia, tehdään haettaessa läheisiä sanoja BK-puusta. Siis suoritusaika kasaa, ja itseasiassa varsin nopeasti.
  

  
- Kun käyttäjä haluaa korjausehdotuksia alleviivatulle sanalle näytetään käyttäjälle korjausehdotusten lista, niin että "Paras", eli lähin, korjaus-ehdotus on ensimmäisenä.

Korjaajan asetuksia voi muuttaa käyttöliittymästä. Jos sanasto, etäisyysfunktio tai itse korjaaja muuttuu, alustetaan korjaaja uudellee.

Ohjelmaan on lisätty myös tekstinkäsittelyä helpottava ominaisuus tallentaa käsitelty teksti tiedostoon, sekä avata teksti tiedostosta.

Suorituskykytestit on toteutettu niin, että niitä voidaan suorittaa vaihtamalla korjaajan parametreja, ja sittenverrata näitä saatuja tuloksia yhteen. Kaikki testitulokset esitetään graafisesti viivakaaviossa. Näistä testeistä tarkemmin [testausdokumentissa](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/testausdokumentti.md). Testituloksia kuitenkin käytetään myös edellä olevassa tietorakenteiden ja algoritmien tarkemmassa tarkastelussa.

# Algoritmit
## Levenshtein-etäisyyden laskeminen
Kahden sanan välisen muokkausetäisyyden laskeminen rajattomalla aakkostolla on NP-täydellinen ongelma. Kuitenkin rajallisella aakkostolla, ja käyttämällä esim. dynaamista ohjelmointia, saadaan erittäin käyttökelpoisia algoritmeja kyseiseen ongelmaan.
Levenshtein-etäisyys lasketaan dynaamiseen ohjelmointiin perustuvalla algoritmilla.
```
int laskeEtaisyys(mjonoX, mjonoY) {
        etaisyysMatriisi = uusi int[mjonoX.length() + 1][mjonoY.length() + 1];
        operaationHinta = 1;

        for (x = 0; x <= |mjonoX|; x++) {
            etaisyysMatriisi[x][0] = x;
        }
        for (int y = 0; y <= |mjonoY|; y++) {
            etaisyysMatriisi[0][y] = y;
        }

        for (int y = 1; y <= |mjonoY|; y++) {
            for (int x = 1; x <= |mjonoX|; x++) {
                if (mjonoX[x - 1] == mjonoY[y - 1]) {
                    etaisyysMatriisi[x][y] = etaisyysMatriisi[x - 1][y - 1];
                } else {
                    etaisyysMatriisi[x][y] = minimi(minimi(etaisyysMatriisi[x - 1][y],
                            etaisyysMatriisi[x][y - 1]),
                            etaisyysMatriisi[x - 1][y - 1]) + operaationHinta;
                }

            }
        }

        return etaisyysMatriisi[|mjonoX|][|mjonoY|];
    }
```
Selvästi nähdään, että algoritmin pahin aikavaativuus ja tilavaativuus ovat O(|mjono1|x|mojono2|). Algoritmi varaa |mjono1|x|mojono2| kokoisen taulukon, ja käy sen kerran läpi, ja palauttaa viimeisen indeksis. Kaikki silmukan sisällä tehtävät operaatiot tapahtuvat vakioajassa. Tämä algortimi on erittäin käyttökelpoinen luonnollisen kielen sanoilla, vaikka kuten suorituskykytestistä näkee, suoritusaika alkaa kasvaa suuremmilla merkkijonoilla nopeasti.

![](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/levenshteinAikaVaativuus.png)


# Tietorakenteet
## Trie-puu
Tietorakenne tarkistamaan nopeasti, kuuluuko sana puuhun. Juureksi valitaan tyhjä merkki. Yhtä solmua puussa vastaa yksi kirjain, johon on liitetty kirjainavaimella toisia solmuja. Puussa muodostuu siis suunnattuja polkuja kohti juuria, ja polut kuvaavat puun sisältämiä sanoja. Jokainen sana, joka lisätään puuhun lisätään niin, että sanan kirjaimen solmulistassa sanan seuraava kirjainta vastaavalla kohdalla on seuraavaa kirjainta esittävä solmu. Sanan viimeistä kirjainta esittävässä solmussa on lisäksi merkki siitä, että solmuun päättyvä polku on sana.

Toteutettu optimoituja solmulistoja käyttäen, jotta tilavaativuus olisi mahdollisimman pieni. Solmulista on maksimissaan sallitun aakkoston kokoinen, jos solmulla ei ole yhtään lasta, on solmulistan koko 0. Jos solmulistaan lisätään vain avaimella "a", niin solmulistan koko on 1, toisaalta merkillä "'", solmulistan kooksi tulee 41, missä suurin osa on tyhjiä alkioita, mutta "'"-merkki on kovin harvinainen, joten tällaisia solmulistoja ei ole kovin montaa.

Tarkistaminen
```
Boolean onkoSana(sana) {
        TrieSolmu nykyinen = juuri;
        for (i = 0; i < |sana|; i++) {
            kirjain = sana[i];
            TrieSolmu lapsi = nykyinen.haeSolmuListasta(kirjain);
            if (lapsi == null) {
                return false;
            }
            nykyinen = lapsi;
        }
        return nykyinen.onSana();
}
```
Silmukka suoritetaan |sana| kertaa, ja sillä solmulistasta hakeminen on solmulistan ollessa taulukko, jonka indeksit vastaavat merkkien kokonaislukuarvoja, niin solmun hakeminen on käytännössä vakioaikainen operaatio. Muutkin silmukan operaatiot ovat vakioaikaisia, siis sanan puuhun kuulumisen tarkistamisen pahin aikavaativuus on O(|sana|).

Lisääminen tapahtuu samalla mekanismilla sillä erotuksella, että solmulistaan lisätään aina uusi solmu, jos vastaan tullut solmu on tyhjä. Lisääminen solmulistaan on myös vakioaikainen operaatioi, ja siis lisäämisen aikavaativuus on O(|sana|). 

Poistaminenkin voidaan tehdä haun kaltaisella operaatiolla ja se omaa myös saman O(|sana|) aikavaativuuden. Vaativuus on hieman suurempi, jos halutaan poistaa tyhjäksi jääneitä polkuja, mutta käytännössä tämä ei juuri kasvata aikavaativuutta.

Pahimmassa tapauksessa, siis jos jokaisen solmun solmulistan koko olisi aakkoston koko, ja solmuilla ei olisi yhteisiä polkuja ja Trie olisi täytetty täyteen x pituisilla sanoilla, Trien tilavaativuudeksi tulisi O(|aakkosto| x N ), missä N on solmujen määrä. Kuitenkin esimerkiksi se, että tällä solmulistatoteutuksella solmun, jolla ei ole lapsia, solmulistan koko on 0, tarkoittaa sitä, että pahimmassa tapauksessa pelkästään lehti solmujen tilavaativuus on |aakkosto|^|aakkosto| kertaa parempi kuin ei optimoidun Trien.

## BK-puu
BK-puu toimii tietorakenteena, joka mahdollistaa nopeiden korjausehdotusten tuottamisen. Ohjelman käynnistyessä BK-puuhun ladataan koko sanasto, ottaen juureksi satunnainen sanaston sana. Puun solmujen tulee toteuttaa metriikka, jotta puu lopulta toimii tehokkaasti. Kahden solmun merkkijonojen välinen muokkausetäisyys on se, millä solmut tässä puun metrisessä avaruudessa erotetaan toisistaan. Jokaisella solmulla voi olla yhtä muokkausetäisyyttä kohden vain yksi lapsi.

Itse muokkausetäisyysfunktio voi muuttua, kunhan metrisyys säilyy. 
Puun tehokkuus perustuu siihen, että vaikka sanasto on iso, niin sillä puu muodostaa metrisen avaruuden, voidaan tiettyä merkkijonoa, tai haettavaa lähimpänä olevia merkkijonoja haettaessa karsia suurin osa puun haaroista pois kolmioepäyhtälön avulla.

Jokaista sanaa kuvaa solmu, ja jokaisella solmulla on solmulista, jossa indeksissä i, on sana, joka on etäisyydellä i solmusta.
Puu sisältää jokaisen sanaston sanan, ja sen tilavaativuus on siten O(|sanasto|).

Lisäys

```
void lisaaSana(String sana) {
        BKSolmu nykyinenSolmu = this.juuri;
        while (true) {
            int etaisyysNykyiseenSolmuun = this.etaisyysLaskija.laskeEtaisyys(nykyinenSolmu.sana, sana);
            BKSolmu nykyisenLapsiLisattavanSananEtaisyydella = nykyinenSolmu.lapsiEtaisyydella(etaisyysNykyiseenSolmuun);
            if (nykyisenLapsiLisattavanSananEtaisyydella == null) {
                nykyinenSolmu.lisaaLapsi(new BKSolmu(sana), etaisyysNykyiseenSolmuun);
                break;
            } else {
                nykyinenSolmu = nykyisenLapsiLisattavanSananEtaisyydella;
            }
        }
    }
```
Lisäyksen aikavaativuuteen vaikuttaa sanan pituus, käytetty etäisyysfunktio, puun tasapaino, sekä itse sana. Voidaan olettaa, että puu pysyy varsin tasapainoisena, ja sillä on joku keskimääräinen syvyys s. Tällöin pahimman tapauksen lisättävien sanojen pitää käydä läpi syvimmän polun verran solmuja ja laskea etäisyyksia. Pahimmassa tapauksessa siis lisäys tasapainotetussa puussa on O(p x e(k)), missä p on syvin polku, ja e(k) on etaisyysfunktion keskimääräinen aikavaativuus sanaston sanoilla. Luonnollisen kielen sanastolla puu pysyy kuitenkin hyvin tasapainossa ja matalana, ja lisäys on riittävän nopeaa. 

Lisäyksestä on suorituskykytesti, jossa mitataan korjaajan alustusaika. Tässä on siis otettu huomioon myös Trien täyttämiseen vaadittava aika:

![](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/alustusAikaVaativuus.png)

Poistaminen

Rakenteesta ei varsinaisesti voi poistaa, tai se ei ainakaan ole kovin triviaalia, sanoja. Tämä siksi, että etäisyyksiä pitäisi tällöin laksea uudestaan, ja liikuttaa monia solmuja. Poistaminen tehdäänkin niin, että jokaisella solmulla on tila onPoistettu, ja se solmu haetaan ja tämä tila laitetaan päälle jos sana halutaan poistaa. Tämä toimii samassa ajassa kuin hakeminen, mutta rakenne tietysti lisää hieman tilavaativuutta.

Lahimpien sanojen hakeminen

```
 String[] haeLahimmatSanat(String sana, int etaisyysToleranssi, int montaHaetaan) {
        JarjestyvaTaulukko lahimmatSanat = new JarjestyvaTaulukko(montaHaetaan, Jarjestys.NOUSEVA);
        Pino<BKSolmu> kandidaatit = new Pino<>();

        kandidaatit.lisaa(this.juuri);

        while (!kandidaatit.onTyhja()) {
            BKSolmu verrattavaSolmu = kandidaatit.poista();
            int etaisyysEtsittavaanSanaan = this.etaisyysLaskija.laskeEtaisyys(sana, verrattavaSolmu.sana);
            if (etaisyysEtsittavaanSanaan <= etaisyysToleranssi && !verrattavaSolmu.onPoistettu) {
                lahimmatSanat.lisaa(new SanaEtaisyysPari(verrattavaSolmu.sana, etaisyysEtsittavaanSanaan));
            }
            int rajausAlaRaja = etaisyysEtsittavaanSanaan - etaisyysToleranssi;
            int rajausYlaRaja = etaisyysEtsittavaanSanaan + etaisyysToleranssi;
            for (int i = rajausAlaRaja; i <= rajausYlaRaja; i++) {
                if (i < 0) {
                    continue;
                }
                BKSolmu uusiKandidaatti = verrattavaSolmu.lapsiEtaisyydella(i);
                if (uusiKandidaatti != null) {
                    kandidaatit.lisaa(uusiKandidaatti);
                }
            }
        }

        return lahimmatSanat.haeMerkkijonoTaulukkona();
    }

```
Lähimpien sanojen hakualgoritmi käyttää hyväksi BK-puun metrisyyttä ja tämän myötä kolmioepäyhtälöä. Itse haku tapahtuu niin, että käydään läpi puun sanoja ja verrataan haettavaan sanaan. Jos verrattava sana on toleranssin sisällä, otetaan se ylös. Kolmioepäyhtälön avulla voidaan karsia läpikäytäviä solmuja. Siis pahimmassa tapauksesssa isolla toleranssilla käydään kuitenkin läpi kaikki solmut ja aikavaativuus on O(|sanasto| x k(s)), missä k(s) on keskimääräinen etäisyyslaskijan aikavaativuus. On vaikea arvioida keskimääräistä käytännön aikavaativuutta tarkkaan riippuen etäisyystoleranssista, mutta etäisyystoleranssilla 2, pitäisi haun käydä läpi sanastosta n 10%. Tälle prosenttimäärälle ei ole suorituskykytestiä, joka on puute. Toleranssin vaikutusta korjausehdotusten keskimääräiseen suoritusaikaan eri sanastoilla on tarkasteltu suorituskykytestillä:

![](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/toleranssiTesti.png)

Se, miten monta sanaa haetaan, vaikuttaa myös suorituksen kestoon, sillä aina kun löydetään toleranssin sisällä oleva sana, pitää se lisätä järjestykseen johonkin tietorakenteeseen. Tässä työssä on toteutettu tämä ehdotusten tallentaminen itsestään järjestyvällä listalla, joka "tiputtaa" aina suurimpia arvoja pois. Tällöin aina kun löydetään uusi toleranssin sisällä oleva sana, ei sen lisäämiseksi palautettaviin tarvitse tehdä kuin niin monta vertailua kuin on määritelty, että ehdotuksia haetaan. Seuraavaa on tarkasteltu myös suorituskykytestillä:

![](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/montaHaetaanVaikutus.png)

Muita havaintoja mitä korjausten hakemisesta BK puun avulla voi suorituskykytestien perusteella tehdä on:

- Mitä suurempi sanasto, sitä enemmän lähimpien haku löytää sanoja. Tällöin korjausten oikeellisuus kärsii. Sanaston koolla on siis käytännön toiminnan kannalta joku kultaisen keskitien koko. 
- Trie toimii hyvin yhdessä BK-puun kanssa, sillä se mahdollistaa itse sovelluksessa sen, että virheelliset tunnistetaan, ja itse ehdotukset lasketaan sanasta vasta kun käyttäjä haluaa. Tämän myötä voidaan käyttää suuriakin toleransseja käytännössä.

## Muut tietorakenteet
Muut tietorakenteet ovat lähinnä apurakenteita BK-puulle ja Trielle, joten niissä ei ole tässä yhteydessä juurikaan analysoimista. Voidaan olettaa, että ne yksinkertaisuutensa myötä toimivat riittävällä tehokkuudella verrattuna javan omiin toteutuksiin.

# Sanastoista
Sanastot on muodostettu seuraavien sanastojen avulla:

[KOTIMAISTEN KIELTEN KESKUKSEN NYKYSUOMEN SANALISTA](http://kaino.kotus.fi/sanat/nykysuomi/): [Lisenssi](https://creativecommons.org/licenses/by/3.0/deed.fi)

[Kirjoitetun suomen kielen sanojen taajuuksia](http://kaino.kotus.fi/sanat/taajuuslista/parole.php): [Lisenssi]()

[Ylen suomenkielinen uutisarkisto, lähdeaineisto 2011-2018](https://metashare.csc.fi/repository/browse/yle-finnish-news-archive-2011-2018-source/178ee7cc614c11e78c02005056be118ef9e611ae760143c98fa4cbac6c63be5d/): [Lisenssi](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/lisenssit/YLE_LICENSE.txt)

- Keskikokoinen sanasto on luotu nykysuomen sanalistasta
- Keskilaaja ja Laaja "Kirjoitetun suomen kielen sanojen taajuuksia" aineistosta
- Loput on generoitu ylen uutisaineistosta

Kokeilujen perusteella käyttökelpoisimmat sanastot ovat Keskilaaja ja Laaja.


# Puutteita
- BK solmun tapauksessa suorituskykytesti, joka mittaa monesa solmussa haun aikana käydään olisi hyödyllinen. Toisaalta puuhun ei haluta lisätä yhtään ylimäräistä laskentaa köyttötarkoituksesta johtuen. Voisi tietysti tehdä erillisen, testattavan BK-Puun.
- Poikkeusten hallinta ei ole toteutettu kovin optimaalisesti
- Testien tuloksia voisi mahdollisesti päivittää dynaamisesti
- Toinen etäisyysfunktio olisi ollut hienoa toteuttaa, mutta aika ei riittänyt. Toisaalta varsin iso osa ajasta "tuhlattiin" ui:n kanssa taistelemiseen.
- Sanan poistaminen sanastosta ei tällä hetkellä onnistu pysyvästi. Suorituksen aikana sanan voi kyllä poistaa, mutta tämä ei tallennu varsinaiseen sanastoon, ja kun tämä sanasto ladataan uuteen korjaajaan uudestaan, ei sanan poistaminen ole rekisteröitynyt.  Tämän yhteydessä ongelmana on se, että 2m kokoisesta sanastosta pitäisi etsiä kaikki poistettavat sanat ja poistaa ne. Tähän keksittiin teorian tasolla ratkaisu. Pidetään poistettavat sanat aakkosjärjestyksessä, kuten myös varsinainen sanasto. Sitten kun poistettavat halutaan rekisteröidä sanastoon, käydään poistettavien lista läpi ylhäältä aakkosjärjestyksessä. Samaan aikaan käydään läpi ylhäältä aakkosjärjestyksessä sanastotiedstoa. Verrataan poistettavaa sanaa sanastosta saatuun sanaan ja jos täsmää, poistetaan rivi. Sitten otetaan seuraava sana poistettavista, ja sillä poistettavat ja sanasto on käyty läpi aakkosjärjestyksessä, niin tiedetään, että uusi käsiteltävä poistettava ei ole voinut tulla aikaisemmin, ja voidaan jatkaa sanaston lukua ja vertaamista. Näin saadaan siis poistettua poistettavat yhdellä sanastotiedoston luvulla. Tässä on kuitenkin väsäämistä, erityisesti kun pitää myös synkronoida sanastoon lisäämiset ja poistamiset tallennusta varten, joten sitä ei nyt alettu toteuttamaan.
- lokituksen voisi tehdä tiedostoon

# Lähteet

[Multivariate Algorithmics forNP-Hard String Problems](https://fpt.akt.tu-berlin.de/publications/fpt-strings-beatcs14.pdf)

[A Guided Tour to Approximate String Matching, NAVARRO. G., 2001](http://users.csc.calpoly.edu/~dekhtyar/570-Fall2011/papers/navarro-approximate.pdf)

R. Baeza-Yates and G. Navarro, "Fast approximate string matching in a dictionary," Proceedings. String Processing and Information Retrieval: A South American Symposium (Cat. No.98EX207), Santa Cruz de La Sierra, Bolivia, 1998, pp. 14-22, doi: 10.1109/SPIRE.1998.712978.

Wikipedia
