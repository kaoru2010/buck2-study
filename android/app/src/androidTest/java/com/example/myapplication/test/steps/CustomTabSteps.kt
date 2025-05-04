package com.example.myapplication.test.steps

import android.app.Instrumentation
import android.content.Intent
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.MainActivity
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.hamcrest.Matchers.allOf
import javax.inject.Inject

@HiltAndroidTest
class CustomTabSteps(
    private val activityScenarioHolder: ActivityScenarioHolder,
) {
    @Inject
    lateinit var customComposableRuleHolder: CustomComposableRuleHolder

    private val composeRule: ComposeTestRule get() = customComposableRuleHolder.composeRule

    private val buttonText = "Chrome Custom Tab を開く"

    @Before
    fun setUpIntents() {
        // Espresso-Intents 初期化
        Intents.init()
        // 外部ブラウザ起動をスタブ化（本物の Chrome を開かない）
        Intents.intending(
            allOf(
                hasAction(Intent.ACTION_VIEW),
            ),
        )
            .respondWith(Instrumentation.ActivityResult(0, null))
    }

    @After
    fun tearDownIntents() {
        Intents.release()
    }

    @Given("アプリが起動している")
    fun launchApp() {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        Intent(targetContext, MainActivity::class.java).also {
            it.action = Intent.ACTION_MAIN
            it.addCategory(Intent.CATEGORY_LAUNCHER)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.let {
            activityScenarioHolder.launch(it)
        }
        // MainActivity は createAndroidComposeRule が自動起動済み。ここでは no-op
    }

    @When("カスタムタブボタンをタップする")
    fun tapButton() {
        composeRule
            .onNodeWithText(buttonText)
            .assertExists()
            .performClick()
    }

    @Then("URL {string} が開かれる")
    fun verifyUrl(url: String) {
        Intents.intended(
            allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(url),
            ),
        )
    }
}
