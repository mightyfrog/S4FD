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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments ?: return null

        return inflater.inflate(R.layout.fragment_tab_web, container, false)?.apply {
            val fullUrl = Select(KHCharacter_Table.fullUrl)
                    .from(KHCharacter::class.java)
                    .where(KHCharacter_Table.id.eq(arguments?.getInt("id")))
                    .querySingle()!!
                    .fullUrl

            with(findViewById<WebView>(R.id.webView)) {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        view?.loadUrl(view.url)
                        return true
                    }

                    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                        if (settings.cacheMode != WebSettings.LOAD_CACHE_ELSE_NETWORK) {
                            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                            loadUrl(fullUrl)
                        }
                        super.onReceivedError(view, errorCode, description, failingUrl)
                    }
                }
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                loadUrl(fullUrl)
            }
        }
    }
}