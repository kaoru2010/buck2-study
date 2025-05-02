package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabsService

/**
 * Custom Tabs を扱えるブラウザのパッケージ名を判定するユーティリティ。
 *
 * - Custom Tabs Service を公開しているパッケージを列挙
 * - その中に「デフォルトの VIEW インテントハンドラ」が含まれていればそれを採用
 * - そうでなければ Chrome Stable → Beta → Dev → Local の順に優先
 *
 * 見つからなければ `null` を返すので、呼び出し側で ACTION_VIEW へフォールバックしてください。
 */
object CustomTabsHelper {

    private const val ACTION_CUSTOM_TABS_CONNECTION =
        CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION   // = "androidx.browser.customtabs.action.CustomTabsService"

    private const val CHROME_PACKAGE = "com.android.chrome"
    private const val CHROME_BETA_PACKAGE = "com.chrome.beta"
    private const val CHROME_DEV_PACKAGE = "com.chrome.dev"
    private const val CHROME_LOCAL_PACKAGE = "com.google.android.apps.chrome"

    /** キャッシュしておくと毎回 PackageManager を回さずに済む */
    private var cachedPackageName: String? = null

    /**
     * 利用すべきブラウザのパッケージ名を返す。見つからなければ `null`。
     */
    fun getPackageNameToUse(context: Context): String? {
        // すでに判定済みなら即返す
        cachedPackageName?.let { return it }

        val pm = context.packageManager
        val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
        val defaultViewHandler = pm.resolveActivity(viewIntent, 0)?.activityInfo?.packageName

        // Custom Tabs Service を持つパッケージを列挙
        val serviceIntent = Intent(ACTION_CUSTOM_TABS_CONNECTION)
        val resolveInfos = pm.queryIntentServices(serviceIntent, 0)
        if (resolveInfos.isNullOrEmpty()) return null   // 該当なし

        val supportingPackages = resolveInfos
            .mapNotNull { it.serviceInfo?.packageName }
            .toSet()

        // 1) デフォルトブラウザが対応していればそれを使う
        if (defaultViewHandler != null && supportingPackages.contains(defaultViewHandler)) {
            cachedPackageName = defaultViewHandler
            return cachedPackageName
        }

        // 2) Chrome 系を優先度順にチェック
        val preferred = listOf(
            CHROME_PACKAGE,
            CHROME_BETA_PACKAGE,
            CHROME_DEV_PACKAGE,
            CHROME_LOCAL_PACKAGE
        )
        preferred.firstOrNull { supportingPackages.contains(it) }?.let {
            cachedPackageName = it
            return cachedPackageName
        }

        // 3) それ以外で 1 つに絞れる場合はそれを
        if (supportingPackages.size == 1) {
            cachedPackageName = supportingPackages.first()
        }
        return cachedPackageName
    }
}
