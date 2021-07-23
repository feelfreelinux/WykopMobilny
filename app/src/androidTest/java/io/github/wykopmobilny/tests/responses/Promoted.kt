package io.github.wykopmobilny.tests.responses

import io.github.wykopmobilny.tests.rules.MockWebServerRule
import okhttp3.mockwebserver.MockResponse

fun MockWebServerRule.promoted() =
    enqueue { successfulResponse("promoted.json") }

fun MockWebServerRule.profile() =
    enqueue { successfulResponse("login.json") }

fun MockWebServerRule.blacklist() =
    enqueue { successfulResponse("blacklist.html") }

fun MockWebServerRule.connectPage() {
    enqueue {
        MockResponse()
            .setBody("ConnectPage")
            .setResponseCode(302)
            .setHeader("Location", "/ConnectSuccess/appkey/irrelevant/login/fixture-login/token/fixture-token/")
    }
    enqueue {
        MockResponse()
            .setBody("Redirected")
    }
}
