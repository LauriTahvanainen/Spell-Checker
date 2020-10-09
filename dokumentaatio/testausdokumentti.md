# Testausdokumentti

## Projektin ajantasainen testikattavuus
![Testikattavuus](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/testikattavuus1.png)
![Testikattavuus](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/testikattavuus2.png)
![Testikattavuus](https://github.com/LauriTahvanainen/Kirjoitusvirhekorjaaja/blob/master/dokumentaatio/testikattavuus3.png)


## Testaamisesta
Testaamiseen on käytetty JUnit:ia, ja testit on ajettavissa automaattisesti komennolla **mvn test**

Projektissa on yksikkötestattu käytetyt tietorakenteet sekä algoritmit. Pääasiallisena testausmetodina on toiminut testitapausten määrittely käsin. Apuna on käytetty myös Mockito-kirjastoa testaamaan mm. sitä kuinka monta kertaa tietorakenne suorittaa tiettyä operaatiota. 

### Algoritmit
Algoritmien testaaminen on toteuttu antamalla algoritmille syötteitä, ja tarkistamalla, että algoritmi tuottaa oikean tuloksen annetulla syötteellä.

### Tietorakenteet
Tietorakenteiden kaikki oleelliset operaatiot (lisäys, poisto, haku) on testattu suorittamalla operaatioita ja tarkastelemalla sitten tietorakenteen tilan oikeellisuutta. Esim. puiden tapauksessa oikeellisuuden tarkastelu on tehty ikään kuin käsin, tarkastelemalla puun tilaa juuresta lähtien.

Tietorakenteiden yhteydessä on testattu myös niiden oikeellista toimintaa Mockito-kirjaston avulla mm. käytettyjen resurssien näkökulmasta. Esim BK-puun yhteydessä testataan, että haun yhteydessä ohitetaan tietty osa puun haaroista, ja näin nopeutetaan hakua.

### Käyttöliittymä
Käyttöliittymä on testattu manuaalisesti.

# Suorituskykytestaus

## TestiAineistosta
Korjaajan suorituskyvyn testaaminen, erityisesti sopivan testiaineiston hankkiminen koitui vaikeaksi. Optimaalisin tapa testata olisi ihan oikeilla käyttäjillä, tai oikeilta käyttäjiltä kerätyllä testiaineistolla. Projektin tapauksessa päädyttiin kuitenkin testaamaan niin, että sanastosta haetaan satunnainen otos ja sitten jokaiselle otoksen sanalle tehdään satunnainen, mutta testistä riippuen ylhäältä rajattu, määrä yksittäisen merkin muokkausoperaatiota. Operaatiot ovat yhden merkin lisäys, poisto, vaihtuminen tai kahden sanan vierekkäisen merkin vaihtuminen keskenään (transpositio). Lisäyksen ja vaihdon yhteydessä käytettiin myös näppäimistön vierekkäisten merkkien heuristiikkaa. Esimerkiksi vaihdon yhteydessä muokattavasta sanasta valitaan ensin satunnainen merkki. Jos tämä merkki on esimerkiksi 'a', niin sen tilalle arvotaan jokin merkeistä 'q', 'w', 's', 'z' tai '<'.

## Suunnitelma
Suorituskykytestit toteutetaan omaan pakettiinsa, jotta ne voidaan ajaa erikseen yksikkötesteistä.

Suorituskykytestausta voi tehdä projektissa monessa eri palasessa. Tässä vaiheessa kuitenkin kun kaikkia tietorakenteita ei ole vielä toteutettu, ei suorituskykytestaustakaan aloiteta täysillä. Testattavia kohteita:

- Sanaston lataaminen Trie- ja BK-puuhun. 
  - Testattava oikeasti isolla sanastolla.
- Sanan sanastoon kuulumisen testaamisen tehokkuus
  - Testattava täydellä sanastolla
  - Keskiarvo
- Virheelliselle sanalle korjausehdotusten generoimisen tehokkuus
  - Testattava täydellisellä sanastolla
  - Keskiarvo
- Oleellisten tietorakenteiden tehokkuuden testaaminen.
- Etäisyysmittausalgortimien, esim Levenshteinin-etäisyyden tehokkuuden testaaminen.
  - Jos ja kun lisätään vaihtoehtoisia etäisyysalgoritmeja, verrataan näiden suorituskykyä, sekä yksittäin, että kokonaisuutena korjausehdotusten generoinnin kanssa.

