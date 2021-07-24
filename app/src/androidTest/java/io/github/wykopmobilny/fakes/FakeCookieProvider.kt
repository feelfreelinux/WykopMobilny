package io.github.wykopmobilny.fakes

import javax.inject.Inject

class FakeCookieProvider @Inject constructor() {

    val cookies = mutableMapOf<String, String>()

    fun cookieForSite(url: String) =
        cookies[url]
}
