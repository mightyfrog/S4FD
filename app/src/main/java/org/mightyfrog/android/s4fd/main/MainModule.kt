package org.mightyfrog.android.s4fd.main

import dagger.Module
import dagger.Provides
import org.mightyfrog.android.s4fd.util.ActivityScope

/**
 * @author Shigehiro Soejima
 */
@Module
class MainModule(val view: MainContract.View) {

    @ActivityScope
    @Provides
    fun provideMainContractView(): MainContract.View = view
}