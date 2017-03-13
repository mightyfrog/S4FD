package org.mightyfrog.android.s4fd.main

import org.mightyfrog.android.s4fd.BaseView
import org.mightyfrog.android.s4fd.data.KHCharacter

/**
 * @author Shigehiro Soejima
 */
interface MainContract {
    interface View : BaseView<MainPresenter> {
        fun showCharacters(list: List<KHCharacter>?)

        fun showDetails(id: Int, position: Int)

        fun showActivityCircle()

        fun hideActivityCircle()

        fun showProgressDialog(msg: CharSequence)

        fun hideProgressDialog()

        fun showFallbackDialog()

        fun showDatabaseCopiedDialog()
    }

    interface Presenter {
        fun loadCharacters()

        fun openCharacter(id: Int, position: Int)

        fun destroy()

        fun fallback()
    }
}