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

Ohjelmaan on lisätty myös tekstinkäsittelyä helpottava ominaisuus tallentaa käsitelty teksti tiedostoon, sekä avata teksti tiedostosta.

# Algoritmit
## Levenshtein-etäisyyden laskeminen
Levenshteinetäisyys lasketaan dynaamiseen ohjelmointiin perustuvalla algoritmilla.

# Tietorakenteet
## Trie

## BK-puu
BK-puu toimii tietorakenteena, joka mahdollistaa nopeiden korjausehdotusten tuottamisen. Ohjelman käynnistyessä BK-puuhun ladataan koko sanasto, ottaen juureksi satunnainen sanaston sana. Puun solmujen tulee toteuttaa metriikka, jotta puu lopulta toimii tehokkaasti. Kahden solmun merkkijonojen välinen muokkausetäisyys on se, millä solmut tässä puun metrisessä avaruudessa erotetaan toisistaan. Jokaisella solmulla voi olla yhtä muokkausetäisyyttä kohden vain yksi lapsi. Itse muokkausetäisyysfunktio voi muuttua, kunhan metrisyys säilyy. 
Puun tehokkuus perustuu siihen, että vaikka sanasto on iso, niin sillä puu muodostaa metrisen avaruuden, voidaan tiettyä merkkijonoa, tai haettavaa lähimpänä olevia merkkijonoja haettaessa karsia suurin osa puun haaroista pois kolmioepäyhtälön avulla.
