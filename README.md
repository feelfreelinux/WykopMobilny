# Wykop Mobilny 📱

> Unofficial [wykop.pl](https://wykop.pl) client for Android, written in Kotlin.

[![Build Status](https://travis-ci.org/feelfreelinux/WykopMobilny.svg?branch=master)](https://travis-ci.org/feelfreelinux/WykopMobilny)
[![Discord](https://img.shields.io/discord/455024671440633857.svg)](https://discord.gg/WgQZJD3)
[![Support via PayPal](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.me/WykopMobilny/)

_[Przeczytaj w języku Polskim](README.pl.md)_

<img src="screenshots/link_details_light.png" height="33%" width="33%"><img src="screenshots/mainpage_dark.png" height="33%" width="33%"><img src="screenshots/tag_light.png" height="33%" width="33%">

## Features

- Browse link feed (Main page, Upcoming, Favorite, Tags)
- Browse entries (MyWykop, Favorite, Hot, Newest, Tags)
- Search entries / links
- Dark theme

## Download

[Download the newest release with built-in update notifier](https://github.com/feelfreelinux/WykopMobilny/releases/latest)

## Dev

Application uses APIv2 which is currently in closed beta. APIv1 keys are not supported. In order to build this project, put `apiKey="APIKEY" apiSecret="APISECRET"` in `credentials.properties`

## Changelogs

Usually I report all progress along with changelogs [here](https://wykop.pl/tag/otwartywykopmobilny).

## Used libraries:

- [RxJava2](https://github.com/ReactiveX/RxJava)
- [Retrofit2](https://github.com/square/retrofit)
- [Dagger2](https://github.com/google/dagger)
- Glide
- AppUpdater
- LeakCanary
- android-job

### License

MIT
