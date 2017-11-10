package org.mightyfrog.android.s4fd.settings

import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.mightyfrog.android.s4fd.R

/**
 * @author Shigehiro Soejima
 */
class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        val TAG: String = SettingsFragment::class.java.simpleName

        fun newInstance(b: Bundle?): SettingsFragment = SettingsFragment().apply {
            arguments = b
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            "open_source" -> {
                Intent(context, OssLicensesMenuActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}