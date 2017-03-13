package org.mightyfrog.android.s4fd.details.tabcontents.miscs

import org.mightyfrog.android.s4fd.data.KHCharacter

/**
 * @author Shigehiro Soejima
 */
class MiscsPresenter(val mView: MiscsContract.View) : MiscsContract.Presenter {
    private var mCharToCompare: KHCharacter? = null

    init {
        mView.setPresenter(this)
    }

    override fun compare(name: String, ownerId: Int) {
        mView.showAttributes(name, ownerId, mCharToCompare?.id)
    }

    override fun setCharToCompare(char: KHCharacter?) {
        mCharToCompare = char
    }
}