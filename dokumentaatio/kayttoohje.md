## Asentaminen ja avaaminen
Kehitettäessä käytössä on ollut java version **1.8.0_212** ja javafx.runtime.version **8.0.212**

[Lataa viimeisin julkaisu](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/releases/tag/v1.0)

Pura sanastot.zip samaan kansioon kuin jar tiedosto. sanastot kansion pitää olla siinä kansiossa mistä jar suoritetaan.

Suorita ohjelma komennolla 
```
java -jar Kirjoitusvirhekorjaaja-1.0.jar
```

Sanastotiedostojen pitää oikeassa kansiossa (sanastot) vakionimillään jotta ohjelma toimii. Itse sanastoja voi kyllä muokata.

## Käyttö
HUOM. Ohjelma tukee vain suomenkielen aakkostoa. Muulla aakkostolla ohjelma ei toimi oikeellisesti!

### Tekstinkäsittely
Ohjelmaa käytetään suomenkielisen tekstin käsittelyyn, pääasiassa kirjoittamiseen. Ohjelma korjaa yksittäisten sanojen kirjoitusvirheitä.

Käynnistyessään ohjelma avaa tyhjän tekstieditorin, johon voi kirjoittaa.

Käyttäjä voi halutessaan avata jo tallennetun tiedoston *Avaa tiedosto*-napista.

*Tallenna nimellä*-napista käyttäjä voi tallentaa tekstinsä valitsemaansa tiedostoon.

Jos käyttäjällä on tiedosto auki, voi tehdyt muutokset tallentaa myös *CTRL+S* pikanäppäimellä.

### Kirjoitusvirheiden korjaaminen

Käyttäjän kirjoittaessa ohjelma tarkastaa automaattisesti kirjoitettujen sanojen oikeellisuuden. Jos kirjoitettu sana on virheellinen, se alleviivataan punaisella.

Klikkaamalla alleviivattua sanaa, käyttäjälle avautuu 10 korjausehdotusta virheelliselle sanalle. Korjausehdotuksen valitseminen tapahtuu joko painamalla ehdotusta hiirellä, tai painamalla *SPACE*- tai *ENTER*-näppäintä kun haluttu korjaus on valittuna. Ohjelma korvaa virheellisen sanan käyttäjän valitsemalla korjauksella. Ehdotukset voi sulkea painamalla *ESC*-näppäintä.

Toinen tapa nähdä korjausehdotuksia virheelliseen sanaan on viedä kursori virheellisen sanan päälle ja painaa näppäinyhdistelmää *CTRL+ENTER*.

*Korjaa kaikki virheelliset*-napista jokainen virheellinen sana korvataan automaattisesti ensimmäisellä korjausehdotuksella.

### Korjaajan valitseminen
*Muuta korjaajan asetuksia*-napista avautuu valikko, josta voi muuttaa korjaajan asetuksia. Muutokset tallentuvat *Tallenna*- napista. Valitut muutokset tallentuvat myös ohjelman sulkeutuessa, ja siis ohjelma säilyttää käyttäjän korjaaja-asetukset.

### Sanastojen hallinta
Virheelliseksi merkatun sanan voi lisätä sanastoon siirtämällä kursorin sanan päälle, ja painamalla *CTRL+L*-näppäinyhdistelmää. Tämä lisäys säilyy valittuna olevassa sanastossa myös ohjelman suljettua.

Oikeelliseksi merkityn sanan taas voi poistaa sanastosa siirtämällä kursorin sanan päälle, ja painamalla *CTRL+P*-näppäinyhdistelmää. HUOM. Tällä hetkellä poistetut sanat eivät rekisteröidy valittuun sanastoon pysyvästi, vaan lataamalla sanaston uudestaan, ovat poistetut sanat taas sanastossa.

### Pikanäppäimet tiivistetysti

Näppäinyhdisstelmä | Seuraus
-------------------| -------
CTRL+S | Tallentaa avattuun tiedostoon tehdyt muokkaukset.
CTRL+ENTER | Avaa korjausehdotukset kursorin ollessa virheellisen sanan päällä.
CTRL+L | Lisää valittuna olevaan sanastoon kursorin alla olevan virheellisen sanan.
CTRL+P | Poistaa kursorin alla olevan oikeellisen sanan sanastosta.


### Suorituskykytestit
Suorituskykytestejä pääsee tarkastelemaan ja ajamaan *Suorituskykytestit*-napista. Testivalikosta voi siirtyä eri testeihin ja eri testien valikossa voi asettaa testin parametreja, ja ajaa testejä. Isoilla parametreilla joidenkin testien suorittamisessa saattaa kestää äärimmäisen kauan.
