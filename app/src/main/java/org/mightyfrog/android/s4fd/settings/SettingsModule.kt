package org.mightyfrog.android.s4fd.settings

import dagger.Module
import dagger.Provides
import org.mightyfrog.android.s4fd.util.ActivityScope

/**
 * @author Shigehiro Soejima
 */
@Module
class SettingsModule(val view: SettingsContract.View) {

    @ActivityScope
    @Provides
    fun providesSettingsContractView(): SettingsContract.View {
        return view
    }
}