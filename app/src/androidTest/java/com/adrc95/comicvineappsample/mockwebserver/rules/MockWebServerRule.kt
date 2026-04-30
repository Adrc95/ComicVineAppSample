package com.adrc95.comicvineappsample.mockwebserver.rules

import com.adrc95.comicvineappsample.mockwebserver.MockWebServerUrlHolder
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MockWebServerRule : TestWatcher() {
    val server = MockWebServer()

    override fun starting(description: Description?) {
        server.start()
        MockWebServerUrlHolder.baseUrl = server.url("/").toString()
    }

    override fun finished(description: Description?) {
        server.shutdown()
    }
}
