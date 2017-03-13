package org.mightyfrog.android.s4fd.details.tabcontents.attacks

import org.mightyfrog.android.s4fd.BaseView
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.Move

/**
 * @author Shigehiro Soejima
 */
interface AttacksContract {
    interface View : BaseView<AttacksPresenter> {
        fun showMoves(list: List<Move>)

        fun showComparison(name: String, charToCompare: KHCharacter?)
    }

    interface Presenter {
        fun loadMoves(id: Int)

        fun compare(name: String)

        fun setCharToCompare(char: KHCharacter?)
    }
}