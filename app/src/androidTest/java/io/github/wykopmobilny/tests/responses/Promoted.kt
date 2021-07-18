package io.github.wykopmobilny.tests.responses

import io.github.wykopmobilny.tests.rules.MockWebServerRule

fun MockWebServerRule.promoted() =
    enqueue { successfulResponse("promoted.json") }

fun MockWebServerRule.login() =
    enqueue { successfulResponse("promoted.json") }
