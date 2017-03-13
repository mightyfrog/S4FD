package org.mightyfrog.android.s4fd.settings

import android.content.Context
import java.util.*
import javax.inject.Inject

/**
 * @author Shigehiro Soejima
 */
class SettingsPresenter @Inject constructor(val mView: SettingsContract.View, val mContext: Context) : SettingsContract.Presenter {

    init {
        mView.setPresenter(this)
    }

    override fun showOpenSourceInfo() {
        var scanner: Scanner? = null
        try {
            scanner = Scanner(mContext.assets.open("licenses.txt")).useDelimiter("\\A")
            mView.showOpenSourceInfo(scanner.next())
        } finally {
            scanner?.close()
        }
    }
}