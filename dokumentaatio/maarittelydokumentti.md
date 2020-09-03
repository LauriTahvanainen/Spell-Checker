# Määrittelydokumentti
## Perustietoja
Projektin kieli: Suomi

Opinto-ohjelma: Tietojenkäsittelytieteen kandidaatti

## Mitä algoritmeja ja tietorakenteita toteutat työssäsi
- Trie-puuta käytetään nopeaan hakemiseen suuresta määrästä merkkijonoja. 
Ohjelman tapauksessa kirjoitusvirhe tunnistetaan, jos sitä ei löydy valmiiksi määritellystä sanastosta. Ohjelman on siis tarpeellista tehdä nopea haku monta merkkijonoa sisältävästä sanastosta.

## Ohjelman ratkaisema ongelma, ja perustelut algoritmi- ja tietorakennevalinnoille
Ohjelma tunnistaa reaaliaikaisesti käyttäjän kirjoittamista sanoista kirjoitusvirheitä, ja ehdottaa käyttäjälle korjauksia virheellisiin sanoihin.
- Trie puuta käytetään nopeaan hakemiseen suuresta määrästä merkkijonoja. 
Ohjelman tapauksessa kirjoitusvirhe tunnistetaan, jos sitä ei löydy valmiiksi määritellystä sanastosta. Ohjelman on siis tarpeellista tehdä nopea haku monta merkkijonoa sisältävästä sanastosta.


## Ohjelman saamat syötteet ja niiden käyttö
Ohjelma saa syötteenä käyttäjän syöttämiä merkkejä ja merkkijonoja yksinkertaiselle tekstinsyöttöalueelle. Jokainen merkkijono tarkistetaan yksittäin kirjoitusvirheen varalta, ja jos kirjoitusvirhe löytyy, ehdotetaan merkkijonolle korjausta.
Ohjelma muistaa ehdotukset, joten ilmoitus virheellisyydestä, sekä mahdolliset ehdotukset, näkyvät jokaisen virheellisen sanan kohdalla kunnes käyttäjä poistaa ilmoituksen.
Siispä, ohjelma tallentaa käyttäjän merkkijonosyötteen käyttöönsä, ja liittää tarvittaessa jokaiseen kirjoitusvirheelliseen merkkijonoon ehdotuksia korjatusta merkkijonosta. 

## Tavoitteena olevat aika- ja tilavaativuudet

## Lähteet

