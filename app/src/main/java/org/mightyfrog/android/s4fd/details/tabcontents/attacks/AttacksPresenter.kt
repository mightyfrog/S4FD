package org.mightyfrog.android.s4fd.details.tabcontents.attacks

import com.raizlabs.android.dbflow.sql.language.Select
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.Move
import org.mightyfrog.android.s4fd.data.Move_Table

/**
 * @author Shigehiro Soejima
 */
class AttacksPresenter(private val mView: AttacksContract.View) : AttacksContract.Presenter {
    private var mCharToCompare: KHCharacter? = null

    init {
        mView.setPresenter(this)
    }

    override fun loadMoves(id: Int) {
        val list = Select().from(Move::class.java)
                .where(Move_Table.ownerId.eq(id))
                .queryList()
        mView.showMoves(list)
    }

    override fun compare(name: String) {
        mView.showComparison(name, mCharToCompare)
    }

    override fun setCharToCompare(char: KHCharacter?) {
        mCharToCompare = char
    }
}