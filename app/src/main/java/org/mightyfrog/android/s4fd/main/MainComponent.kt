package org.mightyfrog.android.s4fd.main

import dagger.Component
import org.mightyfrog.android.s4fd.AppComponent
import org.mightyfrog.android.s4fd.util.ActivityScope

/**
 * @author Shigehiro Soejima
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(MainModule::class))
interface MainComponent {

    fun inject(activity: MainActivity)
}