package org.mightyfrog.android.s4fd.details.tabcontents.miscs

import org.mightyfrog.android.s4fd.data.KHCharacter

/**
 * @author Shigehiro Soejima
 */
class MiscsPresenter(val view: MiscsContract.View) : MiscsContract.Presenter {
    private var mCharToCompare: KHCharacter? = null

    init {
        view.setPresenter(this)
    }

    override fun compare(name: String?, ownerId: Int) {
        name ?: return

        view.showAttributes(name, ownerId, mCharToCompare?.id)
    }

    override fun setCharToCompare(char: KHCharacter?) {
        mCharToCompare = char
    }
}