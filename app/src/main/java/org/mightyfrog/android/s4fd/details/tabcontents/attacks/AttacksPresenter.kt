package org.mightyfrog.android.s4fd.details.tabcontents.attacks

import com.raizlabs.android.dbflow.sql.language.Select
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.Move
import org.mightyfrog.android.s4fd.data.Move_Table

/**
 * @author Shigehiro Soejima
 */
class AttacksPresenter(private val view: AttacksContract.View) : AttacksContract.Presenter {
    private var charToCompare: KHCharacter? = null

    init {
        view.setPresenter(this)
    }

    override fun loadMoves(id: Int) {
        view.showMoves(Select().from(Move::class.java)
                .where(Move_Table.ownerId.eq(id))
                .queryList())
    }

    override fun compare(name: String) {
        view.showComparison(name, charToCompare)
    }

    override fun setCharToCompare(char: KHCharacter?) {
        charToCompare = char
    }
}