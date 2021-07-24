package io.github.wykopmobilny.tests.rules

import io.github.wykopmobilny.tests.responses.successfulResponse
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MockWebServerRule : TestRule {

    private val mockWebServer = MockWebServer()

    private val dispatcher = MockDispatcher()

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                mockWebServer.dispatcher = dispatcher
                mockWebServer.start(port = 8000)
                try {
                    base.evaluate()
                    dispatcher.unmatchedRequest?.let { error("Failed to match response at path=${dispatcher.unmatchedRequest?.path}") }
                } finally {
                    mockWebServer.shutdown()
                }
            }
        }

    fun enqueue(
        requestMatcher: (RecordedRequest) -> Boolean,
        response: () -> MockResponse,
    ) {
        dispatcher.requests.add(requestMatcher to response)
    }
}

private class MockDispatcher : Dispatcher() {
    val requests = mutableListOf<Pair<(RecordedRequest) -> Boolean, () -> MockResponse>>()
    var unmatchedRequest: RecordedRequest? = null

    private val predefinedRequests = listOf<Pair<(RecordedRequest) -> Boolean, () -> MockResponse>>(
        pathMatcher("/favicon.ico") to { successfulResponse("avatar.png") },
    )

    override fun dispatch(request: RecordedRequest): MockResponse {
        val enqueued =
            requests.firstOrNull { (requestMatcher, _) -> requestMatcher(request) }?.also { requests.remove(it) }
                ?: predefinedRequests.firstOrNull { (requestMatcher, _) -> requestMatcher(request) }
                ?: return MockResponse().setResponseCode(400).also { unmatchedRequest = request }

        return enqueued.second()
    }
}

private fun pathMatcher(path: String): (RecordedRequest) -> Boolean =
    { it.path?.substringBefore("/appkey/") == path }

fun MockWebServerRule.enqueue(
    path: String,
    response: () -> MockResponse,
) = enqueue(pathMatcher(path), response)
