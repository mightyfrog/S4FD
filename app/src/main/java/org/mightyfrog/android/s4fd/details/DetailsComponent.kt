package org.mightyfrog.android.s4fd.details

import dagger.Component
import org.mightyfrog.android.s4fd.AppComponent
import org.mightyfrog.android.s4fd.util.ActivityScope

/**
 * @author Shigehiro Soejima
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DetailsModule::class))
interface DetailsComponent {

    fun inject(activity: DetailsActivity)
}