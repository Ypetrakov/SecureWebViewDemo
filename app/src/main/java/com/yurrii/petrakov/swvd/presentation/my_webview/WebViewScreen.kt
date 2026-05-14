package com.yurrii.petrakov.swvd.presentation.my_webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yurrii.petrakov.swvd.R
import com.yurrii.petrakov.swvd.domain.analytics.AnalyticsTracker
import com.yurrii.petrakov.swvd.domain.repository.ContentGateRepository
import com.yurrii.petrakov.swvd.presentation.my_webview.components.ContentGate
import com.yurrii.petrakov.swvd.presentation.my_webview.components.Loading
import com.yurrii.petrakov.swvd.presentation.my_webview.components.MyWebView
import com.yurrii.petrakov.swvd.ui.navigation.TopBarAction
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


@SuppressLint("JavascriptInterface")
@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    viewModel: WebViewModel = koinViewModel(),
    url: String,
    openedWithDeepLin: Boolean = false,
    toggleTopBar: (Boolean) -> Unit,
    setTopBarOptions: (List<TopBarAction>) -> Unit,
    webView: WebView,
    backToSettings: () -> Unit
) {
    val state = viewModel.webViewScreenState.collectAsStateWithLifecycle().value
    val context = LocalContext.current


    val onOpenBrowser: () -> Unit = {
        val currentUrl = webView.url

        if (!currentUrl.isNullOrEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, currentUrl.toUri())
            context.startActivity(intent)
        }
    }
    LaunchedEffect(Unit) {
        setTopBarOptions(
            listOf(
                TopBarAction(
                    icon = R.drawable.refresh,
                    text = "Refresh",
                    onClick = { webView.reload() }
                ),
                TopBarAction(
                    icon = R.drawable.browser,
                    text = "Open in browser",
                    onClick = onOpenBrowser
                )
            )
        )
    }
    val analyticsTracker: AnalyticsTracker = koinInject()
    val contentGateRepository: ContentGateRepository = koinInject()

    LaunchedEffect(url) {
        if (!contentGateRepository.isContentAvailable()) {
            viewModel.updateState(WebViewScreenState.ContentGate())
            analyticsTracker.trackEvent("content_not_available")
            return@LaunchedEffect
        }

        if (!hasInternetConnection(context)) {
            viewModel.updateState(WebViewScreenState.ContentGate("This content is not available because you dont have internet connection."))
            analyticsTracker.trackEvent("no_internet_connection")
            return@LaunchedEffect
        }
        val url = if (url.startsWith("http://")) {
            url.replaceFirst(
                "http://",
                "https://"
            )
        } else url
        viewModel.updateState(WebViewScreenState.Loading)
        webView.loadUrl(url)

        viewModel.url = url

        analyticsTracker.trackEvent("navigate_to_content", url)

    }

    LaunchedEffect(state) {
        toggleTopBar(state != WebViewScreenState.Loading)
    }

    when (state) {
        WebViewScreenState.Loading -> {
            Loading(
                isOpenedViaDeepLink = openedWithDeepLin,
                url = url
            )
        }

        WebViewScreenState.Success -> {
            MyWebView(
                viewModel = viewModel,
                webView = webView,
                openedWithDeepLink = openedWithDeepLin
            )
        }

        is WebViewScreenState.ContentGate -> {
            ContentGate(
                reason = state.reason,
                backToSettings = {backToSettings()}
            )
        }
    }
}

fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}