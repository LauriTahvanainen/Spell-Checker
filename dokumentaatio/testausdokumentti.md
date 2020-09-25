# Testausdokumentti

## Projektin ajantasainen testikattavuus
![Testikattavuus](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/testikattavuus.png)

## Testaamisesta
Testaamiseen on käytetty JUnit:ia, ja testit on ajettavissa automaattisesti komennolla **mvn test**

Projektissa on yksikkötestattu käytetyt tietorakenteet sekä algoritmit. Pääasiallisena testausmetodina on toiminut testitapausten määrittely käsin. Apuna on käytetty myös Mockito-kirjastoa testaamaan mm. sitä kuinka monta kertaa tietorakenne suorittaa tiettyä operaatiota. 

### Algoritmit
Algoritmien testaaminen on toteuttu antamalla algoritmille syötteitä, ja tarkistamalla, että algoritmi tuottaa oikean tuloksen annetulla syötteellä.

### Tietorakenteet
Tietorakenteiden kaikki oleelliset operaatiot (lisäys, poisto, haku) on testattu suorittamalla operaatioita ja tarkastelemalla sitten tietorakenteen tilan oikeellisuutta. Esim. puiden tapauksessa oikeellisuuden tarkastelu on tehty ikään kuin käsin, tarkastelemalla puun tilaa juuresta lähtien.

Tietorakenteiden yhteydessä on testattu myös niiden oikeellista toimintaa mm. käytettyjen resurssien näkökulmasta. Esim BK-puun yhteydessä testataan, että haun yhteydessä ohitetaan tietty osa puun haaroista, ja näin nopeutetaan hakua.
