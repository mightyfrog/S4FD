package org.mightyfrog.android.s4fd.compare

import dagger.Component
import org.mightyfrog.android.s4fd.AppComponent
import org.mightyfrog.android.s4fd.util.ActivityScope

/**
 * @author Shigehiro Soejima
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(CompareModule::class))
interface CompareComponent {

    fun inject(activity: CompareActivity)
}