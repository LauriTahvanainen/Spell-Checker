# Viikkoraportti 5

## Mitä olen tehnyt tällä viikolla?
- Hylkäsin PunaMustaPuun toteutuksen ja toteutin sen tilalle kiinteän pituisen taulukon, joka pysyy aina järjestyksessä, ja jonka viimeiset alkiot tippuvat pois. BK-puun etsinnän aikana tähän taulukkoon lisätään, ja lopulta taulukkoon jää 10 lähintä sanaa. PunaMustaPuun jokainen lisäys veisi O(log n) ja siihen lisättäisiin BK-puun haussa noin 5% sanastosta. Lopulta kun lisäykset olisi tehty, pitäisi vielä poistaa 10 pienintä solmua. Lopullisen sanaston koon ollessa valtavan suuri, on perusteltua käyttää tätä kiinteää taulukkoa, sillä jokainen lisäys tapahtuu vakioajassa, ja BK-haun päätteeksi järjestetty lähimpänä olevien taulukko on jo valmiina.


## Miten ohjelma on edistynyt?

## Mitä opin tällä viikolla?


## Mikä on jäänyt epäselväksi tai tuottanut vaikeuksia?


## Mitä teen seuraavaksi

## Käytetty tuntimäärä
- 16
