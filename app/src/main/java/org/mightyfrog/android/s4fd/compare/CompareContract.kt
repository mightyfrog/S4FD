package org.mightyfrog.android.s4fd.compare

import org.mightyfrog.android.s4fd.BaseView
import org.mightyfrog.android.s4fd.data.Move

/**
 * @author Shigehiro Soejima
 */
interface CompareContract {

    interface View : BaseView<ComparePresenter> {
        fun showMoves(list: List<Move>, scrollPosition: Int)

        fun showSortedMoves(type: Int)
    }

    interface Presenter {
        fun loadMoves(name: String, charId: Int, charToCompareId: Int)

        fun sort(type: Int)
    }
}