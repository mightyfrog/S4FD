package org.mightyfrog.android.s4fd.details

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import com.raizlabs.android.dbflow.sql.language.Select
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.KHCharacter_Table
import org.mightyfrog.android.s4fd.details.tabcontents.attacks.AttacksFragment
import org.mightyfrog.android.s4fd.details.tabcontents.attributes.AttributesFragment
import org.mightyfrog.android.s4fd.details.tabcontents.miscs.MiscsFragment
import java.util.*
import javax.inject.Inject

/**
 * @author Shigehiro Soejima
 */
class DetailsPresenter @Inject constructor(val mView: DetailsContract.View, val mPrefs: SharedPreferences) : DetailsContract.Presenter {

    init {
        mView.setPresenter(this)
    }

    override fun compare(id: Int) {
        val list = Select().from(KHCharacter::class.java).where(KHCharacter_Table.id.notEq(id)).queryList()
        val displayNames = ArrayList<String>(list.size)
        list.mapTo(displayNames) { it.displayName!! }
        var position = 0
        val selectedCharToCompare = mPrefs.getString("selectedCharToCompare", null)
        if (selectedCharToCompare != null) {
            displayNames
                    .takeWhile { selectedCharToCompare != it }
                    .forEach { position++ }
        } else {
            position = -1
        }
        mView.showCompareDialog(list, displayNames, position)
    }

    override fun setCharToCompare(ownerId: Int, charToCompare: KHCharacter?) {
        val f0 = (mView as AppCompatActivity).supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + 0) as AttributesFragment
        f0.setCharToCompare(charToCompare)
        val f1 = mView.supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + 1) as AttacksFragment
        f1.setCharToCompare(charToCompare)
        val f2 = mView.supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + 2) as MiscsFragment
        f2.setCharToCompare(charToCompare)

        val displayName = Select().from(KHCharacter::class.java)
                .where(KHCharacter_Table.id.eq(ownerId))
                .querySingle()?.displayName

        if (charToCompare == null || displayName == charToCompare.displayName) {
            mView.hideVsThumbnail()
        } else {
            mView.showVsThumbnail(charToCompare)
        }

        if (charToCompare == null) {
            mView.updateSubtitle(null)
            mPrefs.edit().remove("selectedCharToCompare").apply()
        } else {
            mView.updateSubtitle((mView as Context).getString(R.string.attr_compare_subtitle, charToCompare.displayName))
            mPrefs.edit().putString("selectedCharToCompare", charToCompare.displayName).apply()
        }

        mView.setCharToCompare(charToCompare)
    }

    override fun setCharToCompareIfAny(ownerId: Int) {
        val character = mPrefs.getString("selectedCharToCompare", null)
        character?.apply {
            setCharToCompare(ownerId, Select().from(KHCharacter::class.java).where(KHCharacter_Table.displayName.eq(this)).querySingle())
        }
    }
}