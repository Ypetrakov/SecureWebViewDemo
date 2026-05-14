package com.yurrii.petrakov.swvd.presentation.my_webview.webview

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.Log
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.yurrii.petrakov.swvd.domain.analytics.AnalyticsTracker
import com.yurrii.petrakov.swvd.presentation.my_webview.WebViewModel
import com.yurrii.petrakov.swvd.presentation.my_webview.WebViewScreenState


class MyWebViewClient(
    private val context: Context,
    val webViewModel: WebViewModel,
    val analyticsTracker: AnalyticsTracker
) : WebViewClient() {


    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest
    ): Boolean {
        webViewModel.url = view?.url ?: ""
        val uri = request.url

        if ("http" == uri.scheme) {
            val httpsUrl = uri.toString()
                .replaceFirst("http://".toRegex(), "https://")

            view!!.loadUrl(httpsUrl)

            return true
        }

        return false
    }



    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        CookieManager.getInstance().flush()
        webViewModel.updateState(WebViewScreenState.Success)
        url?.let {
            analyticsTracker.trackEvent("url_loaded",it )
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)

        trackWebViewError(
            url = view?.url,
            description = "SSL error: ${error?.primaryError}",
            code = error?.primaryError?.toString()
        )
    }


    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)

        trackWebViewError(
            url = request?.url?.toString(),
            description = "HTTP error: ${errorResponse?.statusCode} ${errorResponse?.reasonPhrase}",
            code = errorResponse?.statusCode?.toString()
        )
    }


    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)

        trackWebViewError(
            url = request?.url?.toString(),
            description = error?.description?.toString(),
            code = error?.errorCode?.toString()
        )
    }

    private fun trackWebViewError(
        url: String?,
        description: String?,
        code: String?
    ) {
        Log.d("WEBVIEW_ERROR", buildString {
            append("url=$url")
            append(", code=$code")
            append(", error=$description")
        })
    }




}