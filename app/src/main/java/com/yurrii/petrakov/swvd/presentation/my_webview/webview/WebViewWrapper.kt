package com.yurrii.petrakov.swvd.presentation.my_webview.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.yurrii.petrakov.swvd.domain.analytics.AnalyticsTracker
import com.yurrii.petrakov.swvd.presentation.my_webview.WebViewModel
import org.koin.compose.koinInject


@Composable
fun rememberWebView(
    context: Context,
    viewModel: WebViewModel
): WebView {
    val analyticsTracker: AnalyticsTracker = koinInject()
    return remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            viewModel.onBackHandler = {
                if (this.canGoBack()) this.goBack()
                else (context as Activity).finish()
            }
            setSettings()
            webChromeClient = MyWebChromeClient(context, viewModel)
            webViewClient = MyWebViewClient(context, viewModel, analyticsTracker)
        }
    }
}
@SuppressLint("SetJavaScriptEnabled")
fun WebView.setSettings() {
    this.apply {
        settings.apply {

            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            domStorageEnabled = true
            setSupportZoom(false)
            displayZoomControls = false
            builtInZoomControls = false
            allowFileAccess = true
            allowContentAccess = true
            loadWithOverviewMode = true
            useWideViewPort = true
            javaScriptCanOpenWindowsAutomatically = true
        }
        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        requestFocus(View.FOCUS_DOWN)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    val cookieManager: CookieManager = CookieManager.getInstance()
    cookieManager.setAcceptCookie(true)
    cookieManager.acceptCookie()
    cookieManager.setAcceptThirdPartyCookies(this, true)
    cookieManager.flush()
}