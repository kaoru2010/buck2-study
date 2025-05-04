package com.example.myapplication.test.steps

import androidx.compose.ui.test.junit4.createComposeRule
import io.cucumber.junit.WithJunitRule
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Singleton

@WithJunitRule()
@Singleton
class CustomComposableRuleHolder @Inject constructor() {

    @get:Rule(order = 1)
    val composeRule = createComposeRule()
}
