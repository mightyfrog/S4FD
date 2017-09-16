package org.mightyfrog.android.s4fd.details.tabcontents.miscs

import org.mightyfrog.android.s4fd.BaseView
import org.mightyfrog.android.s4fd.data.KHCharacter

/**
 * @author Shigehiro Soejima
 */
interface MiscsContract {

    interface View : BaseView<MiscsPresenter> {
        fun showAttributes(name: String, ownerId: Int, charToCompareId: Int?)

        fun setCharToCompare(char: KHCharacter?)
    }

    interface Presenter {
        fun compare(name: String?, ownerId: Int)

        fun setCharToCompare(char: KHCharacter?)
    }
}