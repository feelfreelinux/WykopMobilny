package io.github.wykopmobilny.tests.rules

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MockWebServerRule : TestRule {

    private val mockWebServer = MockWebServer()

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                mockWebServer.dispatcher = QueueDispatcher().apply { setFailFast(true) }
                mockWebServer.start(port = 8000)
                base.evaluate()
                mockWebServer.shutdown()
            }
        }

    fun enqueue(response: () -> MockResponse) {
        mockWebServer.enqueue(response())
    }
}
