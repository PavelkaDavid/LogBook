# LogBook
Aplikace pro zaznamenání jízd automobilem.

Uživatel při zadávání jízdy vyplní počáteční pozici, finální pozici, počet km a náklady v Kč na jeden kilometr (tato hodnota bude po prvním vyplnění uložena a poté **předvyplněna** pomocí Shared Preferences).

Uživatel si také může nechat adresu vyplnit dle **aktuální pozice GPS**, nebo pomocí výběru (**Places API od Google**).

Všechny jízdy jsou ve formě listu seřazeny od nejnovější, po nejstarší s možností filtrování dle data (od – do).

Jednotlivé záznamy je možno odstranit pomocí dlouhého stisku na danou jízdu a následným potvrzením dialogu.

Je zde i **statistika** dle vybraného období, včetně **grafu**.

Pomocí tlačítka **exportovatování do formátu CSV** uložíme vybrané jízdy do souboru a vyvoláme sdílení daného souboru.

Data jsou uloženy v **SQLite**.


### Funkce
- Databáze SQLite
- Shared Preferences
- Geo lokace
- Places API
- Export CSV
- Sdílení souboru
- Graf
- Pokročilé GUI – taby, listy, karty

### Video
[odkaz na video](https://photos.app.goo.gl/1sc75BsUxQfjrZ738)