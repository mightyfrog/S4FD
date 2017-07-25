package org.mightyfrog.android.s4fd.details.tabcontents.web

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.raizlabs.android.dbflow.sql.language.Select
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.KHCharacter_Table

/**
 * @author Shigehiro Soejima
 */
class WebFragment : Fragment() {

    companion object {
        fun newInstance(b: Bundle?): WebFragment = WebFragment().apply {
            arguments = b
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val id = arguments.getInt("id")
        val fullUrl = Select(KHCharacter_Table.fullUrl)
                .from(KHCharacter::class.java)
                .where(KHCharacter_Table.id.eq(id))
                .querySingle()!!
                .fullUrl

        val view = inflater?.inflate(R.layout.fragment_tab_web, container, false)
        view?.apply {
            val wv = findViewById<WebView>(R.id.webView)
            wv.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(view.url)
                    return true
                }

                override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                    if (wv.settings.cacheMode != WebSettings.LOAD_CACHE_ELSE_NETWORK) {
                        wv.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                        wv.loadUrl(fullUrl)
                    }
                    super.onReceivedError(view, errorCode, description, failingUrl)
                }
            }
            wv.settings.loadWithOverviewMode = true
            wv.settings.useWideViewPort = true
            wv.settings.cacheMode = WebSettings.LOAD_DEFAULT
            wv.loadUrl(fullUrl)
        }

        return view
    }
}