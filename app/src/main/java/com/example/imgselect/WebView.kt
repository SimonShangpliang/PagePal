package com.example.imgselect

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.RenderProcessGoneDetail
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State

class WebViewHolder(context: Context,onPageFinishedCallback: (Boolean) -> Unit,onPageCurrent:(String)->Unit){

    var webView:WebView=WebView(context)

    private var webChromeClient= object: WebChromeClient(){
        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            Log.d("webviewscreen",consoleMessage.toString())
            return super.onConsoleMessage(consoleMessage)
        }

    }
    private var webViewClient =object : WebViewClient(){
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            Log.d("WebViewScreen","ReceivedError"+error.toString())

            super.onReceivedError(view, request, error)
        }
//        override fun onRenderProcessGone(
//            view: WebView?,
//            detail: RenderProcessGoneDetail?
//        ): Boolean {
//            Log.d("webviewscreen","dededed")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                if (detail?.didCrash() == false) {
//
//                 //   webView.loadUrl(url)
//                    return true
//                }
//            return false
//        }
        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            Log.d("WebViewScreen","ReceivedHttpError")
            super.onReceivedHttpError(view, request, errorResponse)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            Log.d("WebViewScreen","ReceivedSslError")

            super.onReceivedSslError(view, handler, error)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            onPageCurrent(url?: "https://www.google.com")
            Log.d("???","onpagestarted")

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
onPageFinishedCallback(false)
            Log.d("???","onpagefinished")

        }

    }

    init {
        webView.layoutParams=ViewGroup.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        initWebView()
    }

    private fun initWebView()
    {
        val webSettings=webView.settings
        webSettings.javaScriptEnabled=true
        webSettings.loadsImagesAutomatically=true
        webSettings.layoutAlgorithm=WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        webSettings.useWideViewPort=true
        webSettings.setSupportZoom(true) // Enable zoom support
        webSettings.builtInZoomControls = true // Enable built-in zoom controls
        webSettings.displayZoomControls = false // Hide the default zoom controls

        setupWebChromeClient()
        setupWebViewClient()

    }
    private fun setupWebChromeClient(){
        webView.webChromeClient=webChromeClient
    }
    private fun setupWebViewClient(){
        webView.webViewClient=webViewClient
    }
    fun loadUrl(url:String)
    {

        webView.loadUrl(url)
    }
    fun reloadUrl()
    {

        webView.reload()
    }

    fun close() {
        webView.stopLoading()
        webView.clearHistory()
        webView.clearCache(true)
        webView.loadUrl("about:blank")
        webView.removeAllViews()
        webView.destroy()
    }

}
