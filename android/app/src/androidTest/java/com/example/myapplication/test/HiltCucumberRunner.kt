package com.example.myapplication.test

import android.app.Application
import android.content.Context
import dagger.hilt.android.testing.HiltTestApplication
import io.cucumber.android.runner.CucumberAndroidJUnitRunner

class HiltCucumberRunner : CucumberAndroidJUnitRunner() {

    /** Hilt が生成するテスト用 Application を利用 */
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application = super.newApplication(cl, HiltTestApplication::class.java.name, context)
}
