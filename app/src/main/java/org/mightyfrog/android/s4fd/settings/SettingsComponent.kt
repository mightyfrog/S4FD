package org.mightyfrog.android.s4fd.settings

import dagger.Component
import org.mightyfrog.android.s4fd.AppComponent
import org.mightyfrog.android.s4fd.util.ActivityScope

/**
 * @author Shigehiro Soejima
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SettingsModule::class))
interface SettingsComponent {

    fun inject(fragment: SettingsFragment)
}