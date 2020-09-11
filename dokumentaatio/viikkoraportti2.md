# Viikkoraportti 2

## Mitä olen tehnyt tällä viikolla?

- Luonut alustavan tiedostorakenteen ohjelmalle. 
  - Ui raakile 
  - Trie puun alustava toteutus 
  - Tiedostojen lukemisen alustava toteutus. 
  - Checkstyle ja jacoco toimimaan
- Tehnyt taustatyötä korjausehdotusten algoritmien ja tietorakenteiden toteuttamisesta. Löysin viimein hyviä lähteitä aiheella "String matching".
- Tehnyt taustatyötä ui tekstieditorin toteutusta varten.

## Miten ohjelma on edistynyt?

- Ohjelmalle saatiin luotua kelvollinen projektirakenne ja toteutettua alustava virheellisen tekstin tunnistus. Paljon on kuitenkin vielä perehdyttävää, niin varsinaisen ongelman, kuin ui toteutuksenkin saralla.

## Mitä opin tällä viikolla?

- Toteuttamaan Trie-puun.
- Palautin Javan kaunista kieltä mieleeni.

## Mikä on jäänyt epäselväksi tai tuottanut vaikeuksia?

- Miten luon [Tästä](http://kaino.kotus.fi/sanat/nykysuomi/) aineistosta taivutettujen sanojen version.
- Kuinka sovelluksessa on hyvä toteuttaa sanaston lukeminen resurssitiedostosta Trie puuhun. Nyt sijoitin sen i/o-ta käsittelevään luokkaan ikään kuin hard koodattuna ja palautan sanaston listana korjaajalle. Tehokkain olisi kai lisätä suoraan BufferedReaderillä Trie täyteen, mutta en tiedä miten sen saa sopimaan projektirakenteeseen. 


## Mitä teen seuraavaksi

- Luen lisää tarvittavista algoritmeista ja tietorakenteista, sekä ui toteutuksesta
- Koodaan ui:ta hieman paremmaksi
- Keskityn kuitenkin pääasiassa itse virheenkorjauksen toteutukseen. Myös sanaston kanssa pitää hieman työskennellä.

## Käytetty tuntimäärä
- 15 h
