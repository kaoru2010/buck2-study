package com.example.myapplication.test

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.io.PlatformTestStorageRegistry
import io.cucumber.plugin.EventListener
import io.cucumber.plugin.Plugin
import io.cucumber.plugin.event.EventHandler
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.TestRunFinished
import io.cucumber.plugin.event.TestSourceRead
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference

class SaveToTestStoragePlugin :
    Plugin,
    EventListener {

    private val fileId = AtomicReference<String>()

    override fun setEventPublisher(publisher: EventPublisher) {
        publisher.registerHandlerFor(TestRunFinished::class.java, OnTestRunFinished(fileId))
        publisher.registerHandlerFor(TestSourceRead::class.java, OnTestSourceParsed(fileId))
    }

    class OnTestRunFinished(private val fileId: AtomicReference<String>) : EventHandler<TestRunFinished> {
        override fun receive(event: TestRunFinished) {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

            val jsonStr = targetContext.openFileInput("cucumber.json").bufferedReader().readText()

            PlatformTestStorageRegistry.getInstance().openOutputFile("cucumber-${fileId.get()}.json")
                .bufferedWriter()
                .use { it.append(jsonStr) }
        }
    }

    class OnTestSourceParsed(private val fileId: AtomicReference<String>) : EventHandler<TestSourceRead> {
        override fun receive(event: TestSourceRead) {
            fileId.set(UUID.randomUUID().toString())
        }
    }
}
