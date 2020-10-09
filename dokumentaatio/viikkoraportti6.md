# Viikkoraportti 6

## Mitä olen tehnyt tällä viikolla?
- Hylkäsin PunaMustaPuun toteutuksen ja toteutin sen tilalle kiinteän pituisen taulukon, joka pysyy aina järjestyksessä, ja jonka viimeiset alkiot tippuvat pois. BK-puun etsinnän aikana tähän taulukkoon lisätään, ja lopulta taulukkoon jää 10 lähintä sanaa. PunaMustaPuun jokainen lisäys veisi O(log n) ja siihen lisättäisiin BK-puun haussa noin 5% sanastosta. Lopulta kun lisäykset olisi tehty, pitäisi vielä poistaa 10 pienintä solmua. Lopullisen sanaston koon ollessa valtavan suuri, on perusteltua käyttää tätä kiinteää taulukkoa, sillä jokainen lisäys tapahtuu vakioajassa, ja BK-haun päätteeksi järjestetty lähimpänä olevien taulukko on jo valmiina.
- Edistin käyttöliittymää.
- Edistin hieman suorituskykytestejä. Testiaineiston generointiin näppäimistöheuristiikkaa.
- Tein vertaisarvioinnin.

## Miten ohjelma on edistynyt?
- Käyttöliittymä otti isoja askelia kun käyttöliittymäkirjasto alkoi selkiytymään. Kaikki on nyt toteutettu omilla tietorakenteilla, ja korjaukset voi näyttää graafisesti. Enää virheiden lopullinen korjaaminen käyttöliittymässä, ja toiminnallisuus on täysin valmis.

## Mitä opin tällä viikolla?
- RichTextFx kirjastosta.
- Aina ei välttämättä tarvitse yrittää tunkea algoritmiin jotain hienoa uutta tietorakennetta, ihan perus taulukko voi tietylle käyttötarkoitukselle riittää, ja olla selvästi tehokkain.

## Mikä on jäänyt epäselväksi tai tuottanut vaikeuksia?

## Mitä teen seuraavaksi
- Ui loppuun.
- Valmistelen lopullisen sanaston.
- Aloittelin ja pohdin jo hieman erikoismerkkien siistimistä sanastosta, teen sen seuraavaksi.
- Viimeistelen työn.

## Käytetty tuntimäärä
- 20h
