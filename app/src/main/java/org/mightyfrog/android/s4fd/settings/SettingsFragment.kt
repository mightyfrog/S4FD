package org.mightyfrog.android.s4fd.settings

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.Toast
import org.mightyfrog.android.s4fd.App
import org.mightyfrog.android.s4fd.R
import javax.inject.Inject

/**
 * @author Shigehiro Soejima
 */
class SettingsFragment : PreferenceFragmentCompat(), SettingsContract.View {
    @Inject
    lateinit var mPresenter: SettingsPresenter

    companion object {
        val TAG: String = SettingsFragment::class.java.simpleName

        fun newInstance(b: Bundle?): SettingsFragment = SettingsFragment().apply {
            arguments = b
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerSettingsComponent.builder()
                .appComponent((activity.application as App).getAppComponent())
                .settingsModule(SettingsModule(this))
                .build()
                .inject(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            "open_source" -> mPresenter.showOpenSourceInfo()
        }

        return super.onPreferenceTreeClick(preference)
    }

    override fun showOpenSourceInfo(license: String) {
        AlertDialog.Builder(context)
                .setMessage(license)
                .setPositiveButton(android.R.string.ok, null)
                .show()
    }

    override fun setPresenter(presenter: SettingsPresenter) {
        mPresenter = presenter
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }
}