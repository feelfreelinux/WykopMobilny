[![Build Status](https://travis-ci.org/feelfreelinux/WykopMobilny.svg?branch=master)](https://travis-ci.org/feelfreelinux/WykopMobilny)
# Wykop Mobilny
Nieoficjalny klient [wykop.pl](http://wykop.pl) na Android, napisany w Kotlin.

![Light theme](screenshots/link_details_light.png){:height="33%" width="33%"}![Dark theme](screenshots/mainpage_dark.png ){:height="33%" width="33%"}![Light theme](screenshots/tag_light.png ){:height="33%" width="33%"}
# Funkcje
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
# Użyte biblioteki:
- [RxJava2](https://github.com/ReactiveX/RxJava)
- [Retrofit2](https://github.com/square/retrofit)
- [Dagger2](https://github.com/google/dagger)
- Glide
- AppUpdater
- LeakCanary
- android-job
# Budowanie aplikacji
Aplikacja korzysta z APIv2 które nie jest jeszcze publicznie dostępne. Klucze APIv1 nie są wspierane. Aby zbudowac ten projekt umieść klucze APIv2 w tym formacie `apiKey="APIKEY" apiSecret="APISECRET"` w pliku `credentials.properties`
# Zgłoś błąd
Możesz zgłosić błąd bezpośrednio na githubie, lub opisz swój problem pod tagiem [#owmbugi](wykop.pl/tag/owmbugi).

# Lista zmian
Zwykle opisuje postępy w pracach nad aplikacją wraz z listą zmian [tutaj](wykop.pl/tag/otwartywykopmobilny).
# Pobierz aplikacje
[Pobierz najnowszą wersję aplikacji.](https://github.com/feelfreelinux/WykopMobilny/releases/latest) Aplikacja automatycznie powiadomi cię o nowych wersjach :)
# [Zrzuty ekranu](https://github.com/feelfreelinux/WykopMobilny/tree/master/screenshots)