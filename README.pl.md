# Wykop Mobilny 📱

> Nieoficjalny klient [wykop.pl](https://wykop.pl) na Android, napisany w Kotlin.

[![Build Status](https://travis-ci.org/feelfreelinux/WykopMobilny.svg?branch=master)](https://travis-ci.org/feelfreelinux/WykopMobilny)
[![Discord](https://img.shields.io/discord/455024671440633857.svg)](https://discord.gg/WgQZJD3)
[![Support via PayPal](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.me/WykopMobilny/)

<img src="screenshots/link_details_light.png" height="33%" width="33%"><img src="screenshots/mainpage_dark.png" height="33%" width="33%"><img src="screenshots/tag_light.png" height="33%" width="33%">

## Funkcje

- Obsługa znalezisk
- Strona główna
- Wykopalisko
- Funkcje zalogowanego użytkownika (Powiadomienia, odpisywanie na komentarze)
- Wiadomości prywatne
- Mikroblog
- Ulubione
- Mój Wykop
- Wyszukiwarka 
- Ciemny styl

## Instalacja

[Pobierz najnowszą wersję aplikacji](https://github.com/feelfreelinux/WykopMobilny/releases/latest)

## Dev

Aplikacja korzysta z APIv2 które nie jest jeszcze publicznie dostępne. Klucze APIv1 nie są wspierane. Aby zbudowac ten projekt umieść klucze APIv2 w tym formacie `apiKey="APIKEY" apiSecret="APISECRET"` w pliku `credentials.properties`.

## Lista zmian

Zwykle opisuje postępy w pracach nad aplikacją wraz z listą zmian [tutaj](https://wykop.pl/tag/otwartywykopmobilny).

## Użyte biblioteki:

- [RxJava2](https://github.com/ReactiveX/RxJava)
- [Retrofit2](https://github.com/square/retrofit)
- [Dagger2](https://github.com/google/dagger)
- Glide
- AppUpdater
- LeakCanary
- android-job

### Licencja

MIT
