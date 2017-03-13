package org.mightyfrog.android.s4fd.settings

import org.mightyfrog.android.s4fd.BaseView

/**
 * @author Shigehiro Soejima
 */
interface SettingsContract {
    interface View : BaseView<SettingsPresenter> {
        fun showOpenSourceInfo(license: String)
    }

    interface Presenter {
        fun showOpenSourceInfo()
    }
}