[![Build Status](https://travis-ci.org/feelfreelinux/WykopMobilny.svg?branch=master)](https://travis-ci.org/feelfreelinux/WykopMobilny)
# Wykop Mobilny
Unofficial [wykop.pl](http://wykop.pl) client for Android, written in Kotlin.

![Light theme](screenshots/mainpage_light.png)
![Dark theme](screenshots/mainpage_dark.png)
# Features
- Browse link feed (Main page, Upcoming, Favorite, Tags)
- Browse entries (MyWykop, Favorite, Hot, Newest, Tags)
- Search entries / links
- Dark theme
# Used libraries:
- [RxJava2](https://github.com/ReactiveX/RxJava)
- [Retrofit2](https://github.com/square/retrofit)
- [Dagger2](https://github.com/google/dagger)
- Glide
- AppUpdater
- LeakCanary
- android-job
# Building app
Application uses APIv2 which is currently in closed beta. APIv1 keys are not supported. In order to build this project, put `apiKey="APIKEY" apiSecret="APISECRET"` in `credentials.properties`
# Report bug
You can report an issue in this repository, or just describe your issue under [#owmbugi](wykop.pl/tag/owmbugi) tag.

# Changelogs
Usually I report all progress along with changelogs [here](wykop.pl/tag/otwartywykopmobilny).
# Downloads
[Download newest release with built-in update notifier](https://github.com/feelfreelinux/WykopMobilny/releases/latest)
# [Screenshots](https://github.com/feelfreelinux/WykopMobilny/tree/master/screenshots)