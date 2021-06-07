package io.github.wykopmobilny.tests.responses

import okhttp3.mockwebserver.MockResponse
import okio.Buffer

internal fun successfulResponse(filename: String) =
    MockResponse()
        .setResponseCode(200)
        .setBody(Reader.readResource(filename).use(Buffer()::readFrom))

private object Reader

private fun Reader.readResource(name: String) =
    Reader::class.java.classLoader!!.getResourceAsStream(name)
