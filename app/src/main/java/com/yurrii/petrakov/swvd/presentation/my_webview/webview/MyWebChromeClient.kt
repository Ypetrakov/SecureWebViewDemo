package com.yurrii.petrakov.swvd.presentation.my_webview.webview

import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.yurrii.petrakov.swvd.presentation.my_webview.WebViewModel

class MyWebChromeClient(
    private val context: Context,
    private val viewModel: WebViewModel
) : WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        viewModel.progress = newProgress / 100f
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        viewModel.title = view?.title ?: ""
    }


}
