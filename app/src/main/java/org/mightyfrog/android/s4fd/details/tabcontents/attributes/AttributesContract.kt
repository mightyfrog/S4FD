package org.mightyfrog.android.s4fd.details.tabcontents.attributes

import org.mightyfrog.android.s4fd.BaseView
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.MovementDatum

/**
 * @author Shigehiro Soejima
 */
interface AttributesContract {
    interface View : BaseView<AttributesPresenter> {
        fun showAttributes(list: List<MovementDatum>)

        fun setCharToCompare(char: KHCharacter?)
    }

    interface Presenter {
        fun loadAttributes(id: Int)
    }
}