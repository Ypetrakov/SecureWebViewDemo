package com.yurrii.petrakov.swvd.ui.navigation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.yurrii.petrakov.swvd.domain.analytics.AnalyticsTracker
import com.yurrii.petrakov.swvd.domain.util.UrlHandler
import com.yurrii.petrakov.swvd.presentation.control_panel.ControlPanel
import com.yurrii.petrakov.swvd.presentation.my_webview.WebViewModel
import com.yurrii.petrakov.swvd.presentation.my_webview.WebViewScreen
import com.yurrii.petrakov.swvd.presentation.my_webview.webview.rememberWebView
import com.yurrii.petrakov.swvd.ui.CommonViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import kotlin.io.encoding.Base64

@Serializable
data object ControlPanelS : NavKey

@Serializable
data class WebViewS(val url: String, val bool: Boolean = false) : NavKey


@Serializable
data object Empty: NavKey
@Composable
fun Navigation(modifier: Modifier = Modifier,
               intent: Intent?,
               commonViewModel: CommonViewModel
) {

    val context = LocalContext.current
    val urlHandler: UrlHandler = koinInject()

    var isInCustomTab by remember { mutableStateOf(false) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val analyticsTracker: AnalyticsTracker = koinInject()
    val activity = LocalActivity.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (isInCustomTab) {
                    analyticsTracker.trackEvent("custom_tab_closed")
                    activity?.finishAndRemoveTask()
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)

        }
    }

    val deepLinkUrl = remember(intent) {
        val data = intent?.data
        val scheme = data?.scheme
        Log.d("DeepLink", intent?.data.toString() ?: "")
        if (!urlHandler.validateDeepLink(data)) {
            return@remember null
        }
        when (scheme) {
            "fdsqwoxss" -> {
                // Logic for your specific menu scheme
                urlHandler.allowList[0]
            }
            "myapp" -> {
                // Logic for your other intent filter
                data.getQueryParameter("url")
            }
            else -> null
        }
    }

    LaunchedEffect(deepLinkUrl) {
        if (deepLinkUrl != null && !urlHandler.ifUrlInAllowList(deepLinkUrl, true)) {
            isInCustomTab = true
            analyticsTracker.trackEvent("custom_tab_opened")
            openCustomTab(context, urlHandler.decodeUrl(deepLinkUrl))
        }
    }


    val backStack = rememberNavBackStack(
        if (deepLinkUrl == null) ControlPanelS else {
            if (urlHandler.ifUrlInAllowList(deepLinkUrl, true)) {
                WebViewS(deepLinkUrl, false)
            } else {
                Empty
            }
        }
    )

    BackHandler(backStack == ControlPanelS) {
        activity?.finish()
    }


    val viewModel: WebViewModel = koinViewModel()

    val webView = rememberWebView(
        context = context,
        viewModel = viewModel
    )





    LaunchedEffect(Unit) {
        commonViewModel.setBackAction {
            if (backStack.first() == ControlPanelS) {
                backStack.removeLastOrNull()
            } else {
                backStack.removeLastOrNull()
                backStack.add(ControlPanelS)
            }
        }
    }

    LaunchedEffect(deepLinkUrl) {
        if (deepLinkUrl != null) {
            if (urlHandler.ifUrlInAllowList(deepLinkUrl)) {
                analyticsTracker.trackEvent("deep_link_to_game_triggered")
                val title = intent?.data?.getQueryParameter("title")
                commonViewModel.updateTitle(title ?: "No title")
            }
        }
    }


    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is ControlPanelS -> NavEntry(key) {
                    commonViewModel.updateBackVisibility(false)
                    commonViewModel.updateOptionsVisibility(false)
                    commonViewModel.updateTitle("ControlPanel Screen")
                    ControlPanel(
                        loadWebview = { url ->
                            if (urlHandler.ifUrlInAllowList(url, true)) {
                                backStack.add(WebViewS(url, false))
                            } else {
                                isInCustomTab = true
                                openCustomTab(context, urlHandler.decodeUrl(url))
                            }
                        },
                        clearCache = {
                            webView.clearCache(true)
                            analyticsTracker.trackEvent("cache_cleared")
                        }
                    )
                }

                is WebViewS -> NavEntry(key) {
                    commonViewModel.updateBackVisibility(true)
                    commonViewModel.updateOptionsVisibility(true)
                    commonViewModel.updateTitle("WebView Screen")
                    val decodedUrl = String(Base64.decode(key.url), Charsets.UTF_8)
                    WebViewScreen(
                        url = decodedUrl, openedWithDeepLin = key.bool,
                        toggleTopBar = commonViewModel::setVisibility,
                        setTopBarOptions = commonViewModel::setOptionActions,
                        viewModel = viewModel,
                        webView = webView,
                        backToSettings = {
                            if (backStack.first() == ControlPanelS) {
                                backStack.removeLastOrNull()
                            } else {
                                backStack.removeLastOrNull()
                                backStack.add(ControlPanelS)
                            }
                        }
                    )
                }
                else -> NavEntry(key) { }
            }
        }
    )
}


fun openCustomTab(context: Context, url: String) {
    val builder = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .setInstantAppsEnabled(true)

    val customTabsIntent = builder.build()

    try {
        customTabsIntent.launchUrl(context, url.toUri())
    } catch (e: Exception) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }
}