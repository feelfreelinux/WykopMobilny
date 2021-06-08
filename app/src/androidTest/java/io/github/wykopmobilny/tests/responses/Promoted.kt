package io.github.wykopmobilny.tests.responses

import io.github.wykopmobilny.tests.rules.MockWebServerRule

fun MockWebServerRule.promotedEmpty() =
    enqueue { successfulResponse("promoted_empty.json") }

fun MockWebServerRule.promoted() =
    enqueue { successfulResponse("promoted.json") }
