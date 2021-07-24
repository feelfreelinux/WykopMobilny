package io.github.wykopmobilny.tests.responses

import io.github.wykopmobilny.tests.rules.MockWebServerRule
import io.github.wykopmobilny.tests.rules.enqueue
import okhttp3.mockwebserver.MockResponse

fun MockWebServerRule.promoted() =
    enqueue("/links/promoted/page/1") { successfulResponse("promoted.json") }

fun MockWebServerRule.notificationsCountEmpty() =
    enqueue("/notifications/Count") { successfulResponse("notificationscount_empty.json") }

fun MockWebServerRule.hashtagsCountEmpty() =
    enqueue("/notifications/HashTagsCount") { successfulResponse("hashtagscount_empty.json") }

fun MockWebServerRule.githubPatronsEmpty() =
    enqueue("/alufers/owm-patrons/master/patrons.json") { successfulResponse("githubpatrons_empty.json") }

fun MockWebServerRule.profile() =
    enqueue("/login/index") { successfulResponse("login.json") }

fun MockWebServerRule.blacklist() =
    enqueue("/ustawienia/czarne-listy/") { successfulResponse("blacklist.html") }

fun MockWebServerRule.connectPage() {
    enqueue("/Login/Connect") {
        MockResponse()
            .setBody("ConnectPage")
            .setResponseCode(302)
            .setHeader("Location", "/ConnectSuccess/appkey/irrelevant/login/fixture-login/token/fixture-token/")
    }
    enqueue("/ConnectSuccess") {
        MockResponse()
            .setBody("Redirected")
    }
}

fun MockWebServerRule.callsOnAppStart() {
    promoted()
    notificationsCountEmpty()
    hashtagsCountEmpty()
    githubPatronsEmpty()
}
