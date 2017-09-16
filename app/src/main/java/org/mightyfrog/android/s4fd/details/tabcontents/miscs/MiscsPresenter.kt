package org.mightyfrog.android.s4fd.details.tabcontents.miscs

import org.mightyfrog.android.s4fd.data.KHCharacter

/**
 * @author Shigehiro Soejima
 */
class MiscsPresenter(val view: MiscsContract.View) : MiscsContract.Presenter {
    private var charToCompare: KHCharacter? = null

    init {
        view.setPresenter(this)
    }

    override fun compare(name: String?, ownerId: Int) {
        name ?: return

        view.showAttributes(name, ownerId, charToCompare?.id)
    }

    override fun setCharToCompare(char: KHCharacter?) {
        charToCompare = char
    }
}