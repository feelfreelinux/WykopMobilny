# Contributing

👍🎉 First off, thanks for taking the time to contribute! 🎉👍

## Setting up the project
To build the project it is required
- `wykop.appKey` and `wykop.secretKey` are API keys from Wykop, that shall be obtained [from wykop api site][wykop-api] (giving all permissions).
- `wykop.googleKey` is an API key for YouTube player. It shall be obtained following [those developers.google.com instructions][youtube-api].

Add them to your `~/.gradle/local.properties`
```groovy
   wykop.secretKey=""
   wykop.appKey=""
   wykop.googleKey=""
```
or set them as your Environment Variables. The project should compile without them though. 

## Submitting changes
Please create a pull requests and self-review it first on your own.
Comment out at each non-trivial change, point at things you considered doing differently.  

If you're introducing new feature - add at least simple test covering the simplest flow.  
If you're fixing a bug - please do cover the broken functionality with a test.

Ignore codestyle - it should be already kept in place by automated tools 😉

[wykop-api]: https://www.wykop.pl/dla-programistow/nowa-aplikacja/
[youtube-api]: https://developers.google.com/youtube/android/player/register
