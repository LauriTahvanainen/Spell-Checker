# Viikkoraportti 5

## Mitä olen tehnyt tällä viikolla?
- Toteuttanut tarvittavia tietorakenteita omilla toteutuksilla, kuten pino, lista, Trie-uun HashSet korvaaja.
- Testannut
- Aloittanut suorityskykytestejä
- Tehnyt vertaisarvioinnin


## Miten ohjelma on edistynyt?
- Vielä PunaMusta-puun oma toteutus ja niin täysin itse toteutettu korjaaja on valmis. Itse korjaaja kyllä jo toimii. Suorituskykytestejäkin sai hieman aloitettua ja mietittyä. Ui:ta oli tarkoitus katsoa, mutta ei ehtinyt.

## Mitä opin tällä viikolla?
- Reservoir Sampling:stä
- Vähän punamustapuusta.

## Mikä on jäänyt epäselväksi tai tuottanut vaikeuksia?
- Trie solmulistan HashSetin korjaajan toteutuksessa oli hieman mietittävää. Tarkoitus oli, että toteutus olisi mahdollisimman muistitehokas ja päädyttiin kirjainten toimivan avaimina muuttamalla ne taulukon indekseiksi. jotta taulusta sai mahdollisimman pienen, pitää joitain kirjaimia, kuten ääköset, mäpätä uusiin kokonaislukuihin.
- Suorituskykytestauksissa korjaajan tehokkuuden testaaminen on kysymysmerkki. Nyt yritettiin sellaista, että otettiin sanaston sanoja, ja luotiin niihin satunnaisia virheitä perus virheoperaatioita, poisto, vaihto, lisäys, transpoosi, noudattaen. Tämän jälkeen tälle muokatulle sanalle tehtiin korjausehdotuksia, ja tarkastettiin oliko ehdotuksissa alkuperäinen sana. Ennustustulokset vaikuttivat ensitesteillä varsin epäonnistuineilta. Tällä tavalla ei saada sopivaa aineistoa, sillä esim satunnaisten kirjainten lisääminen ei vastaa oikeita kirjoitusvirheitä. Yleensä lisätty kirjain on lähellä kirjoitettavan sanojen kirjaimia. Toisaalta mallikaan ei ota tähän kantaa, vaan laskee vain muokausetäisyyksillä. Olisiko tähän testiaineistoon joku järkevämpi tapa?


## Mitä teen seuraavaksi
- PunaMustaPuu
- Suorituskykytestejä
- ui:ta

## Käytetty tuntimäärä
- 16
