# Määrittelydokumentti
## Perustietoja
Projektin kieli: Suomi

Opinto-ohjelma: Tietojenkäsittelytieteen kandidaatti

## Mitä algoritmeja ja tietorakenteita toteutat työssäsi
- Trie-puu
- Damerau–Levenshtein etäisyyteen perustuva algoritmi
- Näiden yhdistelmän optimointia jollain tavalla, ja mahdollisesti aikataulusta riippuen joku vaihtoehtoinen rakenne, todennäköisimmin kuitenkin perustuen Trie-puu-sanastoon.
- Koska aiheesta ei ole kokemusta, voi lopullinen toteutus elää jonkin verran.

## Ohjelman ratkaisema ongelma, ja perustelut algoritmi- ja tietorakennevalinnoille
Ohjelma tunnistaa reaaliaikaisesti käyttäjän kirjoittamista sanoista kirjoitusvirheitä, ja ehdottaa käyttäjälle korjauksia virheellisiin sanoihin.
- Trie puuta käytetään nopeaan hakemiseen suuresta määrästä merkkijonoja. 
Ohjelman tapauksessa kirjoitusvirhe tunnistetaan, jos sitä ei löydy valmiiksi määritellystä sanastosta. Ohjelman on siis tarpeellista tehdä nopea haku monta merkkijonoa sisältävästä sanastosta.
- Damerau–Levenshtein etäisyyteen perustuvaa algoritmia käytetään ehdotusten tekemiseen, algoritmin voi toteuttaa brute force tekniikalla esimerkiksi niin, että lasketaan virheellisen sanan etäisyys kaikkiin sanaston sanoihin, mutta käytettävyyden kannalta tämän aikavaativuus menee liian suureksi, joten laskentaa pitää optimoida jotenkin.
- Huomioitavaa on, että koska ongelma-alue ei ole ennestään tuttu, voivat toteutus ja valitut algoritmit elää jonkin verran projektin edetessä. Sanojen taivutusmuotojen käsittely aiheuttaa myös kysymysmerkkejä.

## Ohjelman saamat syötteet ja niiden käyttö
Ohjelma saa syötteenä käyttäjän syöttämiä merkkejä ja merkkijonoja yksinkertaiselle tekstinsyöttöalueelle. Jokainen merkkijono tarkistetaan yksittäin kirjoitusvirheen varalta, ja jos kirjoitusvirhe löytyy, ehdotetaan merkkijonolle korjausta.
Ohjelma muistaa ehdotukset, joten ilmoitus virheellisyydestä, sekä mahdolliset ehdotukset, näkyvät jokaisen virheellisen sanan kohdalla kunnes käyttäjä poistaa ilmoituksen.
Siispä, ohjelma tallentaa käyttäjän merkkijonosyötteen käyttöönsä, ja liittää tarvittaessa jokaiseen kirjoitusvirheelliseen merkkijonoon ehdotuksia korjatusta merkkijonosta. 
Ohjelma lataa myös käyttöönsä suomen kielen sanaston, jota se käyttää kielioppivirheiden tunnistamiseen.

## Tavoitteena olevat aika- ja tilavaativuudet
- Trie-puun haun huonoimman tapauksen aikavaatimus pitäisi olla O(m), missä m on haettavan merkkijonon pituus. Trien rakentamisen pahin aikavaativuus voi olla O(nm), missä m on pisimmän sanan pituus, ja n on sanaston koko, sillä puu luodaan valmiiksi ohjelman käynnistyessä, ja sen voisi jopa mahdollisesti säilöä.
- Ehdotusalgoritmin aikavaatimus pitäisi olla keskimäärin sellainen, että jokainen haku tapahtuu käyttäjän näkökulmasta siedettävässä ajassa. Tämä tarkoittaa esim sitä, että ehdotusten muodostamisen pitäisi olla alle O(n), missä n on sanaston koko. Damerau–Levenshtein etäisyyden laskemisen aikavaativuudella ei todennäköisimmin ole niin suurta painoarvoa tehokkuuden kannalta, mutta sen tavoitellaan omaavan keskimäärin aikavaativuuden O(mr), missä m on syötetty merkkijono ja r, se merkkijono johon verrataan. Etäisyyden laskemistakin on mahdollista optimoida.

- Sanaston tilavaativuudeksi tavoitellaan enintäään O(n), missä n on sanojen määrä sanastossa, Trie puulla tämän tosin pitäisi onnistua, vaikkakin trie puu vie myös paljon tilaa. Vaihtoehtona olisi tilan kannalta mahdollisesti myös [Deterministic acyclic finite state automaton](https://en.wikipedia.org/wiki/Deterministic_acyclic_finite_state_automaton). 
- Damerau–Levenshtein etäisyyden tilavaativuuden on oltava enintään O(mr), missä m on syötetty merkkijono ja r, se merkkijono johon verrataan. Tässäkin on varmasti optimoinnin varaa. 


## Lähteet
- Stackoverlflow
- Wikipedia
- [Peter Norvig](http://norvig.com/spell-correct.html)
- Erinäiset helkan kirjastosta löytyvät aiheeseen liittyvät tieteelliset tekstit
