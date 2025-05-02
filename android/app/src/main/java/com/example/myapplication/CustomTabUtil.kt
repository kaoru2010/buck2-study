package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat

/**
 * Chrome Custom Tab の起動補助。Chrome 非インストール時は ACTION_VIEW へフォールバック。
 */
fun openCustomTab(context: Context, uri: Uri) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM) // テーマに合わせ自動切替
        .build()

    // Custom Tabs を扱えるブラウザが無ければ通常 Intent へ
    val packageName = CustomTabsHelper.getPackageNameToUse(context)
    if (packageName != null) {
        customTabsIntent.intent.`package` = packageName
        customTabsIntent.launchUrl(context, uri)
    } else {
        val fallbackIntent = Intent(Intent.ACTION_VIEW, uri)
        ContextCompat.startActivity(context, fallbackIntent, null)
    }
}
