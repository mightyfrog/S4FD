package org.mightyfrog.android.s4fd.settings

import dagger.Module
import dagger.Provides
import org.mightyfrog.android.s4fd.util.ActivityScope

/**
 * @author Shigehiro Soejima
 */
@Module
class SettingsModule(val mView: SettingsContract.View) {

    @ActivityScope
    @Provides
    fun providesSettingsContractView(): SettingsContract.View {
        return mView
    }
}