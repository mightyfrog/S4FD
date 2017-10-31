package org.mightyfrog.android.s4fd.details

import android.content.SharedPreferences
import com.raizlabs.android.dbflow.sql.language.Select
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.KHCharacter_Table
import java.util.*
import javax.inject.Inject

/**
 * @author Shigehiro Soejima
 */
class DetailsPresenter @Inject constructor(val view: DetailsContract.View, private val prefs: SharedPreferences) : DetailsContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun compare(id: Int) {
        val list = Select().from(KHCharacter::class.java).where(KHCharacter_Table.id.notEq(id)).queryList()
        val displayNames = ArrayList<String>(list.size)
        list.mapTo(displayNames) { it.displayName!! }
        var position = 0
        prefs.getString("selectedCharToCompare", null)?.let { selectedCharToCompare ->
            displayNames.takeWhile { selectedCharToCompare != it }
                    .forEach { position++ }
        } ?: run {
            position = -1
        }
        view.showCompareDialog(list, displayNames, position)
    }

    override fun setCharToCompare(ownerId: Int, charToCompare: KHCharacter?) {
        val displayName = Select().from(KHCharacter::class.java)
                .where(KHCharacter_Table.id.eq(ownerId))
                .querySingle()?.displayName

        if (charToCompare == null || displayName == charToCompare.displayName) {
            view.hideVsThumbnail()
        } else {
            view.showVsThumbnail(charToCompare)
        }

        charToCompare?.let {
            view.setSubtitle(R.string.attr_compare_subtitle, charToCompare.displayName)
            prefs.edit().putString("selectedCharToCompare", charToCompare.displayName).apply()
        } ?: run {
            view.setSubtitle(R.string.attr_compare_subtitle, null)
            prefs.edit().remove("selectedCharToCompare").apply()
        }

        view.setCharToCompare(charToCompare)
    }

    override fun setCharToCompareIfAny(ownerId: Int) {
        val character = prefs.getString("selectedCharToCompare", null)
        character?.apply {
            setCharToCompare(ownerId, Select().from(KHCharacter::class.java).where(KHCharacter_Table.displayName.eq(this)).querySingle())
        }
    }
}