package org.mightyfrog.android.s4fd.details

import dagger.Module
import dagger.Provides
import org.mightyfrog.android.s4fd.util.ActivityScope

/**
 * @author Shigehiro Soejima
 */
@Module
class DetailsModule(val mView: DetailsContract.View) {

    @ActivityScope
    @Provides
    fun provideDetailsContractView(): DetailsContract.View = mView
}