package org.mightyfrog.android.s4fd.settings

import android.content.Context
import java.util.*
import javax.inject.Inject

/**
 * @author Shigehiro Soejima
 */
class SettingsPresenter @Inject constructor(val view: SettingsContract.View, val context: Context) : SettingsContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun showOpenSourceInfo() {
        var scanner: Scanner? = null
        try {
            scanner = Scanner(context.assets.open("licenses.txt")).useDelimiter("\\A")
            view.showOpenSourceInfo(scanner.next())
        } finally {
            scanner?.close()
        }
    }
}