# Ohjelman yleisrakenne
## Käyttöliittymä
Käyttöliittymänä toimii yksinkertainen javafx:llä ja richTextfx-kirjastolla toteutettu tekstieditori. 

## Toiminnallisuus
Kielen sanasto ladataan sanastonäytteistä kahteen tietorakenteeseen, Trie-puuhun, sekä BK-puuhun. Puut siis täytetään ohjelman käynnistyessä. Mahdollisesti toteutetaan myös varasto, josta sanaston voisi nopeasti ladata.

Yksittäisen sanan tarkistaminen, ja korjaaminen etenee seuraavalla tavalla:
- Trie-puun avulla testataan ensin kuuluuko sana sanastoon. Jos kuuluu, lopetetaan, jos ei kuulu, tehdään BK-puusta haku, ja haetaan 10 virheellistä sanaa lähimpänä olevaa sanaa.
- Lähimmällä etäisyydellä tarkoitetaan tässä virheellisen sanan, ja sanaston sanan välistä muokkausetäisyyttä. Esim sanojen "kissa" ja "kisso" muokkausetäisyys on 1, sillä kisso saadaan kissasta vaihtamalla viimeinen kirjain a kirjaimeen o.
  - BK-puu on puu, joka on rakennettu sanaston sanojen välisten etäisyyksien perusteella. Toteutuksessa muokkausetäisyys lasketaan oletuksena Levenshtein-etäisyytenä, mutta ohjelmassa on mahdollisesti valittavana myös muita etäisyysfunktioita. Jokatapauksessa, BK-puun käyttämä etäisyysfunktio on vaihdettavissa mihin tahansa metriseen etäisyysfunktioon.

  
- Kun BK-puulta on haettu ennustukset, näytetää käyttäjälle sanan kohdalla virhe, ja käyttäjän painaessa virhemerkistä näytetään käyttäjälle korjausehdotusten lista, niin että "Paras" korjaus-ehdotus on ensimmäisenä.
- Sillä Trie- ja BK-puuhun lisääminen on nopeaa, käyttäjälle annetaan myös mahdollisuus lisätä virheellisesti kirjoitettu sana sanastoon. 

## Algoritmit
### Levenshtein-etäisyyden laskeminen
Levenshteinetäisyys lasketaan dynaamiseen ohjelmointiin perustuvalla algoritmilla.

## Tietorakenteet
### Trie

### BK-puu
BK-puu toimii tietorakenteena, joka mahdollistaa nopeiden korjausehdotusten tuottamisen. Ohjelman käynnistyessä BK-puuhun ladataan koko sanasto, ottaen juureksi satunnainen sanaston sana. Puun solmujen tulee toteuttaa metriikka, jotta puu lopulta toimii tehokkaasti.
