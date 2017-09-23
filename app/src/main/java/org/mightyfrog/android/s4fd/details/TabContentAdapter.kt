package org.mightyfrog.android.s4fd.details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import org.mightyfrog.android.s4fd.details.tabcontents.attacks.AttacksFragment
import org.mightyfrog.android.s4fd.details.tabcontents.attributes.AttributesFragment
import org.mightyfrog.android.s4fd.details.tabcontents.miscs.MiscsFragment
import org.mightyfrog.android.s4fd.details.tabcontents.web.WebFragment

/**
 * @author Shigehiro Soejima
 */
class TabContentAdapter(private val titles: Array<String>, fm: FragmentManager, val id: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val b = Bundle().apply {
            putInt("id", id)
        }
        return when (position) {
            0 -> {
                AttributesFragment.newInstance(b)
            }
            1 -> {
                AttacksFragment.newInstance(b)
            }
            2 -> {
                MiscsFragment.newInstance(b)
            }
            3 -> {
                WebFragment.newInstance(b)
            }
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }

    override fun getCount() = titles.size

    override fun getPageTitle(position: Int) = titles[position]
}