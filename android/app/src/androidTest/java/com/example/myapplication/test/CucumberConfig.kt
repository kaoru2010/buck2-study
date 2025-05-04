package com.example.myapplication.test

import io.cucumber.junit.CucumberOptions

@CucumberOptions(
    features = ["features"],
    plugin = [
        "pretty",
        BuildConfig.JSON_REPORT_PLUGIN,
        "com.example.myapplication.test.SaveToTestStoragePlugin",
    ],
)
class CucumberConfig
