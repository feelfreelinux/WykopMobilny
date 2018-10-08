# Wykop Mobilny 📱

[![Build Status](https://travis-ci.org/feelfreelinux/WykopMobilny.svg?branch=master)](https://travis-ci.org/feelfreelinux/WykopMobilny)
[![Discord](https://img.shields.io/discord/455024671440633857.svg)](https://discord.gg/WgQZJD3)
[![Support via PayPal](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.me/WykopMobilny/)

> Nieoficjalny klient [wykop.pl](https://wykop.pl) na Androida, napisany w Kotlinie.

<img src="screenshots/link_details_light.png" height="33%" width="33%"><img src="screenshots/mainpage_dark.png" height="33%" width="33%"><img src="screenshots/tag_light.png" height="33%" width="33%">

## Funkcje

- Przeglądanie znalezisk (Strona główna, Wykopalisko, Ulubione, Tagi)
- Przeglądanie wpisów (Mój wykop, Ulubione, Gorące, Najnowsze, Tagi)
- Mikroblog
- Ciemny motyw (zarówno szary jak i dla AMOLEDów)
- Ankiety
- Wiadomości prywatne
- Pełna konfigurowalność

## Pobieranie

Możesz [pobrać najnowsze wydanie wraz z wbudowanymi powiadomieniami o aktualizacji][download-link] lub pobrać aplikację ze sklepu Google Play.

[![Pobierz na Google Play][google-play-badge]][google-play-download]

## Development

Jeżeli chcesz zbudować swoją własną wersję Wykopu Mobilnego, skopiuj `credentials.properties.example` do `credentials.properties` i wypełnij go odpowiednimi kluczami API. Pamiętaj, że obecnie aplikacja korzysta z API w wersji 2. Klucze wersji 1 nie są wspierane i nie będą działały.

- `apiKey` oraz `apiSecret` są kluczami API z Wykopu, które powinny zostać uzyskane [stąd][wykop-api] (przyznając wszystkie uprawnienia).
- `googleKey` to klucz API dla YouTube playera. Powinien zostać pozyskany według [instrukcji na developers.google.com][youtube-api].

## Lista zmian

Zwykle opisuje postępy w pracach nad aplikacją wraz z listą zmian [pod #otwartywykopmobilny na Wykopie][wykop-tag].

[build-badge]: https://travis-ci.org/feelfreelinux/WykopMobilny.svg?branch=master
[build]: https://travis-ci.org/feelfreelinux/WykopMobilny
[discord-badge]: https://img.shields.io/discord/455024671440633857.svg
[discord]: https://discord.gg/WgQZJD3
[paypal-badge]: https://img.shields.io/badge/Donate-PayPal-green.svg
[paypal]: https://www.paypal.me/WykopMobilny/
[wykop]: https://wykop.pl
[readme-pl]: README.pl.md
[download-link]: https://github.com/feelfreelinux/WykopMobilny/releases/latest
[google-play-badge]: https://play.google.com/intl/en_us/badges/images/badge_new.png
[google-play-download]: https://play.google.com/store/apps/details?id=io.github.feelfreelinux.wykopmobilny
[wykop-api]: https://www.wykop.pl/dla-programistow/nowa-aplikacja/
[youtube-api]: https://developers.google.com/youtube/android/player/register
[wykop-tag]: https://wykop.pl/tag/otwartywykopmobilny