package io.github.wykopmobilny.tests.responses

import io.github.wykopmobilny.tests.rules.MockWebServerRule
import okhttp3.mockwebserver.MockResponse
import okio.Buffer

fun MockWebServerRule.promotedEmpty() =
    enqueue { successfulResponse("promoted_empty.json") }

fun MockWebServerRule.promoted() =
    enqueue { successfulResponse("promoted.json") }

internal fun successfulResponse(filename: String) =
    MockResponse()
        .setResponseCode(200)
        .setBody(Reader.readResource(filename).use(Buffer()::readFrom))

private object Reader

private fun Reader.readResource(name: String) =
    Reader::class.java.classLoader!!.getResourceAsStream(name)
