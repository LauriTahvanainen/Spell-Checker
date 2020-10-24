# Testausdokumentti

Testit voi ajaa komennolla
```
mvn test
```

Testikattavuusraportin voi generoida komennolla

```
mvn test jacoco:report
```

## Projektin ajantasainen testikattavuus
![Testikattavuus](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/testikattavuus1.png)
![Testikattavuus](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/testikattavuus2.png)
![Testikattavuus](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/testikattavuus3.png)


## Testaamisesta
Testaamiseen on käytetty JUnit:ia, ja testit on ajettavissa automaattisesti komennolla **mvn test**

Projektissa on yksikkötestattu käytetyt tietorakenteet sekä algoritmit. Pääasiallisena testausmetodina on toiminut testitapausten määrittely käsin. Apuna on käytetty myös Mockito-kirjastoa testaamaan muun muassa sitä, kuinka monta kertaa tietorakenne suorittaa tiettyä operaatiota. 

Itse korjaajaluokkaa on testattu vähän koska, luokka on pääasiassa vain runko korjaajalle. Itse korjaajan komponentit, eli tietorakenteet ja algoritmit, on testattu tarkemmin.

### Algoritmit
Algoritmien testaaminen on toteuttu antamalla algoritmille syötteitä, ja tarkistamalla, että algoritmi tuottaa oikean tuloksen annetulla syötteellä.

### Tietorakenteet
Tietorakenteiden kaikki oleelliset operaatiot (lisäys, poisto, haku) on testattu suorittamalla operaatioita ja tarkastelemalla sitten tietorakenteen tilan oikeellisuutta. Esim. puiden tapauksessa oikeellisuuden tarkastelu on tehty ikään kuin käsin, tarkastelemalla puun tilaa juuresta lähtien.

Tietorakenteiden yhteydessä on testattu myös niiden oikeellista toimintaa Mockito-kirjaston avulla muun muassa käytettyjen resurssien näkökulmasta. Esim BK-puun yhteydessä testataan, että haun yhteydessä ohitetaan tietty osa puun haaroista, ja näin nopeutetaan hakua.

### Käyttöliittymä
Käyttöliittymä on testattu manuaalisesti.

# Suorituskykytestaus

## TestiAineistosta
Korjaajan suorituskyvyn testaaminen, erityisesti sopivan testiaineiston hankkiminen koitui vaikeaksi. Optimaalisin testaustapa olisi oikeilla käyttäjillä, tai oikeilta käyttäjiltä kerätyllä testiaineistolla. Projektin tapauksessa päädyttiin kuitenkin testaamaan niin, että sanastosta haetaan satunnainen otos ja sitten jokaiselle otoksen sanalle tehdään satunnainen, mutta testistä riippuen ylhäältä rajattu, määrä yksittäisen merkin muokkausoperaatiota. Operaatiot ovat yhden merkin lisäys, poisto, vaihtuminen tai kahden sanan vierekkäisen merkin vaihtuminen keskenään (transpositio). Lisäyksen ja vaihdon yhteydessä käytettiin myös näppäimistön vierekkäisten merkkien heuristiikkaa. Esimerkiksi vaihdon yhteydessä muokattavasta sanasta valitaan ensin satunnainen merkki. Jos tämä merkki on esimerkiksi 'a', sen tilalle arvotaan jokin merkeistä 'q', 'w', 's', 'z' tai '<'.

## Suorituskykytestaus
Toteutusdokumentissa on käsitelty joitain suorituskykytestien tuloksia.
Suorituskykytestit on toteutettu omaan luokkaansa, josta niitä voi käyttää.

Suorituskykytestit voi suorittaa käyttöliittymästä, ja niiden tulokset näytetään käyttäjälle viivakaaviona. Testien parametreja voi myös vaihtaa helposti käyttöliittymästä.

### Korjaajan suorituskykytestit
Erilaisten korjaajien suorituskykyä voi testata kahdella tavalla. Testaamalla korjaajan keskimääräistä korjausaikaa, sekä korjausonnistumisprosenttia.

Kaikki korjaajan parametrit: etäisyysfunktio, haettavien korjausehdotusten määrä, toleranssi ja sanasto ovat valittavissa testejä varten. Testit voidaan ajaa peräkkäin, ja näin viivakaavioon voi piirtää saman testin tulokset eri korjaajilla.

Viivakaaviossa x-akseli kuvaa sanastojen kokoa. Kuvassa korjaajien korjausprosenttitestejä eri parametreillä:
![](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/korjausProsenttiTesti.png)
Huomataan, että mitä suurempia virheitä sanoissa on, ja mitä suurempi etäisyystoleranssi on, sitä huonompia korjausehdotuksista tulee. Tämä on ymmärrettävää. Erityisesti suurten sanastojen kanssa on vaara siihen, että sanastosta löytyy monia muita sanoja samalla korjausetäisyydellä, ja lopullisiin ehdotuksiin ei päädy oikeaa ehdotusta. Näyttäisi, että ainakin tämän testin perusteella noin 1 miljoonan sanan sanasto toimii hyvin. Samalla etäisyydellä olevien sanojen sanojen suurta määrää voi projektin mallissa kuitenkin helpottaa yksinkertaisesti hakemalla suuremman määrän ehdotuksia. Varsinkin kun huomattiin, että ehdotusten määrän lisääminen ei juurikaan nosta aikavaativuutta.

### Korjaajan alustusaika ja tilavaativuus
Samankaltaiseen viivakaavioon piirretään korjaajan alustusaikaa valituilla sanastoilla kuvaavan testin tulokset. Samassa testinäkymässä voi myös tarkastella korjaajan muistivarausta, mutta tämän mittaaminen ei ole kovin tarkkaa javan muistihallinnan vuoksi.

### Etäisyysfunktioiden aikavaativuus
Tämä testi havainnollistaa valitun etäisyysfunktion aikavaativuutta.

### Puutteita
Havainnollistamisen takia olisi ehkä ollut hyvä, että BK puun haulle eri toleransseilla olisi tehnyt testin. Tämä testi kuitenkin osaltaan sisältyy korjaajan suorituskykytesteihin.
